package com.nurul.medicareplus.Patient_Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.adapter.PatientMedicineAdapter;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;
import com.nurul.medicareplus.pojos.Medicine;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Patient_Medicine extends Fragment {

    private static final String TAG = Patient_Medicine.class.getSimpleName();
    private Context context;

    private AppoinmentedProfile appoinmentedProfile;
    private PatientSignupProfile patientSignupProfile;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private TextView nulltextView;
    private ImageView medicineIV;
    private EditText medicineNameET, totalPicsMedicineET;
    private RecyclerView allmedicineRV;
    private FloatingActionButton mFab;
    private ScrollView medicine_add;
    private RadioGroup mBeforAfterRG;
    private DatePicker medicineStartDateDP;
    private Button addBT;
    private Spinner perDosSP, perDaySp;

    private Uri pic_uri;
    private final int SELECT_IMAGE = 121;
    private String beforAfter, picReference;
    private ArrayList<String> medicineRows;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_patient__medicine, container, false);

        mFab = v.findViewById(R.id.fab);
        allmedicineRV = v.findViewById(R.id.allmedicineRV);
        medicine_add = v.findViewById(R.id.medicine_add);
        nulltextView = v.findViewById(R.id.nulltextView);

        //add medicine layout properties id findding

        medicineIV = v.findViewById(R.id.medicineIV);
        medicineNameET = v.findViewById(R.id.medicineNameET);
        totalPicsMedicineET = v.findViewById(R.id.totalPicsMedicineET);
        mBeforAfterRG = v.findViewById(R.id.mBeforAfterRG);
        medicineStartDateDP = v.findViewById(R.id.medicineStartDateDP);
        addBT = v.findViewById(R.id.addBT);
        perDosSP = v.findViewById(R.id.perDosSP);
        perDaySp = v.findViewById(R.id.perDaySp);






        MedicarePlus.patientMedicineReference(Patient_details.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    medicineRows = new ArrayList<String>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String rowId = d.getKey();
                        medicineRows.add(rowId);
                    }
                    adapter = new PatientMedicineAdapter(getActivity(), medicineRows);
                    allmedicineRV.setAdapter(adapter);
                    if (medicineRows.size() == 0){
                        nulltextView.setVisibility(View.VISIBLE);
                    }else {
                        nulltextView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mBeforAfterRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                beforAfter = ((RadioButton)v.findViewById(checkedId)).getText().toString();
            }
        });

        medicineIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()){
                    Intent pic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pic.setType("image/*");
                    startActivityForResult(pic.createChooser(pic, "Select Image"), SELECT_IMAGE);
                }
            }
        });

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = medicineNameET.getText().toString();
                int perdos = perDosSP.getSelectedItemPosition()+1;
                int dailydos = perDaySp.getSelectedItemPosition()+1;
                int day = medicineStartDateDP.getDayOfMonth();
                int month = medicineStartDateDP.getMonth();
                int year = medicineStartDateDP.getYear();
                int totalMeidicine = Integer.parseInt(totalPicsMedicineET.getText().toString());

                String error = "Field incomplete";

                if (TextUtils.isEmpty(name)){
                    medicineNameET.setError(error);
                    return;
                }else if (perdos < 1){
                    Toast.makeText(getActivity(), "Per Dos Not Selected", Toast.LENGTH_SHORT).show();
                    return;
                }else if (totalMeidicine < 1){
                    totalPicsMedicineET.setError("How Many Pics Medicine need");
                    return;
                }else if (dailydos < 1){
                    Toast.makeText(getActivity(), "Per Day Dos Not Selected", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(beforAfter)){
                    Toast.makeText(getActivity(), "Select Befor Or After", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(beforAfter) && totalMeidicine > 0 && perdos > 0 && perdos > 0){

                    Medicine medicine = new Medicine();
                    medicine.setName(name);
                    medicine.setPerDos(perdos);
                    medicine.setDailyDos(dailydos);
                    medicine.setDay(day);
                    medicine.setMonth(month);
                    medicine.setYear(year);
                    medicine.setBeforeORafter(beforAfter);
                    medicine.setTotalPicsMedicine(totalMeidicine);

                    try {

                        if (pic_uri != null){
                            medicine.setmImageUri(picReference);
                        }else {
                            medicine.setmImageUri("");
                        }

                        String pushId = MedicarePlus.patientMedicineReference(Patient_details.getAppoinmentId()).push().getKey();

                        medicine.setRowId(pushId);

                        MedicarePlus.patientMedicineReference(Patient_details.getAppoinmentId()).child(pushId).child("MedicineDes").setValue(medicine);

                        medicine_add.setVisibility(View.GONE);
                        mFab.setImageResource(android.R.drawable.ic_input_add);
                        allmedicineRV.setVisibility(View.VISIBLE);

                        medicineNameET.setText(null);
                        medicineIV.setImageResource(R.drawable.profile_pic_upload_icon);
                        mBeforAfterRG.clearCheck();
                        adapter.notifyDataSetChanged();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here

                String rw = medicineRows.get(viewHolder.getAdapterPosition());

                if (swipeDir == ItemTouchHelper.LEFT){

                    MedicarePlus.patientMedicineReference(Patient_details.getAppoinmentId()).child(rw).removeValue();

                    viewHolder.getAdapterPosition();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Delated", Toast.LENGTH_SHORT).show();
                }

                if (swipeDir == ItemTouchHelper.RIGHT){
                    adapter.notifyDataSetChanged();
                }

            }
        });
        itemTouchHelper.attachToRecyclerView(allmedicineRV);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicine_add.getVisibility() == View.GONE){
                    medicine_add.setVisibility(View.VISIBLE);
                    allmedicineRV.setVisibility(View.GONE);
                    mFab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

                    if (nulltextView.getVisibility() == View.VISIBLE){
                        nulltextView.setVisibility(View.GONE);
                    }

                }else {
                    medicine_add.setVisibility(View.GONE);
                    allmedicineRV.setVisibility(View.VISIBLE);
                    mFab.setImageResource(android.R.drawable.ic_input_add);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE){
            try {
                pic_uri = data.getData();
                medicineIV.setImageURI(data.getData());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pic_uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] dat = baos.toByteArray();


                CharSequence s  = DateFormat.format("MM-dd-yy-hh-mm", new Date().getTime());
                final StorageReference reference = MedicarePlus.patientMedicinPicStore(Patient_details.getAppoinmentId()).child(s.toString()+"."+getFileExtension(pic_uri));

                UploadTask uploadTask = reference.putBytes(dat);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        picReference = task.getResult().toString();

                        Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                    }
                });

            }catch (Exception e){
                Toast.makeText(getActivity(), "Image Not set", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
}
