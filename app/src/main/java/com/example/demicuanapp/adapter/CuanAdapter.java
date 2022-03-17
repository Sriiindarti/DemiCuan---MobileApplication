package com.example.demicuanapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demicuanapp.R;
import com.example.demicuanapp.entities.Cuan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CuanAdapter extends RecyclerView.Adapter<CuanAdapter.MyViewHolder> {

    private Context context;
    private List<Cuan> transaksiList;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTransaksi;
        public TextView tvKeterangan;
        public TextView tvJumlah;
        public TextView tvStatus;
        public TextView tvTanggal;

        public MyViewHolder(View view) {
            super(view);
            tvTransaksi = view.findViewById(R.id.text_transaksi_id);
            tvKeterangan = view.findViewById(R.id.text_keterangan);
            tvJumlah = view.findViewById(R.id.text_jumlah);
            tvStatus = view.findViewById(R.id.text_status);
            tvTanggal = view.findViewById(R.id.text_tanggal);

        }
    }

    public CuanAdapter(Context context, List<Cuan>noteslist){
        this.context = context;
        this.transaksiList = noteslist;
    }


    //Proses Dipanggilnya List item_keuangan
    //Dan di return kembali
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_transaksi,parent,false);

        return new MyViewHolder(itemView);
    }


    // Untuk membuat list TRANSAKSI
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Cuan cuan = transaksiList.get(position);

        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);

        holder.tvTransaksi.setText(String.valueOf(cuan.getId()));
        holder.tvKeterangan.setText(cuan.getKeterangan());
        holder.tvJumlah.setText("Rp"+rupiahFormat.format(cuan.getJumlah()));
        holder.tvStatus.setText(cuan.getStatus());
        holder.tvTanggal.setText(cuan.getDate());

    }


    @Override
    public int getItemCount() {
        return transaksiList.size();
    }
}
