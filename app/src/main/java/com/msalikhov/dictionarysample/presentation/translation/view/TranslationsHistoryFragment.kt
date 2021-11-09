package com.msalikhov.dictionarysample.presentation.translation.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.FragmentTranslationsHistoryBinding
import com.msalikhov.dictionarysample.di.app.ComponentHolder
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import com.msalikhov.dictionarysample.presentation.translation.viewmodel.TranslationHistoryViewModel
import com.msalikhov.dictionarysample.utils.livedata.LCEState
import com.msalikhov.dictionarysample.utils.recycler.SimpleAdapterFactory

class TranslationsHistoryFragment : Fragment(R.layout.fragment_translations_history) {

    private val binding by viewBinding(FragmentTranslationsHistoryBinding::bind)
    private val viewModel: TranslationHistoryViewModel by viewModels {
        object: AbstractSavedStateViewModelFactory(this, arguments) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                return ComponentHolder.translationComponent.translationsHistoryViewModelFactory.create(handle) as T
            }
        }
    }

    private val adapter: ListAdapter<TranslationHistoryModel, *> = SimpleAdapterFactory.build(
        R.layout.item_history_translation,
        ::TranslationHistoryViewHolder
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.historyItems.adapter = adapter
        viewModel.translationsHistory.observe(viewLifecycleOwner) { state ->
            when(state) {
                is LCEState.Error -> {
                    binding.progressBar.isVisible = false
                    state.consume()?.let { displayError(it) }
                }
                is LCEState.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is LCEState.Success -> {
                    binding.progressBar.isVisible = false
                    adapter.submitList(state.value)
                }
            }
        }
    }

    private fun displayError(throwable: Throwable) {
        Toast
            .makeText(requireContext(), throwable.localizedMessage.orEmpty(), Toast.LENGTH_LONG)
            .show()
    }

    private inner class TranslationHistoryViewHolder(view: View): RecyclerView.ViewHolder(view), (TranslationHistoryModel) -> Unit, View.OnClickListener {
            private val inputLangName: TextView = itemView.findViewById(R.id.inputLangName)
            private val inputText: TextView = itemView.findViewById(R.id.inputText)
            private val outputLangName: TextView = itemView.findViewById(R.id.outputLangName)
            private val outputText: TextView = itemView.findViewById(R.id.outputText)
            private val favouriteMark: ImageView = itemView.findViewById(R.id.favouriteMark)

            init {
                favouriteMark.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                viewModel.toggleFavouriteItem(adapter.currentList[adapterPosition])
            }

            override fun invoke(item: TranslationHistoryModel) {
                inputLangName.text = item.inputLangName
                inputText.text = item.inputText
                outputLangName.text = item.outputLangName
                outputText.text = item.outputText
                favouriteMark.setImageResource(if (item.isFavourite) {
                    R.drawable.ic_baseline_star_24
                } else {
                    R.drawable.ic_baseline_star_border_24
                })
            }
        }
}