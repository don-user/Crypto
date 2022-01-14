package science.involta.crypto.data.repository.coin

import science.involta.crypto.data.local.database.CoinsDatabase
import javax.inject.Inject

/**
 * Класс [CoinLocalDataSource] выступает источником локальны данных
 * для получения информации о конкретной криптовалюте
 *
 * @param database экземпляр класса [CoinsDatabase]
 */
class CoinLocalDataSource @Inject constructor(private val database: CoinsDatabase) {
    /**
     * Выполняет поиск криптовалюты по её сокращенному имени внутри базы данных
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun coinBySymbol(symbol: String) = database.coinsListDao().coinLiveDataFromSymbol(symbol)
}