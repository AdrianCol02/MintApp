package com.example.mint.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mint.Adapter.FoodListAdapter;
import com.example.mint.Domain.Foods;
import com.example.mint.databinding.ActivityListFoodBinding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListFoodActivity extends BaseActivity {
    ActivityListFoodBinding binding;
    private RecyclerView.Adapter adapter;
    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initList();
    }

    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        new LoadFoodListTask().execute(categoryId);
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryID", 0);
        categoryName = getIntent().getStringExtra("CategoryName");

        binding.listaTituloTxt.setText(categoryName);
        binding.backListBtn.setOnClickListener(view -> finish());
    }

    private class LoadFoodListTask extends AsyncTask<Integer, Void, ArrayList<Foods>> {
        @Override
        protected ArrayList<Foods> doInBackground(Integer... params) {
            int categoryId = params[0];
            ArrayList<Foods> list = new ArrayList<>();

            try {
                if (connection != null && !connection.isClosed()) {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Foods WHERE CategoryId = ?");
                    statement.setInt(1, categoryId);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        Foods food = new Foods();
                        food.setNombre(resultSet.getString("name"));
                        list.add(food);
                    }
                    resultSet.close();
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Foods> list) {
            if (list.size() > 0) {
                binding.foodListView.setLayoutManager(new LinearLayoutManager(
                        ListFoodActivity.this, LinearLayoutManager.VERTICAL, false
                ));
                binding.foodListView.setAdapter(new FoodListAdapter(list));
            }
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}
