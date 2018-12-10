package com.nurul.medicareplus.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.SingleMedicineTimer;
import com.nurul.medicareplus.pojos.MedicineTimer;

public class RingTonePlayingService extends Service implements View.OnTouchListener {

    private static final String TAG = RingTonePlayingService.class.getSimpleName();
    private Context context;

    private MediaPlayer tonePlyer;
    private boolean isRunning;
    private int startId;
    private MedicineTimer timer;
    private String state;

    private Intent single_medicine_timer_activity;
    private PendingIntent pendingIntent_timer;
    private Notification notification;
    private NotificationManager notify_manager;
    private GestureDetector gestureDetector;


    public RingTonePlayingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        state = intent.getExtras().getString("extra");
        timer = (MedicineTimer) intent.getSerializableExtra("timer");
        Log.d(TAG, "onStartCommand: "+state);
        this.context = this;
        assert state != null;
        switch (state){
            case "alerm_on":
                startId = 1;
                break;
            case "alerm_off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if ( !this.isRunning && startId == 1){
            tonePlyer = MediaPlayer.create(this, R.raw.medicine_tone);
            tonePlyer.setLooping(true);
            tonePlyer.start();

            this.isRunning = true;
            this.startId = 0;

            notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            this.single_medicine_timer_activity = new Intent( this.getApplicationContext(), SingleMedicineTimer.class);
            this.pendingIntent_timer = PendingIntent.getActivity(this, 0, single_medicine_timer_activity, 0);

            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.notificaton_alerm_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.medicine_timer_icon))
                    .setContentTitle(timer.getName())
                    .setContentText(timer.getTimeString())
                    .setContentIntent(pendingIntent_timer)
                    .setAutoCancel(tonePlyer.isPlaying())
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;

            notify_manager.notify(0, notification);

        }

        if (this.isRunning && startId == 0){
            Log.d(TAG, " inside if ");

            tonePlyer.stop();
            tonePlyer.reset();
            this.isRunning = false;
            this.startId = 0;

        }

        if ( !this.isRunning && startId == 0){
            this.isRunning = false;
            this.startId = 0;
        }

        if (this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 1;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = false;
    }

    private class GestureLister  extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;

        }

        public void onSwipeRight() {
            Intent intentXXX = new Intent(context, SingleMedicineTimer.class);
            context.startActivity(intentXXX);
        }

        public void onSwipeLeft() {
            Intent intentXXY = new Intent(context, SingleMedicineTimer.class);
            context.startActivity(intentXXY);
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }


    public void OnSwiftTouchListener(Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureLister());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
