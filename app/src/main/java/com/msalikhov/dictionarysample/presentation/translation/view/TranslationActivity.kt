package com.msalikhov.dictionarysample.presentation.translation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.ActivityTranslationBinding
import com.msalikhov.dictionarysample.presentation.CiceroneHolder
import com.msalikhov.dictionarysample.presentation.translation.screen.TranslationsHistoryScreen
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class TranslationActivity : AppCompatActivity() {

    private val navigator by lazy { SupportAppNavigator(this, R.id.fragmentContainerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setOnMenuItemClickListener {
            CiceroneHolder.router.navigateTo(TranslationsHistoryScreen())
            true
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                binding.toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24)
                binding.toolbar.menu.findItem(R.id.history).isVisible = false
            }else {
                binding.toolbar.navigationIcon = null
                binding.toolbar.menu.findItem(R.id.history).isVisible = true
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        CiceroneHolder.navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        CiceroneHolder.navigationHolder.removeNavigator()
    }
}