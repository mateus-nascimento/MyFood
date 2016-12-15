package ledare.com.br.myfood.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;

import ledare.com.br.myfood.MyApplication;
import ledare.com.br.myfood.R;
import ledare.com.br.myfood.fragment.PratoFragment;
import ledare.com.br.myfood.fragment.MapaFragment;
import ledare.com.br.myfood.util.CircleTransform;

import static ledare.com.br.myfood.activity.MainActivity.USER_LOGIN;

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressDialog mProgress;

    protected void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }
    }

    protected void setupNavigation(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if(navigationView != null && drawerLayout != null){
            setupHeader(navigationView);
            //
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            drawerLayout.closeDrawers();
                            onNavigationSelected(menuItem);
                            return true;
                        }
                    }
            );
        }

        //Animação
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.bringToFront();
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        drawerLayout.post(new Runnable() {
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    private void setupHeader(NavigationView navigationView) {

        FirebaseUser usuario = MyApplication.getInstance().getAuth().getCurrentUser();

        View headerView = navigationView.getHeaderView(0);
        TextView headerNome = (TextView) headerView.findViewById(R.id.header_nome);
        TextView headerEmail = (TextView) headerView.findViewById(R.id.header_email);
        final ImageView headerImagem = (ImageView) headerView.findViewById(R.id.header_imagem);

        headerNome.setText(usuario.getDisplayName());
        headerEmail.setText(usuario.getEmail());
        Glide.with(this).load(usuario.getPhotoUrl())
                .transform(new CircleTransform(getBaseContext()))
                .into(headerImagem);
    }

    private void onNavigationSelected(MenuItem menuItem) {
        Fragment fragment;
        switch (menuItem.getItemId()){
            case R.id.navigation_item_restaurantes:
                Toast.makeText(this, "Buscando Restaurantes", Toast.LENGTH_SHORT).show();
                fragment = new MapaFragment().newInstance();
                startFragment(fragment);
                break;

            case R.id.navigation_item_pratos_dia:
                Toast.makeText(this, "Montando Pratos", Toast.LENGTH_SHORT).show();
                fragment = new PratoFragment().newInstance();
                startFragment(fragment);
                break;

            case R.id.navigation_item_sair:
                logout();
        }
    }

    private int startFragment(Fragment fragment){
        return getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment, "FRAGMENT")
                .commit();
    }

    private void logout() {
        MyApplication.getInstance().getAuth().signOut();
        LoginManager.getInstance().logOut();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean(USER_LOGIN, false).apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    protected void openDrawer(){
        if(drawerLayout != null){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected void closeDrawer(){
        if(drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    protected void showProgress(){
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
            mProgress.setCancelable(false);
            mProgress.setMessage("Carregando...");
        }
        mProgress.show();
    }

    protected void hideProgress(){
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

}
