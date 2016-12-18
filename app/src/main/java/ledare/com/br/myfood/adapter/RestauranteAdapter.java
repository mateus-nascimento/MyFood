package ledare.com.br.myfood.adapter;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ledare.com.br.myfood.R;

public class RestauranteAdapter implements GoogleMap.InfoWindowAdapter{

    private View popup = null;
    private LayoutInflater inflater = null;

    public RestauranteAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null){
            popup = inflater.inflate(R.layout.adapter_restaurante, null);
        }

        TextView txtNome = (TextView) popup.findViewById(R.id.map_nome);
        TextView txtDesc = (TextView) popup.findViewById(R.id.map_descricao);

        txtNome.setText(marker.getTitle());
        txtDesc.setText(marker.getSnippet());

        return popup;
    }
}
