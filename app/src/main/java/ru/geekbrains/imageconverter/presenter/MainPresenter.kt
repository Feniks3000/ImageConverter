package ru.geekbrains.imageconverter.presenter

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.geekbrains.imageconverter.model.Image
import ru.geekbrains.imageconverter.view.MainView

class MainPresenter(val converter: IConverter, val mainThread: Scheduler) :
    MvpPresenter<MainView>() {

    var conversionDisposable: Disposable? = null

    fun chooseImage() {
        viewState.chooseImage()
    }

    fun convertImage(image: Image) {
        viewState.showInProgress()

        conversionDisposable = converter.convert(image)
            .observeOn(mainThread)
            .subscribe({
                viewState.showSuccessMessage()
            }, {
                viewState.showErrorMessage()
            })
    }

    fun convertationCancel() {
        conversionDisposable?.dispose()
        viewState.showCancelMessage()
    }


}