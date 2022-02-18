package com.tutv.android.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

/** Provides different types of schedulers.  */
class SchedulerProvider : BaseSchedulerProvider {
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        private var INSTANCE: SchedulerProvider? = null

        // Prevent direct instantiation.
        @Synchronized
        fun getInstance(): SchedulerProvider? {
            if (INSTANCE == null) {
                INSTANCE = SchedulerProvider()
            }
            return INSTANCE
        }
    }
}