package ledare.com.br.myfood.activity;

import android.app.Activity;
import android.os.Bundle;

import ledare.com.br.myfood.R;

public class RestauranteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        setupToolbar();
        getSupportActionBar().setTitle("Pratos do Restaurante");
    }
}
