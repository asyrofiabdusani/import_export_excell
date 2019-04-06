package com.example.asyrofiabdusani.ranknasapp.ProsesData;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbContract;
import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RincianDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    Button batal;
    String getId;
    DbHelper mDbHelper;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        navDrawer();
        mDbHelper = new DbHelper(this);
        deklarasi();
    }

    private void deklarasi(){
        nama = findViewById(R.id.nama);
        nama.setClickable(false);
        nama.setCursorVisible(false);
        nama.setFocusableInTouchMode(false);
        nama.setFocusable(false);
        alamat = findViewById(R.id.alamat);
        alamat.setClickable(false);
        alamat.setCursorVisible(false);
        alamat.setFocusableInTouchMode(false);
        alamat.setFocusable(false);
        domisili = findViewById(R.id.domisili);
        domisili.setClickable(false);
        domisili.setEnabled(false);
        domisili.setFocusableInTouchMode(false);
        domisili.setFocusable(false);
        ttl = findViewById(R.id.tanggal_lahir);
        ttl.setClickable(false);
        ttl.setCursorVisible(false);
        ttl.setFocusableInTouchMode(false);
        ttl.setFocusable(false);
        kalender = findViewById(R.id.kalender);
        kalender.setVisibility(View.INVISIBLE);
        telepon = findViewById(R.id.telpon);
        telepon.setClickable(false);
        telepon.setCursorVisible(false);
        telepon.setFocusableInTouchMode(false);
        telepon.setFocusable(false);
        pekerjaan = findViewById(R.id.pekerjaan);
        pekerjaan.setClickable(false);
        pekerjaan.setEnabled(false);
        pekerjaan.setFocusableInTouchMode(false);
        pekerjaan.setFocusable(false);
        alamatKerja = findViewById(R.id.alamat_kerja);
        alamatKerja.setClickable(false);
        alamatKerja.setCursorVisible(false);
        alamatKerja.setFocusableInTouchMode(false);
        alamatKerja.setFocusable(false);
        penghasilan = findViewById(R.id.penghasilan);
        penghasilan.setClickable(false);
        penghasilan.setCursorVisible(false);
        penghasilan.setFocusableInTouchMode(false);
        penghasilan.setFocusable(false);
        saldo = findViewById(R.id.saldo);
        saldo.setClickable(false);
        saldo.setCursorVisible(false);
        saldo.setFocusableInTouchMode(false);
        saldo.setFocusable(false);
        hutang = findViewById(R.id.hutang);
        hutang.setClickable(false);
        hutang.setCursorVisible(false);
        hutang.setFocusableInTouchMode(false);
        hutang.setFocusable(false);
        fasilitas = findViewById(R.id.fasilitas);
        fasilitas.setClickable(false);
        fasilitas.setEnabled(false);
        fasilitas.setFocusableInTouchMode(false);
        fasilitas.setFocusable(false);
        biChecking = findViewById(R.id.bi_checking);
        biChecking.setClickable(false);
        biChecking.setEnabled(false);
        biChecking.setFocusableInTouchMode(false);
        biChecking.setFocusable(false);
        simpan = findViewById(R.id.bt_simpan);
        simpan.setVisibility(View.INVISIBLE);
        batal = findViewById(R.id.bt_hapus);
        batal.setVisibility(View.INVISIBLE);

        bacaDb();
    }

    private void bacaDb(){
        Bundle extra = getIntent().getExtras();
        getId = extra.getString("id");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DbContract.data.TABLE_DATA + " WHERE "
                        + DbContract.data.DATA_ID + " = ?",
                new String[]{getId});
        try {
            int namaDb = cursor.getColumnIndex(DbContract.data.COLOUMN_NAME);
            int alamatDb = cursor.getColumnIndex(DbContract.data.COLOUMN_ALAMAT);
            int domisiliDb = cursor.getColumnIndex(DbContract.data.COLOUMN_DOMISILI);
            int ttlDb = cursor.getColumnIndex(DbContract.data.COLOUMN_TANGGAL_LAHIR);
            int telpDb = cursor.getColumnIndex(DbContract.data.COLOUMN_TELEPON);
            int pekerjaanDb = cursor.getColumnIndex(DbContract.data.COLOUMN_PEKERJAAN);
            int tempatKerjaDb = cursor.getColumnIndex(DbContract.data.COLOUMN_TEMPAT_KERJA);
            int penghasilanDb = cursor.getColumnIndex(DbContract.data.COLOUMN_PENGHASILAN);
            int saldoDb = cursor.getColumnIndex(DbContract.data.COLOUMN_SALDO_TABUNGAN);
            int hutangDb = cursor.getColumnIndex(DbContract.data.COLOUMN_HUTANG);
            int fasilitasDb = cursor.getColumnIndex(DbContract.data.COLOUMN_FASILITAS_DIBRI);
            int biChcekingDb = cursor.getColumnIndex(DbContract.data.COLOUMN_BI_CHECKING);

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
