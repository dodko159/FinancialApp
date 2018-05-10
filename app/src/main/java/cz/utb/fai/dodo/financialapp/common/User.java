package cz.utb.fai.dodo.financialapp.common;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

/**
 * Created by Dodo on 15.03.2018.
 */

public class User {

    /***** VARS *****/

    private String name;
    private String surName;
    private String mail;
    private String uid;
    private String photoUrl;

    /***** CONSTRUCTORS *****/

    public User() {
    }

    public User(String mail, String uid) {
        this.mail = mail;
        this.uid = uid;
    }

    public User(String name, String surName, String mail, String uid) {
        this.name = name;
        this.surName = surName;
        this.mail = mail;
        this.uid = uid;
    }

    public User(FirebaseUser firebaseUser) {
        this.name = firebaseUser.getDisplayName();
        this.mail = firebaseUser.getEmail();
        this.uid = firebaseUser.getUid();
        this.photoUrl = firebaseUser.getPhotoUrl().toString();
    }

    /***** GET, SET *****/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhotoUrl (){
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    /***** CONVERSION METHODS *****/

    /**
     * Vytori z uzivatela Json
     * @param user Uzivatel na formatovanie
     * @return vrati uzivatela vo foramte Json
     */
    public static String userToString(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        return json;
    }

    /***
     * Upravi aktualneho uzivatela podla jsonu a vrati ho.
     * @param userjson uzivatel vo formate Json (String)
     * @return vrati uzivatela
     */
    public void userFromJson(String userjson) {

        User user = new Gson().fromJson(userjson,User.class);

        this.name = user.getName();
        this.surName = user.getSurName();
        this.mail = user.getMail();
        this.uid = user.getUid();
        this.photoUrl = user.getPhotoUrl();
    }

    @Override
    public String toString() {
        return name + " " + surName;
    }

}
