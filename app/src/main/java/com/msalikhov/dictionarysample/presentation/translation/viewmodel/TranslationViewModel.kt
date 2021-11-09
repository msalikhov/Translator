package com.msalikhov.dictionarysample.presentation.translation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.msalikhov.dictionarysample.domain.translation.TranslationInteractor
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.domain.translation.model.TranslationModel
import com.msalikhov.dictionarysample.utils.extensions.mapToResult
import com.msalikhov.dictionarysample.utils.livedata.BaseViewModel
import com.msalikhov.dictionarysample.utils.livedata.LCEState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TranslationViewModel @AssistedInject constructor(
    private val translationsInteractor: TranslationInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val inputSubject = PublishSubject.create<String>()
    private val supportedLanguagesSubject = BehaviorSubject.createDefault(emptyList<LanguageModel>())
    private var lastTranslationModel: TranslationModel? = null

    private val _inputLanguage = MutableLiveData<String>()
    val inputLanguage: LiveData<String> get() = _inputLanguage

    private val _input = MutableLiveData<String>()
    val input: LiveData<String> get() = _input

    private val _translationResult = MutableLiveData<LCEState<String>>()
    val translationResult: LiveData<LCEState<String>> get() = _translationResult

    private val _supportedLanguages = MutableLiveData<LCEState<List<LanguageModel>>>()
    val supportedLanguages: LiveData<LCEState<List<LanguageModel>>> get() = _supportedLanguages

    init {
        Observable
            .combineLatest(inputSubject, supportedLanguagesSubject, ::Pair)
            .doOnNext { _translationResult.value = LCEState.Loading }
            .debounce(QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .switchMapSingle { (text, models) ->
                translationsInteractor
                    .translateText(text, models)
                    .mapToResult()
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                result
                    .onSuccess {
                        _inputLanguage.value = it.inputLangName
                        _translationResult.value = LCEState.Success(it.text)
                        lastTranslationModel = it
                    }
                    .onFailure {
                        _translationResult.value = LCEState.Error(it)
                    }
            }
            .disposeOnCleared()

        translationsInteractor
            .getSupportedLanguages()
            .mapToResult()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _supportedLanguages.value = LCEState.Loading }
            .subscribe { result ->
                result
                    .onSuccess {
                        updateSupportedLanguages(it)
                    }
                    .onFailure {
                        _supportedLanguages.value = LCEState.Error(it)
                    }
            }
            .disposeOnCleared()
    }

    fun onTextChanged(text: String) {
        inputSubject.onNext(text)
    }

    fun changeTargetLanguage(code: String) {
        val models = supportedLanguagesSubject.value ?: return
        updateSupportedLanguages(models.map { it.copy(isSelected = it.code == code) })
    }

    fun swapLanguages() {
        val model = lastTranslationModel ?: return
        changeTargetLanguage(model.inputLangCode)
        _input.value = model.text
    }

    private fun updateSupportedLanguages(it: List<LanguageModel>) {
        supportedLanguagesSubject.onNext(it)
        _supportedLanguages.value = LCEState.Success(it)
    }

    private companion object {
        const val QUERY_DEBOUNCE_MILLIS = 500L
    }

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): TranslationViewModel
    }
}