package com.example.jadwalkuliah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;

    TextView tvGreeting;
    FloatingActionButton btnTambahJadwal;
    Button btnListTugas;
    RecyclerView recyclerHari;

    ArrayList<Jadwal> jadwalList;
    JadwalRecyclerAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        loadJadwal(); // Refresh data setiap kembali

        // Pindahkan cek intent ke sini
        Intent intent = getIntent();
        String successMessage = intent.getStringExtra("success_message");
        if (successMessage != null) {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            intent.removeExtra("success_message"); // Hapus supaya tidak double toast
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvGreeting = findViewById(R.id.tvGreeting);
        btnTambahJadwal = findViewById(R.id.btnTambahJadwal);
        btnListTugas = findViewById(R.id.btnListTugas);
        recyclerHari = findViewById(R.id.recyclerHari);

        // Menampilkan email pengguna yang terautentikasi
        String userEmail = auth.getCurrentUser() != null ? auth.getCurrentUser().getEmail() : "User";
        tvGreeting.setText("Halo, " + userEmail + "!");

        jadwalList = new ArrayList<>();
        adapter = new JadwalRecyclerAdapter(this, jadwalList);

        // Set RecyclerView layout manager
        recyclerHari.setLayoutManager(new LinearLayoutManager(this));
        recyclerHari.setAdapter(adapter);

        // Ambil data dari Firestore
        loadJadwal();

        // Tombol tambah jadwal, membuka activity TambahJadwalActivity
        btnTambahJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahJadwalActivity.class);
            startActivity(intent);
        });

        // Tombol daftar tugas, membuka activity TugasActivity
        btnListTugas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TugasActivity.class);
            startActivity(intent);
        });
    }

    // Fungsi untuk mengambil data jadwal dari Firestore
    private void loadJadwal() {
        String userId = auth.getCurrentUser().getUid();
        CollectionReference jadwalRef = db.collection("users").document(userId).collection("jadwal");

        jadwalRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                jadwalList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Jadwal j = doc.toObject(Jadwal.class);
                    j.setId(doc.getId()); // Menyimpan ID dokumen
                    jadwalList.add(j);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
