package com.example.asyrofiabdusani.ranknasapp.EditData;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EksportData.EksportActivity;
import com.example.asyrofiabdusani.ranknasapp.ImportData.ImportActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;
import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText nama;
    EditText alamat;
    Spinner domisili;
    EditText ttl;
    ImageView kalender;
    EditText telepon;
    Spinner pekerjaan;
    EditText alamatKerja;
    EditText penghasilan;
    EditText saldo;
    EditText hutang;
    Spinner fasilitas;
    Spinner biChecking;
    Button simpan;
    String sPekerjaan, sFasilitas, sDomisili, sBiChecking, getId;
    Calendar myCalendar;
    DbHelper mDbHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mDbHelper = new DbHelper(this);
        navDrawer();
        deklarasi();

        simpan = findViewById(R.id.bt_simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiIsi();
            }
        });

        Button hapus = findViewById(R.id.bt_hapus);
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Apakah Anda Yakin Akan Menghapus Data");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hapusData();
                                Intent intent = new Intent(EditActivity.this, ListNasabahEditActivity.class);
                                String input = "";
                                intent.putExtra("inp",input);
                                startActivity(intent);
                                Toast.makeText(EditActivity.this,"Data berhasil dihapus",Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private void deklarasi(){
        nama = findViewById(R.id.nama);
        nama.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        alamat = findViewById(R.id.alamat);
        domisili = findViewById(R.id.domisili);
        ttl = findViewById(R.id.tanggal_lahir);
        kalender = findViewById(R.id.kalender);
        telepon = findViewById(R.id.telpon);
        pekerjaan = findViewById(R.id.pekerjaan);
        alamatKerja = findViewById(R.id.alamat_kerja);
        penghasilan = findViewById(R.id.penghasilan);
        saldo = findViewById(R.id.saldo);
        hutang = findViewById(R.id.hutang);
        fasilitas = findViewById(R.id.fasilitas);
        biChecking = findViewById(R.id.bi_checking);

        bacaDb();

        domisili.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals("Dalam Kota")){
                        sDomisili = "0";
                    } else {
                        sDomisili = "1";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sDomisili = "0";
            }
        });

        pekerjaan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals("Karyawan Kontrak")){
                        sPekerjaan = "0";
                    } else if (selection.equals("Karyawan Tetap")){
                        sPekerjaan = "1";
                    } else {
                        sPekerjaan = "2";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sPekerjaan = "0";
            }
        });


        fasilitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals("0")){
                        sFasilitas = "0";
                    } else if (selection.equals("1")){
                        sFasilitas = "1";
                    } else if (selection.equals("2")){
                        sFasilitas = "2";
                    } else if (selection.equals("3")){
                        sFasilitas = "3";
                    } else if (selection.equals("4")) {
                        sFasilitas = "4";
                    } else if (selection.equals("5")){
                        sFasilitas="5";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sFasilitas = "0";
            }
        });

        biChecking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)){
                    if (selection.equals("Tidak Bermasalah")){
                        sBiChecking = "0";
                    } else {
                        sBiChecking = "1";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sBiChecking = "0";
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                ttl.setText(sdf.format(myCalendar.getTime()));
            }

        };

        kalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void bacaDb(){
        Bundle extra = getIntent().getExtras();
        getId = extra.getString("id");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + data.TABLE_DATA + " WHERE "
                        + data.DATA_ID + " = ?",
                new String[]{getId});
        try {
            int namaDb = cursor.getColumnIndex(data.COLOUMN_NAME);
            int alamatDb = cursor.getColumnIndex(data.COLOUMN_ALAMAT);
            int domisiliDb = cursor.getColumnIndex(data.COLOUMN_DOMISILI);
            int ttlDb = cursor.getColumnIndex(data.COLOUMN_TANGGAL_LAHIR);
            int telpDb = cursor.getColumnIndex(data.COLOUMN_TELEPON);
            int pekerjaanDb = cursor.getColumnIndex(data.COLOUMN_PEKERJAAN);
            int tempatKerjaDb = cursor.getColumnIndex(data.COLOUMN_TEMPAT_KERJA);
            int penghasilanDb = cursor.getColumnIndex(data.COLOUMN_PENGHASILAN);
            int saldoDb = cursor.getColumnIndex(data.COLOUMN_SALDO_TABUNGAN);
            int hutangDb = cursor.getColumnIndex(data.COLOUMN_HUTANG);
            int fasilitasDb = cursor.getColumnIndex(data.COLOUMN_FASILITAS_DIBRI);
            int biChcekingDb = cursor.getColumnIndex(data.COLOUMN_BI_CHECKING);

            while (cursor.moveToNext()) {
                String currentNama = cursor.getString(namaDb);
                String currentAlamat = cursor.getString(alamatDb);
                String currentDomisili = cursor.getString(domisiliDb);
                String currentTtl = cursor.getString(ttlDb);
                String currentTelp = cursor.getString(telpDb);
                String currentPekerjaan = cursor.getString(pekerjaanDb);
                String currentTempatKerja = cursor.getString(tempatKerjaDb);
                String currentPenghasilan = cursor.getString(penghasilanDb);
                String currentSaldo = cursor.getString(saldoDb);
                String currentHutang = cursor.getString(hutangDb);
                String currentFasilitas = cursor.getString(fasilitasDb);
                String currentBiChecking = cursor.getString(biChcekingDb);

                nama.setText(currentNama);
                alamat.setText(currentAlamat);
                if (currentDomisili.equals("0")){
                    domisili.setSelection(0);
                } else {
                    domisili.setSelection(1);
                }
                ttl.setText(currentTtl);
                telepon.setText(currentTelp);
                if (currentPekerjaan.equals("0")){
                    pekerjaan.setSelection(0);
                } else if (currentPekerjaan.equals("1")){
                    pekerjaan.setSelection(1);
                } else {
                    pekerjaan.setSelection(2);
                }
                alamatKerja.setText(currentTempatKerja);
                penghasilan.setText(currentPenghasilan);
                saldo.setText(currentSaldo);
                hutang.setText(currentHutang);
                if (currentFasilitas.equals("0")){
                    fasilitas.setSelection(0);
                } else if (currentFasilitas.equals("1")){
                    fasilitas.setSelection(1);
                } else if (currentFasilitas.equals("2")){
                    fasilitas.setSelection(2);
                } else if (currentFasilitas.equals("3")){
                    fasilitas.setSelection(3);
                } else if (currentFasilitas.equals("4")){
                    fasilitas.setSelection(4);
                } else {
                    fasilitas.setSelection(5);
                }
                if (currentBiChecking.equals("0")){
                    biChecking.setSelection(0);
                } else {
                    biChecking.setSelection(1);
                }
            }
        } finally {
            cursor.close();
        }
    }

    private void validasiIsi() {
        if (nama.getText().toString().trim().length() == 0) {
            nama.setError("Harus Diisi");
        } else if (alamat.getText().toString().trim().length() == 0) {
            alamat.setError("Harus Diisi");
        } else if (ttl.getText().toString().trim().length() == 0) {
            ttl.setError("Harus Diisi");
        } else if (telepon.getText().toString().trim().length() == 0) {
            telepon.setError("Harus Diisi");
        } else if (alamatKerja.getText().toString().trim().length() == 0) {
            alamatKerja.setError("Harus Diisi");
        } else if (penghasilan.getText().toString().trim().length() == 0) {
            penghasilan.setError("Harus Diisi");
        } else if (saldo.getText().toString().trim().length() == 0) {
            saldo.setError("Harus Diisi");
        } else if (hutang.getText().toString().trim().length() == 0) {
            hutang.setError("Harus Diisi");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Apakah Anda Yakin Akan Mengubah Data");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertData();
                            Intent intent = new Intent(EditActivity.this, ListNasabahEditActivity.class);
                            String input = "";
                            intent.putExtra("inp",input);
                            startActivity(intent);
                            Toast.makeText(EditActivity.this,"Data berhasil dirubah",Toast.LENGTH_LONG).show();
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
        }
    }

    private void insertData(){
        String namaString = nama.getText().toString().trim();
        String alamatString = alamat.getText().toString().trim();
        String ttlString = ttl.getText().toString().trim();
        String telpString = telepon.getText().toString().trim();
        String alamatKerjaString = alamatKerja.getText().toString().trim();
        String penghasilanString = penghasilan.getText().toString().trim();
        String saldoString = saldo.getText().toString().trim();
        String hutangString = hutang.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(data.COLOUMN_NAME, namaString);
        values.put(data.COLOUMN_ALAMAT, alamatString);
        values.put(data.COLOUMN_DOMISILI, sDomisili);
        values.put(data.COLOUMN_TANGGAL_LAHIR, ttlString);
        values.put(data.COLOUMN_TELEPON, telpString);
        values.put(data.COLOUMN_PEKERJAAN, sPekerjaan);
        values.put(data.COLOUMN_TEMPAT_KERJA, alamatKerjaString);
        values.put(data.COLOUMN_PENGHASILAN, penghasilanString);
        values.put(data.COLOUMN_SALDO_TABUNGAN, saldoString);
        values.put(data.COLOUMN_HUTANG, hutangString);
        values.put(data.COLOUMN_FASILITAS_DIBRI, sFasilitas);
        values.put(data.COLOUMN_BI_CHECKING, sBiChecking);

        db.update(data.TABLE_DATA,  values, data.DATA_ID + " = ?", new String[]{getId});

    }

    private void hapusData(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(data.TABLE_DATA, data.DATA_ID + " = ?",  new String[]{getId});
    }

    public void navDrawer(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        kondisi(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public int kondisi (int id){
        if (id == R.id.nav_tambah) {
            Intent intent = new Intent(this, TambahActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit) {
            Intent intent = new Intent(this, ListNasabahEditActivity.class);
            String input = "";
            intent.putExtra("inp",input);
            startActivity(intent);
        } else if (id == R.id.nav_proses) {
            Intent intent = new Intent(this, ProsesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_input) {
            Intent intent = new Intent(this, ListNasabahInputActivity.class);
            String input = "";
            intent.putExtra("inp",input);
            startActivity(intent);
        } else if (id == R.id.nav_beranda) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_kembali) {
            finish();
        }
        return id;
    }
}
