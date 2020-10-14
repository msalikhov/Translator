package com.msalikhov.dictionarysample.di.fragment

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class FragmentMapKey(val value: KClass<out Fragment>)