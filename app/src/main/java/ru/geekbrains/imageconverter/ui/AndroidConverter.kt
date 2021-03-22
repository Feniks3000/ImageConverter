package ru.geekbrains.imageconverter.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.geekbrains.imageconverter.model.Image
import ru.geekbrains.imageconverter.presenter.IConverter
import java.io.File
import java.io.FileOutputStream

class AndroidConverter(val context: Context) : IConverter {
    override fun convert(image: Image) = Completable.fromAction {
        context.let { context ->
            val convertedFile =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), image.name + ".png")
            val stream = FileOutputStream(convertedFile)

            val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        }
    }.subscribeOn(Schedulers.io())
}