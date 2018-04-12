package cz.utb.fai.dodo.financialapp.ui.profile;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.databinding.UserProfileDataBinding;

public class UserProfile extends AppCompatActivity {

    /**** VARS *****/
    UserProfileDataBinding userProfileDataBinding;
    Button updateButton;

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, UserProfile.class);
    }

    /***** LIFECYCLE METHODS *****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfileDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile);

        init();
    }

    /**** HELPER METHODS ****/
    private void init(){
        updateButton = userProfileDataBinding.buttonSaveChanges;

        userProfileDataBinding.setVm(new UserProfileViewModel(getApplication()));
    }
}
