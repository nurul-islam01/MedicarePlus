package com.nurul.medicareplus.Patient_Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.adapter.DSuggestionAdapter;
import com.nurul.medicareplus.pojos.DoctorSuggestions;

import java.util.ArrayList;


public class PatientSuggestion extends Fragment {

    private static final String TAG = PatientSuggestion.class.getSimpleName();
    private Context context;

    private EditText suggestionET;
    private ImageButton saveIB,cancelIB;
    private RecyclerView suggestionRV;
    private CardView noSuggestionCV;
    private ArrayList<DoctorSuggestions> doctorSuggestions;
    private DoctorSuggestions dSuggestion;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_suggestion, container, false);

        suggestionET = view.findViewById(R.id.suggestionET);
        saveIB = view.findViewById(R.id.saveIB);
        cancelIB = view.findViewById(R.id.cancelIB);
        suggestionRV = view.findViewById(R.id.suggestionRV);
        noSuggestionCV = view.findViewById(R.id.noSuggestionCV);

        MedicarePlus.appoinmentedSuggestion(Patient_details.getAppoinmentId(), MedicarePlus.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    doctorSuggestions = new ArrayList<DoctorSuggestions>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        dSuggestion = child.getValue(DoctorSuggestions.class);
                        doctorSuggestions.add(dSuggestion);
                    }
                    adapter = new DSuggestionAdapter(context, doctorSuggestions);
                    suggestionRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (doctorSuggestions.size() > 0){
                        noSuggestionCV.setVisibility(View.GONE);
                    }else {
                        noSuggestionCV.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        suggestionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    cancelIB.setVisibility(View.VISIBLE);
                    saveIB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }else {
                    cancelIB.setVisibility(View.GONE);
                    saveIB.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelIB.getVisibility() == View.VISIBLE){
                    cancelIB.setVisibility(View.GONE);
                    saveIB.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    suggestionET.setText("");
                }
            }
        });

        saveIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelIB.getVisibility() == View.VISIBLE){

                    try {
                        String s = suggestionET.getText().toString().trim();
                        if (TextUtils.isEmpty(s)){
                            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
                            return;
                        }else {

                            String pushId = MedicarePlus.appoinmentedSuggestion(Patient_details.getAppoinmentId(), MedicarePlus.getUserId()).push().getKey();
                            DoctorSuggestions suggestions = new DoctorSuggestions();
                            suggestions.setRowId(pushId);
                            suggestions.setSuggetion(s);
                            MedicarePlus.appoinmentedSuggestion(Patient_details.getAppoinmentId(), MedicarePlus.getUserId()).child(pushId).setValue(suggestions)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                                            cancelIB.setVisibility(View.GONE);
                                            saveIB.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                            suggestionET.setText("");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                    cancelIB.setVisibility(View.GONE);
                                    saveIB.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                    suggestionET.setText("");
                                }
                            });
                        }
                    }catch (Exception e){
                        Log.d(TAG, "onClick: "+e.getMessage());
                        Toast.makeText(context, "Failed Saved", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }
}
