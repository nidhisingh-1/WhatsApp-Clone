package com.example.nidhi.whatsappclone.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.nidhi.whatsappclone.PHONE_NUMBER
import com.example.nidhi.whatsappclone.R
import com.example.nidhi.whatsappclone.otpActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var PhoneNumber: String
    private lateinit var CountryCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //add hint request
        //next button is enabled only if is not empty and number length is less than 10
        phoneNumberEt.addTextChangedListener {
            nextBtn.isEnabled = (it.isNullOrEmpty() || it.length <= 10)
        }

        nextBtn.setOnClickListener {
            checknumber()
        }
    }

    private fun checknumber() {
        CountryCode = ccp.selectedCountryCodeWithPlus
        PhoneNumber = CountryCode + phoneNumberEt.text.toString()

        notifyUser()
    }

    private fun notifyUser() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage(
                "We will be verifying the phone number: $PhoneNumber" +
                        "Is the number correct? or Would you like to edit?"
            )
            setPositiveButton("okay") { _, _ ->
                showOTPactiviy()
            }
            setNegativeButton("Edit") { dialogue, which ->
                dialogue.dismiss()
            }
            setCancelable(false)
            create()
            show()

        }
    }

    private fun showOTPactiviy() {

        startActivity(Intent(this, otpActivity ::class.java).putExtra(PHONE_NUMBER, PhoneNumber))
        finish()
    }
}