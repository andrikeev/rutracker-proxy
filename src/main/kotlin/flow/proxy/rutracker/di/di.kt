package flow.proxy.rutracker.di

import flow.proxy.rutracker.api.HttpClientFactory
import flow.proxy.rutracker.api.HttpClientFactoryImpl
import flow.proxy.rutracker.api.RuTrackerApi
import flow.proxy.rutracker.api.RuTrackerApiImpl
import flow.proxy.rutracker.domain.*
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

val appModule = module {
    single<HttpClientFactory> { HttpClientFactoryImpl() }
    single<RuTrackerApi> { RuTrackerApiImpl(get()) }

    single { AddCommentUseCase(get(), get()) }
    single { AddFavoriteUseCase(get(), get()) }
    single { GetCategoryPageUseCase(get()) }
    single { GetCommentsPageUseCase(get()) }
    single { GetCurrentProfileUseCase(get(), get()) }
    single { GetFavoritesUseCase(get()) }
    single { GetForumUseCase(get()) }
    single { GetIndexUseCase(get()) }
    single { GetProfileUseCase(get()) }
    single { GetSearchPageUseCase(get()) }
    single { GetTorrentFileUseCase(get()) }
    single { GetTopicUseCase(get()) }
    single { GetTorrentUseCase(get()) }
    single { LoginUseCase(get(), get()) }
    single { RemoveFavoriteUseCase(get(), get()) }
    single { WithFormTokenUseCase(get()) }
}

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = lazy { get<T>(qualifier, parameters) }

inline fun <reified T : Any> get(
    qualifier: Qualifier? = null, noinline parameters: ParametersDefinition? = null
) = GlobalContext.get().get<T>(qualifier, parameters)
