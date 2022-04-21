package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jonathan.pam_tas.models.CartModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderedActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    StorageReference storageReference;
    TextView txtHeader, textContinent;
    Button btnBackToCart;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        txtHeader = findViewById(R.id.txtHeader);
        profileImage = findViewById(R.id.profileImage);
        textContinent = findViewById(R.id.textContinent);
        btnBackToCart = findViewById(R.id.btnBackToCart);

        setTopBar();

        List<CartModel> list = (ArrayList<CartModel>)getIntent().getSerializableExtra("itemList");

        if(list!= null && list.size()>0){
            for(CartModel model : list){
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", model.getProductName());
                cartMap.put("productPrice", model.getProductPrice());
                cartMap.put("recipient", model.getRecipient());
                cartMap.put("letter", model.getLetter());
                cartMap.put("img_url", model.getImg_url());

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrder")
                        .add(cartMap);

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart").whereEqualTo("productName", model.getProductName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        if (document.exists()) {
                                            document.getReference().delete();
                                        }
                                    }
                                }
                            }
                        });

                DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
                docRef.update("price", FieldValue.increment(-Integer.parseInt(model.getProductPrice().substring(3))));
            }
        }

        btnBackToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.home_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bookmark_menu:
                        startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                        return true;
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.cart_menu:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    private void setTopBar() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
        }
        else{
            StorageReference fileRef = storageReference.child(auth.getCurrentUser().getUid());
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });
            db.collection("UserData").document(auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    textContinent.setText("Thank you "+document.getString("name")+" for your purchase");
                                }
                            }
                        }
                    });
        }
    }

}