package com.example.playlistmaker.creator

object Creator {
    /*private val gson by lazy { Gson() }

    private fun getApplicationContext(): Context {
        return App.instance.applicationContext
    }

    private fun getRemoteDataSource(): RemoteDataSource {
        return RetrofitDataSource()
    }

    private fun getLocalDataSource(): LocalDataSource {
        return SharedPrefsDataSource(getApplicationContext())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(getRemoteDataSource())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSettingsRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(getApplicationContext())
    }

    fun provideSetAppThemeModeUseCase(): SetAppThemeModeUseCase {
        return SetAppThemeModeUseCase(getSettingsRepository())
    }

    fun provideGetAppThemeModeUseCase(): GetAppThemeModeUseCase {
        return GetAppThemeModeUseCase(getSettingsRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun proidePlayerInterator(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getLocalDataSource())
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(getApplicationContext())
    }

    private fun provideShareRepository(): ShareRepository {
        return ShareRepositoryImpl(
            context = getApplicationContext(),
            externalNavigator = provideExternalNavigator()
        )
    }

    fun provideShareAppUseCase(): ShareAppUseCase {
        return ShareAppUseCase(provideShareRepository())
    }

    fun provideContactSupportUseCase(): ContactSupportUseCase {
        return ContactSupportUseCase(provideShareRepository())
    }

    fun provideOpenEulaUseCase(): OpenEulaUseCase {
        return OpenEulaUseCase(provideShareRepository())
    }

    fun provideGson(): Gson {
        return gson
    }*/
}