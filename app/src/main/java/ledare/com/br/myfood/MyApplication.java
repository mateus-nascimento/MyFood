package ledare.com.br.myfood;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application{

    public static final String TAG = "MyApplication";

    private static MyApplication instance = null;

    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public DatabaseReference getDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }
}
