package com.msalikhov.dictionarysample.utils.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface ViewModelDisposeBag {
    fun Disposable.disposeOnCleared()
    fun onCleared()
}

class ViewModelDisposeBagImpl : ViewModelDisposeBag {
    private val container = CompositeDisposable()

    override fun Disposable.disposeOnCleared() {
        container.add(this)
    }

    override fun onCleared() {
        container.clear()
    }
}