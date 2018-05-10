package cz.utb.fai.dodo.financialapp.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.databinding.TransactionsFragmentDataBinding;
import cz.utb.fai.dodo.financialapp.common.CategorySimple;
import cz.utb.fai.dodo.financialapp.common.MyShared;
import cz.utb.fai.dodo.financialapp.common.PieGraph;
import cz.utb.fai.dodo.financialapp.common.Transaction;
import cz.utb.fai.dodo.financialapp.common.adapters.AdapterCategory;
import cz.utb.fai.dodo.financialapp.ui.detail.category.CategoryDetail;

/**
 */
public class TransactionsFragment extends Fragment implements IAdapterItemClicked<CategorySimple> {

    /***** CONSTANTS *****/
    private static final String ARG_SECTION_MONTH = "section_nonth";

    /***** VARS *****/

    Context context;
    AnimatedPieView animatedPieView;
    TransactionFragmentViewModel viewModel;
    Observer<HashMap<Integer, Double>> observer;
    SharedPreferences.OnSharedPreferenceChangeListener sharedListener;

    TransactionsFragmentDataBinding fragmentDataBinding;

    public TransactionsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     * @param sectionoMonth
     */
    public static TransactionsFragment newInstance(String sectionoMonth) {
        TransactionsFragment fragment = new TransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_MONTH, sectionoMonth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();

        viewModel = ViewModelProviders.of(this).get(TransactionFragmentViewModel.class);
        String moth = getArguments().getString(ARG_SECTION_MONTH);
        viewModel.setMonth(moth);
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions, container, false);
        View rootView = fragmentDataBinding.getRoot();

        fragmentDataBinding.setVm(viewModel);

        init();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        viewModel.getPrices().observe(this, observer);
        MyShared.sGetPrefs(context)
                .registerOnSharedPreferenceChangeListener(sharedListener);

        viewModel.setIncomesOrCost(MyShared.sGetPrefs(context).getBoolean(MyShared.ISINCOMES, false));
    }

    @Override
    public void onStop() {
        super.onStop();

        viewModel.getPrices().removeObserver(observer);
        MyShared.sGetPrefs(context)
                .unregisterOnSharedPreferenceChangeListener(sharedListener);
    }

    /**** HELPER METHODS ****/

    private void init() {

        RecyclerView recyclerView = fragmentDataBinding.transactionRecycleView;

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        final AdapterCategory adapter = new AdapterCategory(new ArrayList<CategorySimple>(), this);

        recyclerView.setAdapter(adapter);

        observer = new Observer<HashMap<Integer, Double>>() {
            @Override
            public void onChanged(@Nullable HashMap<Integer, Double> integerDoubleHashMap) {
                if (integerDoubleHashMap == null) {
                    adapter.setNewList(new ArrayList<CategorySimple>());
                }else {
                    List<CategorySimple> categorySimples = CategorySimple.mapToList(integerDoubleHashMap);
                    adapter.setNewList(categorySimples);
                    updatePieView(categorySimples);
                    animatedPieView.start();
                }
            }
        };

        setListeners();

    }

    private void setListeners() {

        sharedListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(Objects.equals(key, MyShared.ISINCOMES)){
                    viewModel.setIncomesOrCost(sharedPreferences.getBoolean(key, false));
                }
            }
        };
    }

    private void updatePieView(List<CategorySimple> categorySimples) {
        double sum = 0;

        for (CategorySimple cs : categorySimples) {
            sum += cs.getPriceSum();
        }

        setPieView(sum, categorySimples);
    }

    private void setPieView(double sum, List<CategorySimple> categorySimples) {
        animatedPieView = fragmentDataBinding.animatedPieView;
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();

        List<Integer> colors = prepearColors(categorySimples.size());

        config.startAngle(-90)
                .splitAngle(1)
                .strokeMode(true)
                .drawText(true)
                .textSize(26)
                .duration(700);

        int i = 0;
        for (CategorySimple cs : categorySimples) {
            config.addData(new PieGraph(cs, sum, colors.get(i)));
            i++;
        }

        animatedPieView.applyConfig(config);
    }

    private  List<Integer> prepearColors(int size) {
        List<Integer> colors = new ArrayList<>();

        colors.add(Color.HSVToColor(new float[] {270, 0.2f ,0.9f}));

        float s = 0f, v = 90f;

        for(int i = 1; i < size; i++ ){

            s += (80 / (size - 1));
            v -= (40 / (size - 1));

            float[] hsv = {270f, s/100, v/100};

            colors.add(Color.HSVToColor(hsv));
        }

        return  colors;
    }

    @Override
    public void onItemClicked(CategorySimple categorySimple) {

        List<Transaction> transactions = viewModel.getGroupedMap().get(categorySimple.getCategory());

        Intent intent = CategoryDetail.startIntent(getActivity(), transactions, categorySimple.getPriceSum(), categorySimple.getCategory());
        startActivity(intent);
    }
}
