package cz.utb.fai.dodo.financialapp.ui.profile;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.shared.MyShared;

/**
 * Created by Dodo on 27.03.2018.
 */

public class UserProfileViewModel extends ViewModel {

    private User user;
    private String name, surname, mail;
    private Context context;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    UserProfileViewModel(@NonNull Context context) {

        this.context = context;
        this.user = MyShared.getUser(context);

        this.name = user.getName();
        this.surname = user.getSurName();
        this.mail = user.getMail();

    }

    public void save(View view){

        user = MyShared.getUser(context);

        user.setName(name);
        user.setSurName(surname);
        user.setMail(mail);

        MyShared.setUser(context, user);
    }
}
