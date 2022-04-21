package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jonathan.pam_tas.adapters.CartAdapter;
import com.jonathan.pam_tas.models.CartModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    StorageReference storageReference;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    TextView totalPrice, txtTotal;
    Button btnBuy, btnBrowse;
    ImageView profileImage;
    LinearLayout cartEmpty;
    List<CartModel> cartModelList;
    private boolean addedToCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        storageReference = FirebaseStorage.getInstance().getReference();
        totalPrice = findViewById(R.id.totalPrice);
        cartEmpty = findViewById(R.id.cartEmpty);
        txtTotal = findViewById(R.id.txtTotal);
        btnBuy = findViewById(R.id.btnBuy);
        btnBrowse = findViewById(R.id.btnBrowse);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        profileImage = findViewById(R.id.profileImage);

        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartModelList);
        recyclerView.setAdapter(cartAdapter);

        setTopBar();

        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(this, LoginActivity.class));
        }
        else {
            db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                    .collection("AddToCart")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                    if (documentSnapshot.exists()) {

                                        String documentId = documentSnapshot.getId();

                                        CartModel cartModel = documentSnapshot.toObject(CartModel.class);

                                        cartModel.setDocumentId(documentId);
                                        cartModelList.add(cartModel);
                                        cartAdapter.notifyDataSetChanged();

                                        cartEmpty.setVisibility(View.GONE);
                                        txtTotal.setVisibility(View.VISIBLE);
                                        totalPrice.setVisibility(View.VISIBLE);
                                        btnBuy.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }
                    });


            DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            totalPrice.setText("Rp." + document.get("price").toString());
                        } else {

                        }
                    }
                }
            });

        }
        cartEmpty.setVisibility(View.VISIBLE);
        txtTotal.setVisibility(View.GONE);
        totalPrice.setVisibility(View.GONE);
        btnBuy.setVisibility(View.GONE);

        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.cart_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cart_menu:
                        return true;
                    case R.id.bookmark_menu:
                        startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addedToCart = bundle.getBoolean("added");
        }

        if(addedToCart==true){
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success")
                    .setContentText("Product added to the cart")
                    .show();
            addedToCart = false;
        }

    }
    private void setTopBar() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            StorageReference fileRef = storageReference.child(auth.getCurrentUser().getUid());
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}