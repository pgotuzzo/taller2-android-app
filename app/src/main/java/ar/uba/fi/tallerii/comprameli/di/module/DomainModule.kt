package ar.uba.fi.tallerii.comprameli.di.module

import ar.uba.fi.tallerii.comprameli.data.session.SessionDao
import ar.uba.fi.tallerii.comprameli.di.scope.PerApplication
import ar.uba.fi.tallerii.comprameli.domain.SessionService
import ar.uba.fi.tallerii.comprameli.domain.SessionServiceImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    @PerApplication
    fun provideSessionService(sessionDao: SessionDao): SessionService =
            SessionServiceImpl(sessionDao)


}