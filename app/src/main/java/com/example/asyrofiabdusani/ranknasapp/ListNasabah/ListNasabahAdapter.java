package com.example.asyrofiabdusani.ranknasapp.ListNasabah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asyrofiabdusani.ranknasapp.R;

import java.util.ArrayList;

public class ListNasabahAdapter extends ArrayAdapter<ListNasabah> {

    public ListNasabahAdapter(Context context, ArrayList<ListNasabah> list) {
            super(context, 0, list);
        }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_view, parent, false);
        }
        ListNasabah list = getItem(position);

        TextView nomor = listItemView.findViewById(R.id.nomor);
        nomor.setText(list.getmNomor());

        TextView Nama = listItemView.findViewById(R.id.nama);
        Nama.setText(list.getmNama());

        TextView Alamat = listItemView.findViewById(R.id.alamat);
        Alamat.setText(list.getmAlamat());

        TextView Telpon = listItemView.findViewById(R.id.telpon);
        Telpon.setText(list.getmTelpon());

        return listItemView;
    }
}
