package ru.artemsh.touristRoutes.wasPlace;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.adapter.PlaceAdapter;
import ru.artemsh.touristRoutes.database.IDatabase;

public class WasPlaceFramgent extends Fragment {
    private IDatabase database;

    public WasPlaceFramgent(IDatabase database) {
        this.database = database;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showplaces, null);
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setAdapter(new PlaceAdapter(getContext(), database));
        return view;
    }
}
