package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PAM_TAS);
        setContentView(R.layout.activity_main);

        ViewPager2 locationsViewPager = findViewById(R.id.locationsViewPager);

        List<TravelLocation> travelLocations = new ArrayList<>();

        TravelLocation travelLocationBali = new TravelLocation();
        travelLocationBali.imageURL = "https://images.unsplash.com/photo-1537996194471-e657df975ab4?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8YmFsaXxlbnwwfHwwfHw%3D&w=1000&q=80";
        travelLocationBali.title = "Bali";
        travelLocationBali.description = "Indonesia most beautiful island paradise. Temple, beach, seafood, everything that satisfy you much!";
        travelLocations.add(travelLocationBali);

        TravelLocation travelLocationBromo = new TravelLocation();
        travelLocationBromo.imageURL = "https://images.unsplash.com/photo-1505966309334-54eb8f9e3c48?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80";
        travelLocationBromo.title = "Bromo";
        travelLocationBromo.description = "Active volcano in the heart of Indonesia. A perfect place to hike such unique mountain!";
        travelLocations.add(travelLocationBromo);

        TravelLocation travelLocationRajaampat = new TravelLocation();
        travelLocationRajaampat.imageURL = "https://images.unsplash.com/photo-1601844075967-c1376c021732?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80";
        travelLocationRajaampat.title = "Raja Ampat";
        travelLocationRajaampat.description = "Little archipelago paradise with clear blue water. Diving, snorkling, and surfing at its peak!";
        travelLocations.add(travelLocationRajaampat);

        TravelLocation travelLocationLabuanbajo = new TravelLocation();
        travelLocationLabuanbajo.imageURL = "https://images.unsplash.com/photo-1602485530821-e1bb069498dd?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=734&q=80";
        travelLocationLabuanbajo.title = "Labuan Bajo";
        travelLocationLabuanbajo.description = "Adventure here with a lot of beautiful spot to hiking, fishing, and chill in the corner of beautiful beach.";
        travelLocations.add(travelLocationLabuanbajo);

        TravelLocation travelLocationBoalemo = new TravelLocation();
        travelLocationBoalemo.imageURL = "https://images.unsplash.com/photo-1605852967399-59aaa3e7ef19?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80";
        travelLocationBoalemo.title = "Boalemo";
        travelLocationBoalemo.description = "Resort regency in Gorontalo, placed around a big open ocean.";
        travelLocations.add(travelLocationBoalemo);

        locationsViewPager.setAdapter(new TravelLocationAdapter(travelLocations));

        locationsViewPager.setClipToPadding(false);
        locationsViewPager.setClipChildren(false);
        locationsViewPager.setOffscreenPageLimit(3);
        locationsViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.05f);
            }
        });

        locationsViewPager.setPageTransformer(compositePageTransformer);

        // Bottom navigation bar

    }
}