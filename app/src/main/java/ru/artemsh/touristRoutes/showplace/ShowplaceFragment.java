package ru.artemsh.touristRoutes.showplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.adapter.ShowplaceAdapter;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.SimpleItemTouchHelperCallback;

public class ShowplaceFragment extends Fragment {
    private IDatabase database;
    private RecyclerView recycler = null;
    private ShowplaceAdapter adapter = null;
    private View view = null;

    public ShowplaceFragment(IDatabase database) {
        this.database = database;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_showplaces, container, false);

        recycler = view.findViewById(R.id.recycler);
        adapter = new ShowplaceAdapter(this, database);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);

        return view;
    }
}
