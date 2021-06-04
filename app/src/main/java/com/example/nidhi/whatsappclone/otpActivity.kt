package com.example.nidhi.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_otp.*

const val PHONE_NUMBER = "phoneNumber"


class otpActivity : AppCompatActivity() {
    var phoneNumber : String? = null
    private var mCounterDown: CountDownTimer? = null
    private var timeLeft: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        initViews()
        showTimer(60000)
    }

    private fun showTimer(millisecinfuture: Long) {

        resendBtn.isEnabled = false
        mCounterDown = object : CountDownTimer(millisecinfuture, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                countertv.isVisible = true
                countertv.text = "Seconds remaining: " + millisUntilFinished / 1000

                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                resendBtn.isEnabled = true
                countertv.isVisible = false
            }
        }.start()
    }

    private fun initViews() {
        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        verifytv.text = getString(R.string.verify_number, phoneNumber)
        setSpannableString()
    }

    private fun setSpannableString() {
        val span = SpannableString(getString(R.string.verification_text, phoneNumber))
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                showLoginActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ds.linkColor
            }

        }

        span.setSpan(clickableSpan, span.length - 13 , span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        waitingtv.movementMethod = LinkMovementMethod.getInstance()
        waitingtv.text = span

    }

    private fun showLoginActivity() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}