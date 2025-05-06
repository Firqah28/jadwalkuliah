package com.example.jadwalkuliah;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class JadwalRecyclerAdapter extends RecyclerView.Adapter<JadwalRecyclerAdapter.JadwalViewHolder> {

    private Context context;
    private ArrayList<Jadwal> jadwalList;

    public JadwalRecyclerAdapter(Context context, ArrayList<Jadwal> jadwalList) {
        this.context = context;
        this.jadwalList = jadwalList;
    }

    @Override
    public JadwalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_jadwal, parent, false);
        return new JadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JadwalViewHolder holder, int position) {
        Jadwal jadwal = jadwalList.get(position);
        holder.tvNamaMK.setText(jadwal.getNamaMK());
        holder.tvDeskripsi.setText(jadwal.getDosen() + " - " + jadwal.getJam() + " - " + jadwal.getRuangan());
        holder.tvHari.setText(jadwal.getHari());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditJadwalActivity.class);
            intent.putExtra("id", jadwal.getId());
            intent.putExtra("namaMK", jadwal.getNamaMK());
            intent.putExtra("dosen", jadwal.getDosen());
            intent.putExtra("hari", jadwal.getHari());
            intent.putExtra("jam", jadwal.getJam());
            intent.putExtra("ruangan", jadwal.getRuangan());
            context.startActivity(intent);
        });

        holder.btnHapus.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            db.collection("users")
                    .document(userId)
                    .collection("jadwal")
                    .document(jadwal.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        jadwalList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, jadwalList.size());
                        Toast.makeText(context, "Jadwal dihapus", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public static class JadwalViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMK, tvDeskripsi, tvHari;
        ImageButton btnEdit, btnHapus;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            tvNamaMK = itemView.findViewById(R.id.tvNamaMK);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvHari = itemView.findViewById(R.id.tvHari);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}
