package com.jonathan.pam_tas.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jonathan.pam_tas.CartActivity;
import com.jonathan.pam_tas.LoginActivity;
import com.jonathan.pam_tas.MainActivity;
import com.jonathan.pam_tas.R;
import com.jonathan.pam_tas.models.CartModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModel> cartModelList;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(cartModelList.get(position).getImg_url()).into(holder.productImage);
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.recipient.setText(cartModelList.get(position).getRecipient());
        holder.letter.setText(cartModelList.get(position).getLetter());
        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .document(cartModelList.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    cartModelList.remove(cartModelList.get(position));
                                    notifyDataSetChanged();
                                    new SweetAlertDialog(context)
                                            .setTitleText("Gift successfully deleted from cart")
                                            .show();
                                }
                                else{
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText(task.getException().getMessage())
                                            .show();
                                }
                            }
                        });
                DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
                docRef.update("price", FieldValue.increment(-Integer.parseInt(cartModelList.get(position).getProductPrice().substring(3))));
                context.startActivity(new Intent(context, CartActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, iconDelete;
        TextView name, price, recipient, letter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            recipient = itemView.findViewById(R.id.recipientName);
            letter = itemView.findViewById(R.id.personalLetter);
            iconDelete = itemView.findViewById(R.id.iconDelete);

        }
    }
}
