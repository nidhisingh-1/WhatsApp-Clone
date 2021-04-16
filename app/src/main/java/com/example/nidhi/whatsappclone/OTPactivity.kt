package com.example.nidhi.whatsappclone

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.nidhi.whatsappclone.R.layout
import com.example.nidhi.whatsappclone.R.string
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.otp_activity.*
import java.util.concurrent.TimeUnit

const val PHONE_NUMBER = "PhoneNumber"

class OTPactivity : AppCompatActivity() {
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var phoneNumber: String? = null
    var mVerificationId: String? = null
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.otp_activity)
        initViews()
        startVerify()

    }

    private fun startVerify() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber!!,
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
        showTimer(60000)
    }


    private fun showTimer(millisecInFuture: Long) {
        resendBtn.isEnabled = false
        object : CountDownTimer(millisecInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                counterTv.isVisible = true
                counterTv.text = getString(string.second_remaining, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                resendBtn.isEnabled = true
                counterTv.isVisible = false

            }

        }.start()

    }

    private fun initViews() {
        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        verifyTv.text = getString(R.string.verify_number, phoneNumber)
        setSpannableString()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                val smsCode: String? = credential.smsCode
                if (smsCode.isNullOrBlank())
                    sentcodeEt.setText(smsCode)
                // signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {


                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("Exception:", "FirebaseAuthInvalidCredentialsException", e)
                    Log.e("=========:", "FirebaseAuthInvalidCredentialsException " + e.message)
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("Exception:", "FirebaseTooManyRequestsException", e)
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                mVerificationId = verificationId
                mResendToken = token
            }
        }
    }


    private fun setSpannableString() {
        val span = SpannableString(getString(R.string.waiting_text, phoneNumber))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                //send back
                showloginActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ds.linkColor
            }

        }

        span.setSpan(clickableSpan, span.length - 13, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        waitingTv.movementMethod = LinkMovementMethod.getInstance()
        waitingTv.text = span
    }

    private fun showloginActivity() {

        startActivity(
            Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) //stack of an activity
        )

    }

    override fun onBackPressed() {

    }

    fun Context.createProgressDialog(message: String, isCancelable: Boolean): ProgressDialog {
        return ProgressDialog(this).apply {
            setCancelable(isCancelable)
            setCanceledOnTouchOutside(false)
            setMessage(message)
        }
    }

}