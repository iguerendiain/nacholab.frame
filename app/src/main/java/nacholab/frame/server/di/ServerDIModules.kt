package nacholab.frame.server.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nacholab.frame.server.data.repository.MediaItemRepositoryFS
import nacholab.frame.server.data.repository.ServerConfigRepositorySP
import nacholab.frame.server.data.repository.SettingsRepositorySP
import nacholab.frame.server.domain.repository.MediaItemRepository
import nacholab.frame.server.domain.repository.ServerConfigRepository
import nacholab.frame.server.domain.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServerRespositoryProviders{

    @Provides
    fun providesSettingsRepository(sp: SharedPreferences): SettingsRepository = SettingsRepositorySP(sp)

    @Provides
    fun providesMediaItemsRepository(@ApplicationContext context: Context): MediaItemRepository =
        MediaItemRepositoryFS(context)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ServerConfigModule {

    @Binds
    @Singleton
    abstract fun bindServerConfigRepository(
        impl: ServerConfigRepositorySP
    ): ServerConfigRepository

}
