package com.nurul.medicareplus.reg_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.drower_fragments.AppoinmentsRequest;

public class Identifier extends Fragment implements View.OnClickListener {

    private Button patientBT, doctorBT;
    private TextView signInTV;

    private Context context;
    public Identifier() {
        // Required empty public constructor
    }


    public static Identifier newInstance(String param1, String param2) {
        Identifier fragment = new Identifier();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_identifier, container, false);

        patientBT = v.findViewById(R.id.patientBT);
        doctorBT = v.findViewById(R.id.doctorBT);
        signInTV = v.findViewById(R.id.signInTV);

        signInTV.setOnClickListener(this);
        patientBT.setOnClickListener(this);
        doctorBT.setOnClickListener(this);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new Fragment();
        Bundle arguments = new Bundle();
        switch (v.getId()){
            case R.id.patientBT:
                arguments.putString("patient", "Patient");
                fragment = new PatientSignup();
                fragment.setArguments(arguments);
                break;

            case R.id.doctorBT:
                arguments.putString("doctor", "Doctor");
                fragment = new DoctorSignup();
                fragment.setArguments(arguments);
                break;
            case R.id.signInTV:
                fragment = new SignIn();
                break;


        }
        transaction.addToBackStack(null).replace(R.id.regFragmentContainer, fragment);
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();
    }
}
