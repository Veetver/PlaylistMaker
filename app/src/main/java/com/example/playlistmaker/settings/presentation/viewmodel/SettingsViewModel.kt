package com.example.playlistmaker.settings.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.model.AppThemeMode
import com.example.playlistmaker.settings.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.settings.domain.usecase.SetAppThemeModeUseCase
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

    private val _appThemeModeLiveData: MutableLiveData<AppThemeMode> = MutableLiveData(getAppThemeModeUseCase.execute())
    val appThemeModeLiveData: LiveData<AppThemeMode> = _appThemeModeLiveData

    fun changeTheme(darkMode: Boolean) {
        if (_appThemeModeLiveData.value?.value == darkMode) return

        val mode = when (darkMode) {
            true -> AppThemeMode.DarkMode
            false -> AppThemeMode.LightMode
        }
        val success = setAppThemeModelUseCase.execute(mode)
        if (success) _appThemeModeLiveData.value = mode
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
}