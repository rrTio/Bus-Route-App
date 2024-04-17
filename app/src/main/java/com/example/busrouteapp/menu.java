package com.example.busrouteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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
                Intent intent = new Intent(menu.this, main.class);
                startActivity(intent);
            }
        });
    }
}
