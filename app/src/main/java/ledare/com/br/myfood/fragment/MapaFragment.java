package ledare.com.br.myfood.fragment;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ledare.com.br.myfood.R;


public class MapaFragment extends Fragment implements OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;

    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("MAPA", "onCreate");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //Configura a localização do usuário
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MAPA", "onCreateView");
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_mapa, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        return layout;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("MAPA", "onMapReady");
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void setMapLocation(Location location) {
        Log.d("MAPA", "setMapLocation");

        if(map != null && location != null){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLatitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 5);
            map.animateCamera(update);

            //Indica posição
            CircleOptions circle = new CircleOptions().center(latLng);
            circle.fillColor(Color.RED);
            circle.radius(999999999);
            map.clear();
            map.addCircle(circle);
            Log.d("MAPA", "circuloPosition");
        }
    }

    @Override
    public void onStart() {
        Log.d("MAPA", "onStart");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        Log.d("MAPA", "onStop");
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("MAPA", "onConnected");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        setMapLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("MAPA", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("MAPA", "onConnectionFailed:" + connectionResult);
    }
}
