package com.khmsouti.printer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import java.io.ByteArrayOutputStream


class Main : AppCompatActivity() {
    private var printing: Printing? = null
    lateinit var btnPiarUnpair: Button
    lateinit var btnPrint: Button
    lateinit var btnPrintImages: Button
    lateinit var generateImage: Button
    lateinit var image: ImageView
    lateinit var image22: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)
        var ss = "xxxxx"
        var bitmap = getQrCodeBitmap(ss)
        image22 = findViewById(R.id.generatedImageView)
        image = findViewById(R.id.imageView222)

        generateImage = findViewById(R.id.generateImageView)
        btnPiarUnpair = findViewById(R.id.pairUnpair2)
        btnPrintImages = findViewById(R.id.printImage2)
        btnPrint = findViewById(R.id.PrintTest2)


        image.setOnClickListener {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            generator.generateImage(encoded, image, this)
        }



        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()
        generateImage.setOnClickListener {
        }

        image22.setOnClickListener {
            val view = findViewById<View>(R.id.cardView)
            val bitmap =
                Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            image22.setImageBitmap(bitmap)
            if (!Printooth.hasPairedPrinter()) {
                startActivityForResult(
                    Intent(this, ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER
                )
                Log.e("TAG", "onClick: noPrinter")
            } else {
                printSomeImages(bitmap)
            }
        }

        initListeners()

    }


    private fun initViews() {
        btnPiarUnpair.text =
            if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"
    }

    private fun initListeners() {
        btnPrint.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(
                Intent(
                    this,
                    ScanningActivity::class.java
                ),
                ScanningActivity.SCANNING_FOR_PRINTER
            )
            else PrintText("")
        }


        btnPiarUnpair.setOnClickListener {
            if (Printooth.hasPairedPrinter()) Printooth.removeCurrentPrinter()
            else startActivityForResult(
                Intent(this, ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER
            )
            initViews()
        }

        printing?.printingCallback = object : PrintingCallback {
            override fun connectingWithPrinter() {
                Toast.makeText(this@Main, "Connecting with printer", Toast.LENGTH_SHORT).show()
            }

            override fun printingOrderSentSuccessfully() {
                Toast.makeText(this@Main, "Order sent to printer", Toast.LENGTH_SHORT).show()
            }

            override fun connectionFailed(error: String) {
                Toast.makeText(this@Main, "Failed to connect printer", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: String) {
                Toast.makeText(this@Main, error, Toast.LENGTH_SHORT).show()
            }

            override fun onMessage(message: String) {
                Toast.makeText(this@Main, "Message: $message", Toast.LENGTH_SHORT).show()
            }

            override fun disconnected() {
                Toast.makeText(this@Main, "Disconnected Printer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun PrintText(str: String) {
        val printables = getSomePrintables2(str)
        printing?.print(printables)
    }

    fun printSomeImages(bitmap: Bitmap) {
        val printables = ArrayList<Printable>().apply {
            add(
                ImagePrintable.Builder(bitmap).setNewLinesAfter(3)
                    .setAlignment(DefaultPrinter.ALIGNMENT_LEFT).build()
            )
        }
        printing?.print(printables)
    }


    private fun getSomePrintables2(str: String): ArrayList<Printable> {
        var list: ArrayList<Printable> = ArrayList()
        for (i in 1..24) {
            list.add(
                TextPrintable.Builder()
                    .setText(str)
                    .setLineSpacing(DefaultPrinter.LINE_SPACING_30)
                    .setFontSize(DefaultPrinter.FONT_SIZE_NORMAL)
                    .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                    .setNewLinesAfter(1)
                    .build()
            )
        }
        list.add(
            TextPrintable.Builder().setText("\n\n\n\n")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .build()
        )
        return list
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            PrintText("")
        initViews()
    }

    fun getQrCodeBitmap(ssid: String): Bitmap {
        val size = 200 //pixels
        val bits = QRCodeWriter().encode(ssid, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.WHITE else Color.TRANSPARENT)
                }
            }
        }
    }
}