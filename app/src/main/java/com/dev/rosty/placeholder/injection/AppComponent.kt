package com.dev.rosty.placeholder.injection

import com.dev.rosty.placeholder.common.RxSchedulers
import com.dev.rosty.placeholder.data.DataSource
import com.dev.rosty.placeholder.presentation.details.DetailsViewModel
import com.dev.rosty.placeholder.presentation.posts.PostsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun dataSource(): DataSource
    fun rxSchedulers(): RxSchedulers

    fun inject(viewModel: PostsViewModel)
    fun inject(viewModel: DetailsViewModel)

    interface Injectable {

        fun inject(appComponent: AppComponent)
    }
}
