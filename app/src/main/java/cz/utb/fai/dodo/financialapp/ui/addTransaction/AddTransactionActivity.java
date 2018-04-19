package cz.utb.fai.dodo.financialapp.ui.addTransaction;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.databinding.AddTransactionDataBinding;
import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.adapters.AdapterCategoryGrid;

public class AddTransactionActivity extends AppCompatActivity implements IAdapterItemClicked<Integer>{

    private AddTransactionDataBinding addTransactionDataBinding;
    private RecyclerView recyclerView;
    private AddTransactionViewModel viewModel;

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
                if(aBoolean){
                    AddTransactionActivity.this.finish();
                }
            }
        });
    }

    /**** HELAPER METHODS ***/

    private void init() {
        viewModel = ViewModelProviders.of(this).get(AddTransactionViewModel.class);
        addTransactionDataBinding.setVm(viewModel);

        recyclerView = addTransactionDataBinding.recycleViewAddTransaction;
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
                if(aBoolean){
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
