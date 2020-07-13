package com.example.to_be_decided;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class my_medication_list extends Fragment {
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ArrayList<Pills> pills = new ArrayList<>();
    boolean test = true;
    View views;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_medication_list, container, false);
    }

    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(my_medication_list.this).attach(my_medication_list.this).commit();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        views = view;
        pills = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pills")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name = document.getData().get("name").toString();
                                String barcode = document.getData().get("barcode").toString();
                                String time = document.getData().get("time").toString();
                                String id = document.getId();
                                pills.add(new Pills(id,name,barcode, document.getData().get("owner").toString(),time, document.getData().get("week").toString()));
                            }

                                PillsAdapter adapter = new PillsAdapter(views.getContext(),R.layout.list_base,pills);
                                list = views.findViewById(R.id.meds_list_name);
                                list.setAdapter(adapter);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    CustomDialog cdd=new CustomDialog((Activity)view.getContext(),pills.get(position), my_medication_list.this);
                                    cdd.show();

                                    cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            refresh();
                                        }
                                    });

                                   /* AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());


                                    LinearLayout layout = new LinearLayout(view.getContext());
                                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    layout.setLayoutParams(parms);

                                    layout.setGravity(Gravity.CLIP_VERTICAL);
                                    layout.setPadding(2, 2, 2, 2);

                                    TextView tv = new TextView(view.getContext());
                                    tv.setText("Text View title");
                                    tv.setPadding(40, 40, 40, 40);
                                    tv.setGravity(Gravity.CENTER);
                                    tv.setTextSize(20);


                                    builder.setTitle(pills.get(position).getName());
                                    builder.setCustomTitle(tv);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.setNegativeButton("Remover", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
*/
                                }
                            });


                            //adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CustomDialogAdd cdd2=new CustomDialogAdd((Activity)view.getContext(), my_medication_list.this);
                cdd2.show();

                cdd2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        refresh();
                    }
                });

            }
        }
        );


        view.findViewById(R.id.backMyMedList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(my_medication_list.this)
                        .navigate(R.id.action_my_house_to_SecondFragment);
            }
        });

    }
}
