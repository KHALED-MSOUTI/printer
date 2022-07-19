package com.khmsouti.printer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback


class Main : AppCompatActivity() {
    private var printing : Printing? = null

    lateinit var btnPiarUnpair:Button
    lateinit var btnPrint :Button
    lateinit var btnPrintImages :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        btnPiarUnpair=findViewById(R.id.pairUnpair2)
        btnPrintImages= findViewById(R.id.printImage2)
        btnPrint=findViewById(R.id.PrintTest2)
        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()

        initListeners()
        btnPrintImages.setOnClickListener({
            if (!Printooth.hasPairedPrinter()){
                startActivityForResult(Intent(this, ScanningActivity::class.java), ScanningActivity.SCANNING_FOR_PRINTER) }
            else printSomeImages()
        })
    }

    private fun initViews() {
        btnPiarUnpair.text = if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer"
    }

    private fun initListeners() {
        btnPrint.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(Intent(this,
                ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER)
            else PrintText()
        }

        btnPrintImages.setOnClickListener {
            if (!Printooth.hasPairedPrinter()) startActivityForResult(Intent(this,
                ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER)
            else printSomeImages()
        }

        btnPiarUnpair.setOnClickListener {
            if (Printooth.hasPairedPrinter()) Printooth.removeCurrentPrinter()
            else startActivityForResult(Intent(this, ScanningActivity::class.java),
                ScanningActivity.SCANNING_FOR_PRINTER)
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

    public fun PrintText(str:String) {
        val printables = getSomePrintables2(str)
        printing?.print(printables)
    }

    public fun printSomeImages() {
        val printables = ArrayList<Printable>().apply {
            add(ImagePrintable.Builder(R.drawable.image1, resources).build())
            add(ImagePrintable.Builder(R.drawable.image2, resources).build())
            add(ImagePrintable.Builder(R.drawable.image3, resources).setNewLinesAfter(3).build())
        }
        printing?.print(printables)
    }

    private fun getSomePrintables() = ArrayList<Printable>().apply {
        add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build()) // feed lines example in raw mode
        add(TextPrintable.Builder()
            .setText("Hello World")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
            .setNewLinesAfter(1)
            .build())
    }

    private fun getSomePrintables2( str:String):ArrayList<Printable>{
        var list:ArrayList<Printable> = ArrayList()
        for (i in 1..24){
            list.add(TextPrintable.Builder()
                .setText(str)
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
                .setNewLinesAfter(1)
                .build())
        }
        list.add(TextPrintable.Builder().setText("\n\n\n\n")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .build())
        return list
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK)
            PrintText()
        initViews()
    }
}