package ru.geekbrains.imageconverter.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun chooseImage()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showInProgress()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showSuccessMessage()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorMessage()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showCancelMessage()
}