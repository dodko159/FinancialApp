package cz.utb.fai.dodo.financialapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import cz.radovanholik.library.utils.Utils;
import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.databinding.MainTabDataBinding;
import cz.utb.fai.dodo.financialapp.shared.MyDate;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.adapters.SectionsPagerAdapter;
import cz.utb.fai.dodo.financialapp.ui.addTransaction.AddTransactionActivity;
import cz.utb.fai.dodo.financialapp.ui.profile.UserProfile;

public class MainTabActivity extends AppCompatActivity {

    //todo zobrazenie grafu - bud zmensit alebo scrolovat

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    View.OnClickListener addTransClickListener;
    MainTabViewModel viewModel;
    MainTabDataBinding dataBinding;

    FloatingActionButton addTransaction;

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, MainTabActivity.class);
    }

    /***** LIFECYCLE METHODS *****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(MainTabViewModel.class);
        viewModel.init();

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_tab);
        dataBinding.setVm(viewModel);

        Toolbar toolbar = dataBinding.toolbar;
        setSupportActionBar(toolbar);

        addTransaction = dataBinding.addTransactionButton;

        mViewPager = dataBinding.container;

        viewModel.getMonths().observe(this, new Observer<List<String>>(){
            @Override
            public void onChanged(@Nullable List<String> strings) {
                init(strings);
            }
        });

        checkInternet();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        addTransaction.setOnClickListener(addTransClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            Intent intent = UserProfile.startIntent(MainTabActivity.this);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
        viewModel.getMonths().removeObservers(this);
    }

    /***** HELPER METHODS *****/

    void init(List<String> moths){

        // Create the adapter that will return a fragment for each
        // sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), moths);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = dataBinding.tabs;
        tabLayout.removeAllTabs();

        TabLayout.Tab tab;
        for (String month : moths) {
            tab = tabLayout.newTab().setText(MyDate.toPreatyMonthYear(month));
            tabLayout.addTab(tab);
        }

        mViewPager.clearOnPageChangeListeners();
        tabLayout.clearOnTabSelectedListeners();

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.setCurrentItem(mSectionsPagerAdapter.getCount() - 1);
    }

    private void setListeners(){

        addTransClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddTransactionActivity.startIntent(MainTabActivity.this);
                startActivity(intent);
            }
        };

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    finish();
                }
            }
        };
    }

    /**
     * Zobrazi dialogove okno ak nieje pristup k internetu
     */
    private void checkInternet() {
        if(!Utils.isInternetAvailable(this) && !MyShared.wasInternetDialogShown(getApplication())){
            /* no internet dialog */
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(R.string.no_internet_title);
            alertDialog.setMessage(getResources().getString(R.string.no_internet_text));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            MyShared.internetDialogShown(getApplication(), true);
        }
    }
}
