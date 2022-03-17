package com.example.demicuanapp;

import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.demicuanapp.entities.Cuan;
import com.example.demicuanapp.helper.SqliteHelper;
import com.example.demicuanapp.models.DemiCuanModel;

public class PengeluaranActivity extends AppCompatActivity {

    // All About Declaration
    Button btn_simpan;
    String status = "- KELUAR";
    SqliteHelper sqliteHelper;
    CurrencyEditText edit_jumlah;
    public DemiCuanModel mAturGajian;
    private ArrayAdapter<String> mAdapter;
    private String[] edit_keterangan = {"Pilih", "Makanan & Minuman", "Pendidikan", "Kesehatan", "Social Life", "Top Up", "Transaportasi","Investasi"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengeluaran);
        initData();
        initUI();

        Spinner ket = (Spinner) findViewById(R.id.spinner1);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, edit_keterangan);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ket.setAdapter(mAdapter);


        // Action untuk Button Simpan untuk pengeluaran
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EditJumlah = edit_jumlah.getText().toString();

                if (EditJumlah.length() <=2) {
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar", Toast.LENGTH_LONG).show();
                } else {
                    String keterangan = ket.getSelectedItem().toString();

                    Cuan cuanBaru = new Cuan();
                    cuanBaru.setStatus(status);
                    cuanBaru.setJumlah(edit_jumlah.getRawValue());
                    cuanBaru.setKeterangan(keterangan);

                    mAturGajian.insert(cuanBaru);
                    Toast.makeText(getApplicationContext(), "Transaksi berhasil disimpan", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pengeluaran");
    }


    //Cara mengelompokkan init view supaya rapih, ga numpuk dan tidak diletakkan di oncreate caranya buat methode baru
    //Terus Panggil Methodenya di On Create
    private void initUI() {
        sqliteHelper = new SqliteHelper(this);
        edit_jumlah = (CurrencyEditText) findViewById(R.id.edt_jumlah);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
    }


    private void initData() {
        this.mAturGajian = new DemiCuanModel(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
