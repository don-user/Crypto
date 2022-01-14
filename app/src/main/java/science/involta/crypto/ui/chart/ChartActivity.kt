package science.involta.crypto.ui.chart

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chart.*
import kotlinx.android.synthetic.main.item_line_chart.*
import science.involta.crypto.R
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.databinding.ActivityChartBinding
import science.involta.crypto.utils.*

@AndroidEntryPoint
class ChartActivity: AppCompatActivity() {
    private val viewModel: ChartViewModel by viewModels()
    private lateinit var binding: ActivityChartBinding

    private var symbol: String? = null
    private var symbolId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart)
        binding.apply {
            lifecycleOwner = this@ChartActivity
            viewModel = this@ChartActivity.viewModel
        }

        //подключаем Toolbar к Activity
        setSupportActionBar(toolbar)

        //включаем показ кнопки для возврата на предыдущий экран
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //получаем сокращенное имя криптовалюты из объекта Intent
        if (intent?.hasExtra(Constants.EXTRA_SYMBOL) == true) {
            symbol = intent?.getStringExtra(Constants.EXTRA_SYMBOL)
        }

        //получаем идентификатор криптовалюты из объекта Intent
        if (intent?.hasExtra(Constants.EXTRA_SYMBOL_ID) == true) {
            symbolId = intent?.getStringExtra(Constants.EXTRA_SYMBOL_ID)
        }

        //устанавливаем заголовок в Toolbar
        supportActionBar?.title = symbol ?: ""

        observeViewModel()

        viewModel.historicalData(symbolId)
    }

    /**
     * Функция внутри которой выполняется подписка на обновление объектов LiveData
     */
    private fun observeViewModel() {
        symbol?.let {
            viewModel.coinBySymbol(it).doOnChange(this) { coin ->
                initializeViews(coin)
            }

            viewModel.historicalData.doOnChange(this) { data ->
                ChartHelper.drawHistoricalLineChart(lineChartLayout, it, data)
            }

            viewModel.dataError.doOnChange(this) { error ->
                if (error) showToast("Не удалось получить данные об изменении цены")
            }
        }
    }

    /**
     * Инициализируем и передаем в View информацию
     * о криптовалюте, график которой мы смотрим.
     */
    private fun initializeViews(coinsListEntity: CoinsListEntity) {
        coinSymbolTextView.text = coinsListEntity.symbol
        coinNameTextView.text = coinsListEntity.name
        coinPriceTextView.text = coinsListEntity.price.dollarString()
        PriceHelper.showChangePrice(coinChangeTextView, coinsListEntity.changePercent)
        ImageLoader.loadImage(coinImageView, coinsListEntity.image ?: "")
    }

    /**
     * Отображает на экране Toast
     *
     * @param message текст сообщения
     * @param duration время показа сообщения на экране
     */
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    /**
     * Обрабатываем нажатие на кнопку "Назад" в Toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}