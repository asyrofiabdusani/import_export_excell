package com.example.asyrofiabdusani.ranknasapp.ProsesData;

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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.data;
import com.example.asyrofiabdusani.ranknasapp.Db.DbContract;
import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.EksportData.EksportActivity;
import com.example.asyrofiabdusani.ranknasapp.ImportData.ImportActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.ListNasabah.ListNasabah;
import com.example.asyrofiabdusani.ranknasapp.ListNasabah.ListNasabahAdapter;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.Search.SearchEditActivity;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProsesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DbHelper mDbHelper = new DbHelper(this);
    String [] dId;
    String [] dPenghasilan;
    String [] dHutang;
    String [] dPekerjaan;
    String [] dFasilitas;
    String [] dLahir;
    String [] dBi;
    String [] dDomisili;
    int [] usia;
    int [] penghasilan;
    int [] hutang;
    int pj;
    Double [] iPenghasilan;
    Double [] iHutang;
    Double [] iPekerjaan;
    Double [] iFasilitas;
    Double [] iLahir;
    Double [] iBi;
    Double [] iDomisili;
    Double [] bobot;
    Double maxPenghasilan, minHutang, maxPekerjaan, maxFasilitas, maxLahir;
    Double bobotPenghasilan = 0.25, bobotHutang = 0.25, bobotPekerjaan = 0.20, bobotFasilitas = 0.15, bobotLahir = 0.15;
    ListView listView;
    ListNasabahAdapter mAdapter;
    private ArrayList<ListNasabah> list = new ArrayList<ListNasabah>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses);
        navDrawer();

        dataDb();
    }

    private void dataDb() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                data.DATA_ID,
                data.COLOUMN_PENGHASILAN,
                data.COLOUMN_HUTANG,
                data.COLOUMN_PEKERJAAN,
                data.COLOUMN_FASILITAS_DIBRI,
                data.COLOUMN_TANGGAL_LAHIR,
                data.COLOUMN_BI_CHECKING,
                data.COLOUMN_DOMISILI
        };

        Cursor cursor = db.query(
                data.TABLE_DATA,
                projection,
                null,
                null,
                null,
                null,
                null);

        pj = cursor.getCount();
        dId = new String[pj];
        dPenghasilan = new String[pj];
        dHutang = new String[pj];
        dPekerjaan = new String[pj];
        dFasilitas = new String[pj];
        dLahir = new String[pj];
        dBi = new String[pj];
        dDomisili = new String[pj];

        try {
            for (int i = 0; i < pj; i++) {
                cursor.moveToNext();
                dId[i] = cursor.getString(cursor.getColumnIndex(data.DATA_ID));
                dPenghasilan[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_PENGHASILAN));
                dHutang[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_HUTANG));
                dPekerjaan[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_PEKERJAAN));
                dFasilitas[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_FASILITAS_DIBRI));
                dLahir[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_TANGGAL_LAHIR));
                dBi[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_BI_CHECKING));
                dDomisili[i] = cursor.getString(cursor.getColumnIndex(data.COLOUMN_DOMISILI));
            }
        } finally {
            cursor.close();
        }
        konversiNilai();
    }

    private void konversiNilai() {
        usia = new int[pj];
        penghasilan = new int[pj];
        hutang = new int[pj];
        Date lsDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df = new SimpleDateFormat("dd");
        SimpleDateFormat mf = new SimpleDateFormat("MM");
        SimpleDateFormat yf = new SimpleDateFormat("yyyy");
        String lsDays = df.format(lsDate);
        String lsMonth = mf.format(lsDate);
        String lsYear = yf.format(lsDate);
        int lsDy = Integer.parseInt(lsDays);
        int lsMt = Integer.parseInt(lsMonth);
        int lsYr = Integer.parseInt(lsYear);
        Date stDate = null;

        for (int i = 0; i < pj; i++) {
            try {
                stDate = format.parse(dLahir[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String stDays = df.format(stDate);
            String stMonth = mf.format(stDate);
            String stYear = yf.format(stDate);
            int stMt = Integer.parseInt(stMonth);
            int stYr = Integer.parseInt(stYear);

            int jmlYr = 0;
            if (lsMt >= stMt) {
                 jmlYr = (lsYr - stYr);
            } else {
                jmlYr = ((lsYr - stYr) - 1);
            }
            usia [i]=jmlYr;

            penghasilan[i] = Integer.parseInt(dPenghasilan[i]);
            hutang[i] = Integer.parseInt(dHutang[i]);
        }
        konversiBobot();
    }

    void konversiBobot(){
        iPenghasilan = new Double[pj];
        iHutang = new Double[pj];
        iPekerjaan = new Double[pj];
        iFasilitas = new Double[pj];
        iLahir = new Double[pj];
        iBi = new Double[pj];
        iDomisili = new Double[pj];

        kondisiBobot();
    }

    void kondisiBobot(){
        int rasio;

        for (int i = 0; i < pj; i++){
            if (penghasilan[i] >= 0 && penghasilan[i] <= 15000000) {
                iPenghasilan[i] = 0.25;
            } else if (penghasilan[i] > 15000000 && penghasilan[i] <= 25000000) {
                iPenghasilan[i] = 0.5;
            } else {
                iPenghasilan[i] = 0.75;
            }
            rasio = ((hutang[i]/penghasilan[i])*100);
            if (rasio >= 0 && rasio <= 10){
                iHutang[i] = 1.0;
            } else if (rasio > 10 && rasio <= 20){
                iHutang[i] = 0.75;
            } else if (rasio > 20 && rasio <= 30){
                iHutang[i] = 0.5;
            } else if (rasio > 30 && rasio <= 50){
                iHutang[i] = 0.25;
            } else {
                iHutang[i] = 0.0;
            }
            if (dPekerjaan[i].equals("0")){
                iPekerjaan[i] = 0.5;
            } else if (dPekerjaan[i].equals("1")){
                iPekerjaan[i] = 1.0;
            } else {
                iPekerjaan[i]=0.75;
            }
            if (dFasilitas[i].equals("0")){
                iFasilitas[i] = 0.50;
            } else if (dFasilitas[i].equals("1")){
                iFasilitas[i] = 0.75;
            } else if (dFasilitas[i].equals("2")){
                iFasilitas[i] = 0.75;
            } else if (dFasilitas[i].equals("3")){
                iFasilitas[i] = 0.75;
            } else if (dFasilitas[i].equals("4")){
                iFasilitas[i] = 1.0;
            } else if (dFasilitas[i].equals("5")){
                iFasilitas[i] = 0.50;
            }
            if (usia[i] >= 0 && usia[i] <= 30){
                iLahir[i] = 0.25;
            } else if (usia[i] > 30 && usia[i] <= 45){
                iLahir[i] = 1.0;
            } else {
                iLahir[i] = 0.5;
            }
        }
       nilaiMax();
    }

    void nilaiMax(){
        minHutang = iHutang[0]; maxPenghasilan = iPenghasilan[0]; maxPekerjaan = iPekerjaan[0]; maxFasilitas = iFasilitas[0]; maxLahir = iLahir[0];
        for (int i = 1; i < pj; i++) {
            if (minHutang >= iHutang[i]) {
                minHutang = iHutang[i];
            }
            if (maxPenghasilan <= iPenghasilan[i]) {
                maxPenghasilan = iPenghasilan[i];
            }
            if (maxPekerjaan <= iPekerjaan[i]) {
                maxPekerjaan = iPekerjaan[i];
            }
            if (maxFasilitas <= iFasilitas[i]) {
                maxFasilitas = iFasilitas[i];
            }
            if (maxLahir <= iLahir[i]) {
                maxLahir = iLahir[i];
            }
        }
        normalisasi();
    }

    void normalisasi(){
        for (int i = 0; i < pj; i++) {
            iPenghasilan[i] = iPenghasilan[i]/maxPenghasilan;
            iHutang[i] = minHutang/iHutang[i];
            iPekerjaan[i] = iPekerjaan[i]/maxPekerjaan;
            iFasilitas[i] = iFasilitas[i]/maxFasilitas;
            iLahir[i] = iLahir[i]/maxLahir;
        }
        perkalianBobot();
    }

    void perkalianBobot(){
       for (int i = 0; i < pj; i++) {
            iPenghasilan[i] = iPenghasilan[i] * bobotPenghasilan;
            iHutang[i] = iHutang[i] * bobotHutang;
            iPekerjaan[i] = iPekerjaan[i] * bobotPekerjaan;
            iFasilitas[i] = iFasilitas[i] * bobotFasilitas;
            iLahir[i] = iLahir[i] * bobotLahir;
        }
        hitungBobot();
    }

    void hitungBobot(){
        bobot = new Double[pj];

        for (int i = 0; i < pj; i++) {
            if (dDomisili[i].equals("1") || dBi[i].equals("1")){
                bobot [i] = 0.0;
            } else {
                bobot[i] = iPenghasilan[i] + iHutang[i] + iPekerjaan[i] + iFasilitas[i] + iLahir[i];
            }
        }
        insertBobot();
    }

    void insertBobot(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (int i = 0; i < pj; i++) {
            ContentValues values = new ContentValues();
            values.put(data.COLOUMN_BOBOT, bobot[i]);

            db.update(data.TABLE_DATA, values, data.DATA_ID + " = ?", new String[]{String.valueOf(i+1)});
        }
        bacaDb();
    }

    void bacaDb(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery
                ("SELECT * FROM "
                + data.TABLE_DATA
                + " ORDER BY "
                + data.COLOUMN_BOBOT
                + " DESC", null);

        int i = 0;

        try {
            int idColoumnIndex = cursor.getColumnIndex(data.DATA_ID);
            int nameColoumnIndex = cursor.getColumnIndex(data.COLOUMN_NAME);
            int alamatColumnIndex = cursor.getColumnIndex(data.COLOUMN_ALAMAT);
            int telpColumnIndex = cursor.getColumnIndex(data.COLOUMN_TELEPON);

            while (cursor.moveToNext()) {
                i++;
                String currentId = cursor.getString(idColoumnIndex);
                String currentName = cursor.getString(nameColoumnIndex);
                String currentAlamat = cursor.getString(alamatColumnIndex);
                String currentTelp = cursor.getString(telpColumnIndex);

                list.add(new ListNasabah(String.valueOf(i),currentId, currentName, currentAlamat, currentTelp));
            }
        } finally {
            cursor.close();
        }

        listView = findViewById(R.id.data_list);
        mAdapter = new ListNasabahAdapter(this, list);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProsesActivity.this, RincianDataActivity.class);
                ListNasabah pilihId = mAdapter.getItem(position);
                intent.putExtra("id", pilihId.getmId());
                startActivity(intent);
            }
        });
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
