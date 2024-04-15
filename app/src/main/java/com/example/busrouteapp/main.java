package com.example.busrouteapp;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import org.jetbrains.annotations.NotNull;
import com.example.busrouteapp.database.DBHandler;
import org.jetbrains.annotations.Nullable;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class main extends AppCompatActivity {
    Button btnScan;
    SurfaceView surfaceView;
    TextView txtBarcodeValue, alertDetails;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    private static final String SECRET_KEY = "123456789";
    private static final String SALTVALUE = "abcdefg";
    DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        initializeDetectorsAndSources();
        handler = new DBHandler(this);
        /*
        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initViews();
                initializeDetectorsAndSources();
                Log.e("d", "Button Pressed");
            }
        });*/
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
                                String decryptedValue = decrypt(intentData);

                                // Decrypted Data to Array
                                //String[] decryptedArray = decryptedValue.split(",");

                                //Add to Database array elements
                                //handler.addTransaction(decryptedArray[0],decryptedArray[1],decryptedArray[2],decryptedArray[3],decryptedArray[4],decryptedArray[5],decryptedArray[6],decryptedArray[7],decryptedArray[8],decryptedArray[9],decryptedArray[10],decryptedArray[11],decryptedArray[12]);

                                //Log for Debugging
                                Log.e("d", "Intent Data: " + intentData);

                                showDetails(intentData, decryptedValue);
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
        btnPositive.setTextSize(35);
        layoutParamsPositive.weight = 10;
        btnPositive.setLayoutParams(layoutParamsPositive);

        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams layoutParamsNegative = (LinearLayout.LayoutParams) btnNegative.getLayoutParams();
        btnNegative.setBackground(getDrawable(R.drawable.default_btn_negative));
        btnNegative.setTextColor(Color.parseColor("#FDBE12"));
        btnNegative.setTextSize(35);
        layoutParamsNegative.weight = 10;
        btnPositive.setLayoutParams(layoutParamsNegative);
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
        btnPositive.setBackground(getDrawable(R.drawable.default_btn_positive));
        btnPositive.setTextColor(Color.parseColor("#FDBE12"));
        btnPositive.setTextSize(35);
        layoutParamsPositive.weight = 10;
        btnPositive.setLayoutParams(layoutParamsPositive);
    }

     void dbAddTransaction(String[] decryptedArray){
        handler.addTransaction(decryptedArray[0],decryptedArray[1],decryptedArray[2],decryptedArray[3],decryptedArray[4],decryptedArray[5],decryptedArray[6],decryptedArray[7],decryptedArray[8],decryptedArray[9],decryptedArray[10],decryptedArray[11],decryptedArray[12]);
        Toast.makeText(getApplicationContext(), intentData, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException |
               InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)
        {
            System.out.println("Error occured during decryption: " + e.toString());
        }
        return null;
    }

    /*
    @Override
    protected void onPause(){
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initializeDetectorsAndSources();
    }
    */
}