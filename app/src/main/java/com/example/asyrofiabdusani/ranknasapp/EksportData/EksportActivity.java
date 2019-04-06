package com.example.asyrofiabdusani.ranknasapp.EksportData;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.ImportData.ImportActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class EksportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eksport);

        navDrawer();

        Date dataId = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy_kkmm");
        final String tgl = df.format(dataId);
        final String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Laporan Nasabah/"+tgl+"/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DbHelper.DATABASE_NAME, directory_path);
        sqliteToExcel.exportAllTables("Laporan.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(EksportActivity.this,"Eksport Laporan Berhasil", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(EksportActivity.this,"Eksport Laporan Gagal", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void navDrawer(){
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

class SQLiteToExcel {

    private static Handler handler = new Handler(Looper.getMainLooper());

    private Context mContext;
    private SQLiteDatabase database;
    private String mDbName;
    private String mExportPath;
    private HSSFWorkbook workbook;

    public SQLiteToExcel(Context context, String dbName) {
        this(context, dbName, Environment.getExternalStorageDirectory().toString() + File.separator);
    }

    public SQLiteToExcel(Context context, String dbName, String exportPath) {
        mContext = context;
        mDbName = dbName;
        mExportPath = exportPath;
        try {
            database = SQLiteDatabase.openOrCreateDatabase(mContext.getDatabasePath(mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllTables() {
        ArrayList<String> tables = new ArrayList<>();
        Cursor cursor = database.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0));
        }
        cursor.close();
        return tables;
    }

    private ArrayList<String> getColumns(String table) {
        ArrayList<String> columns = new ArrayList<>();
        Cursor cursor = database.rawQuery("PRAGMA table_info(" + table + ")", null);
        while (cursor.moveToNext()) {
            columns.add(cursor.getString(1));
        }
        cursor.close();
        return columns;
    }

    private void exportTables(List<String> tables, final String fileName) throws Exception {
        workbook = new HSSFWorkbook();
        for (int i = 0; i < tables.size(); i++) {
            if (!tables.get(i).equals("android_metadata")) {
                HSSFSheet sheet = workbook.createSheet(tables.get(i));
                createSheet(tables.get(i), sheet);
            }
        }
        File file = new File(mExportPath, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.flush();
        fos.close();
        workbook.close();
        database.close();
    }

    public void exportSingleTable(final String table, final String fileName, ExportListener listener) {
        List<String> tables = new ArrayList<>();
        tables.add(table);
        startExportTables(tables, fileName, listener);
    }

    public void exportSpecificTables(final ArrayList<String> tables, String fileName, ExportListener listener) {
        startExportTables(tables, fileName, listener);
    }

    public void exportAllTables(final String fileName, ExportListener listener) {
        ArrayList<String> tables = getAllTables();
        startExportTables(tables, fileName, listener);
    }

    public void startExportTables(final List<String> tables, final String fileName, final ExportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    exportTables(tables, fileName);
                    if (listener != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onCompleted(mExportPath + fileName);
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (database != null && database.isOpen()) {
                        database.close();
                    }
                    if (listener != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(e);
                            }
                        });
                }
            }
        }).start();
    }

    private void createSheet(String table, HSSFSheet sheet) {
        HSSFRow rowA = sheet.createRow(0);
        ArrayList<String> columns = getColumns(table);
        for (int i = 0; i < columns.size(); i++) {
            HSSFCell cellA = rowA.createCell(i);
            cellA.setCellValue(new HSSFRichTextString("" + columns.get(i)));
        }
        insertItemToSheet(table, sheet, columns);
    }

    private void insertItemToSheet(String table, HSSFSheet sheet, ArrayList<String> columns) {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        Cursor cursor = database.rawQuery("select * from " + table, null);
        cursor.moveToFirst();
        int n = 1;
        while (!cursor.isAfterLast()) {
            HSSFRow rowA = sheet.createRow(n);
            for (int j = 0; j < columns.size(); j++) {
                HSSFCell cellA = rowA.createCell(j);
                if (cursor.getType(j) == Cursor.FIELD_TYPE_BLOB) {
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) j, n, (short) (j + 1), n + 1);
                    anchor.setAnchorType(3);
                    patriarch.createPicture(anchor, workbook.addPicture(cursor.getBlob(j), HSSFWorkbook.PICTURE_TYPE_JPEG));
                } else {
                    cellA.setCellValue(new HSSFRichTextString(cursor.getString(j)));
                }
            }
            n++;
            cursor.moveToNext();
        }
        cursor.close();
    }

    public interface ExportListener {
        void onStart();

        void onCompleted(String filePath);

        void onError(Exception e);
    }

}
