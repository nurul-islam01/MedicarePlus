package com.nurul.medicareplus.reg_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;

import java.io.Serializable;

public class PatientSignup extends Fragment {

    private Context context;

    private EditText pNameET, pAgeET, pWeghitET, pAddress;
    private RadioGroup pGenderRG;
    private Button nextBT;

    private String type;
    private String gender;

    public PatientSignup() {
        // Required empty public constructor
    }


    public static PatientSignup newInstance(String param1, String param2) {
        PatientSignup fragment = new PatientSignup();
        Bundle args = new Bundle();

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
        final View v = inflater.inflate(R.layout.fragment_patient_signup, container, false);

        pNameET = v.findViewById(R.id.pNameET);
        pAgeET = v.findViewById(R.id.pAgeET);
        pWeghitET = v.findViewById(R.id.pWeghitET);
        pAddress = v.findViewById(R.id.pAddress);
        pGenderRG = v.findViewById(R.id.pGenderRG);
        nextBT = v.findViewById(R.id.nextBT);

        pGenderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton)v.findViewById(checkedId)).getText().toString();
            }
        });

        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientSignup();
            }
        });



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context = context_;
        Bundle patient =getArguments();
        type = patient.getString("patient");
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void patientSignup(){
        String name = pNameET.getText().toString();
        String age = pAgeET.getText().toString();
        String weight = pWeghitET.getText().toString();
        String address = pAddress.getText().toString();

        String error = "Complete This Field";

        if (TextUtils.isEmpty(name)){
            pNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(age)){
            pAgeET.setError(error);
            return;
        }else if (age.length() > 3){
            pAgeET.setError("Not Perfect");
            return;
        } else if (TextUtils.isEmpty(weight)){
            pWeghitET.setError(error);
        }else if (weight.length() > 3){
            pWeghitET.setError("Not Perfect");
            return;
        }else if (TextUtils.isEmpty(address)){
            pAddress.setError(error);
            return;
        }else if (TextUtils.isEmpty(gender)){
            Toast.makeText(context, "Select Gender", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(type)){
            Toast.makeText(context, "Type Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && age.length() <= 3 &&
                !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(gender)){

            PatientSignupProfile patient = new PatientSignupProfile();

            patient.setName(name);
            patient.setAge(age);
            patient.setAddress(address);
            patient.setType(type);
            patient.setWeight(weight);
            patient.setVerfied(false);
            patient.setGender(gender);

            Bundle bundle = new Bundle();

            bundle.putString("type", "Patient");
            bundle.putSerializable("Patient", (Serializable) patient);

            Signup signup = new Signup();

            signup.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.regFragmentContainer, signup);
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();

        }
    }

}
