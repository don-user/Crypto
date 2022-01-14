package science.involta.crypto.data.repository.coin

import science.involta.crypto.api.ApiInterface
import science.involta.crypto.api.BaseRemoteDataSource
import science.involta.crypto.api.Result
import science.involta.crypto.api.model.HistoryPriceResponse
import javax.inject.Inject

/**
 * Класс [CoinRemoteDataSource] выступает источником удаленных (находящихся на удаленном сервере) данных
 *
 * @param service интерфейс описывающий сетевые запросы
 */
class CoinRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {
    /**
     * Выполняет запрос на сервер для получения данных об изменении цены
     * на конкретную криптовалюту за указанный промежуток времени
     *
     * @param symbolId идентификатор криптовалюты
     * @param targetCur целевая валюта по отношению к которой запрашиваются цены
     * @param days промежуток времени (в днях) за который запрашиваются изменение, по умолчанию запрошивается за 30 дней
     */
    suspend fun historyPrice(symbolId: String, targetCur: String, days: Int = 30): Result<HistoryPriceResponse> =
        getResult {
            service.historicalPrice(symbolId, targetCur, days)
        }
}