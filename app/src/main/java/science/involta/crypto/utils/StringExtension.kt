package science.involta.crypto.utils

import java.text.DecimalFormat

//проверяет строку на null, и при необходимости возвращает пустую строку
fun String?.emptyIfNull(): String {
    return this ?: ""
}

//убираем скобки из строки
fun String?.trimParanthesis(): String {
    return this?.replace(Regex("[()]"), "") ?: ""
}

//добавляет в строку знак доллара
fun Double?.dollarString(): String {
    return this?.let {
        val numberFormat = DecimalFormat("#,##0.00")
        "$ ${numberFormat.format(this)}"
    } ?: ""
}