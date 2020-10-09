package com.msalikhov.dictionarysample

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule(private val testScheduler: Scheduler) : TestRule {

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            RxAndroidPlugins.reset()
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }
            RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }

            RxJavaPlugins.reset()
            RxJavaPlugins.setIoSchedulerHandler { testScheduler }
            RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
            RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

            base.evaluate()
        }
    }
}