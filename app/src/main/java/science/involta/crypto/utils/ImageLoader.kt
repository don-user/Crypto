package science.involta.crypto.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import science.involta.crypto.R

object ImageLoader {
    fun loadImage(view: ImageView, url: String, placeholder: Int = R.drawable.baseline_image) {
        Glide.with(view) //передаем ImageView или Context
            .load(url) //передаем ссылку на изобрежение
            .placeholder(placeholder) //указываем картинку по умолчанию
            .error(placeholder) //указываем картинку на случай какой-то ошибки
            .fallback(placeholder) //указываем картинку на случай если сервер не отдаст картинку
            .into(view) //передаем ImageView в который хотим загрузить изображение
    }
}