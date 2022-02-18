package com.tutv.android.di

import android.content.Context
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

object ContainerLocator {
    private var container: Container? = null
    fun locateComponent(context: Context?): Container? {
        if (container == null) setComponent(AppContainer(context))
        return container
    }

    fun setComponent(container: Container?) {
        ContainerLocator.container = container
    }
}