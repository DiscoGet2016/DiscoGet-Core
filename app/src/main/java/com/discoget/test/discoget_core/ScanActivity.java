package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        final SurfaceView cameraView = (SurfaceView)findViewById(R.id.camera_view);
        final TextView barcodeInfo = (TextView)findViewById(R.id.code_info);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();

        if(!barcodeDetector.isOperational()) {
            barcodeInfo.setText("Could not set up the detector!");
            return;
        }

        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() > 0) {
                    // Use the post method of the TextView
                    barcodeInfo.post(new Runnable() {
                        public void run() {
                            // Update the TextView
                            barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                        }
                    });

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("scanned_barcode", barcodeInfo.getText());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });
    }
}
