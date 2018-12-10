package com.nurul.medicareplus.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.Patient_Fragments.PProfile;
import com.nurul.medicareplus.Patient_Fragments.PatientSuggestion;
import com.nurul.medicareplus.Patient_Fragments.Patient_Documents;
import com.nurul.medicareplus.Patient_Fragments.Patient_Medicine;
import com.nurul.medicareplus.Patient_Fragments.Patient_Medicine_Timer;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.interfaces.Patient_interface;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;


public class Patient_details extends AppCompatActivity {

    private static final String TAG = Patient_details.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter;
    private static String appoinmentId;

    private Patient_interface patientInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Intent intent = getIntent();
        try{
            appoinmentId = intent.getStringExtra("patientId");
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Medicine"));
        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Timer"));
        tabLayout.addTab(tabLayout.newTab().setText("Suggestion"));



        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOffscreenPageLimit(3);
    }

    public static String getAppoinmentId(){
        return appoinmentId;
    }

    public void aptientListener(final Patient_interface patientInterface){

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        int tabCount;
        public ViewPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int i) {

            switch (i){
                case 0:
                    return new PProfile();
                case 1:
                    return new Patient_Medicine();
                case 2:
                    return new Patient_Documents();
                case 3:
                    return new Patient_Medicine_Timer();
                case 4:
                    return new PatientSuggestion();

            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
