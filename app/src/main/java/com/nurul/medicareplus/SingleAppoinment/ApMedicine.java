package com.nurul.medicareplus.SingleAppoinment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.SingleAppoinment;
import com.nurul.medicareplus.adapter.MedicineAdapter;

import java.util.ArrayList;

/**
 * This is Created by Nurul Islam Tipu on 11/24/2018
 */
public class ApMedicine extends Fragment implements SingleAppoinment.AppoinmenttDoctor {

    private static final String TAG = ApMedicine.class.getSimpleName();
    private Context context;

    private RecyclerView allmedicineRV;
    private TextView nulltextView;

    private ArrayList<String> medicineRows;
    private RecyclerView.Adapter adapter;
    private String appointedId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
        ((SingleAppoinment) context).setAppoinmenttDoctor(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ap_medicine, container, false);

        allmedicineRV = view.findViewById(R.id.allmedicineRV);
        nulltextView = view.findViewById(R.id.nulltextView);

        MedicarePlus.medicineReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    medicineRows = new ArrayList<String>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String rowId = d.getKey();
                        medicineRows.add(rowId);
                    }
                    adapter = new MedicineAdapter(getActivity(), medicineRows);
                    allmedicineRV.setAdapter(adapter);
                    if (medicineRows.size() == 0){
                        nulltextView.setVisibility(View.VISIBLE);
                    }else {
                        nulltextView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void appoinment(String appoinmentId) {
        this.appointedId = appoinmentId;
    }
}
