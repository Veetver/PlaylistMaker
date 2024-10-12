package com.example.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.model.AppThemeMode
import com.example.playlistmaker.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.domain.usecase.SetAppThemeModeUseCase
import com.example.playlistmaker.share.domain.usecase.ContactSupportUseCase
import com.example.playlistmaker.share.domain.usecase.OpenEulaUseCase
import com.example.playlistmaker.share.domain.usecase.ShareAppUseCase

class SettingsViewModel(
    getAppThemeModeUseCase: GetAppThemeModeUseCase,
    private val setAppThemeModelUseCase: SetAppThemeModeUseCase,
    private val shareAppUseCase: ShareAppUseCase,
    private val contactSupportUseCase: ContactSupportUseCase,
    private val openEulaUseCase: OpenEulaUseCase,
) : ViewModel() {

    private val _appThemeModeLiveData: MutableLiveData<AppThemeMode> =
        MutableLiveData(getAppThemeModeUseCase.execute())
    val appThemeModeLiveData: LiveData<AppThemeMode> = _appThemeModeLiveData

    fun changeTheme(darkMode: Boolean) {
        val mode = when (darkMode) {
            true -> AppThemeMode.DarkMode
            false -> AppThemeMode.LightMode
        }
        val success = setAppThemeModelUseCase.execute(mode)
        if (success) _appThemeModeLiveData.postValue(mode)
    }

    fun shareApp() {
        shareAppUseCase.execute()
    }

    fun contactSupport() {
        contactSupportUseCase.execute()
    }

    fun openEula() {
        openEulaUseCase.execute()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val getAppThemeModeUseCase = Creator.provideGetAppThemeModeUseCase()
                val setAppThemeModelUseCase = Creator.provideSetAppThemeModeUseCase()
                val shareAppUseCase = Creator.provideShareAppUseCase()
                val contactSupportUseCase = Creator.provideContactSupportUseCase()
                val openEulaUseCase = Creator.provideOpenEulaUseCase()

                SettingsViewModel(
                    getAppThemeModeUseCase,
                    setAppThemeModelUseCase,
                    shareAppUseCase,
                    contactSupportUseCase,
                    openEulaUseCase
                )
            }
        }
    }
}