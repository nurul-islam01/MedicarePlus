package com.nurul.medicareplus.Patient_Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.activity.OcrCameraPreview;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Patient_Documents extends Fragment implements View.OnClickListener {

    private static final String TAG = Patient_details.class.getSimpleName();
    private Context context;

    private ImageButton imageBT, cameraBT;
    private TextView sussesText;

    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;
    private static final int RC_OCR_CAPTURE = 9003;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient__documents, container, false);

        imageBT = view.findViewById(R.id.imageBT);
        cameraBT = view.findViewById(R.id.cameraBT);
        sussesText = view.findViewById(R.id.sussesText);

        imageBT.setOnClickListener(this);
        cameraBT.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraBT :

                cameraActivity();

                break;
            case R.id.imageBT:

                break;
        }
    }


    public void cameraActivity(){
        Intent intent = new Intent(context, OcrCameraPreview.class);
        startActivityForResult(intent, RC_OCR_CAPTURE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_OCR_CAPTURE){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data != null){
                    String text = data.getStringExtra(OcrCameraPreview.TextBlockObject);
                    sussesText.setText(text);
                    Toast.makeText(context, ""+text.toString(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Not get value", Toast.LENGTH_SHORT).show();
                    sussesText.setText("No Text Capctured");
                }
            }else {
                Toast.makeText(context, "Not Ok", Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
