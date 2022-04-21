package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jonathan.pam_tas.adapters.AllProductAdapter;
import com.jonathan.pam_tas.adapters.ProductAdapter;
import com.jonathan.pam_tas.models.AllProductModel;
import com.jonathan.pam_tas.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    RecyclerView productRecycler;
    FirebaseAuth auth;
    List<ProductModel> productModelList;
    ProductAdapter productAdapter;
    FirebaseFirestore db;
    StorageReference storageReference;
    EditText inputSearch;

    private List<AllProductModel> allProductModelList;
    private RecyclerView searchRecycler;
    private AllProductAdapter allProductAdapter;

    ImageView icFilter,profileImage;
    TextView giftCategoryBirthday, giftCategoryGraduation, giftCategoryAnniversary, giftCategoryWedding, txtHeader, textContinent, txtSeeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PAM_TAS);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        icFilter = findViewById(R.id.iconFilter);
        giftCategoryBirthday = findViewById(R.id.giftCategoryBirthday);
        giftCategoryGraduation = findViewById(R.id.giftCategoryGraduation);
        giftCategoryAnniversary = findViewById(R.id.giftCategoryAnniversary);
        giftCategoryWedding = findViewById(R.id.giftCategoryWedding);
        txtHeader = findViewById(R.id.txtHeader);
        profileImage = findViewById(R.id.profileImage);
        textContinent = findViewById(R.id.textContinent);
        txtSeeAll = findViewById(R.id.txtSeeAll);
        inputSearch = findViewById(R.id.inputSearch);
        searchRecycler = findViewById(R.id.searchRecycler);


        allProductModelList = new ArrayList<>();
        allProductAdapter = new AllProductAdapter(getApplicationContext(), allProductModelList);
        searchRecycler.setLayoutManager(new GridLayoutManager(this,2));
        searchRecycler.setAdapter(allProductAdapter);
        searchRecycler.setHasFixedSize(true);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    allProductModelList.clear();
                    allProductAdapter.notifyDataSetChanged();
                    txtHeader.setVisibility(View.VISIBLE);
                    txtSeeAll.setVisibility(View.VISIBLE);
                    productRecycler.setVisibility(View.VISIBLE);
                }else{
                    txtHeader.setVisibility(View.GONE);
                    txtSeeAll.setVisibility(View.GONE);
                    productRecycler.setVisibility(View.GONE);
                    searchProduct(s.toString());
                }

            }
        });




        setTopBar();

        productRecycler = findViewById(R.id.recyclerView);
        productRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false));
        productModelList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productModelList);
        productRecycler.setAdapter(productAdapter);

        setproduct();

        giftCategoryBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtHeader.getText()=="Birthday Gift"){
                    txtHeader.setText("Recommended");
                    icFilter.clearColorFilter();
                    giftCategoryBirthday.setBackgroundResource(R.drawable.category_bg);
                    setproduct();
                }
                else {
                    productModelList.clear();
                    db.collection("Product").whereEqualTo("type", "birthday")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ProductModel productModel = document.toObject(ProductModel.class);
                                            productModelList.add(productModel);
                                        }
                                        productAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    txtHeader.setText("Birthday Gift");
                    icFilter.setColorFilter(Color.argb(255, 64, 150, 254));
                    giftCategoryBirthday.setBackgroundResource(R.drawable.category_bg_activated);
                    giftCategoryWedding.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryGraduation.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryAnniversary.setBackgroundResource(R.drawable.category_bg);

                }
            }
        });

        giftCategoryWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtHeader.getText() == "Wedding Gift") {
                    txtHeader.setText("Recommended");
                    icFilter.clearColorFilter();
                    giftCategoryWedding.setBackgroundResource(R.drawable.category_bg);
                    setproduct();
                } else {
                    productModelList.clear();
                    db.collection("Product").whereEqualTo("type", "wedding")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ProductModel productModel = document.toObject(ProductModel.class);
                                            productModelList.add(productModel);
                                        }
                                        productAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    txtHeader.setText("Wedding Gift");
                    icFilter.setColorFilter(Color.argb(255, 64, 150, 254));
                    giftCategoryWedding.setBackgroundResource(R.drawable.category_bg_activated);
                    giftCategoryBirthday.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryGraduation.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryAnniversary.setBackgroundResource(R.drawable.category_bg);
                }
            }
        });

        giftCategoryGraduation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtHeader.getText() == "Graduation Gift") {
                    txtHeader.setText("Recommended");
                    icFilter.clearColorFilter();
                    giftCategoryGraduation.setBackgroundResource(R.drawable.category_bg);
                    setproduct();
                } else {
                    productModelList.clear();
                    db.collection("Product").whereEqualTo("type", "graduation")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ProductModel productModel = document.   toObject(ProductModel.class);
                                            productModelList.add(productModel);
                                        }
                                        productAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    txtHeader.setText("Graduation Gift");
                    icFilter.setColorFilter(Color.argb(255, 64, 150, 254));
                    giftCategoryGraduation.setBackgroundResource(R.drawable.category_bg_activated);
                    giftCategoryWedding.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryBirthday.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryAnniversary.setBackgroundResource(R.drawable.category_bg);
                }
            }
        });

        giftCategoryAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtHeader.getText() == "Anniversary Gift") {
                    txtHeader.setText("Recommended");
                    icFilter.clearColorFilter();
                    giftCategoryAnniversary.setBackgroundResource(R.drawable.category_bg);
                    setproduct();
                } else {
                    productModelList.clear();
                    db.collection("Product").whereEqualTo("type", "anniversary")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            ProductModel productModel = document.toObject(ProductModel.class);
                                            productModelList.add(productModel);
                                        }
                                        productAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    txtHeader.setText("Anniversary Gift");
                    icFilter.setColorFilter(Color.argb(255, 64, 150, 254));
                    giftCategoryAnniversary.setBackgroundResource(R.drawable.category_bg_activated);
                    giftCategoryWedding.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryGraduation.setBackgroundResource(R.drawable.category_bg);
                    giftCategoryBirthday.setBackgroundResource(R.drawable.category_bg);
                }
            }
        });

        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllProductActivity.class));
            }
        });

        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.home_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_menu:
                        return true;
                    case R.id.bookmark_menu:
                        startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cart_menu:
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
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

    }

    private void searchProduct(String type){
        if(!type.isEmpty()){
            db.collection("Product").document(type)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful() && task.getResult() != null){
                                allProductModelList.clear();
                                allProductAdapter.notifyDataSetChanged();
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()) {
                                    AllProductModel allProductModel = document.toObject(AllProductModel.class);
                                    allProductModelList.add(allProductModel);
                                    allProductAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
        }
    }

    private void setTopBar() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            textContinent.setText("HELLO, LOOKING FOR SOME GIFT?");
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
                                    textContinent.setText("Hello "+document.getString("name"));
                                }
                            }
                        }
                    });
        }
    }

    private void setproduct() {
        db = FirebaseFirestore.getInstance();

        productModelList.clear();

        db.collection("Product").whereGreaterThan("love", 10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = document.toObject(ProductModel.class);
                                productModelList.add(productModel);
                            }
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}