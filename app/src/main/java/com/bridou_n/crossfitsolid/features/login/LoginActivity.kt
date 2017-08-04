package com.bridou_n.crossfitsolid.features.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ScrollView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridou_n.crossfitsolid.API.LoginService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.MainActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import com.bridou_n.crossfitsolid.utils.extensionFunctions.hideKeyboard
import com.bridou_n.crossfitsolid.utils.extensionFunctions.hideView
import com.bridou_n.crossfitsolid.utils.extensionFunctions.showSnackbar
import com.wang.avi.AVLoadingIndicatorView
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginContract.View {

    @BindView(R.id.container) lateinit var container: ScrollView
    @BindView(R.id.et_email) lateinit var email: EditText
    @BindView(R.id.et_password) lateinit  var password: EditText
    @BindView(R.id.fab_login) lateinit var fabLogin: FloatingActionButton
    @BindView(R.id.loading_indicator) lateinit var loading: AVLoadingIndicatorView

    @Inject lateinit var loginService: LoginService
    @Inject lateinit var prefs: PreferencesManager

    lateinit var presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        component().inject(this)

        presenter = LoginPresenter(this, loginService, prefs)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    @OnClick(R.id.forgot_password)
    fun onForgotPasswordClicked() {
        val view = layoutInflater.inflate(R.layout.dialog_reset_password, null)
        val input = view.findViewById(R.id.input_field) as EditText

        val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setTitle(getString(R.string.enter_your_email))
                .setView(view)
                .setPositiveButton(getString(android.R.string.ok), DialogInterface.OnClickListener {
                    _, _ -> presenter.onForgotPassword(input.text.toString())
                })
                .setNegativeButton(getString(android.R.string.cancel), null)

        builder.show()
    }

    @OnClick(R.id.fab_login)
    fun onLoginClicked() {
        presenter.onLoginClicked(email.text.toString(), password.text.toString())
    }

    override fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            hideKeyboard()
            fabLogin.hideView(true)
            loading.show()
        } else {
            fabLogin.show()
            loading.hideView(true)
        }
    }

    override fun showError(err: Int) {
        showSnackbar(container, getString(err))
    }

    override fun onPause() {
        presenter.stop()
        super.onPause()
    }
}
