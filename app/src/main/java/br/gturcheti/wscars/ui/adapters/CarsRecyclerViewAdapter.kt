package br.gturcheti.wscars.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.gturcheti.wscars.databinding.ItemCarsBinding
import br.gturcheti.wscars.ui.vo.CarsVO

class CarsRecyclerViewAdapter(
    private val onItemClicked: (cars: CarsVO) -> Unit
) : RecyclerView.Adapter<CarsRecyclerViewAdapter.ViewHolder>() {

    private val cars: MutableList<CarsVO> = mutableListOf()

    inner class ViewHolder(
        private val binding: ItemCarsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cars: CarsVO) {
            with(binding) {
                carsValue.text = cars.valor
                carsModel.text = "${cars.marca} - ${cars.modelo}"
                carsYear.text = cars.ano
                carsDoors.text = cars.portas
                carsColor.text = cars.cor
                carsFuel.text = cars.combustivel
                carButton.setOnClickListener { onItemClicked.invoke(cars) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCarsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    fun updateCars(cars: List<CarsVO>){
        this.cars.clear()
        this.cars.addAll(cars)
        notifyDataSetChanged()
    }

}