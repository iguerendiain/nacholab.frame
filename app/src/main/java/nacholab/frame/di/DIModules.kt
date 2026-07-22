package nacholab.frame.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nacholab.frame.domain.repository.MediaItemRepository
import nacholab.frame.domain.repository.SettingsRepository
import nacholab.frame.data.repository.ServerConfigRepositoryImpl
import nacholab.frame.domain.repository.ServerConfigRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RespositoryProviders{

    @Provides
    fun providesSettingsRepository(sp: SharedPreferences) = SettingsRepository(sp)

    @Provides
    fun providesMediaItemsRepository() = MediaItemRepository()

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ServerConfigModule {

    @Binds
    @Singleton
    abstract fun bindServerConfigRepository(
        impl: ServerConfigRepositoryImpl
    ): ServerConfigRepository

}

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "stuff_nacho_cares_about",
            Context.MODE_PRIVATE
        )
    }
}