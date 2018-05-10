package cz.utb.fai.dodo.financialapp.common.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cz.utb.fai.dodo.financialapp.ui.main.TransactionsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<String> moths;

    public SectionsPagerAdapter(FragmentManager fm, List<String> moths) {
        super(fm);
        this.moths = moths;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return TransactionsFragment.newInstance(moths.get(position));
    }

    @Override
    public int getCount() {
        if (moths == null) {
            return 1;
        }
        return moths.size();
    }
}
