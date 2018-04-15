package com.dev.rosty.placeholder.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.rosty.placeholder.PlaceholderApp
import com.dev.rosty.placeholder.injection.VMFactory

abstract class BaseFragment<VM : ViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    protected var layout: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (layout == null) layout = inflater.inflate(getLayoutRes(), container, false)

        return layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders
                .of(this, VMFactory(context!!.applicationContext as PlaceholderApp))
                .get(getViewModelClass())
    }

    abstract fun getLayoutRes(): Int
    abstract fun getViewModelClass(): Class<VM>
}