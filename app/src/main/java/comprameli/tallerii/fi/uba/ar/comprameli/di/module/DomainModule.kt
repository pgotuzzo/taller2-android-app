package comprameli.tallerii.fi.uba.ar.comprameli.di.module

import com.pgotuzzo.llavero.di.scope.PerApplication
import comprameli.tallerii.fi.uba.ar.comprameli.data.SessionDao
import comprameli.tallerii.fi.uba.ar.comprameli.domain.SessionService
import comprameli.tallerii.fi.uba.ar.comprameli.domain.SessionServiceImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    @PerApplication
    fun provideSessionService(sessionDao: SessionDao): SessionService =
            SessionServiceImpl(sessionDao)


}