package ledare.com.br.myfood.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ledare.com.br.myfood.R;
import ledare.com.br.myfood.util.EasyPermission;

public class BaseActivity extends AppCompatActivity{

    protected Toolbar toolbar;
    private ProgressDialog mProgress;

    protected void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
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
