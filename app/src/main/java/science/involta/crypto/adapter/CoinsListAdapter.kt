package science.involta.crypto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_coin_list.view.*
import science.involta.crypto.R
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.utils.ImageLoader
import science.involta.crypto.utils.PriceHelper
import science.involta.crypto.utils.dollarString
import science.involta.crypto.utils.emptyIfNull

/**
 * Интерфейс для получения информации о совершении клика по элементу
 * из списка или по иконке для добавления/удаления в избранное
 */
interface OnItemClickCallback {
    /**
     * Функция передающая клик по элементу
     *
     * @param symbol сокращенное имя криптовалюты
     * @param id индентификатор криптовалюты
     */
    fun onItemClick(symbol: String, id: String)

    /**
     * Функция передающая клик по иконкре добавления/удаления избранного
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun onFavoriteClick(symbol: String)
}

/**
 * Класс адаптер для отображения списка криптовалют в RecyclerView
 *
 * @param onItemClickCallback интерфейс для обработки кликов [OnItemClickCallback]
 */
class CoinsListAdapter (private val onItemClickCallback: OnItemClickCallback) : RecyclerView.Adapter<CoinsListAdapter.CoinsListViewHolder>() {
    /**
     * Переменная хранящая список объектов [CoinsListEntity], по умолчанию содержит пустой список
     */
    private val coinsList: MutableList<CoinsListEntity> = mutableListOf()


    /**
     * Создает новый объет [CoinsListViewHolder] каждый раз когда RecyclerView в этом нуждается.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsListViewHolder {
        return CoinsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_list, parent, false)
        )
    }

    /**
     * Принимает объект ViewHolder и устанавливает необходимые
     * данные для соответствующего элемента RecyclerView
     */
    override fun onBindViewHolder(holder: CoinsListViewHolder, position: Int) {
        holder.bind(coinsList[position], onItemClickCallback)
    }

    /**
     * Возвращает общее количество элементов списка.
     */
    override fun getItemCount(): Int = coinsList.size

    /**
     * Функция обновляющая список криптовалют [coinsList] внутри RecyclerView
     *
     * @param list список объектов [CoinsListEntity]
     */
    fun updateList(list: List<CoinsListEntity>) {
        this.coinsList.clear() //очистка списка криптовалют
        this.coinsList.addAll(list) //заполнение списка новыми данными
        notifyDataSetChanged()  //обновление RecyclerView
    }

    class CoinsListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(model: CoinsListEntity, onItemClickCallback: OnItemClickCallback) {
            itemView.coinsItemSymbolTextView.text = model.symbol

            //проверяем имя криптовалюты на null, и устанавливаем пустую строку если true
            itemView.coinsItemNameTextView.text = model.name.emptyIfNull()

            //добавляем символ доллара к цене
            itemView.coinsItemPriceTextView.text = model.price.dollarString()

            //проверям упала ли цена, если да то устанавливае красный цвет и  стрелку вниз
            //в противном случае устанавливаем зеленый текст и стрелку вверх
            PriceHelper.showChangePrice(itemView.coinsItemChangeTextView, model.changePercent)

            //устанавливаем иконку для кнопки избранного
            itemView.favoriteImageView.setImageResource(
                if (model.isFavourite) R.drawable.item_favorite
                else R.drawable.item_favorite_border
            )

            //обрабатываем нажатие на кнопку добавления/удаления избранного
            itemView.favoriteImageView.setOnClickListener {
                onItemClickCallback.onFavoriteClick(model.symbol)
            }

            //загружаем изображение криптовалюты
            ImageLoader.loadImage(itemView.coinsItemImageView, model.image ?: "")

            //обрабатываем клик по элементу списка
            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(
                    model.symbol,
                    model.id ?: model.symbol
                )
            }
        }
    }
}