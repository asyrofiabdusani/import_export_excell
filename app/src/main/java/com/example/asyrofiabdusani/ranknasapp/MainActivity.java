package com.example.asyrofiabdusani.ranknasapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.EksportData.EksportActivity;
import com.example.asyrofiabdusani.ranknasapp.ImportData.ImportActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.ListNasabah.ListNasabah;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) ;
            else
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        LinearLayout Tambah = findViewById(R.id.tambah_data);
        LinearLayout Edit = findViewById(R.id.edit_data);
        LinearLayout Proses = findViewById(R.id.proses_data);
        LinearLayout Imp = findViewById(R.id.import_data);
        LinearLayout Eks = findViewById(R.id.eksport_data);
        LinearLayout Inp = findViewById(R.id.input_laporan);

        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahActivity.class);
                startActivity(intent);
            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListNasabahEditActivity.class);
                String input = "";
                intent.putExtra("inp", input);
                startActivity(intent);
            }
        });
        Proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Apakah Anda Yakin Akan Memproses Laporan");
                builder.setCancelable(false);
                builder.setPositiveButton(
                        "Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, ProsesActivity.class);
                                startActivity(intent);
                            }
                        }
                );
                builder.setNegativeButton(
                        "Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        Inp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListNasabahInputActivity.class);
                String input = "";
                intent.putExtra("inp", input);
                startActivity(intent);
            }
        });
        Imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImportActivity.class);
                startActivity(intent);
            }
        });
        Eks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Apakah Anda Yakin Akan Menyimpan Laporan");
                builder.setCancelable(false);
                builder.setPositiveButton(
                        "Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, EksportActivity.class);
                                startActivity(intent);
                            }
                        }
                );
                builder.setNegativeButton(
                        "Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.exit){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Apakah Anda Yakin Akan Keluar Dari Aplikasi");

            builder.setPositiveButton(
                    "Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.finishAffinity(MainActivity.this);
                            finish();
                        }
                    }
            );
            builder.setNegativeButton(
                    "Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }
            );
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
