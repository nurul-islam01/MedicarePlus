package com.nurul.medicareplus.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.StoreUser;
import com.nurul.medicareplus.drower_fragments.AllDoctors;
import com.nurul.medicareplus.drower_fragments.AllMedicine;
import com.nurul.medicareplus.drower_fragments.Appoinmented;
import com.nurul.medicareplus.drower_fragments.Appoinments;
import com.nurul.medicareplus.drower_fragments.AppoinmentsRequest;
import com.nurul.medicareplus.drower_fragments.DoctorProfile;
import com.nurul.medicareplus.drower_fragments.MedicineTimer;
import com.nurul.medicareplus.drower_fragments.PatientProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();


    private ImageView userphoto;
    private TextView userName, userEmail;
    private FrameLayout drawerLayout;
    private String userId;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };



    @Override
    protected void onRestart() {
        super.onRestart();

        if (MedicarePlus.getUser() == null){
            startActivity(new Intent(this, Regestration.class));
            finish();
        }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();

        if (MedicarePlus.getUser() == null){
            startActivity(new Intent(this, Regestration.class));
            finish();
        }

        try {

            Intent intent = getIntent();
            userId = intent.getStringExtra("userId");
            if (userId == null){
                userId = MedicarePlus.getUserId();
            }
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawerFragment);

        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userEmail = v.findViewById(R.id.userEmail);

        try {
            MedicarePlus.getUserTypeDatabaseReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        String type = dataSnapshot.getValue(String.class);
                        userDetails(type);
                        try {
                            if (type.equals("Doctor")){

                                navigationView.inflateMenu(R.menu.activity_doctor_drawer);

                                getSupportActionBar().setTitle("Request");
                                getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AppoinmentsRequest()).commit();
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }else if (type.equals("Patient")){

                                navigationView.inflateMenu(R.menu.activity_patient_drawer);

                                getSupportActionBar().setTitle("All Doctors");
                                getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllDoctors()).commit();
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            }
                        }catch (Exception e){
                            Log.i(TAG, "onDataChange: "+e.getMessage());
//                            startActivity(new Intent(MainActivity.this, Regestration.class));
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.i(TAG, "onCreate: "+e.getMessage());
        }

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);



    }

    private void userDetails(final String userType){
        MedicarePlus.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    if (userType.equals("Doctor")){
                        DoctorSignupProfile profile = dataSnapshot.getValue(DoctorSignupProfile.class);
                        Picasso.get().load(profile.getPic_uri()).error(R.drawable.doctors_icon).placeholder(R.drawable.doctor_image_icon)
                                .into(userphoto);
                        userName.setText(profile.getName());
                        userEmail.setText(profile.getPhone());
                    }else if (userType.equals("Patient")){
                        PatientSignupProfile profile = dataSnapshot.getValue(PatientSignupProfile.class);
                        userName.setText(profile.getName());
                        userEmail.setText(profile.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                MedicarePlus.getFirebaseAuth().signOut();
                startActivity(new Intent(this, Regestration.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
           switch (item.getItemId()){
               case R.id.appoinmentRequest:
                   getSupportActionBar().setTitle("Appoinment Request");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AppoinmentsRequest()).commit();
                   break;
               case R.id.appoinmented:
                   getSupportActionBar().setTitle("Appoinmented");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new Appoinmented()).commit();
                   break;
               case R.id.doctorProfile:
                   getSupportActionBar().setTitle("Profile");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new DoctorProfile()).commit();
                   break;

               case R.id.alldoctor:
                   getSupportActionBar().setTitle("All Doctors");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllDoctors()).commit();
                   break;
               case R.id.appoinments:
                   getSupportActionBar().setTitle("Appoinments");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new Appoinments()).commit();
                   break;
               case R.id.patientProfile:
                   getSupportActionBar().setTitle("Profile");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new PatientProfile()).commit();
                   break;
               case R.id.medictinetime:
                   getSupportActionBar().setTitle("Medicine Times");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new MedicineTimer()).commit();
                   break;
               case R.id.medicine:
                   getSupportActionBar().setTitle("Medicins");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllMedicine()).commit();
                   break;


           }
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


}
