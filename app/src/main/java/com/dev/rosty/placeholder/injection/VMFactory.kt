package com.dev.rosty.placeholder.injection

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.dev.rosty.placeholder.PlaceholderApp

class VMFactory(private val application: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val t = super.create(modelClass)

        if (t is AppComponent.Injectable)
            t.inject((application as PlaceholderApp).appComponent)

        return t
    }
}