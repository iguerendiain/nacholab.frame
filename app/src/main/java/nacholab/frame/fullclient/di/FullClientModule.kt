package nacholab.frame.fullclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.frame.fullclient.data.repository.ConnectionConfigRepositoryImpl
import nacholab.frame.fullclient.data.repository.RemoteReceptorClientRepositoryImpl
import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import nacholab.frame.fullclient.domain.repository.RemoteReceptorClientRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FullClientModule {

    @Binds
    @Singleton
    abstract fun bindConnectionConfigRepository(
        impl: ConnectionConfigRepositoryImpl
    ): ConnectionConfigRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReceptorClientRepository(
        impl: RemoteReceptorClientRepositoryImpl
    ): RemoteReceptorClientRepository

}
