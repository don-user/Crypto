package science.involta.crypto.api.model

/**
 * Класс описывающий модель данных содержащий список параметров для построения графика
 *
 * @param prices список с изменением цен
 */
data class HistoryPriceResponse(val prices: List<DoubleArray>)
