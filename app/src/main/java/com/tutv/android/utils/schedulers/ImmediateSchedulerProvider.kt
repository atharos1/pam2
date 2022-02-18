package com.tutv.android.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

/**
 * Implementation of the [BaseSchedulerProvider] making all [Scheduler]s execute
 * synchronously so we can easily run assertions in our tests.
 *
 *
 * To achieve this, we are using the [io.reactivex.internal.schedulers.TrampolineScheduler]
 * from the [Schedulers] class.
 */
class ImmediateSchedulerProvider : BaseSchedulerProvider {
    override fun computation(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return Schedulers.trampoline()
    }
}