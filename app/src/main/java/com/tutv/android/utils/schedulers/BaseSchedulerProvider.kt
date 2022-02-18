package com.tutv.android.utils.schedulers

import io.reactivex.Scheduler
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

/** Allow providing different types of [Scheduler]s.  */
interface BaseSchedulerProvider {
    open fun computation(): Scheduler
    open fun io(): Scheduler
    open fun ui(): Scheduler
}