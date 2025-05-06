package com.example.jadwalkuliah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TambahTugasActivity extends AppCompatActivity {

    private EditText etJudulTugas, etDeskripsi, etDeadline;
    private Button btnSimpanTugas, btnBatalTugas;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tugas);

        etJudulTugas = findViewById(R.id.etNamaTugas);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etDeadline = findViewById(R.id.etDeadline);
        btnSimpanTugas = findViewById(R.id.btnSimpanTugas);
        btnBatalTugas = findViewById(R.id.btnBatalTugas);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSimpanTugas.setOnClickListener(v -> simpanTugas());

        btnBatalTugas.setOnClickListener(v -> {
            Toast.makeText(this, "Batal menambahkan tugas", Toast.LENGTH_SHORT).show();
            finish(); // kembali ke TugasActivity tanpa menyimpan
        });
    }

    private void simpanTugas() {
        String judul = etJudulTugas.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();

        if (judul.isEmpty() || deskripsi.isEmpty() || deadline.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> tugas = new HashMap<>();
        tugas.put("judul", judul);
        tugas.put("deskripsi", deskripsi);
        tugas.put("deadline", deadline);

        db.collection("users")
                .document(userId)
                .collection("tugas")
                .add(tugas)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Tugas berhasil disimpan", Toast.LENGTH_SHORT).show();

                    // Set result agar TugasActivity tahu bahwa ada tugas baru
                    setResult(RESULT_OK);
                    finish(); // Ini akan kembali ke TugasActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menyimpan tugas", Toast.LENGTH_SHORT).show();
                });
    }
}
