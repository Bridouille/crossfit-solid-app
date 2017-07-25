package com.bridou_n.crossfitsolid.features.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.ScrollView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.MainActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.component
import com.bridou_n.crossfitsolid.utils.showSnackbar
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract.View {

    @BindView(R.id.container) lateinit var container: ScrollView
    @BindView(R.id.et_email) lateinit var email: EditText
    @BindView(R.id.et_password) lateinit  var password: EditText

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager

    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        component().inject(this)

        presenter = LoginPresenter(this, bookingService, prefs)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    @OnClick(R.id.fab_login)
    fun onLoginClicked() {
        presenter.onLoginClicked(email.text.toString(), password.text.toString())
    }

    override fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showError(err: String) {
        showSnackbar(container, err)
    }

    override fun onPause() {
        presenter.stop()
        super.onPause()
    }
}
