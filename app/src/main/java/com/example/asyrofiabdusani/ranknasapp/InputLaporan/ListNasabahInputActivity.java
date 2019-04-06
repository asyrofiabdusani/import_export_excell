package com.example.asyrofiabdusani.ranknasapp.InputLaporan;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.asyrofiabdusani.ranknasapp.Db.DbContract;
import com.example.asyrofiabdusani.ranknasapp.Db.DbHelper;
import com.example.asyrofiabdusani.ranknasapp.EditData.EditActivity;
import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.ListNasabah.ListNasabah;
import com.example.asyrofiabdusani.ranknasapp.ListNasabah.ListNasabahAdapter;
import com.example.asyrofiabdusani.ranknasapp.MainActivity;
import com.example.asyrofiabdusani.ranknasapp.ProsesData.ProsesActivity;
import com.example.asyrofiabdusani.ranknasapp.R;
import com.example.asyrofiabdusani.ranknasapp.Search.SearchEditActivity;
import com.example.asyrofiabdusani.ranknasapp.Search.SearchInputActivity;
import com.example.asyrofiabdusani.ranknasapp.TambahData.TambahActivity;

import java.util.ArrayList;

public class ListNasabahInputActivity extends AppCompatActivity {
    DbHelper mDbHelper = new DbHelper(this);
    private Cursor cursor;
    private ListNasabahAdapter mAdapter;
    private ArrayList<ListNasabah> list = new ArrayList<ListNasabah>();
    private ListView listView;
    private EditText search;
    private String getInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getValue();
        search = findViewById(R.id.search);
        search.setText(getInput);
        String searchString = search.getText().toString().trim();

        if (searchString.equals("")){
            dataDb();
        } else {
            DataSearch();
        }

        listView = findViewById(R.id.data_list);
        mAdapter = new ListNasabahAdapter(this, list);
        listView.setAdapter(mAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNasabahInputActivity.this, SearchInputActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListNasabahInputActivity.this, InputActivity.class);
                ListNasabah pilihId = mAdapter.getItem(position);
                intent.putExtra("id", pilihId.getmId());
                startActivity(intent);
            }
        });
    }

    private void getValue(){
        Bundle extra = getIntent().getExtras();
        getInput = extra.getString("input");
    }

    private void DataSearch(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT * FROM " + DbContract.data.TABLE_DATA+ " WHERE "
                        + DbContract.data.COLOUMN_NAME + " = ?",
                new String[]{getInput});

        try {
            int idColoumnIndex = cursor.getColumnIndex(DbContract.data.DATA_ID);
            int nameColoumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_NAME);
            int alamatColumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_ALAMAT);
            int telpColumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_TELEPON);

            while (cursor.moveToNext()) {
                String currentId = cursor.getString(idColoumnIndex);
                String currentName = cursor.getString(nameColoumnIndex);
                String currentAlamat = cursor.getString(alamatColumnIndex);
                String currentTelp = cursor.getString(telpColumnIndex);

                list.add(new ListNasabah("",currentId,currentName,currentAlamat,currentTelp));
            }
        } finally {
            cursor.close();
        }
    }

    private void dataDb() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DbContract.data.DATA_ID,
                DbContract.data.COLOUMN_NAME,
                DbContract.data.COLOUMN_ALAMAT,
                DbContract.data.COLOUMN_TELEPON
        };

        cursor = db.query(
                DbContract.data.TABLE_DATA,
                projection,
                null,
                null,
                null,
                null,
                DbContract.data.COLOUMN_NAME + " ASC");


        try {
            int idColoumnIndex = cursor.getColumnIndex(DbContract.data.DATA_ID);
            int nameColoumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_NAME);
            int alamatColumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_ALAMAT);
            int telpColumnIndex = cursor.getColumnIndex(DbContract.data.COLOUMN_TELEPON);

            while (cursor.moveToNext()) {
                String currentId = cursor.getString(idColoumnIndex);
                String currentName = cursor.getString(nameColoumnIndex);
                String currentAlamat = cursor.getString(alamatColumnIndex);
                String currentTelp = cursor.getString(telpColumnIndex);

                list.add(new ListNasabah("",currentId, currentName,currentAlamat,currentTelp));
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
