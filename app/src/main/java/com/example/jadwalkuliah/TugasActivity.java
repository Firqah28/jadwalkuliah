package com.example.jadwalkuliah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TugasActivity extends AppCompatActivity {

    private ListView listTugas;
    private FloatingActionButton btnTambahTugas;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<Tugas> tugasList = new ArrayList<>();
    private TugasAdapter adapter;

    // Modern ActivityResultLauncher
    private final ActivityResultLauncher<Intent> tambahTugasLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadTugasDariFirestore();
                    Toast.makeText(this, "Tugas baru ditambahkan", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);

        listTugas = findViewById(R.id.listTugas);
        btnTambahTugas = findViewById(R.id.btnTambahTugas);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        adapter = new TugasAdapter(this, tugasList);
        listTugas.setAdapter(adapter);

        btnTambahTugas.setOnClickListener(v -> {
            Intent intent = new Intent(this, TambahTugasActivity.class);
            tambahTugasLauncher.launch(intent); // gunakan launcher modern
        });

        loadTugasDariFirestore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTugasDariFirestore(); // Ini akan jalan setiap kali activity ini muncul lagi
    }

    private void loadTugasDariFirestore() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User tidak terautentikasi", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).collection("tugas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    tugasList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Tugas tugas = doc.toObject(Tugas.class);
                        tugasList.add(tugas);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengambil data tugas", Toast.LENGTH_SHORT).show();
                });
    }
}
