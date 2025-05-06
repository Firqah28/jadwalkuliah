package com.example.jadwalkuliah;

public class Tugas {
    private String judul;
    private String deskripsi;
    private String deadline;

    // Constructor kosong WAJIB untuk Firestore
    public Tugas() {
    }

    // Constructor opsional
    public Tugas(String judul, String deskripsi, String deadline) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.deadline = deadline;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
