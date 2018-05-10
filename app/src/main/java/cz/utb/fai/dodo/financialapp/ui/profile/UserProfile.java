package cz.utb.fai.dodo.financialapp.ui.profile;


import android.arch.lifecycle.ViewModelProviders;
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

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, UserProfile.class);
    }

    /***** LIFECYCLE METHODS *****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserProfileDataBinding userProfileDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_profile);
        UserProfileViewModel viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileDataBinding.setVm(viewModel);
    }
}
