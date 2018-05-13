package cz.utb.fai.dodo.financialapp.ui.addTransaction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.databinding.AddTransactionDataBinding;
import cz.utb.fai.dodo.financialapp.common.Category;
import cz.utb.fai.dodo.financialapp.common.adapters.AdapterCategoryGrid;

public class AddTransactionActivity extends AppCompatActivity implements IAdapterItemClicked<Integer>{

    private AddTransactionDataBinding addTransactionDataBinding;
    private AddTransactionViewModel viewModel;

    @NonNull
    public static Intent startIntent(@NonNull FragmentActivity activity) {
        return new Intent(activity, AddTransactionActivity.class);
    }

    /**** LIVECYCLE METHODS ****/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTransactionDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getCloseActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Boolean finish;
                if (aBoolean == null) {
                    finish = false;
                }
                else {
                    finish = aBoolean;
                }
                if(finish){
                    AddTransactionActivity.this.finish();
                }
            }
        });
    }

    /**** HELAPER METHODS ***/

    private void init() {
        viewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);
        addTransactionDataBinding.setVm(viewModel);

        RecyclerView recyclerView = addTransactionDataBinding.recycleViewAddTransaction;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] categories = Category.getIncome();
        if(!viewModel.getIncome()){
            categories = Category.getCost();
        }

        final AdapterCategoryGrid adapter = new AdapterCategoryGrid(categories,this, getApplication().getApplicationContext());
        recyclerView.setAdapter(adapter);

        viewModel.getIncomeLive().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Boolean in;
                if (aBoolean == null) {
                    in = false;
                }
                else {
                    in = aBoolean;
                }
                if(in){
                    adapter.setNewList(Category.getIncome());
                }else {
                    adapter.setNewList(Category.getCost());
                }
            }
        });
    }

    @Override
    public void onItemClicked(Integer position) {
        int offset = 0;
        if(!viewModel.getIncome()){
            offset = Category.OFFSET;
        }
        viewModel.setCategory(offset + position);
    }
}
