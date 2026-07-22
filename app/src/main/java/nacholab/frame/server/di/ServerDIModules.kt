package nacholab.frame.server.di

import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.frame.server.data.repository.MediaItemRepositoryFS
import nacholab.frame.server.data.repository.ServerConfigRepositorySP
import nacholab.frame.server.data.repository.SettingsRepositorySP
import nacholab.frame.server.domain.repository.ServerConfigRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServerRespositoryProviders{

    @Provides
    fun providesSettingsRepository(sp: SharedPreferences) = SettingsRepositorySP(sp)

    @Provides
    fun providesMediaItemsRepository() = MediaItemRepositoryFS()

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
