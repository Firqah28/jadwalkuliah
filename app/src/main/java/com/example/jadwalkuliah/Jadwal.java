package com.example.jadwalkuliah;

public class Jadwal {
    private String namaMK;
    private String dosen;
    private String hari;
    private String jam;
    private String ruangan;
    private String id;

    public Jadwal() {
        // diperlukan untuk Firestore
    }

    public Jadwal(String namaMK, String dosen, String hari, String jam, String ruangan) {
        this.namaMK = namaMK;
        this.dosen = dosen;
        this.hari = hari;
        this.jam = jam;
        this.ruangan = ruangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaMK() { return namaMK; }
    public String getDosen() { return dosen; }
    public String getHari() { return hari; }
    public String getJam() { return jam; }
    public String getRuangan() { return ruangan; }

    @Override
    public String toString() {
        return hari + ": " + namaMK + " (" + jam + ")";
    }
}
