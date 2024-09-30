package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.AppThemeMode
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private val setAppThemeModeUseCase by lazy { Creator.provideSetAppThemeModeUseCase() }
    private val getAppThemeModeUseCase by lazy { Creator.provideGetAppThemeModeUseCase() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_settings)
        val shareAppItemSettings = findViewById<FrameLayout>(R.id.share_app_item_settings)
        val supportItemSettings = findViewById<FrameLayout>(R.id.support_item_settings)
        val eulaItemSettings = findViewById<FrameLayout>(R.id.eula_item_settings)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = getAppThemeModeUseCase.execute().value

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setAppThemeModeUseCase.execute(AppThemeMode.DarkMode)
            } else {
                setAppThemeModeUseCase.execute(AppThemeMode.LightMode)
            }
        }

        toolbar.setNavigationOnClickListener { this.finish() }

        shareAppItemSettings.setOnClickListener { onShareClick() }

        supportItemSettings.setOnClickListener { onSupportClick() }

        eulaItemSettings.setOnClickListener { onEulaClick() }
    }

    private fun onShareClick() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
            setType("text/plain")
        }

        startActivity(intent)
    }

    private fun onSupportClick() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_template)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email_template))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.text_email_template))
        }

        startActivity(intent)
    }

    private fun onEulaClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.eula_link)))

        startActivity(intent)
    }
}