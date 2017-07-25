package com.bridou_n.crossfitsolid.features.login

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.utils.setStatusBarColor

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.fab_login)
    lateinit var fabLogin : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        setStatusBarColor(R.color.divider)

        /*bookingService.login(LoginRequest("nicobr65@gmail.com", "toto42h"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    Log.d("hey there", "${resp.expires_in}")
                }, { err ->
                    err.printStackTrace()
                })*/
    }
}
