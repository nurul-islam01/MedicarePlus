package com.nurul.medicareplus.drower_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.AppoinmentAdapter;

import java.util.ArrayList;

public class Appoinmented extends Fragment {


    private TextView noAppoinmentTV;
    private RecyclerView allAppoinmentedRV;

    private RecyclerView.Adapter adapter;
    private ArrayList<String> appoinmentListRow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appoinmented, container, false);

        allAppoinmentedRV = v.findViewById(R.id.allAppoinmentedRV);
        noAppoinmentTV = v.findViewById(R.id.noAppoinmentTV);

        MedicarePlus.appoinmentedStore().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    appoinmentListRow = new ArrayList<>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String s = d.getKey();
                        appoinmentListRow.add(s);
                    }
                    adapter = new AppoinmentAdapter(getActivity(), appoinmentListRow);
                    allAppoinmentedRV.setAdapter(adapter);
                    if (appoinmentListRow.size() == 0){
                        noAppoinmentTV.setVisibility(View.VISIBLE);
                    }else {
                        noAppoinmentTV.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here

                String rw = appoinmentListRow.get(viewHolder.getAdapterPosition());

                if (swipeDir == ItemTouchHelper.LEFT){

                    MedicarePlus.appoinmentedStore().child(rw).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Not Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Delated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(allAppoinmentedRV);


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
