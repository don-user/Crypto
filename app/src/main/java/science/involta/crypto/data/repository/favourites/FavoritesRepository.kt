package science.involta.crypto.data.repository.favourites

import androidx.lifecycle.LiveData
import science.involta.crypto.api.Result
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.utils.Constants
import javax.inject.Inject

/**
 * Класс [FavoritesRepository] выполняет роль репозитория при помощи которого
 * ViewModel в дальнейшем будет получать данные
 *
 * @param favoritesDataSource источник локальных данных [FavoritesDataSource]
 */
class FavoritesRepository @Inject constructor(private val favoritesDataSource: FavoritesDataSource) {
    /**
     * Список избранных криптовалют
     */
    val favoriteCoins: LiveData<List<CoinsListEntity>> = favoritesDataSource.favoriteCoins

    /**
     * Получение списка сокращенных имен для всех избранных криптовалют
     */
    suspend fun favoriteSymbols(): List<String> = favoritesDataSource.favoriteSymbols()

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    suspend fun updateFavoriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = favoritesDataSource.updateFavoriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }
}