package cz.utb.fai.dodo.financialapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.common.MyDate;
import cz.utb.fai.dodo.financialapp.common.PieGraph;
import cz.utb.fai.dodo.financialapp.common.adapters.AdapterCategory;
import cz.utb.fai.dodo.financialapp.common.CategorySimple;
import cz.utb.fai.dodo.financialapp.common.Transaction;
import cz.utb.fai.dodo.financialapp.common.User;
import cz.utb.fai.dodo.financialapp.databinding.MainActivityDataBinding;
import cz.utb.fai.dodo.financialapp.common.MyShared;
import cz.utb.fai.dodo.financialapp.ui.addTransaction.AddTransactionActivity;
import cz.utb.fai.dodo.financialapp.ui.detail.category.CategoryDetail;

public class MainActivity extends AppCompatActivity implements IAdapterItemClicked<CategorySimple>{

    /***** CONSTANTS *****/
    private static final String USERJSON = "userJson";

    /***** VARS *****/
    private Button button;

    Button logOutBtn, profileBtn;
    AnimatedPieView animatedPieView;
    FloatingActionButton addTransaction;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    FirebaseUser user;
    User me = new User();
    MainViewModel viewModel;
    Observer<HashMap<Integer, Double>> observer;

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
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setMonth(MyDate.longTimeToMonthYear(System.currentTimeMillis()));
        viewModel.start();

        mainActivityDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityDataBinding.setVm(viewModel);

        profileBtn = mainActivityDataBinding.buttonProfile;

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListeners();

        mAuth.addAuthStateListener(mAuthListener);
        viewModel.getPrices().observe(this, observer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
        viewModel.getPrices().removeObserver(observer);
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

        addTransaction = mainActivityDataBinding.addTransactionButton;

        recyclerView = mainActivityDataBinding.transactionRecycleView;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        final AdapterCategory adapter = new AdapterCategory(new ArrayList<CategorySimple>(), this);

        final Map<Integer, Double> map = viewModel.getPrices().getValue();
        if (map == null) {
            adapter.setNewList(new ArrayList<CategorySimple>());
        }else {
            adapter.setNewList(CategorySimple.mapToList(map));
            updatePieView(CategorySimple.mapToList(map));
        }

        recyclerView.setAdapter(adapter);

        observer = new Observer<HashMap<Integer, Double>>() {
            @Override
            public void onChanged(@Nullable HashMap<Integer, Double> integerDoubleHashMap) {
                if (integerDoubleHashMap == null) {
                    adapter.setNewList(new ArrayList<CategorySimple>());
                }else {
                    List<CategorySimple> categorySimples = CategorySimple.mapToList(integerDoubleHashMap);
                    adapter.setNewList(categorySimples);
                    updatePieView(categorySimples);
                    animatedPieView.start();
                }
            }
        };
    }

    private void updatePieView(List<CategorySimple> categorySimples) {
        double sum = 0;

        for (CategorySimple cs : categorySimples) {
            sum += cs.getPriceSum();
        }

        setPieView(sum, categorySimples);
    }

    private void setPieView(double sum, List<CategorySimple> categorySimples) {
        animatedPieView = mainActivityDataBinding.animatedPieView;
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();

        List<Integer> colors = prepearColors(categorySimples.size());

        config.startAngle(-90)
                .splitAngle(1)
                .strokeMode(true)
                .drawText(true)
                .textSize(26)
                .duration(700);

        int i = 0;
        for (CategorySimple cs : categorySimples) {
            config.addData(new PieGraph(cs, sum, colors.get(i)));
            i++;
        }

        animatedPieView.applyConfig(config);
    }

    private  List<Integer> prepearColors(int size) {
        List<Integer> colors = new ArrayList<>();

        colors.add(Color.HSVToColor(new float[] {270, 0.2f ,0.9f}));

        float s = 0f, v = 90f;

        for(int i = 1; i < size; i++ ){

            s += (80 / (size - 1));
            v -= (40 / (size - 1));

            float[] hsv = {270f, s/100, v/100};

            colors.add(Color.HSVToColor(hsv));
        }

        return  colors;
    }

    private void setListeners(){
       /*logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
*/
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = UserProfile.startIntent(MainActivity.this);
                //startActivity(intent);

                startActivity(new Intent(MainActivity.this, MainTabActivity.class));
            }
        });

       addTransaction.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = AddTransactionActivity.startIntent(MainActivity.this);
               startActivity(intent);
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

    @Override
    public void onItemClicked(CategorySimple categorySimple) {

        List<Transaction> transactions = viewModel.getGroupedMap().get(categorySimple.getCategory());

        Intent intent = CategoryDetail.startIntent(this, transactions, categorySimple.getPriceSum(), categorySimple.getCategory());
        startActivity(intent);
    }
}
