package com.msalikhov.dictionarysample.presentation.view.translation

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
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.FragmentTranslationBinding
import com.msalikhov.dictionarysample.domain.translation.model.LanguageModel
import com.msalikhov.dictionarysample.presentation.presenter.translation.TranslationPresenter
import com.msalikhov.dictionarysample.utils.recycler.ItemMarginDecorator
import com.msalikhov.dictionarysample.utils.recycler.SimpleAdapterFactory
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.max

class TranslationFragment @Inject constructor(
    private val presenterProvider: Provider<TranslationPresenter>
) : MvpAppCompatFragment(R.layout.fragment_translation), TranslationView {

    private val binding by viewBinding(FragmentTranslationBinding::bind)
    private val presenter by moxyPresenter { presenterProvider.get() }
    private val supportedLanguagesAdapter: ListAdapter<LanguageModel, *> =
        SimpleAdapterFactory.build(
            R.layout.item_supported_language,
            this::createSupportedLanguageViewHolder
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.supportedLanguages.adapter = supportedLanguagesAdapter
        binding.supportedLanguages.addItemDecoration(
            ItemMarginDecorator(resources.getDimension(R.dimen.default_margin_half).toInt())
        )
        binding.translationInput.doAfterTextChanged {
            presenter.onTextChanged(it?.toString().orEmpty())
        }
        binding.swapLanguages.setOnClickListener {
            presenter.swapLanguages()
        }
    }

    override fun updateInputLanguage(name: String) {
        binding.inputLanguage.text = name
    }

    override fun setInput(text: String) {
        binding.translationInput.setText(text)
    }

    override fun displayTranslationResult(result: String) {
        binding.translationOutput.text = result
    }

    override fun displaySupportedLanguages(results: List<LanguageModel>) {
        supportedLanguagesAdapter.submitList(results) {
            binding.supportedLanguages.scrollToPosition(max(results.indexOfFirst { it.isSelected }, 0))
        }
    }

    override fun displayError(throwable: Throwable) {
        Toast
            .makeText(requireContext(), throwable.localizedMessage.orEmpty(), Toast.LENGTH_LONG)
            .show()
    }

    override fun updateSupportedLanguagesProgress(loading: Boolean) {
        binding.supportedLanguagesProgressBar.isVisible = loading
    }

    override fun updateTranslationProgress(loading: Boolean) {
        binding.translationProgressBar.isVisible = loading
    }

    private fun createSupportedLanguageViewHolder(view: View) =
        object : RecyclerView.ViewHolder(view), (LanguageModel) -> Unit, View.OnClickListener {
            private val textView = itemView as TextView

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                presenter.changeTargetLanguage(supportedLanguagesAdapter.currentList[adapterPosition].code)
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