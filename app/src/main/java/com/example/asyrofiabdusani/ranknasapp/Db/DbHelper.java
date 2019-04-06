package com.example.asyrofiabdusani.ranknasapp.Db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.data;
import com.example.asyrofiabdusani.ranknasapp.Db.DbContract.laporan;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asyrofi Abdusani on 24/10/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "basic.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_DATA = "CREATE TABLE " + data.TABLE_DATA + " ("
                + data.DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + data.COLOUMN_NAME + " TEXT, "
                + data.COLOUMN_ALAMAT + " TEXT, "
                + data.COLOUMN_DOMISILI + " TEXTL, "
                + data.COLOUMN_TANGGAL_LAHIR + " TEXT, "
                + data.COLOUMN_TELEPON + " TEXT, "
                + data.COLOUMN_PEKERJAAN + " TEXT, "
                + data.COLOUMN_TEMPAT_KERJA + " TEXT, "
                + data.COLOUMN_PENGHASILAN + " TEXT, "
                + data.COLOUMN_SALDO_TABUNGAN + " TEXT, "
                + data.COLOUMN_HUTANG + " TEXT, "
                + data.COLOUMN_FASILITAS_DIBRI + " TEXT, "
                + data.COLOUMN_BI_CHECKING + " TEXT, "
                + data.COLOUMN_BOBOT + " TEXT);";

        String SQL_CREATE_TABLE_LAPORAN = "CREATE TABLE " + laporan.TABLE_LAPORAN + " ("
                + laporan.LAPORAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + laporan.COLOUMN_NASABAH_ID + " TEXT, "
                + laporan.COLOUMN_NAME + " TEXT, "
                + laporan.COLOUMN_TANGGAL + " TEXT, "
                + laporan.COLOUMN_LAPORAN + " TEXT);";

        db.execSQL(SQL_CREATE_TABLE_DATA);
        db.execSQL(SQL_CREATE_TABLE_LAPORAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<HashMap<String, String>> getAllProducts() {
        ArrayList<HashMap<String, String>> proList;
        proList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + data.TABLE_DATA;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Id", cursor.getString(0));
                map.put("Company", cursor.getString(1));
                map.put("Name", cursor.getString(2));
                map.put("Price", cursor.getString(3));
                proList.add(map);
            } while (cursor.moveToNext());
        }
        return proList;
    }
}