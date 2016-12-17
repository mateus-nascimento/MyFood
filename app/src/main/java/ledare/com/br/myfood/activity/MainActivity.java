package ledare.com.br.myfood.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import ledare.com.br.myfood.MyApplication;
import ledare.com.br.myfood.R;
import ledare.com.br.myfood.fragment.MapaFragment;
import ledare.com.br.myfood.util.CircleTransform;

public class MainActivity extends BaseActivity {

    public static final String USER_LOGIN = "USER_LOGIN";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment fragment;

    private static final int REQUEST_PERMISSIONS_CODE = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkUser()){
            setupToolbar();
            getSupportActionBar().setTitle("MyFood");
            setupNavigation();
        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public boolean checkUser() {
        if(MyApplication.getInstance().getAuth().getCurrentUser() != null){
            return true;
        }else{
            return false;
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

        switch (menuItem.getItemId()){
            case R.id.navigation_item_restaurantes:
                verificarPermissao();
                break;
            case R.id.navigation_item_sair:
                logout();
                break;
        }
    }

    private void verificarPermissao() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                callDialog(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE);
            }
        }else{
            fragment = new MapaFragment().newInstance();
            startFragment(fragment);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSIONS_CODE){
            if(permissions[0].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fragment = new MapaFragment().newInstance();
                startFragment(fragment);
            }
        }
    }

    private void callDialog(final String[] permissions) {
        new AlertDialog.Builder(this)
                .setTitle("Permissão")
                .setMessage("Precisamos de sua localização para buscarmos os restaurantes.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                permissions,
                                REQUEST_PERMISSIONS_CODE);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
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
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void toast(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
