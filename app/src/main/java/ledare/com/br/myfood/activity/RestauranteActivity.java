package ledare.com.br.myfood.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import ledare.com.br.myfood.R;

public class RestauranteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        setupToolbar("Restaurante");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView appBarImg = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this)
                .load("https://media-cdn.tripadvisor.com/media/photo-s/0b/12/73/f8/rock-e-ribs-steakhouse.jpg")
                .into(appBarImg);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
