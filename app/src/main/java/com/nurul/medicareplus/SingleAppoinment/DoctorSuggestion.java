package com.nurul.medicareplus.SingleAppoinment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.SingleAppoinment;

public class DoctorSuggestion extends Fragment implements SingleAppoinment.AppoinmenttDoctor {

    private static final String TAG = DoctorSuggestion.class.getSimpleName();

    private Context context;

    private String appoinmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = context = getActivity();
        ((SingleAppoinment) context).setAppoinmenttDoctor(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_suggestion, container, false);
        return view;
    }


    @Override
    public void appoinment(String appoinmentId) {
        this.appoinmentId = appoinmentId;
    }
}
