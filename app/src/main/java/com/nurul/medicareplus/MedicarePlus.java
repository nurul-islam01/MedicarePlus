package com.nurul.medicareplus;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.activity.Regestration;
import com.nurul.medicareplus.pojos.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicarePlus extends Application {

    private static FirebaseAuth auth;
    private static FirebaseUser user;
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;
    private static StorageReference storageReference;

    public static final String TYPE_DOCTOR = "Doctor";
    public static final String TYPE_PATIENT = "Patient";


    @Override
    public void onCreate() {
        super.onCreate();
         FirebaseApp.initializeApp(this);
         auth = FirebaseAuth.getInstance();
         user = auth.getCurrentUser();
         storage = FirebaseStorage.getInstance();
         storageReference = storage.getReference();
         if (user == null){
             Intent intent = new Intent(this, Regestration.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
         }else {
             FirebaseDatabase.getInstance().setPersistenceEnabled(true);
             FirebaseStorage.getInstance().getApp();
         }

    }


    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public static String getUserId(){
        return getUser().getUid();
    }

    public static FirebaseUser getUser(){
        auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }

    public static StorageReference getStorageReference(){
        storage = FirebaseStorage.getInstance();
        return storage.getReference();
    }

    public static StorageReference profilePicStore(){
        return getStorageReference().child(getUserId()).child("Profile");
    }

    public static StorageReference medicinPicStore(){
        return getStorageReference().child(getUserId()).child("Medicine");
    }

    public static StorageReference patientMedicinPicStore(String patientId){
        return getStorageReference().child(patientId).child("Medicine");
    }

    public static StorageReference doctorProfilePicStore(){
        return getStorageReference().child(getUserId()).child("ProfilePic");
    }

    public static DatabaseReference getDatabaseReference(){
        database = FirebaseDatabase.getInstance();
        return database.getReference();
    }

    public static DatabaseReference getUserProfileDatabaseReference(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("PROFILE");
    }
    public static DatabaseReference getAllUserUserKey(){
        return getDatabaseReference().child("ALL_USER");
    }


    public static DatabaseReference getAppoinmentReq(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("NewRequest");
    }

    public static DatabaseReference appoinmentedStore(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Appoinmented");
    }

    public static DatabaseReference getMedicineTimerReference(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("MedicineTimes");
    }

    public static DatabaseReference patientMedicineTimerReference(String patientId){
        return getDatabaseReference().child("ALL_USER").child(patientId).child("MedicineTimes");
    }

    public static DatabaseReference removeMedicineTimer(String rowId){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("MedicineTimes").child(rowId);
    }
    public static DatabaseReference removePatientMedicineTimer(String rowId, String patientId){
        return getDatabaseReference().child("ALL_USER").child(patientId).child("MedicineTimes").child(rowId);
    }


    public static DatabaseReference medicineReference(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Medicins");
    }

    public static DatabaseReference patientMedicineReference(String patientId){
        return getDatabaseReference().child("ALL_USER").child(patientId).child("Medicins");
    }
    public static DatabaseReference appoinmentedSuggestion(String patientId, String doctorId){

        // getUser() is the doctor id, that is used on row in patients

        return getDatabaseReference().child("ALL_USER").child(patientId).child("Appoinments").child(doctorId).child("SUGGESTIONS");
    }


    public static DatabaseReference recentlyAddMedicineDesc(String medicineId){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Medicins").child(medicineId).child("TakenDes");
    }
    public static DatabaseReference getmedicineDesc(String medicineId){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Medicins").child(medicineId).child("MedicineDes");
    }


    public static DatabaseReference PatientMedicineDesc(String pushId, String patientId){
        return getDatabaseReference().child("ALL_USER").child(patientId).child("Medicins").child(pushId).child("MedicineDes");
    }


    public static DatabaseReference getUserIDdatabaseReference(String userId){
        return getDatabaseReference().child("ALL_USER").child(userId).child("PROFILE");
    }

    public static DatabaseReference confirmRequest(String userId){
        return getDatabaseReference().child("ALL_USER").child(userId).child("Appoinments");
    }

    public static DatabaseReference appoinmentsofDoctors(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Appoinments");
    }

    public static DatabaseReference patientAppointmentDesc(String doctorId){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Appoinments").child(doctorId).child("AppoinmentDes");
    }



    public static DatabaseReference appoinmentRequestSend(String doctorId, String reqId){
        database = FirebaseDatabase.getInstance();
        return getDatabaseReference().child("ALL_USER").child(doctorId).child("NewRequest").child(reqId);
    }


    public static DatabaseReference getUserTypeDatabaseReference(){
        return getDatabaseReference().child("ALL_USER").child(getUserId()).child("Type");
    }

}
