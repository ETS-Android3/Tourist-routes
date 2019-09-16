package ru.artemsh.touristRoutes.main;


import android.Manifest;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.CreateShowplaceBottomFragment;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.database.LocalDatabase;
import ru.artemsh.touristRoutes.map.CustomMarkerClusteringDemoActivity;
import ru.artemsh.touristRoutes.showplace.ShowplaceFragment;
import ru.artemsh.touristRoutes.wasPlace.WasPlaceFramgent;


public class MainActivity extends AppCompatActivity {

    private Fragment showplace;
    private Fragment wasPlace;
    private Fragment map;

    private BottomNavigationView navView;
    private FloatingActionButton actionButton;

    private IDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        database = new LocalDatabase();

        showplace = new ShowplaceFragment(database);
        wasPlace = new WasPlaceFramgent(database);
        map = new CustomMarkerClusteringDemoActivity();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_map);

        actionButton = findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateShowplaceBottomFragment bottomSheetDialog = CreateShowplaceBottomFragment.getInstance(null, database);
                bottomSheetDialog.show(getSupportFragmentManager(), getResources().getString(R.string.title_create_showplace));
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {//getString(R.string.title_showplace)
                case R.id.navigation_showplace:
                    replaceFragment(showplace);
                    return true;
                case R.id.navigation_was_place:
                    replaceFragment(wasPlace);
                    return true;
                case R.id.navigation_map:
                    replaceFragment(map);
                    return true;
            }
            return false;
        }
    };

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
        fragmentTransaction.commit();
    }
}
