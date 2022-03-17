package com.example.demicuanapp.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.demicuanapp.entities.Cuan;
import com.example.demicuanapp.helper.SqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class DemiCuanModel {
    private Context context;
    private SqliteHelper db;

    public DemiCuanModel(Context context){
        this.context = context;
        this.db = new SqliteHelper(context);
    }

    // untuk meng-get all data/read data dan dipanggil di main Activity
    public List<Cuan> allTransaksi(){
        List<Cuan> cuans = new ArrayList<>();
        String selectQuery = "SELECT *, strftime('%d/%m/%Y',tanggal) AS tgl FROM transaksi ORDER BY transaksi_id DESC";

        SQLiteDatabase db = this.db.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        int i;

        //proses looping
        for (i=0; i < cursor.getCount(); i++){

            cursor.moveToPosition(i);
            Cuan cuan = new Cuan();

            cuan.setId(cursor.getInt(0));
            cuan.setStatus(cursor.getString(1));
            cuan.setJumlah(cursor.getLong(2));
            cuan.setKeterangan(cursor.getString(3));
            cuan.setDate(cursor.getString(4));
            cuans.add(cuan);
        }

        db.close();
        return cuans;
    }


    // Proses Insert Ke Database SQLITE
    public void insert(Cuan cuanBaru){
        String status = cuanBaru.getStatus();
        Long jumlah = cuanBaru.getJumlah();
        String keterangan = cuanBaru.getKeterangan();

        String sql = "INSERT INTO transaksi (status,jumlah,keterangan) VALUES ('"+status+"','"+jumlah+"','"+keterangan+"')";
        this.db.executeWrite(sql);
    }


    // Proses Delete Ke Database SQLITE
    public void delete(String transaksi){
        String sql = "DELETE FROM transaksi WHERE transaksi_id = '"+transaksi+"' ";
        this.db.executeWrite(sql);
    }
}
