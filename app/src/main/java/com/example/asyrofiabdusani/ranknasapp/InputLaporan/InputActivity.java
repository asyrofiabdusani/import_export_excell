package com.example.asyrofiabdusani.ranknasapp.InputLaporan;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.data;
import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.laporan;
import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.EksportData.EksportActivity;
import com.example.asyrofiabdusani.ranknasapp.ImportData.ImportActivity;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nama;
    TextView telepon;
    TextView alamat;
    EditText laporanEdit;
    Button simpan;
    Button batal;
    DbHelper mDbHelper;
    String getId, getNama, getAlamat, getTelepon;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        mDbHelper = new DbHelper(this);
        navDrawer();
        deklarasi();

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiIsi();
            }
        });
    }

    private void deklarasi(){
        nama = findViewById(R.id.nama);
        telepon = findViewById(R.id.telepon);
        alamat = findViewById(R.id.alamat);
        laporanEdit = findViewById(R.id.laporan);
        batal = findViewById(R.id.bt_batal);
        simpan = findViewById(R.id.bt_simpan);

        bacaDb();
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
            int telpDb = cursor.getColumnIndex(data.COLOUMN_TELEPON);

            while (cursor.moveToNext()) {
                getNama = cursor.getString(namaDb);
                getAlamat = cursor.getString(alamatDb);
                getTelepon = cursor.getString(telpDb);

                nama.setText(getNama);
                alamat.setText(getAlamat);
                telepon.setText(getTelepon);
            }
        } finally {
            cursor.close();
        }
    }

    private void validasiIsi() {
        if (laporanEdit.getText().toString().trim().length() == 0) {
            laporanEdit.setError("Harus Diisi");
        } else {
            insertLaporan();
        }
    }
    private void insertLaporan(){
        Date dataId = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String tanggal = df.format(dataId);

        String laporanString = laporanEdit.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(laporan.COLOUMN_TANGGAL, tanggal);
        values.put(laporan.COLOUMN_NASABAH_ID, getId);
        values.put(laporan.COLOUMN_NAME, getNama);
        values.put(laporan.COLOUMN_LAPORAN, laporanString);

        long newRowId = db.insert(laporan.TABLE_LAPORAN, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Laporan gagal disimpan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Laporan berhasil disimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
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

