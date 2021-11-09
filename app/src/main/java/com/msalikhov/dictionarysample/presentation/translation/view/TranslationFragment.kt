package com.msalikhov.dictionarysample.presentation.translation.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.FragmentTranslationBinding
import com.msalikhov.dictionarysample.di.app.ComponentHolder
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.presentation.translation.viewmodel.TranslationViewModel
import com.msalikhov.dictionarysample.utils.livedata.LCEState
import com.msalikhov.dictionarysample.utils.recycler.ItemMarginDecorator
import com.msalikhov.dictionarysample.utils.recycler.SimpleAdapterFactory
import kotlin.math.max

class TranslationFragment : Fragment(R.layout.fragment_translation) {

    private val binding by viewBinding(FragmentTranslationBinding::bind)
    private val viewModel: TranslationViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, arguments) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                return ComponentHolder.translationComponent.translationViewModelFactory.create(handle) as T
            }
        }
    }
    private val supportedLanguagesAdapter: ListAdapter<LanguageModel, *> =
        SimpleAdapterFactory.build(
            R.layout.item_supported_language,
            ::SupportedLanguagesViewHolder
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.supportedLanguages.adapter = supportedLanguagesAdapter
        binding.supportedLanguages.addItemDecoration(
            ItemMarginDecorator(resources.getDimension(R.dimen.default_margin_half).toInt())
        )
        binding.translationInput.doAfterTextChanged {
            viewModel.onTextChanged(it?.toString().orEmpty())
        }
        binding.swapLanguages.setOnClickListener {
            viewModel.swapLanguages()
        }
        viewModel.inputLanguage.observe(viewLifecycleOwner) {
            binding.inputLanguage.text = it
        }
        viewModel.input.observe(viewLifecycleOwner) {
            binding.translationInput.setText(it)
        }
        viewModel.supportedLanguages.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LCEState.Error -> {
                    state.consume()?.let { throwable -> displayError(throwable) }
                    binding.supportedLanguagesProgressBar.isVisible = false
                }
                is LCEState.Loading -> {
                    binding.supportedLanguagesProgressBar.isVisible = true
                }
                is LCEState.Success -> {
                    binding.supportedLanguagesProgressBar.isVisible = false
                    supportedLanguagesAdapter.submitList(state.value) {
                        binding.supportedLanguages.scrollToPosition(max(state.value.indexOfFirst { it.isSelected }, 0))
                    }
                }
            }
        }
        viewModel.translationResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LCEState.Error -> {
                    state.consume()?.let { throwable -> displayError(throwable) }
                    binding.translationProgressBar.isVisible = false
                }
                is LCEState.Loading -> {
                    binding.translationProgressBar.isVisible = true
                }
                is LCEState.Success -> {
                    binding.translationProgressBar.isVisible = false
                    binding.translationOutput.text = state.value
                }
            }
        }
    }

    private fun displayError(throwable: Throwable) {
        Toast
            .makeText(requireContext(), throwable.localizedMessage.orEmpty(), Toast.LENGTH_LONG)
            .show()
    }

    private inner class SupportedLanguagesViewHolder(view: View) : RecyclerView.ViewHolder(view), (LanguageModel) -> Unit, View.OnClickListener {
        private val textView = itemView as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            viewModel.changeTargetLanguage(supportedLanguagesAdapter.currentList[bindingAdapterPosition].code)
        }

        override fun invoke(model: LanguageModel) {
            textView.text = if (model.isSelected) {
                buildSpannedString {
                    inSpans(StyleSpan(Typeface.BOLD), UnderlineSpan(), ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorAccent))) {
                        append(model.name)
                    }
                }
            } else {
                buildSpannedString {
                    inSpans(ForegroundColorSpan(Color.BLACK)) {
                        append(model.name)
                    }
                }
            }
        }
    }
}