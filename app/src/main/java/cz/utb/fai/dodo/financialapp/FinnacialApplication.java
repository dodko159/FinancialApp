package cz.utb.fai.dodo.financialapp;

import android.app.Application;

import cz.radovanholik.library.ConnectivityInformer;
import cz.utb.fai.dodo.financialapp.common.Category;
import cz.utb.fai.dodo.financialapp.common.DBManager;
import cz.utb.fai.dodo.financialapp.common.MyShared;

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

        Category.loadDataFromResources(this);
    }
}
