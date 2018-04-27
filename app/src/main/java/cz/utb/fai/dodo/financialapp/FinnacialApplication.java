package cz.utb.fai.dodo.financialapp;

import android.app.Application;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import cz.radovanholik.library.ConnectivityInformer;
import cz.radovanholik.library.utils.Utils;
import cz.utb.fai.dodo.financialapp.shared.DBManager;
import cz.utb.fai.dodo.financialapp.shared.MyShared;

/**
 * Created by Dodo on 27.04.2018.
 */

public class FinnacialApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ConnectivityInformer.getInstance().init(this);

        DBManager.initDB();

        MyShared.internetDialogShown(this, false);
    }
}
