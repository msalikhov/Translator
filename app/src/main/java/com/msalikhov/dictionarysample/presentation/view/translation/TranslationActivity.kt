package com.msalikhov.dictionarysample.presentation.view.translation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import com.msalikhov.dictionarysample.R
import com.msalikhov.dictionarysample.databinding.ActivityTranslationBinding
import com.msalikhov.dictionarysample.di.app.ComponentHolder
import com.msalikhov.dictionarysample.presentation.CiceroneHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

class TranslationActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private val navigator by lazy { SupportAppNavigator(this, R.id.fragmentContainerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentHolder.translationComponent.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
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