package com.example.asyrofiabdusani.ranknasapp.ListNasabah;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asyrofiabdusani.ranknasapp.R;

import java.util.ArrayList;

/**
 * Created by Asyrofi Abdusani on 23/11/2018.
 */

public class ListNasabah {
    private String mNomor;
    private String mId;
    private String mNama;
    private String mAlamat;
    private String mTelpon;

    public ListNasabah (String defNomor, String defId, String defNama, String defAlamat, String defTelpon){
        mNomor = defNomor;
        mId = defId;
        mNama = defNama;
        mAlamat = defAlamat;
        mTelpon = defTelpon;
    }
    public String getmNomor(){ return mNomor; }
    public String getmId(){ return mId; }
    public String getmNama(){ return mNama; }
    public String getmAlamat(){ return mAlamat; }
    public String getmTelpon(){ return mTelpon; }
}


