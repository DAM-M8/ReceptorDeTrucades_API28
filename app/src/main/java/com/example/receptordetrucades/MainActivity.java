package com.example.receptordetrucades;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;


import android.view.View;
import android.widget.Button;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_CALL_LOG;

/**
 *
 * @author Laura López - Novembre de 2022
 * Exemple per a crear un receptor de broadcast a partir d'una trucada entrant. Funciona amb APIs iguals o posteriors a la 28 (Android Pie)
 * basat en: https://www.digitalocean.com/community/tutorials/android-runtime-permissions-example
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        private static final int PERMISSION_REQUEST_CODE = 200; //Els identificadors dels permisos son arbitraris
        private View view;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Button check_permission = (Button) findViewById(R.id.check_permission);
            Button request_permission = (Button) findViewById(R.id.request_permission);
            check_permission.setOnClickListener(this);
            request_permission.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {

            view = v;

            int id = v.getId();
            switch (id) {
                case R.id.check_permission:
                    if (checkPermission()) {

                        Snackbar.make(view, "Permis donat.", Snackbar.LENGTH_LONG).show();

                    } else {

                        Snackbar.make(view, "Necessites demanar permis.", Snackbar.LENGTH_LONG).show();
                    }
                    break;
                case R.id.request_permission:
                    if (!checkPermission()) {

                        requestPermission();

                    } else {

                        Snackbar.make(view, "Permis garantit.", Snackbar.LENGTH_LONG).show();

                    }
                    break;
            }

        }

        private boolean checkPermission() {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }

        private void requestPermission() {

            ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE, READ_CALL_LOG}, PERMISSION_REQUEST_CODE);

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0) {

                        boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                        if (locationAccepted && cameraAccepted)
                            Snackbar.make(view, "Permis garantit, pots accedir a l'estat del telefon.", Snackbar.LENGTH_LONG).show();
                        else {

                            Snackbar.make(view, "Permis denegat, no pots accedir a l'estat del telefon.", Snackbar.LENGTH_LONG).show();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                    showMessageOKCancel("Necessites permetre ambdos permisos",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        requestPermissions(new String[]{READ_PHONE_STATE, READ_CALL_LOG},
                                                                PERMISSION_REQUEST_CODE);
                                                    }
                                                }
                                            });
                                    return;
                                }
                            }

                        }
                    }


                    break;
            }
        }

   private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
   }

}


