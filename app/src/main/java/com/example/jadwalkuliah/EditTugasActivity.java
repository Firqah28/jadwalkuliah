package com.example.jadwalkuliah;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditTugasActivity extends AppCompatActivity {

    EditText editJudul, editDeskripsi, editDeadline;
    Button btnSimpan;

    FirebaseFirestore db;
    FirebaseAuth auth;

    String judulLama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tugas);

        editJudul = findViewById(R.id.editJudul);
        editDeskripsi = findViewById(R.id.editDeskripsi);
        editDeadline = findViewById(R.id.editDeadline);
        btnSimpan = findViewById(R.id.btnSimpan);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Ambil data dari Intent
        judulLama = getIntent().getStringExtra("judul");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String deadline = getIntent().getStringExtra("deadline");

        // Tampilkan data ke EditText
        editJudul.setText(judulLama);
        editDeskripsi.setText(deskripsi);
        editDeadline.setText(deadline);

        btnSimpan.setOnClickListener(v -> simpanPerubahan());
    }

    private void simpanPerubahan() {
        String uid = auth.getCurrentUser().getUid();
        String judulBaru = editJudul.getText().toString();
        String deskripsiBaru = editDeskripsi.getText().toString();
        String deadlineBaru = editDeadline.getText().toString();

        // Cari dokumen dengan judul lama
        db.collection("users").document(uid).collection("tugas")
                .whereEqualTo("judul", judulLama)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Update hanya dokumen pertama yang ditemukan
                        queryDocumentSnapshots.getDocuments().get(0).getReference().update(new HashMap<String, Object>() {{
                            put("judul", judulBaru);
                            put("deskripsi", deskripsiBaru);
                            put("deadline", deadlineBaru);
                        }}).addOnSuccessListener(aVoid -> {
                            Toast.makeText(EditTugasActivity.this, "Tugas berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            finish(); // Kembali ke halaman sebelumnya
                        });
                    } else {
                        Toast.makeText(EditTugasActivity.this, "Tugas tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal memperbarui tugas", Toast.LENGTH_SHORT).show());
    }
}
