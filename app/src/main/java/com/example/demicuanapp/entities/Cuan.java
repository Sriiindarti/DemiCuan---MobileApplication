package com.example.demicuanapp.entities;

// Membuat Model Data/tipe data
// Sebagai Setter dan Getter untuk mendapatkan data
public class Cuan {
    private int transaksi_id;
    private String status;
    private Long jumlah;
    private String keterangan;
    private String date;

    public int getId() {
        return transaksi_id;
    }

    public void setId(int id) {
        this.transaksi_id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Cuan()
    {
        this.transaksi_id = -1;
    }
}
