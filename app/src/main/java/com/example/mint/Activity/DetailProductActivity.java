package com.example.mint.Activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.mint.Domain.Foods;
import com.example.mint.Helper.ManagmentCart;
import com.example.mint.databinding.ActivityDetailProductBinding;

public class DetailProductActivity extends BaseActivity {
    ActivityDetailProductBinding binding;
    private Foods object;
    private int num = 1;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        getVariable();

    }

    private void getVariable() {
        managmentCart = new ManagmentCart();

        binding.backeBtn.setOnClickListener(view -> finish());

        Glide.with(this)
                .load(object.getImageData())
                .transform(new CenterCrop(), new RoundedCorners(60))
                .into(binding.detailProdPic);

        binding.precioTxt.setText(object.getPrice() + "€");
        binding.tituloTxt.setText(object.getNombre());
        binding.descripcionTxt.setText(object.getDescription());
        binding.ratingTxt.setText(object.getStar() + "Valoración");
        binding.ratingBar.setRating((float) object.getStar());
        binding.totalTxt.setText((num * object.getPrice()) + "€");

        binding.plusBtn.setOnClickListener(view -> {
            num = num + 1;
            binding.numTxt.setText(num + "");
            binding.totalTxt.setText((num * object.getPrice()) + "€");
        });

        binding.minusBtn.setOnClickListener(view -> {
            if (num > 1){
                num = num - 1;
                binding.numTxt.setText(num + "");
                binding.totalTxt.setText((num * object.getPrice()) + "€");
            }
        });

        binding.addToCartBtn.setOnClickListener(view -> {
            object.setNumberInCart(num);
            managmentCart.insertFood(object);
        });
    }

    private void getIntentExtra() {
        object = (Foods) getIntent().getSerializableExtra("object");
    }
}