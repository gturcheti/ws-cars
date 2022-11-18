package br.gturcheti.wscars.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import br.gturcheti.wscars.App
import br.gturcheti.wscars.PushLeadService
import br.gturcheti.wscars.R
import br.gturcheti.wscars.data.model.Lead
import br.gturcheti.wscars.databinding.FormularioLeadBinding
import br.gturcheti.wscars.ui.vo.CarsVO

class UserFormDialog(private val context: Context, private val cars: CarsVO) {

    fun show() {
        FormularioLeadBinding.inflate(LayoutInflater.from(context)).apply {

            val alert = AlertDialog.Builder(context, R.style.Style_Dialog_Rounded_Corner)
                .setView(root).show()

            fun showAlert() = alert

            this.userPhoneEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())

            this.leadCloseBtn.setOnClickListener {
                alert.dismiss()
            }

            this.leadSendBtn.setOnClickListener {

                if (isValidEmail(this.userEmailEt.text.toString())
                    && this.userPhoneEt.text.toString().length >= 12) {

                    Thread {
                        val app = context.applicationContext as App
                        val dao = app.db.leadDao()

                        val id = cars.id
                        val nome = this.userNameEt.text.toString()
                        val phone = this.userPhoneEt.text.toString()
                        val email = this.userNameEt.text.toString()

                        dao.insert(
                            Lead(
                                carId = id,
                                clientName = nome,
                                clientPhoneNumber = phone,
                                clientEmail = email
                            )
                        )

                    }.start()

                    alert.dismiss()

                } else if (!isValidEmail(this.userEmailEt.text.toString())) {
                    this.userEmailTf.setError("Informe um e-mail válido")
                } else if (this.userPhoneEt.text.toString().length < 12) {
                    this.userPhoneTf.setError("Verifique o seu telefone")
                }

            }

            showAlert()

        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

}
