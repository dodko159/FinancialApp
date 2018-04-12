package cz.utb.fai.dodo.financialapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.databinding.MainActivityDataBinding;
import cz.utb.fai.dodo.financialapp.shared.MyShared;

public class MainActivity extends AppCompatActivity {

    /***** CONSTANTS *****/
    private static final String USERJSON = "userJson";

    /***** VARS *****/
    private Button button;

    Button logOutBtn, profileBtn;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User me = new User();

    MainActivityDataBinding mainActivityDataBinding;

    private RecyclerView recyclerView;

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     *
     * @param context aktualny context
     * @param userjson user vo formáte json
     * @return Vrací intent s vloženým objektem user vo formate Json.
     */
    public static Intent startIntent(@NonNull Context context, String userjson) {
        Intent intent = startIntent(context);
        intent.putExtra(USERJSON, userjson);

        return intent;
    }

    /***** LIFECYCLE METHODS *****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel mainViewModel = new MainViewModel(getApplication());

        mainActivityDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityDataBinding.setVm(mainViewModel);

        init();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    /**** HELPER METHODS ****/

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        String userjson = null;

        if (getIntent().getExtras() != null) {
            userjson = getIntent().getStringExtra(USERJSON);
        }

        if (userjson != null) {
            me.userFromJson(userjson);
        }else{
            me = MyShared.getUser(this);
        }

        recyclerView = mainActivityDataBinding.transactionRecycleView;
        //RecyclerView.Adapter mAdapter = new MyAdapter(myDataset);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setAdapter(mAdapter);
    }

    private void setListeners(){
       /*logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserProfile.startIntent(MainActivity.this);
                startActivity(intent);
            }
        });*/

       button = mainActivityDataBinding.detailButton;

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //
           }
       });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user == null){
                    finish();
                }
            }
        };
    }
}
