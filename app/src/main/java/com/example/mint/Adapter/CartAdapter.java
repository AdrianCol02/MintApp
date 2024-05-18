package com.example.mint.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.mint.Domain.Foods;
import com.example.mint.Helper.ChangeNumberItemsListener;
import com.example.mint.Helper.ManagmentCart;
import com.example.mint.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Foods> list;
    private ManagmentCart managmentCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Foods> list, ManagmentCart managmentCart,
                       ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        this.managmentCart = managmentCart;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getNombre());
        holder.feeEachItem.setText(list.get(position).getNumberInCart() *
                list.get(position).getPrice() + "€");
        holder.num.setText(list.get(position).getNumberInCart() + "");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImageData())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(view -> managmentCart.plusNumberItem(
                list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.minusItem.setOnClickListener(view -> managmentCart.minusNumberItem(
                list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

        holder.trashBtn.setOnClickListener(view -> managmentCart.removeItem(list, position, () -> {
            notifyDataSetChanged();
            changeNumberItemsListener.change();
        }));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView num;
        ConstraintLayout trashBtn;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleCartViewhTxt);
            pic = itemView.findViewById(R.id.cartViewhPic);
            feeEachItem = itemView.findViewById(R.id.feeEachItemTxt);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numItemsCartTxt);
            trashBtn = itemView.findViewById(R.id.trashBtn);
        }
    }
}
