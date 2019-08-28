package ru.artemsh.touristRoutes.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowplaceAdapter extends RecyclerView.Adapter<ShowplaceViewHolder> implements com.honeyneutrons.undoswipe.helper.ItemTouchHelperAdapter {
    private final Context context;

    public ShowplaceAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShowplaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowplaceViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {

    }
}
