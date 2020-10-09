package com.msalikhov.dictionarysample.di.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject
import javax.inject.Provider

class DaggerFragmentFactory @Inject constructor(private val fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment = try {
        val fragmentClass = loadFragmentClass(classLoader, className)
        fragmentProviders[fragmentClass]?.get() ?: fragmentClass.getConstructor().newInstance()
    } catch (e: InstantiationException) {
        throw Fragment.InstantiationException(
            "Unable to instantiate fragment $className: make sure class name exists, is public, and has an empty constructor that is public",
            e
        )
    } catch (e: IllegalAccessException) {
        throw Fragment.InstantiationException(
            "Unable to instantiate fragment $className: make sure class name exists, is public, and has an empty constructor that is public",
            e
        )
    } catch (e: NoSuchMethodException) {
        throw Fragment.InstantiationException(
            "Unable to instantiate fragment $className: could not find Fragment constructor",
            e
        )
    } catch (e: InvocationTargetException) {
        throw Fragment.InstantiationException(
            "Unable to instantiate fragment $className: calling Fragment constructor caused an exception",
            e
        )
    }
}