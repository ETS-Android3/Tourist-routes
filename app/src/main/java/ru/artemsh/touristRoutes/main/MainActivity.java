package ru.artemsh.touristRoutes.main;


import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.map.MapFragment;
import ru.artemsh.touristRoutes.showplace.ShowplaceFragment;
import ru.artemsh.touristRoutes.wasPlace.WasPlaceFramgent;

public class MainActivity extends AppCompatActivity {

    private Fragment showplace;
    private Fragment wasPlace;
    private Fragment map;

    private BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showplace = new ShowplaceFragment();
        wasPlace = new WasPlaceFramgent();
        map = new MapFragment();
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {//getString(R.string.title_showplace)
                case R.id.navigation_showplace:

                    return true;
                case R.id.navigation_was_place:

                    return true;
                case R.id.navigation_map:

                    return true;
            }
            return false;
        }
    };

    private void setDefaultFragment(Fragment defaultFragment)
    {
        this.replaceFragment(defaultFragment);
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
