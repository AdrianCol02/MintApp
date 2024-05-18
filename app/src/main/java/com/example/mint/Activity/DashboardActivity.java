package com.example.mint.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.mint.Adapter.CategoryAdapter;
import com.example.mint.Adapter.SliderAdapter;
import com.example.mint.Domain.Category;
import com.example.mint.Domain.SliderItems;
import com.example.mint.R;
import com.example.mint.databinding.ActivityDashboardBinding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardActivity extends BaseActivity {
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new InitializeDatabaseTask().execute();

        initCategory();
        initBanner();
        setVariable();
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        new LoadBannerTask().execute();
    }

    private void banners(ArrayList<SliderItems> items) {
        binding.viewpager2.setAdapter(new SliderAdapter(items, binding.viewpager2));
        binding.viewpager2.setClipChildren(false);
        binding.viewpager2.setClipToPadding(false);
        binding.viewpager2.setOffscreenPageLimit(3);
        binding.viewpager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpager2.setPageTransformer(compositePageTransformer);
    }

    private void setVariable() {
        binding.bottomMenu.setItemSelected(R.id.home, true);
        binding.bottomMenu.setOnItemSelectedListener(i -> {
            if (i == R.id.carrito) {
                startActivity(new Intent(DashboardActivity.this, CartActivity.class));
            }
        });
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        new LoadCategoryTask().execute();
    }

    private class LoadBannerTask extends AsyncTask<Void, Void, ArrayList<SliderItems>> {
        @Override
        protected ArrayList<SliderItems> doInBackground(Void... voids) {
            ArrayList<SliderItems> items = new ArrayList<>();
            try {
                if (connection != null && !connection.isClosed()) {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Banners");
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        SliderItems sliderItem = new SliderItems();
                        byte[] imageData = resultSet.getBytes("image_data");
                        sliderItem.setImage(imageData);
                        items.add(sliderItem);
                    }
                    resultSet.close();
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<SliderItems> items) {
            banners(items);
            binding.progressBarBanner.setVisibility(View.GONE);
        }
    }

    private class LoadCategoryTask extends AsyncTask<Void, Void, ArrayList<Category>> {
        @Override
        protected ArrayList<Category> doInBackground(Void... voids) {
            ArrayList<Category> list = new ArrayList<>();
            try {
                if (connection != null && !connection.isClosed()) {
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM Category");
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        Category category = new Category();
                        category.setName(resultSet.getString("name"));
                        list.add(category);
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
        protected void onPostExecute(ArrayList<Category> list) {
            if (list.size() > 0) {
                binding.recyclerViewCategory.setLayoutManager(new GridLayoutManager(DashboardActivity.this, 3));
                binding.recyclerViewCategory.setAdapter(new CategoryAdapter(list));
            }
            binding.progressBarCategory.setVisibility(View.GONE);
        }
    }
}
