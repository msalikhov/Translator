package com.msalikhov.dictionarysample.presentation.view.translation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.FragmentTranslationsHistoryBinding
import com.msalikhov.dictionarysample.domain.translation.model.TranslationHistoryModel
import com.msalikhov.dictionarysample.presentation.presenter.translation.TranslationsHistoryPresenter
import com.msalikhov.dictionarysample.utils.recycler.SimpleAdapterFactory
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class TranslationsHistoryFragment @Inject constructor(
    private val presenterProvider: Provider<TranslationsHistoryPresenter>
) : MvpAppCompatFragment(R.layout.fragment_translations_history), TranslationsHistoryView {

    private val binding by viewBinding(FragmentTranslationsHistoryBinding::bind)
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val adapter: ListAdapter<TranslationHistoryModel, *> = SimpleAdapterFactory.build(
        R.layout.item_history_translation,
        this::createItemViewHolder
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.historyItems.adapter = adapter
    }

    override fun displayTranslationHistoryItems(items: List<TranslationHistoryModel>) {
        adapter.submitList(items)
    }

    override fun displayError(throwable: Throwable) {
        Toast
            .makeText(requireContext(), throwable.localizedMessage.orEmpty(), Toast.LENGTH_LONG)
            .show()
    }

    override fun updateProgress(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun createItemViewHolder(view: View) =
        object : RecyclerView.ViewHolder(view), (TranslationHistoryModel) -> Unit, View.OnClickListener {
            private val inputLangName: TextView = itemView.findViewById(R.id.inputLangName)
            private val inputText: TextView = itemView.findViewById(R.id.inputText)
            private val outputLangName: TextView = itemView.findViewById(R.id.outputLangName)
            private val outputText: TextView = itemView.findViewById(R.id.outputText)
            private val favouriteMark: ImageView = itemView.findViewById(R.id.favouriteMark)

            init {
                favouriteMark.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                presenter.toggleFavouriteItem(adapter.currentList[adapterPosition])
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