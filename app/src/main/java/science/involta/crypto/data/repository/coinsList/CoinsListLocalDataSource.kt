package science.involta.crypto.data.repository.coinsList

import androidx.lifecycle.LiveData
import science.involta.crypto.data.local.database.CoinsDatabase
import science.involta.crypto.data.local.database.CoinsListEntity
import javax.inject.Inject

/**
 * Класс [CoinsListLocalDataSource] выступает источником локальны данных для списка криптовалют
 *
 * @param database экземпляр класса [CoinsDatabase]
 */
class CoinsListLocalDataSource @Inject constructor(private val database: CoinsDatabase) {
    /**
     * Получение полного списка криптовалют хранящихся в базе данных
     */
    val allCoinsList: LiveData<List<CoinsListEntity>> = database.coinsListDao().coinsList()

    /**
     * Вставляет данные в базу данных
     *
     * @param coinsToInsert список объкетов [CoinsListEntity]
     */
    suspend fun insertCoinsIntoDatabase(coinsToInsert: List<CoinsListEntity>) {
        //проверяем список на пустоту
        if (coinsToInsert.isNotEmpty()) {
            //если список не пустой вставляем список в базу данных
            database.coinsListDao().insert(coinsToInsert)
        }
    }

    /**
     * Получение списка сокращенных имен для всех избранных криптовалют
     */
    suspend fun favouriteSymbols(): List<String> = database.coinsListDao().favouriteSymbols()

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    suspend fun updateFavouriteStatus(symbol: String): CoinsListEntity? {
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