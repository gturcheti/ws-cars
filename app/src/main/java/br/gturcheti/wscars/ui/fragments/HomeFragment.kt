package br.gturcheti.wscars.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.gturcheti.wscars.App
import br.gturcheti.wscars.PushLeadService
import br.gturcheti.wscars.R
import br.gturcheti.wscars.data.model.Lead
import br.gturcheti.wscars.data.model.LeadDao
import br.gturcheti.wscars.databinding.FragmentHomeBinding
import br.gturcheti.wscars.ui.adapters.CarsRecyclerViewAdapter
import br.gturcheti.wscars.ui.dialog.UserFormDialog
import br.gturcheti.wscars.ui.viewmodel.HomeViewModel
import br.gturcheti.wscars.ui.viewmodel.Result
import br.gturcheti.wscars.ui.vo.CarsVO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val carsAdapter = CarsRecyclerViewAdapter(::onItemClicked)
    private lateinit var leads: List<Lead>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    override fun onResume() { //ESSE CÓDIGO ESTÁ STARTANDO O SERVIÇO QUE DEVERIA TER INICIADO COM O ALARM
        super.onResume()
        val intent = Intent(requireContext(), PushLeadService::class.java)
        requireContext().startService(intent)
    }

    private fun setupObservers() {
        viewModel.carsList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading()
                is Result.Error -> showError()
                is Result.Success -> showContent(it.data)
            }
        }
    }

    private fun showContent(cars: List<CarsVO>) {
        binding.rvError.isVisible = false
        binding.rvLoading.isVisible = false
        binding.rvCars.isVisible = true
        updateListView(cars)
    }

    private fun updateListView(cars: List<CarsVO>) {
        if (cars.isEmpty()) {
            binding.rvEmpty.isVisible = true
            carsAdapter.updateCars(cars)
        } else {
            binding.rvEmpty.isVisible = false
            carsAdapter.updateCars(cars)
        }

    }

    private fun showError() {
        binding.rvError.isVisible = true
        binding.rvLoading.isVisible = false
        binding.rvCars.isVisible = false
    }

    private fun showLoading() {
        binding.rvError.isVisible = false
        binding.rvLoading.isVisible = true
        binding.rvCars.isVisible = false
    }

    private fun setupViews() {
        binding = FragmentHomeBinding.bind(requireView())
        with(binding) {
            rvCars.adapter = carsAdapter
            viewModel.fetchCars()

            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                private var job: Job? = null

                override fun onQueryTextSubmit(queryName: String?): Boolean {
                    mainSearch.clearFocus()
                    queryName.isNullOrEmpty().let { boo ->
                        if (!boo) viewModel.searchCars(queryName!!)
                    }
                    return false
                }

                override fun onQueryTextChange(queryName: String?): Boolean {
                    job?.cancel()
                    queryName.isNullOrEmpty().let { boo ->
                        job = lifecycleScope.launch {
                            delay(1_000)
                            if (!boo) viewModel.searchCars(queryName!!)
                            else viewModel.fetchCars()
                        }
                    }
                    return false
                }
            })

        }
    }

    private fun getAllLeadsAndPushToUrl() {
        lifecycleScope.launch {
            dao().getAllLeads().collect { result ->
                leads = result.toList()
                viewModel.sendLeads(leads, viewLifecycleOwner)
            }
        }
    }

    private fun onItemClicked(cars: CarsVO) {
        cars.let {
            UserFormDialog(requireContext(), it).show()
        }
    }

    private fun dao(): LeadDao {
        val app = requireContext().applicationContext as App
        return app.db.leadDao()
    }

}