package com.example.busrouteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.microsoft.aad.*;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class menu extends AppCompatActivity {

    Button btnScan, btnLogs;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.menu);

        btnScan = findViewById(R.id.btnScanQR);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openScanner();
            }
        });

        btnLogs = findViewById(R.id.btnShowLogs);
        btnLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogs();
            }
        });
    }

    public void openScanner(){
        startActivity(new Intent(menu.this, main.class));
    }

    public void openLogs(){
        startActivity(new Intent(menu.this, logs.class));
    }
}
