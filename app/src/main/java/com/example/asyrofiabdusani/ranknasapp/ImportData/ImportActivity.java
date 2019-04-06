package com.example.asyrofiabdusani.ranknasapp.ImportData;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asyrofiabdusani.ranknasapp.Db.DbContract;
import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.InputActivity;
import com.example.asyrofiabdusani.ranknasapp.InputLaporan.ListNasabahInputActivity;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ImportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;
    Button btnUpDirectory, btnSDCard;
    ArrayList<String> pathHistory;
    String lastDirectory;
    int j, count = 0;
    String [] sNama, sAlamat, sDomisili, sLahir, sTelepon, sPekerjaan, sTempatkerja, sPenghasilan, sSaldo, sHutang, sFasilitas, sBi;
    ArrayList<listNasabah> uploadData;
    ListView lvInternalStorage;
    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        navDrawer();
        mDbHelper = new DbHelper(this);

        lvInternalStorage = findViewById(R.id.lvInternalStorage);
        btnUpDirectory = findViewById(R.id.btnUpDirectory);
        btnSDCard = findViewById(R.id.btnViewSDCard);
        uploadData = new ArrayList<>();

        btnSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                pathHistory = new ArrayList<String>();
                pathHistory.add(count,System.getenv("EXTERNAL_STORAGE"));
                Log.d(TAG, "btnSDCard: " + pathHistory.get(count));
                checkInternalStorage();
            }
        });

        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){

                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                }
            }
        });

        lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if(lastDirectory.equals(adapterView.getItemAtPosition(i))){
                    readExcelData(lastDirectory);
                } else {
                    count++;
                    pathHistory.add(count,(String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                }
            }
        });
    }

    private void checkInternalStorage() {
        Log.d(TAG, "checkInternalStorage: Started.");
        try{
            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                toastMessage("No SD card found.");
            }
            else{
                file = new File(pathHistory.get(count));
                Log.d(TAG, "checkInternalStorage: directory path: " + pathHistory.get(count));
            }

            listFile = file.listFiles();
            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FilePathStrings);
            lvInternalStorage.setAdapter(adapter);
        }   catch(NullPointerException e){
            Log.e(TAG, "checkInternalStorage: NULLPOINTEREXCEPTION " + e.getMessage() );
        }
    }

    private void readExcelData(String filePath) {
        Log.d(TAG, "readExcelData: Reading Excel File.");
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                for (int c = 0; c < cellsCount; c++) {
                    if(c>12){
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! " );
                        toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    } else {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        sb.append(value + ",");
                    }
                }
                sb.append(":");
            }
            parseStringBuilder(sb);

        }catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage() );
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage() );
        }
    }

    public void parseStringBuilder(StringBuilder mStringBuilder){
       String[] rows = mStringBuilder.toString().split(":");

        for(int i=0; i<rows.length; i++) {
            String[] columns = rows[i].split(",");

            try{
                String nama = columns[0];
                String alamat = columns[1];
                String domisili = columns[2];
                String lahir = columns[3];
                String telepon = columns[4];
                String pekerjaan = columns[5];
                String tempatKerja = columns[6];
                String penghasilan = columns[7];
                String saldo = columns[8];
                String hutang = columns[9];
                String fasilitas = columns[10];
                String Bi = columns[11];

                uploadData.add(new listNasabah(nama, alamat,domisili, lahir, telepon, pekerjaan, tempatKerja, penghasilan, saldo, hutang, fasilitas, Bi));
                } catch (NumberFormatException e){
            }
        }
        printDataToLog();
    }

    private void printDataToLog() {
        j = uploadData.size();
        sNama = new String[j];
        sAlamat = new String[j];
        sDomisili = new String[j];
        sLahir = new String[j];
        sTelepon = new String[j];
        sPekerjaan = new String[j];
        sTempatkerja = new String[j];
        sPenghasilan = new String[j];
        sSaldo = new String[j];
        sHutang = new String[j];
        sFasilitas = new String[j];
        sBi = new String[j];

        for(int i = 0; i< j; i++){
            String nama = uploadData.get(i).getmNama();
            String alamat = uploadData.get(i).getmAlamat();
            String domisili = uploadData.get(i).getmDomisili();
            String lahir = uploadData.get(i).getmLahir();
            String telepon = uploadData.get(i).getmTelepon();
            String pekerjaan = uploadData.get(i).getmPekerjaan();
            String tempatKerja = uploadData.get(i).getmTempatKerja();
            String penghasilan = uploadData.get(i).getmPenghasilan();
            String saldo = uploadData.get(i).getmSaldo();
            String hutang = uploadData.get(i).getmHutang();
            String fasilitas = uploadData.get(i).getmFasilitas();
            String bi = uploadData.get(i).getmBi();

            sNama [i] = nama;
            sAlamat [i] = alamat;
            sDomisili [i] = domisili;
            sLahir [i] = lahir;
            sTelepon [i] = telepon;
            sPekerjaan [i] = pekerjaan;
            sTempatkerja [i] = tempatKerja;
            sPenghasilan [i] = penghasilan;
            sSaldo [i] = saldo;
            sHutang [i] = hutang;
            sFasilitas [i] = fasilitas;
            sBi [i] = bi;
        }
        insertData();
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = ""+cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = ""+numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = ""+cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage() );
        }
        return value;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void insertData(){
        for (int i = 0; i<j; i++) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DbContract.data.COLOUMN_NAME, sNama[i]);
            values.put(DbContract.data.COLOUMN_ALAMAT, sAlamat[i]);
            values.put(DbContract.data.COLOUMN_DOMISILI, sDomisili[i]);
            values.put(DbContract.data.COLOUMN_TANGGAL_LAHIR, sLahir[i]);
            values.put(DbContract.data.COLOUMN_TELEPON, sTelepon[i]);
            values.put(DbContract.data.COLOUMN_PEKERJAAN, sPekerjaan[i]);
            values.put(DbContract.data.COLOUMN_TEMPAT_KERJA, sTempatkerja[i]);
            values.put(DbContract.data.COLOUMN_PENGHASILAN, sPenghasilan[i]);
            values.put(DbContract.data.COLOUMN_SALDO_TABUNGAN, sSaldo[i]);
            values.put(DbContract.data.COLOUMN_HUTANG, sHutang[i]);
            values.put(DbContract.data.COLOUMN_FASILITAS_DIBRI, sFasilitas[i]);
            values.put(DbContract.data.COLOUMN_BI_CHECKING, sBi[i]);

            db.insert(DbContract.data.TABLE_DATA, null, values);
        }
        Toast.makeText(this, "Import Data Berhasil", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void navDrawer() {
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

class listNasabah {
    private String mNama, mAlamat, mDomisili, mLahir, mTelepon, mPekerjaan, mTempatKerja, mPenghasilan, mSaldo, mHutang, mFasilitas, mBi;

    public listNasabah(String defNama, String defAlamat, String defDomisili, String defLahir, String defTelepon, String defPekerjaan, String defTempatKerja,
    String defPenghasilan, String defSaldo, String defHutang, String defFasilitas, String defBi) {
        mNama = defNama;
        mAlamat = defAlamat;
        mDomisili = defDomisili;
        mLahir = defLahir;
        mTelepon = defTelepon;
        mPekerjaan = defPekerjaan;
        mTempatKerja = defTempatKerja;
        mPenghasilan = defPenghasilan;
        mSaldo = defSaldo;
        mHutang = defHutang;
        mFasilitas = defFasilitas;
        mBi = defBi;
    }

    public String getmNama(){
        return mNama;
    }
    public String getmAlamat(){
        return mAlamat;
    }
    public String getmDomisili(){
        return mDomisili;
    }
    public String getmLahir(){
        return mLahir;
    }
    public String getmTelepon(){
        return mTelepon;
    }
    public String getmPekerjaan(){
        return mPekerjaan;
    }
    public String getmTempatKerja(){
        return mTempatKerja;
    }
    public String getmPenghasilan(){
        return mPenghasilan;
    }
    public String getmSaldo(){
        return mSaldo;
    }
    public String getmHutang(){
        return mHutang;
    }
    public String getmFasilitas(){
        return mFasilitas;
    }
    public String getmBi(){
        return mBi;
    }
}
