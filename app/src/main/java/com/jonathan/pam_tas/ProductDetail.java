package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.jonathan.pam_tas.models.AllProductModel;
import com.jonathan.pam_tas.models.BookmarkModel;
import com.jonathan.pam_tas.models.ProductModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {

    private ImageView detailImg, love;
    private TextView name, description, sold, price;
    private Button btnAddToCart;
    private EditText recipientName, personalLetter;
    ProductModel productModel = null;
    BookmarkModel bookmarkModel = null;
    AllProductModel allProductModel = null;
    FirebaseFirestore db;
    String productId;
    FirebaseAuth auth;
    ImageView btnBack;
    Boolean loved = false;
    String loves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        detailImg = findViewById(R.id.detail_img);
        name = findViewById(R.id.productName);
        description = findViewById(R.id.productDesc);
        love = findViewById(R.id.iconHeart);
        sold = findViewById(R.id.productSold);
        price = findViewById(R.id.productPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);
        recipientName = findViewById(R.id.recipientName);
        personalLetter = findViewById(R.id.personalLetter);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof ProductModel){
                productModel = (ProductModel) object;
        }
        else if(object instanceof BookmarkModel){
            bookmarkModel = (BookmarkModel) object;
        }
        else if(object instanceof AllProductModel){
            allProductModel = (AllProductModel) object;
        }

        if(productModel != null){
            Glide.with(getApplicationContext()).load(productModel.getImg_url()).into(detailImg);
            name.setText(productModel.getName());
            description.setText(productModel.getDescription());
            sold.setText(productModel.getSold()+" sold");
            price.setText("Rp "+productModel.getPrice());

            db.collection("UserData").document(auth.getCurrentUser().getUid())
                    .collection("Liked Product").document(productModel.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    loves = document.getString("loved");
                                    if(loves.equals("yes")){
                                        love.setColorFilter(Color.argb(255, 235, 43, 84));
                                    }
                                    else {
                                        love.clearColorFilter();
                                    }
                                } else {
                                    Log.d("LOGGER", "No such document");
                                }
                            } else {
                                Log.d("LOGGER", "get failed with ", task.getException());
                            }
                        }
                    });

            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(love.getColorFilter()==null){
                        love.setColorFilter(Color.argb(255, 235, 43, 84));

                        Map<String, Object> lovedProduct = new HashMap<>();
                        lovedProduct.put("loved", "yes");
                        lovedProduct.put("description", productModel.getDescription());
                        lovedProduct.put("img_url", productModel.getImg_url());
                        lovedProduct.put("love", productModel.getLove());
                        lovedProduct.put("name", productModel.getName());
                        lovedProduct.put("price", productModel.getPrice());
                        lovedProduct.put("sold", productModel.getSold());
                        lovedProduct.put("type", productModel.getType());


                        db.collection("Product").document(productModel.getName())
                                .update("love", FieldValue.increment(1));
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(productModel.getName())
                                .set(lovedProduct);
                    }
                    else if(love.getColorFilter()!=null){
                        love.clearColorFilter();
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(productModel.getName())
                                .update("loved","no");
                        db.collection("Product").document(productModel.getName())
                                .update("love", FieldValue.increment(-1));
                    }
                }

            });

        }
        else if(bookmarkModel != null){
            Glide.with(getApplicationContext()).load(bookmarkModel.getImg_url()).into(detailImg);
            name.setText(bookmarkModel.getName());
            description.setText(bookmarkModel.getDescription());
            sold.setText(bookmarkModel.getSold()+" sold");
            price.setText("Rp "+bookmarkModel.getPrice());

            db.collection("UserData").document(auth.getCurrentUser().getUid())
                    .collection("Liked Product").document(bookmarkModel.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    loves = document.getString("loved");
                                    if(loves.equals("yes")){
                                        love.setColorFilter(Color.argb(255, 235, 43, 84));
                                    }
                                    else {
                                        love.clearColorFilter();
                                    }
                                } else {
                                    Log.d("LOGGER", "No such document");
                                }
                            } else {
                                Log.d("LOGGER", "get failed with ", task.getException());
                            }
                        }
                    });

            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(love.getColorFilter()==null){
                        love.setColorFilter(Color.argb(255, 235, 43, 84));

                        Map<String, Object> lovedProduct = new HashMap<>();
                        lovedProduct.put("loved", "yes");
                        lovedProduct.put("description", bookmarkModel.getDescription());
                        lovedProduct.put("img_url", bookmarkModel.getImg_url());
                        lovedProduct.put("love", bookmarkModel.getLove());
                        lovedProduct.put("name", bookmarkModel.getName());
                        lovedProduct.put("price", bookmarkModel.getPrice());
                        lovedProduct.put("sold", bookmarkModel.getSold());
                        lovedProduct.put("type", bookmarkModel.getType());


                        db.collection("Product").document(bookmarkModel.getName())
                                .update("love", FieldValue.increment(1));
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(bookmarkModel.getName())
                                .set(lovedProduct);
                    }
                    else if(love.getColorFilter()!=null){
                        love.clearColorFilter();
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(bookmarkModel.getName())
                                .update("loved","no");
                        db.collection("Product").document(bookmarkModel.getName())
                                .update("love", FieldValue.increment(-1));
                    }
                }

            });
        }
        else if(allProductModel != null){
            Glide.with(getApplicationContext()).load(allProductModel.getImg_url()).into(detailImg);
            name.setText(allProductModel.getName());
            description.setText(allProductModel.getDescription());
            sold.setText(allProductModel.getSold()+" sold");
            price.setText("Rp "+allProductModel.getPrice());

            db.collection("UserData").document(auth.getCurrentUser().getUid())
                    .collection("Liked Product").document(allProductModel.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    loves = document.getString("loved");
                                    if(loves.equals("yes")){
                                        love.setColorFilter(Color.argb(255, 235, 43, 84));
                                    }
                                    else {
                                        love.clearColorFilter();
                                    }
                                } else {
                                    Log.d("LOGGER", "No such document");
                                }
                            } else {
                                Log.d("LOGGER", "get failed with ", task.getException());
                            }
                        }
                    });

            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(love.getColorFilter()==null){
                        love.setColorFilter(Color.argb(255, 235, 43, 84));

                        Map<String, Object> lovedProduct = new HashMap<>();
                        lovedProduct.put("loved", "yes");
                        lovedProduct.put("description", allProductModel.getDescription());
                        lovedProduct.put("img_url", allProductModel.getImg_url());
                        lovedProduct.put("love", allProductModel.getLove());
                        lovedProduct.put("name", allProductModel.getName());
                        lovedProduct.put("price", allProductModel.getPrice());
                        lovedProduct.put("sold", allProductModel.getSold());
                        lovedProduct.put("type", allProductModel.getType());


                        db.collection("Product").document(allProductModel.getName())
                                .update("love", FieldValue.increment(1));
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(allProductModel.getName())
                                .set(lovedProduct);
                    }
                    else if(love.getColorFilter()!=null){
                        love.clearColorFilter();
                        db.collection("UserData").document(auth.getCurrentUser().getUid())
                                .collection("Liked Product").document(allProductModel.getName())
                                .update("loved","no");
                        db.collection("Product").document(allProductModel.getName())
                                .update("love", FieldValue.increment(-1));
                    }
                }

            });
        }





        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(recipientName.getText())){
                    recipientName.setError( "Recipient name is required!" );
                }
                else if(TextUtils.isEmpty(personalLetter.getText())){
                    personalLetter.setError( "Personal letter is required!" );
                }
                else {
                    addtoCart();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    private void addtoCart() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(ProductDetail.this, LoginActivity.class));
        }
        else {
            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            if(productModel!=null) {
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", productModel.getName());
                cartMap.put("productPrice", price.getText().toString());
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("currentDate", saveCurrentDate);
                cartMap.put("recipient", recipientName.getText().toString());
                cartMap.put("letter", personalLetter.getText().toString());
                cartMap.put("img_url", productModel.getImg_url());

                final HashMap<String, Object> price = new HashMap<>();
                price.put("price", Integer.parseInt(productModel.getPrice()));

                DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                docRef.update("price",FieldValue.increment(Integer.parseInt(productModel.getPrice())));
                            } else {
                                docRef.set(price);
                            }
                        }
                    }
                });

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .add(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                startActivity(new Intent(getApplicationContext(), CartActivity.class)
                                        .putExtra("added",true));
                            }
                        });

            }
            else if(bookmarkModel!=null){
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", bookmarkModel.getName());
                cartMap.put("productPrice", price.getText().toString());
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("currentDate", saveCurrentDate);
                cartMap.put("recipient", recipientName.getText().toString());
                cartMap.put("letter", personalLetter.getText().toString());
                cartMap.put("img_url", bookmarkModel.getImg_url());

                final HashMap<String, Object> price = new HashMap<>();
                price.put("price", Integer.parseInt(bookmarkModel.getPrice()));

                DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                docRef.update("price",FieldValue.increment(Integer.parseInt(bookmarkModel.getPrice())));
                            } else {
                                docRef.set(price);
                            }
                        }
                    }
                });

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .add(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                startActivity(new Intent(getApplicationContext(), CartActivity.class)
                                        .putExtra("added",true));
                            }
                        });
            }
            else if(allProductModel!=null){
                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("productName", allProductModel.getName());
                cartMap.put("productPrice", price.getText().toString());
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("currentDate", saveCurrentDate);
                cartMap.put("recipient", recipientName.getText().toString());
                cartMap.put("letter", personalLetter.getText().toString());
                cartMap.put("img_url", allProductModel.getImg_url());

                final HashMap<String, Object> price = new HashMap<>();
                price.put("price", Integer.parseInt(allProductModel.getPrice()));

                DocumentReference docRef = db.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                docRef.update("price",FieldValue.increment(Integer.parseInt(allProductModel.getPrice())));
                            } else {
                                docRef.set(price);
                            }
                        }
                    }
                });

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("AddToCart")
                        .add(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                startActivity(new Intent(getApplicationContext(), CartActivity.class)
                                        .putExtra("added",true));
                            }
                        });
            }



        }

    }


}