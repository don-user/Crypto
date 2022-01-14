package science.involta.crypto.api

/**
 * [Result] вспомагательный класс в который оборачивается ответ от сервера
 * [Success] содержит исходный ответ от сервера
 * [Error] содержит сообщение об ошибке в случае её возникновения
 * [Loading] содержит информациб о том, выполняется сетевой запрос в текущий момент или нет
 */
sealed class Result<out R> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val message: String): Result<Nothing>()
    object Loading: Result<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success<*> -> "Success [data = $data]"
            is Error -> "Error [message = $message]"
            is Loading -> "Loading"
        }
    }
}

/**
 * Для [Result]<out R>, где R — ковариантный параметризованный тип с верхней границей RUpper,
 * [Result]<*> является эквивалентом [Result]<out RUpper>. Это значит, что когда R неизвестен,
 * вы можете безопасно читать значения типа RUpper из [Result]<*>.
 *
 * Свойство [successed] позволяет проверить, успешно ли завершился запрос данных от сервера
 * и что данные полученные от сервера не равны null
 */
val Result<*>.successed
    get() = this is Result.Success && data != null