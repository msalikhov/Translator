package com.msalikhov.dictionarysample.utils.livedata

sealed class LCEState<out T: Any> {
    object Loading: LCEState<Nothing>()
    data class Error(val throwable: Throwable): LCEState<Nothing>(), Consumable<Throwable> by ConsumableImpl(throwable)
    data class Success<T: Any>(val value: T): LCEState<T>(), Consumable<T> by ConsumableImpl(value)
}