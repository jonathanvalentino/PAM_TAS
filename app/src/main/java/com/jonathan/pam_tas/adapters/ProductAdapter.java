package com.jonathan.pam_tas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jonathan.pam_tas.ProductDetail;
import com.jonathan.pam_tas.R;
import com.jonathan.pam_tas.models.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> productModelList;

    public ProductAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(productModelList.get(position).getImg_url()).into(holder.productImage);
        holder.name.setText(productModelList.get(position).getName());
        holder.description.setText(productModelList.get(position).getDescription());
        holder.love.setText(productModelList.get(position).getLove().toString()+" love this");
        holder.sold.setText(productModelList.get(position).getSold()+" sold");
        holder.price.setText("Rp."+productModelList.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("detail", productModelList.get(position));
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView name, description, love, sold, price;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            description = itemView.findViewById(R.id.productDesc);
            love = itemView.findViewById(R.id.productLove);
            sold = itemView.findViewById(R.id.productSold);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
}
