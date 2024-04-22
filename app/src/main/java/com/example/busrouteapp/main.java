package com.example.busrouteapp;
import com.example.busrouteapp.encryption;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import org.jetbrains.annotations.NotNull;
import com.example.busrouteapp.database.DBHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static android.R.*;

public class main extends AppCompatActivity {
    Button btnScan;
    SurfaceView surfaceView;
    TextView txtBarcodeValue, alertDetails;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";

    DBHandler handler;
    int textSize = 20;
    int paramsWeight = 10;

    String currentDate, currentTime;
    String testusrReader = "TEST USER";
    String testusrOwner = "TEST OWNER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        initializeDetectorsAndSources();
        handler = new DBHandler(this);
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txt_barcodeValue);
        surfaceView = findViewById(R.id.sv_View);
    }

    private void initializeDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Camera is on", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(main.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(main.this,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Camera stopped", Toast.LENGTH_SHORT).show();
                //initializeDetectorsAndSources();
            }

            @Override
            public void receiveDetections(@NonNull @NotNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                intentData = barcodes.valueAt(0).displayValue;

                                // Decryption
                                String decryptedValue = encryption.decrypt(intentData);

                                //Log for Debugging
                                Log.e("d", "Intent Data: " + intentData);
                                if(decryptedValue != null){
                                    showDetails(intentData, decryptedValue);
                                }
                                else{
                                    showError(intentData);
                                }
                            } catch (Exception e) {
                                showError(intentData);
                            }
                        }
                    });
                }
            }
        });
    }

    public void showDetails(String alertIntentData, String decryptedData) {
        cameraSource.release();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.scanned_info_alert);

        builder.setPositiveButton("ACCEPT", (dialog, which) ->
                {
                    dialog.dismiss();
                    dialog.cancel();
                    initViews();
                    initializeDetectorsAndSources();
                    //ADD ACTION TO ADD TO DATABASE
                    // Decrypted Data to Array
                    String[] decryptedArray = decryptedData.split(",");

                    //Add to Database array elements
                    dbAddLogs(alertIntentData, "BTN POSITIVE", "ACCEPTED", testusrOwner, testusrReader);

                    try {
                        if (ActivityCompat.checkSelfPermission(main.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(surfaceView.getHolder());
                            Toast.makeText(this, "CAMERA SOURCE START", Toast.LENGTH_SHORT).show();
                        } else {
                            ActivityCompat.requestPermissions(main.this,
                                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            cameraSource.start(surfaceView.getHolder());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        builder.setNegativeButton("DECLINE", (dialog, which) ->
            {
                dialog.dismiss();
                dialog.cancel();
                initViews();
                initializeDetectorsAndSources();
                //ADD ACTION TO ADD TO DATABASE IF NECESSARY
                dbAddLogs(alertIntentData, "BTN NEGATIVE", "DECLINED", testusrOwner, testusrReader);
                try {
                    if (ActivityCompat.checkSelfPermission(main.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                        Toast.makeText(this, "CAMERA SOURCE START", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(main.this,
                                new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                        cameraSource.start(surfaceView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDetails = dialog.findViewById(R.id.lbl_details);
        alertDetails.setText("Encrypted Data: " + alertIntentData + "\n" + "Decrypted Data: " + decryptedData);

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams layoutParamsPositive = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        btnPositive.setBackground(getDrawable(R.drawable.default_btn_positive));
        btnPositive.setTextColor(Color.parseColor("#FDBE12"));
        btnPositive.setTextSize(textSize);
        layoutParamsPositive.weight = paramsWeight;
        btnPositive.setLayoutParams(layoutParamsPositive);

        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParamsNegative = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();
        btnNegative.setBackground(getDrawable(R.drawable.default_btn_negative));
        btnNegative.setTextColor(Color.parseColor("#FDBE12"));
        btnNegative.setTextSize(textSize);
        layoutParamsNegative.weight = paramsWeight;
        btnNegative.setLayoutParams(layoutParamsNegative);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void showError(String error){
        cameraSource.release();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.invalid_scanned_info);

        builder.setPositiveButton("OK", (dialog, which) ->
                {
                    dialog.dismiss();
                    dialog.cancel();
                    initViews();
                    initializeDetectorsAndSources();
                    Toast.makeText(this, "PRESSED ACCEPT", Toast.LENGTH_SHORT).show();
                    //ADD ACTION TO ADD TO DATABASE IF NECESSARY
                    dbAddLogs(error, "BTN ERROR", "ERROR", testusrOwner, testusrReader);
                    try {
                        if (ActivityCompat.checkSelfPermission(main.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(surfaceView.getHolder());
                            Toast.makeText(this, "CAMERA SOURCE START", Toast.LENGTH_SHORT).show();
                        } else {
                            ActivityCompat.requestPermissions(main.this,
                                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            cameraSource.start(surfaceView.getHolder());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDetails = dialog.findViewById(R.id.lbl_details);
        alertDetails.setText("INVALID DATA\n" + error);

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams layoutParamsPositive = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        btnPositive.setBackground(getDrawable(R.drawable.default_btn_negative));
        btnPositive.setTextColor(Color.parseColor("#FFFFFF"));
        btnPositive.setTextSize(textSize);
        layoutParamsPositive.weight = paramsWeight;
        layoutParamsPositive.gravity = Gravity.CENTER_HORIZONTAL;
        btnPositive.setLayoutParams(layoutParamsPositive);
    }

    public void dbAddTransaction(String transID, String transDate, String transTime, String route, String client, String startingLocation, String endingLocation, String travelSchedDate, String travelSchedTime, String bookingStatus, String boardingStatus, String seatReserved, String reservationBalance){
        handler.addTransaction(transID ,transDate,transTime,route,client,startingLocation,endingLocation,travelSchedDate,travelSchedTime,bookingStatus,boardingStatus,seatReserved,reservationBalance);
        Toast.makeText(getApplicationContext(), "Inserted TransID: " + transID, Toast.LENGTH_SHORT).show();
    }

    public void dbAddLogs(String logValue, String logAction, String logStatus, String logOwner, String logReader){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            currentTime = time.format(now);
            currentDate = date.format(now);
        }
        handler.addLogs(currentDate, currentTime, logValue, logAction, logStatus, logOwner, logReader);
    }
}