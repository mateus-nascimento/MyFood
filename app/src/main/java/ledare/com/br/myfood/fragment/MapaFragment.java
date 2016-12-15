package ledare.com.br.myfood.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ledare.com.br.myfood.R;
import ledare.com.br.myfood.util.Permissoes;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    public static MapaFragment newInstance() {
        MapaFragment fragment = new MapaFragment();
        return fragment;
    }

    public MapaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_mapa, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        //inicia o google maps dentro do fragment
        mapFragment.getMapAsync(this);
        return layout;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //botão para mostrar a localização
        //map.setMyLocationEnabled(true);

        Double latitude = -8.063171;
        Double longitude = -34.871143;
        //localizacao baseada na Lat e Lng
        LatLng location = new LatLng(latitude, longitude);

        //posiciona o mapa na coordenada
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
        map.moveCamera(update);

        //marcador do local
        map.addMarker(new MarkerOptions().title("Marco Zero")
                .snippet("Onde tivemos a ideia do aplicativo")
                .position(location));

        //tipo do mapa
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                alertAndFinish();
                return;
            }
        }

        // Se chegou aqui está OK :-)
    }

    private void alertAndFinish() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, você precisa aceitar as permissões.");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().finish();
                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();

        }
    }
}
