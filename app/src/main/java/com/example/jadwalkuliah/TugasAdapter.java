package com.example.jadwalkuliah;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class TugasAdapter extends BaseAdapter {
    private Context context;
    private List<Tugas> tugasList;
    private LayoutInflater inflater;

    public TugasAdapter(Context context, List<Tugas> tugasList) {
        this.context = context;
        this.tugasList = tugasList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tugasList.size();
    }

    @Override
    public Object getItem(int position) {
        return tugasList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_tugas, parent, false);
        }

        Tugas tugas = tugasList.get(position);

        TextView txtJudul = convertView.findViewById(R.id.txtJudul);
        TextView txtDeskripsi = convertView.findViewById(R.id.txtDeskripsi);
        TextView txtDeadline = convertView.findViewById(R.id.txtDeadline);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnHapus = convertView.findViewById(R.id.btnHapus);

        txtJudul.setText(tugas.getJudul());
        txtDeskripsi.setText(tugas.getDeskripsi());
        txtDeadline.setText(tugas.getDeadline());

        // Hapus tugas
        btnHapus.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();

            db.collection("users").document(uid).collection("tugas")
                    .whereEqualTo("judul", tugas.getJudul()) // Bisa tambahkan kondisi lain juga
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            doc.getReference().delete();
                        }
                        tugasList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Tugas dihapus", Toast.LENGTH_SHORT).show();
                    });
        });

        // Edit tugas
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTugasActivity.class);
            intent.putExtra("judul", tugas.getJudul());
            intent.putExtra("deskripsi", tugas.getDeskripsi());
            intent.putExtra("deadline", tugas.getDeadline());
            context.startActivity(intent);
        });

        return convertView;
    }


}
