package ru.artemsh.touristRoutes.map;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.database.DBHelper;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.MapCallback;

public class CustomMarkerFragment extends Fragment {

    private IDatabase database;
    private MapCallback mapCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);

        database = DBHelper.initialization(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapCallback = new MapCallback(getFragmentManager(), database, getContext());
        mapFragment.getMapAsync(mapCallback);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mapCallback.pause();
    }
}
