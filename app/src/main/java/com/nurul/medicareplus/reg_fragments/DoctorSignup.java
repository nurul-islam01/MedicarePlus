package com.nurul.medicareplus.reg_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;

import java.util.ArrayList;
import java.util.List;

    public class DoctorSignup extends Fragment implements View.OnClickListener {

        private Button nextBT;
        private EditText doctorName, doctorDeg, specility, consulttanttype, chember, doctorDesc;
        private ImageView doctorImg;
        private TextView errorTV;
        private RadioGroup genderRG;

        private String doctorgender;

        private final int SELECT_IMAGE = 11;
    private Uri pic_uri;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public DoctorSignup() {
        // Required empty public constructor
    }


    public static DoctorSignup newInstance(String param1, String param2) {
        DoctorSignup fragment = new DoctorSignup();
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
        final View v = inflater.inflate(R.layout.fragment_doctor_signup, container, false);

        nextBT = v.findViewById(R.id.nextBT);
        doctorName = v.findViewById(R.id.doctorName);
        doctorDeg = v.findViewById(R.id.doctorDeg);
        specility = v.findViewById(R.id.specility);
        consulttanttype = v.findViewById(R.id.consulttanttype);
        chember = v.findViewById(R.id.chember);
        doctorDesc = v.findViewById(R.id.doctorDesc);
        doctorImg = v.findViewById(R.id.doctorImg);
        errorTV = v.findViewById(R.id.errorTV);
        genderRG = v.findViewById(R.id.genderRG);

        doctorImg.setDrawingCacheEnabled(true);
        doctorImg.buildDrawingCache();
        doctorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()){
                    Intent pic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pic.setType("image/*");
                    startActivityForResult(pic.createChooser(pic, "Select Image"), SELECT_IMAGE);
                }

            }
        });



        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doctorgender = ((RadioButton) v.findViewById(checkedId)).getText().toString();
            }
        });

        nextBT.setOnClickListener(this);


        return v;
    }


    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        Bundle arguments = getArguments();
        context_ = getActivity();
        String s = arguments.getString("doctor");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextBT){
            doctor();
        }
    }

    private void doctor() {
        String name = doctorName.getText().toString().trim();
        String degination = doctorDeg.getText().toString();
        String spe = specility.getText().toString();
        String consType = consulttanttype.getText().toString();
        String chem = chember.getText().toString();
        String des = doctorDesc.getText().toString();


        String error = "Please complete this field";
        if (TextUtils.isEmpty(name)){
            doctorName.setError(error);
            return;
        }else if (TextUtils.isEmpty(degination)){
            doctorDeg.setError(error);
            return;
        }else if (TextUtils.isEmpty(spe)){
            specility.setError(error);
            return;
        }else if (TextUtils.isEmpty(consType)){
            consulttanttype.setError(error);
            return;
        }else if (TextUtils.isEmpty(chem)){
            chember.setError(error);
            return;
        }else if (TextUtils.isEmpty(des)){
            doctorDesc.setError(error);
            return;
        }else if (TextUtils.isEmpty(doctorgender)){
            Toast.makeText(getActivity(), "Gender Not Selected", Toast.LENGTH_SHORT).show();
            errorTV.setError("Please Select Gender");
            return;
        }else if (pic_uri == null){
            Toast.makeText(getActivity(), "Profile Image not selected", Toast.LENGTH_SHORT).show();
            errorTV.setError("Please Select Your Image");
            return;
        }

        else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(degination) && !TextUtils.isEmpty(spe)
                && !TextUtils.isEmpty(consType) && !TextUtils.isEmpty(chem)
                && !TextUtils.isEmpty(des) && !TextUtils.isEmpty(doctorgender) && pic_uri != null){
            DoctorSignupProfile doctorProfile = new DoctorSignupProfile();

            doctorProfile.setName(name);
            doctorProfile.setEmailVerification(false);
            doctorProfile.setChember(chem);
            doctorProfile.setConsultantType(consType);
            doctorProfile.setDegination(degination);
            doctorProfile.setGender(doctorgender);
            doctorProfile.setPic_uri(pic_uri.getPath().toString());
            doctorProfile.setSepecility(spe);

            Signup signupfragment = new Signup();
            Bundle bundle = new Bundle();
            Signup signup = new Signup();
            bundle.putString("type", "Doctor");
            bundle.putParcelable("uri", pic_uri);
            bundle.putSerializable("doctorSignup", doctorProfile);
            signup.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.regFragmentContainer, signup);
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();

        }


    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE){
            try {
                pic_uri = data.getData();
                doctorImg.setImageURI(data.getData());
            }catch (Exception e){
                Toast.makeText(getActivity(), "Image Not set", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
}
