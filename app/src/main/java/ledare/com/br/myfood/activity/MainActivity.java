package ledare.com.br.myfood.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseUser;


import ledare.com.br.myfood.MyApplication;
import ledare.com.br.myfood.R;
import ledare.com.br.myfood.fragment.RestauranteFragment;
import ledare.com.br.myfood.util.CircleTransform;
import ledare.com.br.myfood.util.EasyPermission;

public class MainActivity extends BaseActivity
    implements EasyPermission.OnPermissionResult{

    public static final String USER_LOGIN = "USER_LOGIN";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment fragment;
    public EasyPermission easyPermission;

    private AlertDialog.Builder mAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkUser()){
            setupToolbar();
            getSupportActionBar().setTitle("MyFood");
            setupNavigation();
            easyPermission = new EasyPermission();
            easyPermission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
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
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()){
            case R.id.navigation_item_restaurantes:
                fragment = new RestauranteFragment().newInstance();
                startFragment(fragment);
                break;
            case R.id.navigation_item_sair:
                logout();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        if(permission.equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)){
            if(!isGranted){
                callDialog();
            }
        }
    }

    private int startFragment(Fragment fragment){
        return getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, fragment, "FRAGMENT")
                .commit();
    }

    private void logout() {
        Log.d("TESTE", (MyApplication.getInstance().getAuth().getCurrentUser().getProviderId()));
        MyApplication.getInstance().getAuth().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void toast(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void callDialog(){
        mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("Permissão");
        mAlert.setMessage("Permita o acesso a localização para poder acessar o mapa.");
        mAlert.setCancelable(false);
        mAlert.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        easyPermission.requestPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                });
        AlertDialog aviso = mAlert.create();
        aviso.show();

    }

}
