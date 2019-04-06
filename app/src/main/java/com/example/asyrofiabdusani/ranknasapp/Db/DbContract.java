package com.example.asyrofiabdusani.ranknasapp.Db;

import android.provider.BaseColumns;

/**
 * Created by Asyrofi Abdusani on 24/10/2018.
 */

public class DbContract {
    private DbContract() {}

    public static final class data implements BaseColumns {
        public final static String TABLE_DATA = "table_nasabah";

        public final static String DATA_ID = BaseColumns._ID;
        public final static String COLOUMN_NAME = "nama";
        public final static String COLOUMN_ALAMAT = "alamat";
        public final static String COLOUMN_DOMISILI = "domisili";
        public final static String COLOUMN_TANGGAL_LAHIR = "tanggal_lahir";
        public final static String COLOUMN_TELEPON = "telepon";
        public final static String COLOUMN_PEKERJAAN = "pekerjaan";
        public final static String COLOUMN_TEMPAT_KERJA = "tempat_kerja";
        public final static String COLOUMN_PENGHASILAN = "penghasilan";
        public final static String COLOUMN_SALDO_TABUNGAN= "saldo";
        public final static String COLOUMN_HUTANG = "hutang";
        public final static String COLOUMN_FASILITAS_DIBRI = "fasilitas";
        public final static String COLOUMN_BI_CHECKING = "bi_checking";
        public final static String COLOUMN_BOBOT = "bobot";
    }

    public static final class laporan implements BaseColumns {
        public final static String TABLE_LAPORAN = "table_laporan";

        public final static String LAPORAN_ID = BaseColumns._ID;
        public final static String COLOUMN_NASABAH_ID = "id_nasabah";
        public final static String COLOUMN_NAME = "nama";
        public final static String COLOUMN_TANGGAL = "tanggal";
        public final static String COLOUMN_LAPORAN = "laporan";
    }
}
