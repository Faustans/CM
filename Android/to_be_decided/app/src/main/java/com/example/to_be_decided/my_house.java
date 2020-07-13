package com.example.to_be_decided;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class my_house extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap mGoogleMap;
    ArrayList<MarkerOptions> markers = new ArrayList<>();
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MapsInitializer.initialize(getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_house, container, false);
    }

    private LatLng pt;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mapView =  this.getActivity().findViewById(R.id.mapView);
        if (mapView != null) {
            // Initialise the MapView
            mapView.onCreate(null);
            mapView.onResume();
            // Set the map ready callback to receive the GoogleMap object
            mapView.getMapAsync(this);
        }

        view.findViewById(R.id.backMyHouse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(my_house.this)
                        .navigate(R.id.action_my_house_to_SecondFragment);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        final SharedPreferences  mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String house = mPrefs.getString("house","");
        if(!house.isEmpty()){
            Log.d(TAG,house);
            String[] x = house.split("º");
            Log.d(TAG,x[0]);
            Log.d(TAG,x[1]);
            LatLng l = new LatLng(Double.parseDouble(x[0]),Double.parseDouble(x[1]));
            MarkerOptions marker = new MarkerOptions().position(new LatLng(l.latitude, l.longitude)).title("A minha Casa");
            markers.add(marker);
            mGoogleMap.addMarker(marker);
            System.out.println(l.latitude + "---" + l.longitude);
        }
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                pt = point;
                if(markers.size()==0) {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("A minha Casa");
                    markers.add(marker);
                    mGoogleMap.addMarker(marker);
                    System.out.println(point.latitude + "---" + point.longitude);
                    prefsEditor.putString("house",point.latitude + "º" + point.longitude);
                    prefsEditor.commit();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Confirm");
                    builder.setMessage("Tem a certeza que quer mudar a sua casa?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            markers.clear();
                            mGoogleMap.clear();
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(pt.latitude, pt.longitude)).title("A minha Casa");
                            markers.add(marker);
                            mGoogleMap.addMarker(marker);
                            System.out.println(pt.latitude + "---" + pt.longitude);
                            prefsEditor.putString("house",pt.latitude + "º" + pt.longitude);
                            prefsEditor.commit();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
