package com.example.asyrofiabdusani.ranknasapp.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.asyrofiabdusani.ranknasapp.EditData.ListNasabahEditActivity;
import com.example.asyrofiabdusani.ranknasapp.R;

public class SearchEditActivity extends AppCompatActivity {
    private String Nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final EditText searchText = findViewById(R.id.search_text);
        searchText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        ImageView searchBt = findViewById(R.id.search_setting);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nama = searchText.getText().toString().trim();
                Intent intent = new Intent(SearchEditActivity.this, ListNasabahEditActivity.class);
                intent.putExtra("input", Nama);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
