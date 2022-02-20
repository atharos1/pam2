package com.tutv.android.di

import android.content.Context

// TODO: refactor the @JvmStatic method on UI migration!
object ContainerLocator {
    private lateinit var container: Container

    @JvmStatic
    fun locateContainer(context: Context): Container {
        if (!this::container.isInitialized) setContainer(AppContainer(context))
        return container
    }

    private fun setContainer(container: Container) {
        ContainerLocator.container = container
    }
}