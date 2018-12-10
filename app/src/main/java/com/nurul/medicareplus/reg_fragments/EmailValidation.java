package com.nurul.medicareplus.reg_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nurul.medicareplus.activity.MainActivity;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;

public class EmailValidation extends Fragment {

    private DoctorSignupProfile doctorSignupProfile;
    private PatientSignupProfile patientSignupProfile;

    private ProgressBar progressBar;
    private EditText validationTV;
    private Button refreshBT;

    private String email;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email_validation, container, false);

        progressBar = v.findViewById(R.id.progress_bar);
        validationTV = v.findViewById(R.id.validationTV);
        refreshBT = v.findViewById(R.id.refreshBT);


        refreshBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validationRefresh();

            }
        });

        validationTV.setText("A Verifcation send on | "+email);


        return v;
    }


    private void validationRefresh() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressBar.setVisibility(View.VISIBLE);
        if (user.isEmailVerified()){
            Toast.makeText(getActivity(), "Verified", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();

        }if (!user.isEmailVerified()){

            user.reload();
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Not Verfied", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Bundle bundle = getArguments();

        email = bundle.getString("email");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
