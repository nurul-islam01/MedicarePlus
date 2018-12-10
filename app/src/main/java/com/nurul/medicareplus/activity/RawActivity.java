package com.nurul.medicareplus.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.raw_activity_fragments.Doctor_profile_for_appoinment;

public class RawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw);

        Intent intent = getIntent();
        String getFragment = intent.getStringExtra("fragment");

        if (getFragment.equals("doctorProfile")){
            String doctorId = intent.getStringExtra("doctorId");
            Bundle bundle = new Bundle();
            bundle.putString("doctorId", doctorId);

            Doctor_profile_for_appoinment appoinment = new Doctor_profile_for_appoinment();

            appoinment.setArguments(bundle);

            FragmentManager fragmentManager = RawActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.raw_frament_container, appoinment);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();
        }


    }
}
