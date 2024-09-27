package com.example.firebaseapp.Ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.internal.ApiFeature;

import com.example.firebaseapp.Adapter.ProductAdapter;
import com.example.firebaseapp.Model.Product;
import com.example.firebaseapp.R;
import com.example.firebaseapp.fragment.CartFragment;
import com.example.firebaseapp.fragment.CurrentOrdersFragment;
import com.example.firebaseapp.fragment.DeliveredOrdersFragment;
import com.example.firebaseapp.fragment.FavouriteFragment;
import com.example.firebaseapp.fragment.HomeFragment;
import com.example.firebaseapp.fragment.ProfileFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView ivOPenDrawer;
    TextView tvName, tvEmail;
    FirebaseAuth mAuth;
    FirebaseUser user;
    BottomNavigationView btNavigation;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_PROFILE_IMAGE_URI = "profile_image_uri";
    String userId;
    CircleImageView ci1;
    private static final int PICK_IMAGE_REQUEST = 100;
    private ArrayList<Product> productList;
    private ProductAdapter productAdapter;
    FirebaseFirestore db;
    SearchView searchView;
    AdView adView;
    InterstitialAd mInterstitialAd;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });


        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, getApplicationContext(), false);

        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);

        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Resources resources = getResources();
        GridItemDecoration itemDecoration = new GridItemDecoration(resources);

        productRecyclerView.addItemDecoration(itemDecoration);
        productRecyclerView.setAdapter(productAdapter);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        userId = mAuth.getCurrentUser().getUid();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        ivOPenDrawer = findViewById(R.id.ivOpenDrawer);
        btNavigation = findViewById(R.id.btNavigationView);
        searchView = findViewById(R.id.searchView);

        tvName = navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        ci1 = navigationView.getHeaderView(0).findViewById(R.id.ci1);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String savedImageUri = sharedPreferences.getString(KEY_PROFILE_IMAGE_URI, null);
        if (savedImageUri != null) {
            Uri imageUri = Uri.parse(savedImageUri);
            ci1.setImageURI(imageUri);
        }

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Map<String, Object> data = task.getResult().getData();
                        if (data != null) {
                            String email = (String) data.get("Email");
                            String name = (String) data.get("Name");
                            tvEmail.setText(email);
                            tvName.setText(name);
                        }
                    } else {
                        Log.d(TAG, "Error getting user data: ", task.getException());
                    }
                });

        btNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Home) {
                    setFragment(new HomeFragment(), true);
                } else if (item.getItemId() == R.id.cart) {
                    setFragment(new CartFragment(), false);
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivity2.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                } else if (item.getItemId() == R.id.favourite) {
                    setFragment(new FavouriteFragment(), false);
                } else {
                    setFragment(new ProfileFragment(), false);
                }
                return true;
            }
        });
        btNavigation.setSelectedItemId(R.id.Home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_current_orders) {
                    setFragment(new CurrentOrdersFragment(), false);

                } else if (id == R.id.nav_delivered_orders) {
                    setFragment(new DeliveredOrdersFragment(), false);

                } else {
                    FirebaseAuth.getInstance().signOut();
                    clearUserData();
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ivOPenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isOpen()) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        ci1.setOnClickListener(v -> openGallery());

        if (getIntent().getBooleanExtra("openCart", false)) {
            setFragment(new CartFragment(), false);
        }
    }

    private void clearUserData() {
        tvName.setText("");
        tvEmail.setText("");
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ci1.setImageURI(selectedImageUri);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_PROFILE_IMAGE_URI, selectedImageUri.toString());
            editor.apply();
        }
    }

    private void setFragment(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

        if (flag) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ivOPenDrawer.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            ivOPenDrawer.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
        }
    }
}
