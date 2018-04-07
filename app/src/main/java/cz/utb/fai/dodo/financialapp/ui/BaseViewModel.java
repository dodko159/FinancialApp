package cz.utb.fai.dodo.financialapp.ui;

import android.databinding.BindingConversion;
import android.view.View;

/**
 * Created by Dodo on 03.04.2018.
 */

public class BaseViewModel {
    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}


