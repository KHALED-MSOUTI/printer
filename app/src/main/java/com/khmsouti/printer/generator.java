package com.khmsouti.printer;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ImageView;

import java.util.Base64;

public class generator {
    public static void generateImage(String base64, ImageView imageView,  Context context) {
        // Define the dimensions of the label
        int labelWidth = 55; // in millimeters
        int labelHeight = 20; // in millimeters

        String[] texts = {"Hello", "World"};
        // Define the dimensions and alignment of the QR code
        int qrCodeSize = 15; // in millimeters
        int qrCodeAlignmentX = labelWidth - qrCodeSize;

        // Define the dimensions and alignment of the text
        int textAlignmentX = 5; // in millimeters
        int textAlignmentY = 3; // in millimeters

        // Create a blank image with the label dimensions
        int imageWidth = (int) (labelWidth * 2.83); // Convert millimeters to pixels (assuming 72 DPI)
        int imageHeight = (int) (labelHeight * 2.83); // Convert millimeters to pixels (assuming 72 DPI)
        Bitmap imageBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);

        // Create a Canvas to draw on the bitmap
        Canvas canvas = new Canvas(imageBitmap);
        canvas.drawColor(Color.WHITE);

        // Base64 encoded QR code image

        // Convert the Base64 string to a byte array
        Log.e("base64",base64);
        byte[] qrCodeImageData = Base64.getDecoder().decode(base64);

        // Convert the byte array to a Bitmap
        Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(qrCodeImageData, 0, qrCodeImageData.length);

        imageView.setImageBitmap(qrCodeBitmap);
        // Draw the QR code bitmap on the main image bitmap
        canvas.drawBitmap(qrCodeBitmap, qrCodeAlignmentX * 2.83f, 0, null);

        // Create and configure the Paint object for text
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(12 * context.getResources().getDisplayMetrics().density); // Customize the font size as needed
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // Add the text to the image
        float textY = textAlignmentY * context.getResources().getDisplayMetrics().density;
        for (String text : texts) {
            canvas.drawText(text, textAlignmentX * context.getResources().getDisplayMetrics().density, textY, textPaint);
            textY += 12 * context.getResources().getDisplayMetrics().density; // Increase the Y position for the next text
        }

        // Set the generated image to the ImageView
    }

}
