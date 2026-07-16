package nacholab.frame.fullclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.frame.fullclient.data.repository.ConnectionConfigRepositoryImpl
import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FullClientModule {

    @Binds
    @Singleton
    abstract fun bindConnectionConfigRepository(
        impl: ConnectionConfigRepositoryImpl
    ): ConnectionConfigRepository

}
