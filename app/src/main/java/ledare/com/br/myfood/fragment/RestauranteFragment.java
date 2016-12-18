package ledare.com.br.myfood.fragment;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import ledare.com.br.myfood.R;

public class RestauranteFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static final String TAG = "RestauranteFragment";
    public static final int DEFAULT_ZOOM = 13;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private Snackbar mSnackBar;

    public static RestauranteFragment newInstance() {
        RestauranteFragment fragment = new RestauranteFragment();
        return fragment;
    }

    public RestauranteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_restaurante, container, false);

        mSnackBar= Snackbar.make(layout.getRootView(), "Para descobrir sua localização ative o GPS.", Snackbar.LENGTH_LONG);

        //Build the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        //
        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("DISPLAY", "onConnected");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e("DISPLAY", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("DISPLAY", "onConnectionFailed");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_mapa, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();
        if(item == R.id.item_localizacao){
            //Pega a localização

            Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            //Atualiza a localização
            setMapLocation(l);

            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void setMapLocation(Location l) {
        if(mMap != null && l != null){
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mMap.animateCamera(update);

            //Desenha bolinha vermelha
            CircleOptions circle = new CircleOptions().center(latLng);
            circle.fillColor(Color.RED);
            circle.strokeColor(Color.BLUE);
            circle.radius(5);//em metros
            mMap.clear();
            mMap.addCircle(circle);
        }else{
            mSnackBar.show();
        }
    }


}
