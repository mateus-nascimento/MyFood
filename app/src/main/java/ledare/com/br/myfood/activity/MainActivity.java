package ledare.com.br.myfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ledare.com.br.myfood.R;

public class MainActivity extends BaseActivity{

    public static final String USER_LOGIN = "USER_LOGIN";

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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean accepted = pref.getBoolean(USER_LOGIN, false);
        return accepted;
    }
}
