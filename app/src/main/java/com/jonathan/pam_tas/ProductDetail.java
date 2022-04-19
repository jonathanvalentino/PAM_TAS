package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    FirebaseFirestore db;
    String productId;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        detailImg = findViewById(R.id.detail_img);
        name = findViewById(R.id.productName);
        description = findViewById(R.id.productDesc);
        love = findViewById(R.id.iconHeart);
        sold = findViewById(R.id.productSold);
        price = findViewById(R.id.productPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        recipientName = findViewById(R.id.recipientName);
        personalLetter = findViewById(R.id.personalLetter);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof ProductModel){
                productModel = (ProductModel) object;
        }

        if(productModel != null){
            Glide.with(getApplicationContext()).load(productModel.getImg_url()).into(detailImg);
            name.setText(productModel.getName());
            description.setText(productModel.getDescription());
            sold.setText(productModel.getSold()+" sold");
            price.setText("Rp "+productModel.getPrice());

        }

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(love.getColorFilter()==null){
                    love.setColorFilter(Color.argb(255, 235, 43, 84));

                    Map<String, Object> docData = new HashMap<>();
                    docData.put("stringExample", "Hello world!");
                    docData.put("booleanExample", true);

                    db.collection("Product").document(productModel.getName())
                            .update("love", FieldValue.increment(1));
                }
                else if(love.getColorFilter()!=null){
                    love.clearColorFilter();
                }
            }

        });

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

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("productName", productModel.getName());
            cartMap.put("productPrice", price.getText().toString());
            cartMap.put("currentTime", saveCurrentTime);
            cartMap.put("currentDate", saveCurrentDate);
            cartMap.put("recipient", recipientName.getText().toString());
            cartMap.put("letter", personalLetter.getText().toString());
            cartMap.put("img_url",productModel.getImg_url());

            final HashMap<String, Object> price = new HashMap<>();
            price.put("price",Integer.parseInt(productModel.getPrice()));



            DocumentReference docRef = db.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("CartTotalPrice").document("Total");
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

            db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("CurrentUser")
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