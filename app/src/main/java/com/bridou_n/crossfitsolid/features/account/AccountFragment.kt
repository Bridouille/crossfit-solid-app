package com.bridou_n.crossfitsolid.features.account

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.BuildConfig
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.features.login.LoginActivity
import com.bridou_n.crossfitsolid.models.account.Profile
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import com.bridou_n.crossfitsolid.utils.extensionFunctions.setStatusBarColor
import com.bridou_n.crossfitsolid.utils.extensionFunctions.snackBar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.realm.Realm
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class AccountFragment : Fragment(), AccountContract.View {

    @BindView(R.id.container) lateinit var container: CoordinatorLayout

    @BindView(R.id.profile_image) lateinit var profileImage: CircleImageView
    @BindView(R.id.name) lateinit var name: TextView
    @BindView(R.id.email) lateinit var email: TextView
    @BindView(R.id.cardnumber) lateinit var cardnumber: TextView
    @BindView(R.id.customernumber) lateinit var customernumber: TextView
    @BindView(R.id.phonenumber) lateinit var phone: TextView

    @BindView(R.id.app_version) lateinit var version: TextView

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager
    @Inject lateinit var gson: Gson
    var realm: Realm = Realm.getDefaultInstance()

    lateinit var presenter: AccountContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        setStatusBarColor(R.color.colorPrimaryDark)
        presenter = AccountPresenter(this, bookingService, realm, prefs, gson)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_account, container, false)
        ButterKnife.bind(this, v)
        presenter.start()

        version.text = String.format("version %s %s", BuildConfig.VERSION_NAME, if (BuildConfig.DEBUG) "(debug)" else "")

        return v
    }

    override fun showProfile(profile: Profile) {
        val profileUrl = profile.profileImage ?: "https://crossfitsolid.brponline.se/images/profile-image-${profile.sex}.2017.1102.png"

        Picasso.with(context)
                .load(profileUrl)
                .placeholder(R.drawable.image_placeholder)
                .noFade()
                .into(profileImage)

        name.text = "${profile.firstName} ${profile.lastName}"
        email.text = profile.email ?: getString(R.string.not_provided)
        cardnumber.text = String.format("%s%s", getString(R.string.cardnumber), profile.cardNumber)
        customernumber.text = String.format("%s%s", getString(R.string.customernumber), profile.customerNumber)
        phone.text = if (profile.mobilePhone == null) {
            getString(R.string.not_provided)
        } else {
            String.format("+%d %s", profile.mobilePhone.countryCode ?: 46, profile.mobilePhone.number ?: "")
        }
    }

    override fun showError(err: String?) {
        val snackBar = snackBar(container, err ?: getString(R.string.an_error_occurred), Snackbar.LENGTH_INDEFINITE, {
            presenter.refresh()
        })

        snackBar.show()
    }

    @OnClick(R.id.logout_btn)
    fun onLogoutClicked() {
        presenter.logout()
    }

    override fun logoutRedirect() {
        startActivity(Intent(activity, LoginActivity::class.java))
        activity.finish()
    }

    override fun onDestroy() {
        presenter.stop()
        realm.close()
        super.onDestroy()
    }
}
