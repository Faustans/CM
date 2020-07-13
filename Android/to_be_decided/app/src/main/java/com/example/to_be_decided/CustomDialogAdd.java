package com.example.to_be_decided;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomDialogAdd extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private Fragment fragment;
    private boolean dismissed_;

    private String name;
    private String barcode;
    private String time;
    private String week;

    public CustomDialogAdd(Activity a, Fragment fragment) {
        super(a);
        dismissed_ =false;
        // TODO Auto-generated constructor stub
        this.c = a;
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_alert);



        yes = (Button) findViewById(R.id.btn_yes1);
        no = (Button) findViewById(R.id.btn_no1);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }


    public boolean dismissed(){
        if(dismissed_){
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes1:
                TextView text = (TextView)findViewById(R.id.editTextName);
                name = text.getText().toString();
                TextView text1 = (TextView)findViewById(R.id.editTextBarCode);
                barcode = text1.getText().toString();
                TextView text2 = (TextView)findViewById(R.id.editTextTime);
                time = text2.getText().toString();
                TextView text3 = (TextView)findViewById(R.id.editTextDay);
                week = text3.getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser fu = FirebaseAuth.getInstance().getCurrentUser();
                Pills p = new Pills(name,name,barcode,fu.getDisplayName(),time,week);

                scheduleNotification(getContext(), calculateTime(time),SecondFragment.notID,p);
                SecondFragment.notID++;
                db.collection("pills").document(this.name)
                        .set(p)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                dismissed_ =true;


                dismissed_ =true;
                dismiss();
                break;
            case R.id.btn_no1:

                dismiss();
                break;
            default:
                break;
        }
        dismissed_ =true;
        dismiss();

    }

    public void scheduleNotification(Context context, long delay, int notificationId, Pills p) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "pills")
                .setContentTitle(p.getName())
                .setContentText("Não se esqueça de tomar o seu "+ p.getName() + " às " + p.getTime())
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.logo)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context,  c.getClass());
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        Log.d(ContentValues.TAG, "Scheduling");
    }

    public long calculateTime(String time){

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        try {
            date1 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String time1 = hour + ":" + minutes;

        Date date2 = null;
        try {
            date2 = format.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = 0;
        if(date2.getTime()<date1.getTime()){
            difference  = date2.getTime() - date1.getTime();
        }
        else {
            difference  = date2.getTime() - date1.getTime();
            difference += 86400000;
        }
        Log.d(ContentValues.TAG,"--------------" + difference);
        return difference;
    }
}