package com.bridou_n.crossfitsolid.features.classes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.R
import com.bridou_n.crossfitsolid.models.GroupActivity
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.component
import com.bridou_n.crossfitsolid.utils.extensionFunctions.showSnackbar
import javax.inject.Inject

/**
 * Created by bridou_n on 27/07/2017.
 */

class ClassesFragment : Fragment(), ClassesContract.View {

    @Inject lateinit var bookingService: BookingService
    @Inject lateinit var prefs: PreferencesManager

    lateinit var presenter: ClassesContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)

        presenter = ClassesPresenter(this, bookingService, prefs)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_classes, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun displayClasses(bookings: Array<GroupActivity>) {

    }

    override fun showLoading(show: Boolean) {

    }

    override fun showError(err: String?) {
        // showSnackbar(view, err)
    }

    override fun onPause() {
        super.onPause()
        presenter.stop()
    }
}