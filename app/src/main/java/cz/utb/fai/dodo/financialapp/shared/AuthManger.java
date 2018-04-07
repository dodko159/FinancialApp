package cz.utb.fai.dodo.financialapp.shared;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Dodo on 27.03.2018.
 */

public class AuthManger {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context context;

    public AuthManger() {
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    public AuthManger(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    public void setAuthStateListener(FirebaseAuth.AuthStateListener authStateListener){
        mAuth.addAuthStateListener(authStateListener);
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getUser() {
        return user;
    }
}
