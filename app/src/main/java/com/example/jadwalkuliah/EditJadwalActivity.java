package com.example.jadwalkuliah;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class EditJadwalActivity extends AppCompatActivity {

    private EditText etNamaMK, etDosen, etHari, etJam, etRuangan;
    private Button btnSimpan;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String jadwalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jadwal);

        // Inisialisasi
        etNamaMK = findViewById(R.id.etNamaMK);
        etDosen = findViewById(R.id.etDosen);
        etHari = findViewById(R.id.etHari);
        etJam = findViewById(R.id.etJam);
        etRuangan = findViewById(R.id.etRuangan);
        btnSimpan = findViewById(R.id.btnSimpan);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Ambil data dari intent
        jadwalId = getIntent().getStringExtra("id");
        String namaMK = getIntent().getStringExtra("namaMK");
        String dosen = getIntent().getStringExtra("dosen");
        String hari = getIntent().getStringExtra("hari");
        String jam = getIntent().getStringExtra("jam");
        String ruangan = getIntent().getStringExtra("ruangan");

        // Tampilkan ke EditText
        etNamaMK.setText(namaMK);
        etDosen.setText(dosen);
        etHari.setText(hari);
        etJam.setText(jam);
        etRuangan.setText(ruangan);

        // Simpan perubahan
        btnSimpan.setOnClickListener(v -> {
            String namaMKBaru = etNamaMK.getText().toString();
            String dosenBaru = etDosen.getText().toString();
            String hariBaru = etHari.getText().toString();
            String jamBaru = etJam.getText().toString();
            String ruanganBaru = etRuangan.getText().toString();

            if (jadwalId != null && !jadwalId.isEmpty()) {
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .collection("jadwal")
                        .document(jadwalId)
                        .update(
                                "namaMK", namaMKBaru,
                                "dosen", dosenBaru,
                                "hari", hariBaru,
                                "jam", jamBaru,
                                "ruangan", ruanganBaru
                        )
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Jadwal berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Mengirim hasil kembali ke MainActivity
                            finish(); // Kembali ke MainActivity
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
