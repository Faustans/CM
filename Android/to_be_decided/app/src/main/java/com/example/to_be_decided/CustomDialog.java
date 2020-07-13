package com.example.to_be_decided;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private Pills p;
    private Fragment fragment;
    private boolean dismissed_;

    public CustomDialog(Activity a,Pills p, Fragment fragment) {
        super(a);
        dismissed_ =false;
        // TODO Auto-generated constructor stub
        this.c = a;
        this.p = p;
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.remove_alert);
        TextView t = (TextView) findViewById(R.id.txt_name2);
        t.setText(p.getName());
        TextView t1 = (TextView) findViewById(R.id.txt_Barcode2);
        t1.setText(p.getBarcode());
        TextView t2 = (TextView) findViewById(R.id.txt_time2);
        t2.setText(p.getTime());
        TextView t3 = (TextView) findViewById(R.id.txt_day2);
        t3.setText(p.getWeek());
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
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
            case R.id.btn_yes:
                dismissed_ =true;
                dismiss();
                break;
            case R.id.btn_no:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("pills").document(p.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                dismissed_ =true;

                dismiss();
                break;
            default:
                break;
        }
        dismissed_ =true;
        dismiss();

    }
}