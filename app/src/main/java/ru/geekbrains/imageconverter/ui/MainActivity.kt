package ru.geekbrains.imageconverter.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.geekbrains.imageconverter.databinding.ActivityMainBinding
import ru.geekbrains.imageconverter.model.Image
import ru.geekbrains.imageconverter.presenter.MainPresenter
import ru.geekbrains.imageconverter.view.MainView


class MainActivity : MvpAppCompatActivity(), MainView {

    private val vb by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val presenter by moxyPresenter {
        MainPresenter(
            AndroidConverter(applicationContext),
            AndroidSchedulers.mainThread()
        )
    }

    private val REQUEST_ID = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        vb.convert.setOnClickListener {
            presenter.chooseImage()
        }
    }

    override fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выбери изображение"),
            REQUEST_ID
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                data?.data?.let { uri ->
                    val bytes =
                        applicationContext?.contentResolver?.openInputStream(uri)?.buffered()
                            ?.use { it.readBytes() }
                    var fileName: String? = null
                    if (uri.scheme.equals("content")) {
                        val cursor = contentResolver.query(uri, null, null, null, null)
                        try {
                            if (cursor != null && cursor.moveToFirst()) {
                                fileName =
                                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            }
                        } finally {
                            cursor?.close()
                        }
                    }
                    bytes?.let { presenter.convertImage(Image(bytes, fileName ?: "converted")) }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showInProgress() {
        vb.info.text = "Идет конвертация"
    }

    override fun showSuccessMessage() {
        vb.info.text = "Конвертация успешно завершена"
    }

    override fun showErrorMessage() {
        vb.info.text = "Конвертация завершена с ошибкой"
    }

    override fun showCancelMessage() {
        vb.info.text = "Конвертация отменена"
    }
}