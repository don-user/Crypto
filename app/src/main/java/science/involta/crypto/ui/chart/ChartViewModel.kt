package science.involta.crypto.ui.chart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import science.involta.crypto.api.Result
import science.involta.crypto.data.repository.coin.CoinRepository
import science.involta.crypto.ui.main.coinList.CoinListFragment
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(private val repository: CoinRepository): ViewModel() {
    /**
     * [_isLoading] хранит изменяемую с статусом загрузки данных из сети
     * [isLoading] позволяет получить доступ к статусу из View [ChartActivity]
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * [_dataError] хранит статус наличия ошибки
     * [dataError] позволяет получить статус наличия ошибки из View [ChartActivity]
     */
    private val _dataError = MutableLiveData<Boolean>()
    val dataError: LiveData<Boolean> = _dataError

    /**
     * [_historicalData] хранит данные об изменении цены
     * [historicalData] позволяет получить данные об изменении цены из View [ChartActivity]
     */
    private val _historicalData = MutableLiveData<List<DoubleArray>>()
    val historicalData: LiveData<List<DoubleArray>> = _historicalData

    /**
     * Возвращает объект криптовалюты из базы данных
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun coinBySymbol(symbol: String) = repository.coinBySymbol(symbol)

    /**
     * Запрашивает данные по изменению цены на конкретную криптовалюту
     *
     * @param symbolId идентификатор криптовалюты
     */
    fun historicalData(symbolId: String?) {
        symbolId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                val result = repository.historyPrice(symbolId)
                _isLoading.postValue(false)

                when (result) {
                    is Result.Success -> {
                        _historicalData.postValue(result.data.prices)
                        _dataError.postValue(false)
                    }
                    is Result.Error -> {
                        _dataError.postValue(true)
                    }
                }
            }
        }
    }
}