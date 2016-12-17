package ledare.com.br.myfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import ledare.com.br.myfood.R;
import ledare.com.br.myfood.MyApplication;
import ledare.com.br.myfood.model.Usuario;

import static ledare.com.br.myfood.activity.MainActivity.USER_LOGIN;

public class LoginActivity extends BaseActivity {

    private LoginButton mButtonFacebook;
    private CallbackManager mCallbackManager;
    private SignInButton mButtonGoogle;
    private static final int RC_GOOGLE_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        setupToolbar();
        getSupportActionBar().setTitle("Bem Vindo");

        initFacebookLogin();
        initGoogleLogin();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                showProgress();
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                toast("Falha no login com o Google");
            }
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initFacebookLogin(){

        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        mButtonFacebook = (LoginButton) findViewById(R.id.button_facebook_login);
        mButtonFacebook.setReadPermissions("email", "public_profile");
        mButtonFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "onCancel!");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK", "onError!", error);
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken token){
        showProgress();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        MyApplication.getInstance().getAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onAuthSucess(task.getResult().getUser());
                        }else{
                            Log.d("FACEBOOK", "firebaseAuthWithFacebook!");
                            hideProgress();
                            toast("Falha na autenticação do Facebook");
                        }

                    }
                });
    }

    private void initGoogleLogin() {

        mButtonGoogle = (SignInButton) findViewById(R.id.button_google_login);
        mButtonGoogle.setSize(SignInButton.SIZE_WIDE);

        mButtonGoogle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onGoogleLogin();
                    }
                }
        );

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_key))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        toast("Falha na conexão com o Google");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void onGoogleLogin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        MyApplication.getInstance().getAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            onAuthSucess(task.getResult().getUser());
                        }else{
                            toast("Falha na autenticação com o Google");
                        }
                    }
                });
    }

    private void onAuthSucess(FirebaseUser user) {

        Usuario usuario = new Usuario();
        usuario.id = user.getUid();
        usuario.nome = user.getDisplayName();
        usuario.email = user.getEmail();
        usuario.foto = user.getPhotoUrl().toString();

        MyApplication.getInstance().getDatabaseReference()
                .child("usuario")
                .child(user.getUid())
                .setValue(usuario);

        hideProgress();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void toast(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

}
