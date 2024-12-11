package justicway.base.repo.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import justicway.base.repo.CurrencyRepository
import justicway.base.repo.CurrencyRepositoryImpl
import justicway.base.repo.TaiwanStockRepository
import justicway.base.repo.TaiwanStockRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryProvideModule {
    @Provides
    @Singleton
    fun provideTaiwanStockRepository(taiwanStockRepositoryImpl: TaiwanStockRepositoryImpl): TaiwanStockRepository {
        return taiwanStockRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(currencyRepository: CurrencyRepositoryImpl): CurrencyRepository {
        return currencyRepository
    }

}