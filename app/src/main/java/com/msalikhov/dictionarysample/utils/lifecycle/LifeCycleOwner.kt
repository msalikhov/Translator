package com.msalikhov.dictionarysample.utils.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

inline fun Fragment.savedStateViewModelFactory(crossinline provider: (handle: SavedStateHandle) -> ViewModel) =
    abstractSavedStateViewModelFactory(this, arguments, provider)

inline fun AppCompatActivity.savedStateViewModelFactory(crossinline provider: (handle: SavedStateHandle) -> ViewModel) =
    abstractSavedStateViewModelFactory(this, intent.extras, provider)

inline fun simpleViewModelFactory(crossinline provider: () -> ViewModel) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return provider() as T
        }
    }

inline fun abstractSavedStateViewModelFactory(owner: SavedStateRegistryOwner, defaultArgs: Bundle?, crossinline provider: (SavedStateHandle) -> ViewModel) =
    object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
            return provider(handle) as T
        }
    }