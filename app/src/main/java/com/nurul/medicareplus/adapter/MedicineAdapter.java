package com.nurul.medicareplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.SingleMedicine;
import com.nurul.medicareplus.pojos.Medicine;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> rows = new ArrayList<String>();

    public MedicineAdapter(Context context, ArrayList<String> rows) {
        this.context = context;
        this.rows = rows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.medicine_raw, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final String rowId = rows.get(i);

        MedicarePlus.getmedicineDesc(rowId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);

                    try {
                        viewHolder.medicineNameTV.setText(medicine.getName());
                        viewHolder.timeDosTV.setText(medicine.getPerDos()+" pill "+ medicine.getDailyDos() + " times daily");
                        viewHolder.medicineStartTV.setText(medicine.getBeforeORafter());
                        if (medicine.getmImageUri() == ""){
                            viewHolder.medicineIV.setImageResource(R.drawable.medicin_icon);
                        }else {
                            Picasso.get()
                                    .load(medicine.getmImageUri())
                                    .placeholder(R.drawable.medicin_icon)
                                    .error(R.drawable.medicin_icon)
                                    .into(viewHolder.medicineIV);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MedicarePlus.getmedicineDesc(rowId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){

                            Medicine medicine = dataSnapshot.getValue(Medicine.class);

                            Intent intent = new Intent(context, SingleMedicine.class);
                            intent.putExtra("medicine", medicine);
                            context.startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return rows.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView medicineIV;
        private TextView medicineNameTV, timeDosTV, medicineStartTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            medicineIV = itemView.findViewById(R.id.medicineIV);
            medicineNameTV = itemView.findViewById(R.id.medicineNameTV);
            timeDosTV = itemView.findViewById(R.id.timeDosTV);
            medicineStartTV = itemView.findViewById(R.id.medicineStartTV);

        }
    }
}
