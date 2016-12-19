package ledare.com.br.myfood.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ledare.com.br.myfood.R;
import ledare.com.br.myfood.adapter.PratoAdapter;
import ledare.com.br.myfood.model.Prato;
import ledare.com.br.myfood.util.PratoOnClickListener;

public class RestauranteActivity extends BaseActivity {

    protected RecyclerView recyclerView;
    private List<Prato> pratoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        setupToolbar();
        getSupportActionBar().setTitle("Restaurante");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView appBarImg = (ImageView) findViewById(R.id.appBarImg);
        Glide.with(this)
                .load("https://media-cdn.tripadvisor.com/media/photo-s/0b/12/73/f8/rock-e-ribs-steakhouse.jpg")
                .into(appBarImg);

        pratoList = new ArrayList<>();
        for (int i=1; i<20 ; i++){
            Prato prato = new Prato();
            prato.id = String.valueOf(i);
            prato.nome = "Arroz com carne assada: " + i;
            prato.foto = "http://barrancas.com.br/wp-content/uploads/2016/07/bar1.jpg";
            pratoList.add(prato);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerPrato);

        if(getResources().getBoolean(R.bool.portrait)){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new PratoAdapter(this, pratoList, onClickPrato()));

    }

    //Tratamento do evento do clique
    private PratoOnClickListener onClickPrato(){
        return new PratoOnClickListener() {
            @Override
            public void onClickPrato(View view, int position) {
                Prato prato = pratoList.get(position);
                Toast.makeText(RestauranteActivity.this, "Prato: " + prato.nome, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
