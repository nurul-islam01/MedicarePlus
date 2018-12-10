package com.nurul.medicareplus.BReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nurul.medicareplus.Services.RingTonePlayingService;

public class Alarm_Receiver extends BroadcastReceiver {

    private static final String TAG = Alarm_Receiver.class.getSimpleName();
    private String myString, asd;
    private Intent service_intent;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: Inside alerm Reciver");

        myString = intent.getExtras().getString("extra");
        asd = intent.getExtras().getString("extra1");

        service_intent = new Intent(context, RingTonePlayingService.class);
        service_intent.putExtra("extra", myString);
        context.startActivity(service_intent);
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
