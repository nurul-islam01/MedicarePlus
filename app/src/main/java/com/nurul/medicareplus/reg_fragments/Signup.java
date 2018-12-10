package com.nurul.medicareplus.reg_fragments;

import android.arch.core.executor.TaskExecutor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.activity.MainActivity;
import com.nurul.medicareplus.nessary_classes.PathUtil;


import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Signup extends Fragment implements View.OnClickListener {

    private static final String TAG = Signup.class.getSimpleName();

    private static Context context;
    private TextView errorshowTV;
    private EditText  phoneET,codeET ;
    private ImageView pictureDoctor;
    private Button signupBT;
    private ProgressBar progressBar;
    private LinearLayout verifyBTS;
    private Button verifyBT, resendCodeBT;
    private TextInputLayout phoneTIL, codeTIL;

    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    private DoctorSignupProfile doctorSignupProfile;
    private PatientSignupProfile patientSignupProfile;
    private String usertype;
    private Uri filePic;

    private String verificationId;
    private String phone;

    public Signup() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    public static Signup newInstance(Context context_) {
        Signup fragment = new Signup();
        context = context_;
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
        final View v =inflater.inflate(R.layout.fragment_signup, container, false);
        auth = FirebaseAuth.getInstance();

        phoneET = v.findViewById(R.id.phoneET);
        codeET = v.findViewById(R.id.codeET);
        signupBT = v.findViewById(R.id.signupBT);
        errorshowTV = v.findViewById(R.id.errorshowTV);
        progressBar = v.findViewById(R.id.progress_bar);
        pictureDoctor = v.findViewById(R.id.pictureDoctor);

        phoneTIL = v.findViewById(R.id.phoneTIL);
        codeTIL = v.findViewById(R.id.codeTIL);
        verifyBT = v.findViewById(R.id.verifyBT);
        resendCodeBT = v.findViewById(R.id.resendCodeBT);
        verifyBTS = v.findViewById(R.id.verifyBTS);

        codeTIL.setVisibility(View.GONE);
        verifyBTS.setVisibility(View.GONE);

        signupBT.setOnClickListener(this);
        resendCodeBT.setOnClickListener(this);
        verifyBT.setOnClickListener(this);

        if (filePic != null){
            pictureDoctor.setVisibility(View.VISIBLE);
            pictureDoctor.setImageURI(filePic);

        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.verifyBT:
                final String code = codeET.getText().toString().trim();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(context, "Code Is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(code);
                }
                break;
            case R.id.signupBT:
                progressBar.setVisibility(View.VISIBLE);
                sendVerificationCode();
                break;
            case R.id.resendCodeBT:
                if (verifyBTS.getVisibility() == View.VISIBLE){
                    phoneET.setEnabled(true);
                    codeTIL.setVisibility(View.GONE);
                    verifyBTS.setVisibility(View.GONE);
                    signupBT.setVisibility(View.VISIBLE);
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context = context_;
        Bundle bundle = getArguments();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        usertype = bundle.getString("type");

        if (usertype.equals("Doctor")){
            doctorSignupProfile = (DoctorSignupProfile) bundle.getSerializable("doctorSignup");
            filePic = bundle.getParcelable("uri");
        }else if (usertype.equals("Patient")){
            patientSignupProfile = (PatientSignupProfile) bundle.getSerializable("Patient");
        }
    }



    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signinWithCredential(credential);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            if (usertype.equals("Doctor")){
                                doctorSignup();
                            }else if (usertype.equals("Patient")){
                                patientSignup();
                            }
                        }else {
                            Toast.makeText(getActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendVerificationCode(){
        phone = phoneET.getText().toString().toLowerCase().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText(getActivity(), "Code Send", Toast.LENGTH_SHORT).show();
            phoneET.setEnabled(false);
            codeTIL.setVisibility(View.VISIBLE);
            verifyBTS.setVisibility(View.VISIBLE);
            signupBT.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                codeET.setText(code);
                progressBar.setVisibility(View.GONE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Verification send Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void patientSignup() {

        String error = "Incomplete This Field";

           patientSignupProfile.setPhone(phone);

           progressBar.setVisibility(View.VISIBLE);

            final FirebaseUser user = auth.getCurrentUser();
            final String userId = user.getUid();

            MedicarePlus.getDatabaseReference().child("ALL_USER").child(user.getUid()).child("PROFILE").setValue(patientSignupProfile);
        progressBar.setVisibility(View.GONE);
            MedicarePlus.getDatabaseReference().child("ALL_USER").child(user.getUid()).child("Type").setValue(usertype);
            MedicarePlus.getDatabaseReference().child(usertype).child(user.getUid()).setValue(user.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });



    }

    private void doctorSignup() {

            progressBar.setVisibility(View.VISIBLE);

            final FirebaseUser user = auth.getCurrentUser();
            final String userId = user.getUid();

            CharSequence s  = DateFormat.format("MM-dd-yy-hh-mm", new Date().getTime());

            storageReference = MedicarePlus.doctorProfilePicStore().child(s+filePic.getLastPathSegment()+"."+getFileExtension(filePic));


            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePic);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] dat = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(dat);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    doctorSignupProfile.setPic_uri(task.getResult().toString());
                    doctorSignupProfile.setUserId(userId);
                    doctorSignupProfile.setPhone(phone);
                    doctorSignupProfile.setEmailVerification(true);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();

                    MedicarePlus.getUserProfileDatabaseReference().setValue(doctorSignupProfile);

                    progressBar.setVisibility(View.GONE);

                    reference = database.getReference();
                    reference.child("ALL_USER").child(userId).child("Type").setValue(usertype);
                    reference = database.getReference();
                    reference.child(usertype).child(userId).setValue(userId).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Complete picupload", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}
