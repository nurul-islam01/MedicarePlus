package com.nurul.medicareplus.reg_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.activity.MainActivity;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.SignUp;
import com.nurul.medicareplus.StoreUser;

import java.util.concurrent.TimeUnit;

public class SignIn extends Fragment implements View.OnClickListener {

    private static final String TAG = SignIn.class.getSimpleName();
    private Context context;

    private FirebaseAuth auth;

    private TextView newAccountTV, errorShowTV;
    private EditText emailET, passwordET;
    private Button singinBT;

    private ProgressBar progressBar;

    private Button signupBT;
    private LinearLayout verifyBTS;
    private EditText  phoneET,codeET ;
    private Button verifyBT, resendCodeBT;
    private TextInputLayout phoneTIL, codeTIL;
    private String verificationId;
    private String phone;


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        auth = FirebaseAuth.getInstance();
        newAccountTV = v.findViewById(R.id.newAccountTV);
        errorShowTV = v.findViewById(R.id.errorshowTV);

        progressBar = v.findViewById(R.id.progress_bar);

        phoneET = v.findViewById(R.id.phoneET);
        codeET = v.findViewById(R.id.codeET);
        signupBT = v.findViewById(R.id.signupBT);
        progressBar = v.findViewById(R.id.progress_bar);

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
        newAccountTV.setOnClickListener(this);
        signupBT.setOnClickListener(this);

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verifyBT:
                try {
                    final String code = codeET.getText().toString().trim();
                    if (TextUtils.isEmpty(code)){
                        Toast.makeText(context, "Code Is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                }catch (Exception e){
                    Toast.makeText(context, "Somgthing Wrong", Toast.LENGTH_SHORT).show();
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
            case R.id.newAccountTV:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null).replace(R.id.regFragmentContainer, new Identifier());
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.commit();
                break;
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

                            MedicarePlus.getAllUserUserKey().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()){
                                        if ( child.getKey().toString().equals(auth.getCurrentUser().getUid().toString()) ){
                                            Toast.makeText(context, "Login", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("userId", auth.getUid());
                                            context.startActivity(intent);

                                        }else if ( child.getKey().toString().equals(auth.getCurrentUser().getUid().toString()) ){
                                            auth.signOut();
                                            errorShowTV.setText("Please Register First");
                                            Toast.makeText(context, "Please Register First", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


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
        try {
            phone = phoneET.getText().toString().toLowerCase().trim();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    60,
                    TimeUnit.SECONDS,
                    TaskExecutors.MAIN_THREAD,
                    mCallback
            );
            if (phone.isEmpty()){
                Toast.makeText(context, "Number Empty", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "sendVerificationCode: "+e.getMessage());
        }

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


}
