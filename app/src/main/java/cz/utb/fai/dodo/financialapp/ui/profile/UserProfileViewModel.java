package cz.utb.fai.dodo.financialapp.ui.profile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.view.View;

import cz.utb.fai.dodo.financialapp.common.DBManager;
import cz.utb.fai.dodo.financialapp.common.User;
import cz.utb.fai.dodo.financialapp.common.MyShared;

/**
 * Created by Dodo on 27.03.2018.
 */

public class UserProfileViewModel extends AndroidViewModel {

    /**** VARS ****/
    private User user;
    private String name, surname, mail;

    /**** CONSTRUCTOR ****/
    UserProfileViewModel(@NonNull Application application) {
        super(application);

        this.user = MyShared.getUser(application);

        this.name = user.getName();
        this.surname = user.getSurName();
        this.mail = user.getMail();

    }

    /**** VARS ****/
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

    /**** METHODS ****/

    /***
     * Uozi zmeny do shapred a DB
     */
    public void save(View view){

        user = MyShared.getUser(this.getApplication());

        user.setName(name);
        user.setSurName(surname);
        user.setMail(mail);

        MyShared.setUser(this.getApplication(), user);
        DBManager.updateUserInDB(user);
    }
}
