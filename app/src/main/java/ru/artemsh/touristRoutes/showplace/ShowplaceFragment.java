package ru.artemsh.touristRoutes.showplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.adapter.ShowplaceAdapter;
import ru.artemsh.touristRoutes.database.DBHelper;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.ICallbackShowPlace;
import ru.artemsh.touristRoutes.helper.SimpleItemTouchHelperCallback;

public class ShowplaceFragment extends Fragment {
    private IDatabase database;
    private RecyclerView recycler = null;
    private ShowplaceAdapter adapter = null;
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_showplaces, container, false);

        database = DBHelper.initialization(getContext());

        recycler = view.findViewById(R.id.recycler);
        adapter = new ShowplaceAdapter(this, database, callback);
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
    ICallbackShowPlace callback = new ICallbackShowPlace() {
        @Override
        public void request(ShowplaceAdapter showplaceAdapter) {
            for (int i=0;i<adapter.getItemCount();i++){
                ((TextView)recycler.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.set_value)).setText(String.valueOf(i+1));
                showplaceAdapter.setPosition(((TextView)recycler.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.id)).getText().toString(),
                        i+1);
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
//        for (int i=0;i<adapter.getItemCount();i++){
//            adapter.update(Integer.parseInt(((TextView)recycler.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.id)).getText().toString()),
//                    Integer.parseInt(((TextView)recycler.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.set_value)).getText().toString()));
//        }
    }
}
