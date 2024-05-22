package com.example.mint.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mint.Adapter.CartAdapter;
import com.example.mint.Helper.ManagmentCart;
import com.example.mint.databinding.ActivityCartBinding;

public class CartActivity extends BaseActivity {
    ActivityCartBinding binding;
    private ManagmentCart managmentCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        calculateCart();
        initCartList();

    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()){
            binding.vacioTxt.setVisibility(View.VISIBLE);
            binding.scrolViewCart.setVisibility(View.GONE);
        }else{
            binding.vacioTxt.setVisibility(View.GONE);
            binding.scrolViewCart.setVisibility(View.VISIBLE);
        }
        binding.cartView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(
                managmentCart.getListCart(), managmentCart, () -> calculateCart()));
    }

    private void calculateCart() {
        double percentTax = 0.04;
        double delivery = 2.5;
        double tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.subtotalPricetxt.setText(itemTotal + "€");
        binding.IVAPriceTxt.setText(tax + "€");
        binding.deliveryPriceTxt.setText(delivery + "€");
        binding.totalPriceTxt.setText(total + "€");
    }

    private void setVariable() {
        binding.backCartBtn.setOnClickListener(view -> startActivity(
                new Intent(CartActivity.this, DashboardActivity.class)
        ));
    }
}