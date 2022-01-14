package science.involta.crypto.api

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import science.involta.crypto.api.model.GenericResponse
import science.involta.crypto.utils.Constants

/**
 * [BaseRemoteDataSource] выполняет сетевой вызов и преобразует ответ
 * в [Result] который далее используется внутри ViewModel
 */
abstract class BaseRemoteDataSource {
    /**
     * Функция для обработки данных полученых из сети
     *
     * @param call лямбда-функция возвращающая ответ от сервера
     */
    suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            //вызов функции для получения ответа с сервера
            val response = call()

            //если запрос выполнен успешно
            if (response.isSuccessful) {
                //получаем тело ответа из самого ответа
                val body = response.body()
                //если тело не равно null возращаем класс с телом ответа
                if (body != null) return Result.Success(body)
            } else if (response.errorBody() != null) { //иначе если тело ошибки не пустое
                //получаем тело ошибки и преобразуем его к объекту класс GenericResponse
                val errorBody = getErrorBody(response.errorBody())
                //если сообщение не равно null возращаем ошибку с сообщением из ответа,
                //в противном случае, возвращаем сообщение из нашей константы
                return error(errorBody?.message ?: Constants.GENERIC_ERROR)
            }

            return error(Constants.GENERIC_ERROR)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    /**
     * Функция формирующая результат содержащий ошибку
     *
     * @param message сообщение об ошибке
     */
    private fun error(message: String): Result.Error = Result.Error(message)

    /**
     * Функция преобразует ответ от сервера к классу GenericResponse
     *
     * @param responseBody экземпляр класса содержащий ответ от сервера
     */
    private fun getErrorBody(responseBody: ResponseBody?): GenericResponse? =
        try {
            Gson().fromJson(responseBody?.charStream(), GenericResponse::class.java)
        } catch (e: Exception) {
            null
        }
}