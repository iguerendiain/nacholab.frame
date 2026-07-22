package nacholab.frame.fullclient.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nacholab.frame.fullclient.data.repository.ConnectionConfigRepositorySP
import nacholab.frame.fullclient.data.repository.RemoteControlClientRepositorySocket
import nacholab.frame.fullclient.domain.repository.ConnectionConfigRepository
import nacholab.frame.fullclient.domain.repository.RemoteControlClientRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FullClientModule {

    @Binds
    @Singleton
    abstract fun bindConnectionConfigRepository(
        impl: ConnectionConfigRepositorySP
    ): ConnectionConfigRepository

    @Binds
    @Singleton
    abstract fun bindRemoteReceptorClientRepository(
        impl: RemoteControlClientRepositorySocket
    ): RemoteControlClientRepository

}
