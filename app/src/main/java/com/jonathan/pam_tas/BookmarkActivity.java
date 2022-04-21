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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.jonathan.pam_tas.adapters.BookmarkAdapter;
import com.jonathan.pam_tas.adapters.ProductAdapter;
import com.jonathan.pam_tas.models.BookmarkModel;
import com.jonathan.pam_tas.models.CartModel;
import com.jonathan.pam_tas.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ImageView profileImage;
    List<BookmarkModel> bookmarkModelList;
    BookmarkAdapter bookmarkAdapter;
    RecyclerView bookmarkRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        profileImage = findViewById(R.id.profileImage);
        bookmarkRecycler = findViewById(R.id.recyclerView);

        bookmarkRecycler.setLayoutManager(new GridLayoutManager(this,2));
        bookmarkModelList = new ArrayList<>();
        bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this,bookmarkModelList);
        bookmarkRecycler.setAdapter(bookmarkAdapter);

        setTopBar();



        db.collection("UserData").document(auth.getCurrentUser().getUid())
                .collection("Liked Product")
                .whereEqualTo("loved", "yes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BookmarkModel bookmarkModel = document.toObject(BookmarkModel.class);
                                bookmarkModelList.add(bookmarkModel);
                            }
                            bookmarkAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.bookmark_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bookmark_menu:
                        return true;
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
}