package science.involta.crypto.data.repository.favourites

import androidx.lifecycle.LiveData
import science.involta.crypto.data.local.database.CoinsDatabase
import science.involta.crypto.data.local.database.CoinsListEntity
import javax.inject.Inject

/**
 * Класс [FavoritesDataSource] выступает источником локальных данных
 * для получения информации об избранных криптовалютах
 *
 * @param database экземпляр класса [CoinsDatabase]
 */
class FavoritesDataSource @Inject constructor(private val database: CoinsDatabase) {
    /**
     * Получение списка избранных криптовалют
     */
    val favoriteCoins: LiveData<List<CoinsListEntity>> = database.coinsListDao().favouriteCoins()

    /**
     * Получение списка сокращенных имен для всех избранных криптовалют
     */
    suspend fun favoriteSymbols(): List<String> = database.coinsListDao().favouriteSymbols()

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    suspend fun updateFavoriteStatus(symbol: String): CoinsListEntity? {
        val coin = database.coinsListDao().coinFromSymbol(symbol)
        coin?.let {
            val coinsListEntity = CoinsListEntity(
                it.symbol,
                it.id,
                it.name,
                it.price,
                it.changePercent,
                it.image,
                it.isFavourite.not()
            )

            if (database.coinsListDao().updateCoinsListEntity(coinsListEntity) > 0) {
                return coinsListEntity
            }
        }
        return null
    }
}