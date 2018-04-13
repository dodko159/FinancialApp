package cz.utb.fai.dodo.financialapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.shared.AdapterCategory;
import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.CategorySimple;
import cz.utb.fai.dodo.financialapp.shared.Transaction;
import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.databinding.MainActivityDataBinding;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.ui.detail.category.CategoryDetail;

public class MainActivity extends AppCompatActivity implements IAdapterItemClicked<CategorySimple>{

    /***** CONSTANTS *****/
    private static final String USERJSON = "userJson";

    /***** VARS *****/
    private Button button;

    Button logOutBtn, profileBtn;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User me = new User();
    MainViewModel model;

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

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
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

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        recyclerView = mainActivityDataBinding.transactionRecycleView;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        AdapterCategory adapter = new AdapterCategory(new ArrayList<CategorySimple>(), this);
        adapter.setNewList(CategorySimple.mapToList(model.getPrices()));
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onItemClicked(CategorySimple categorySimple) {

        List<Transaction> transactions = model.getGroupedMap().get(categorySimple.getCategory());

        Intent intent = CategoryDetail.startIntent(this, transactions);
        startActivity(intent);

        Toast.makeText(this, "Redirecting to > " + Category.getCategoryName(categorySimple.getCategory()), Toast.LENGTH_SHORT).show();
    }
}
