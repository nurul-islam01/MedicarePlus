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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.SingleAppoinment.ApDoctorProfile;
import com.nurul.medicareplus.SingleAppoinment.ApMedicine;
import com.nurul.medicareplus.SingleAppoinment.AppointedDocument;
import com.nurul.medicareplus.SingleAppoinment.DoctorSuggestion;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;
import com.squareup.picasso.Picasso;

public class SingleAppoinment extends AppCompatActivity {

    private static final String TAG = SingleAppoinment.class.getSimpleName();

    private String rowId;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private AppoinmenttDoctor appoinmenttDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_appoinment);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.appoinmentVP);

        tabLayout.addTab(tabLayout.newTab().setText("Doctor"));
        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Medicine"));
        tabLayout.addTab(tabLayout.newTab().setText("Suggestion"));

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

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        Intent intent = getIntent();
        rowId = intent.getStringExtra("rowId");


    }

    public void setAppoinmenttDoctor(AppoinmenttDoctor appoinmenttDoctor){
        this.appoinmenttDoctor = appoinmenttDoctor;
        this.appoinmenttDoctor.appoinment(rowId);
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
                    return new ApDoctorProfile();
                case 1:
                    return new AppointedDocument();
                case 2:
                    return new ApMedicine();
                case 3:
                    return new DoctorSuggestion();

            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    public interface AppoinmenttDoctor {
        public void appoinment(String appoinmentId);
    }
}
