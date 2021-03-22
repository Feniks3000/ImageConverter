package ru.geekbrains.imageconverter.presenter

import io.reactivex.rxjava3.core.Completable
import ru.geekbrains.imageconverter.model.Image

interface IConverter {
    fun convert(image: Image): Completable
}