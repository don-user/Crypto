package science.involta.crypto.data.repository.coin

import javax.inject.Inject

/**
 * Класс [CoinRepository] выполняет роль репозитория при помощи которого
 * ViewModel в дальнейшем будет получать данные
 *
 * @param coinLocalDataSource источник локальных данных [CoinLocalDataSource]
 * @param coinRemoteDataSource источник удаленных данных [CoinRemoteDataSource]
 */
class CoinRepository @Inject constructor(
    private val coinLocalDataSource: CoinLocalDataSource,
    private val coinRemoteDataSource: CoinRemoteDataSource
) {
    /**
     * Получает из [CoinLocalDataSource] объект конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun coinBySymbol(symbol: String) = coinLocalDataSource.coinBySymbol(symbol)

    /**
     * Получает из [CoinRemoteDataSource] данные с информацией
     * по изменению цены для конкретной криптовалюты
     *
     * @param symbolId идентификатор криптовалюты
     * @param targetCur целевая валюта по отношению к которой
     * запрашиваются цены, по умолчанию "usd" (Доллар США)
     */
    suspend fun historyPrice(symbolId: String, targetCur: String = "usd") = coinRemoteDataSource.historyPrice(symbolId, targetCur)
}