package nacholab.frame.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nacholab.frame.data.MediaItemRepository
import nacholab.frame.data.SettingsRepository
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