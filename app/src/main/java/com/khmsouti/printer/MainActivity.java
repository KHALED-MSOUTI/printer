package com.khmsouti.printer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity  {

    Button pairUnpair;
    Button printTest;
    Button printImage;
    Button gotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        gotos = findViewById(R.id.Gotos);
        gotos.setOnClickListener(view -> {
            startActivity(new Intent(this, Main.class));
        });
    }

    private void initView() {
        pairUnpair = findViewById(R.id.pairUnpair);
        printImage = findViewById(R.id.printImage);
        printTest = findViewById(R.id.PrintTest);

        pairUnpair.setOnClickListener(view -> {
            String[] stringArray= new String[]{"","",""};
        });

        printImage.setOnClickListener(view -> {
            doPhotoPrint();
        });

        printTest.setOnClickListener(view -> {

        });
        int PERMISSION_BLUETOOTH=1;
        int PERMISSION_BLUETOOTH_ADMIN=2;
        int PERMISSION_BLUETOOTH_CONNECT=3;
        int PERMISSION_BLUETOOTH_SCAN=4;

        for (int i = 0;i==7;i++){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
        } else {
            Toast.makeText(this, "all permition is granted", Toast.LENGTH_SHORT).show();
            i=1;
        }
        }
    }
    private void doPhotoPrint() {

    }
}