package com.example.jadwalkuliah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TambahJadwalActivity extends AppCompatActivity {
    EditText etNamaMK, etDosen, etHari, etJam, etRuangan;
    Button btnBatal, btnSimpan;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etNamaMK = findViewById(R.id.etNamaMK);
        etDosen = findViewById(R.id.etDosen);
        etHari = findViewById(R.id.etHari);
        etJam = findViewById(R.id.etJam);
        etRuangan = findViewById(R.id.etRuangan);
        btnBatal = findViewById(R.id.btnBatal);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnBatal.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String namaMK = etNamaMK.getText().toString().trim();
            String dosen = etDosen.getText().toString().trim();
            String hari = etHari.getText().toString().trim();
            String jam = etJam.getText().toString().trim();
            String ruangan = etRuangan.getText().toString().trim();

            if (namaMK.isEmpty() || dosen.isEmpty() || hari.isEmpty() || jam.isEmpty() || ruangan.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = auth.getCurrentUser().getUid();
            Jadwal jadwal = new Jadwal(namaMK, dosen, hari, jam, ruangan);

            db.collection("users")
                    .document(userId)
                    .collection("jadwal")
                    .add(jadwal)
                    .addOnSuccessListener(docRef -> {
                        // Beri tahu bahwa data berhasil disimpan
                        Toast.makeText(TambahJadwalActivity.this, "Jadwal berhasil ditambah", Toast.LENGTH_SHORT).show();

                        // Tutup TambahJadwalActivity dan kembali ke MainActivity
                        finish();  // Hanya menggunakan finish untuk kembali ke MainActivity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
