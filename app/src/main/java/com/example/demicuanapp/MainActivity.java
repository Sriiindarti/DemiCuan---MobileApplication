package com.example.demicuanapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demicuanapp.adapter.CuanAdapter;
import com.example.demicuanapp.entities.Cuan;
import com.example.demicuanapp.helper.SqliteHelper;
import com.example.demicuanapp.models.DemiCuanModel;
import com.example.demicuanapp.utils.RecyclerItemClickListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    //All About Deklarasi
    private List<Cuan> ListTransaksi = new ArrayList<Cuan>();
    private RecyclerView recyclerView;
    private CuanAdapter adapter;
    public static String transaksi_id;
    public static String tgl_dari;
    public static String tgl_ke;
    public static boolean filter;
    DemiCuanModel demiCuanModel;
    SwipeRefreshLayout swipe_refresh;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1;
    FloatingActionButton floatingActionButton2;
    SqliteHelper sqliteHelper;
    TextView text_masuk;
    TextView text_keluar;
    TextView text_total;
    TextView text_jumlah;
    String query_total;
    String query_kas;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initUI();

        // Untuk pindah activity dari Main Activity ke Activity Keluar(Pengeluaran) Menggunakan Intent
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PengeluaranActivity.class));
            }
        });

        // Untuk pindah activity dari Main Activity ke Activity Keluar(Pemasukkan) Menggunakan Intent
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PemasukanActivity.class));
            }
        });


        // untuk menjumlahakan saldo nih
        query_total = "SELECT SUM (jumlah) AS total, " +
                "(SELECT SUM (jumlah) FROM transaksi WHERE status = 'MASUK') AS masuk, " +
                "(SELECT SUM (jumlah) FROM transaksi WHERE status = 'KELUAR') AS keluar " +
                "FROM transaksi";


        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query_total = "SELECT SUM (jumlah) AS total, " +
                        "(SELECT SUM (jumlah) FROM transaksi WHERE status = 'MASUK') AS masuk, " +
                        "(SELECT SUM (jumlah) FROM transaksi WHERE status = 'KELUAR') AS keluar " +
                        "FROM transaksi";
                KasAdapter();
            }
        });
        KasAdapter();
    }


    private void KasAdapter() {
        ListTransaksi.clear();
        recyclerView.setAdapter(null);

        ListTransaksi.addAll(demiCuanModel.allTransaksi());

        adapter = new CuanAdapter(this, ListTransaksi);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        transaksi_id = ((TextView) view.findViewById(R.id.text_transaksi_id)).getText().toString();
                        ListMenu();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        KasTotal();
    }


    private void KasTotal() {
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);

        SQLiteDatabase database = sqliteHelper.getReadableDatabase();
        cursor = database.rawQuery(query_total, null);
        cursor.moveToFirst();
        text_masuk.setText("Rp" + rupiahFormat.format(cursor.getDouble(1)));
        text_keluar.setText("Rp" + rupiahFormat.format(cursor.getDouble(2)));
        text_total.setText("Rp" + rupiahFormat.format(cursor.getDouble(1) - cursor.getDouble(2))
        );
        swipe_refresh.setRefreshing(false);
    }


    //Cara mengelompokkan init view supaya rapih, ga numpuk dan tidak diletakkan di oncreate caranya buat methode baru
    //Terus Panggil Methodenya di On Create
    private void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_keuangan);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1); //Buletan Menu
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2); //Buletan Menu
        sqliteHelper = new SqliteHelper(this);
        transaksi_id = "";
        tgl_dari = "";
        tgl_ke = "";
        query_kas = "";
        query_total = "";
        filter = false;
        text_masuk = (TextView) findViewById(R.id.text_masuk);
        text_keluar = (TextView) findViewById(R.id.text_keluar);
        text_total = (TextView) findViewById(R.id.text_total);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        text_jumlah = (TextView) findViewById(R.id.text_jumlah);

    }


    // Supaya List Transaksi Tetap Terlihat
    @Override
    public void onResume() {
        super.onResume();

        query_total = "SELECT SUM (jumlah) AS total, " +
                "(SELECT SUM (jumlah) FROM transaksi WHERE status = '+ MASUK') AS masuk, " +
                "(SELECT SUM (jumlah) FROM transaksi WHERE status = '- KELUAR') AS keluar " +
                "FROM transaksi";
        KasAdapter();
    }


    // Memunculkan PopUp Edit dan Hapus
    private void ListMenu() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.edit_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button text_edit = (Button) dialog.findViewById(R.id.text_edit);
        Button text_hapus = (Button) dialog.findViewById(R.id.text_hapus);
        dialog.show();

        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });

        text_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Hapus();
            }
        });
    }


    // Methode Hapus/Delete
    // Memunculkan Dialog Konfirmasi
    private void Hapus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk mengahapus transaksi ini?");
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        demiCuanModel.delete(transaksi_id);

                        Toast.makeText(getApplicationContext(), "Tranksaki berhasil dihapus", Toast.LENGTH_LONG).show();

                        KasAdapter();
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }


    private void initData() {
        this.demiCuanModel = new DemiCuanModel(this);
    }
}
