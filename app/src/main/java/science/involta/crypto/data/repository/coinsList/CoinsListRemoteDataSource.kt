package science.involta.crypto.data.repository.coinsList

import science.involta.crypto.api.ApiInterface
import science.involta.crypto.api.BaseRemoteDataSource
import science.involta.crypto.api.Result
import science.involta.crypto.api.model.Coin
import javax.inject.Inject

/**
 * Класс [CoinsListRemoteDataSource] выступает источником удаленных (находящихся на удаленном сервере) данных
 *
 * @param service интерфейс описывающий сетевые запросы
 */
class CoinsListRemoteDataSource @Inject constructor(private val service: ApiInterface): BaseRemoteDataSource() {
    /**
     * Получает полный список криптовалют с удаленного сервера
     *
     * @param targetCur целевая валюта относительно которой мы получим курс
     */
    suspend fun coinsList(targetCur: String): Result<List<Coin>> =
        getResult {
            service.coinList(targetCur)
        }
}