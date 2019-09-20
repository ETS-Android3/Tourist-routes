package ru.artemsh.touristRoutes.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.SetTimeDialog;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.ItemTouchHelperAdapter;
import ru.artemsh.touristRoutes.model.Showplace;

public class ShowplaceAdapter extends RecyclerView.Adapter<ShowplaceAdapter.PlaceViewHolder> implements ItemTouchHelperAdapter {

    private IDatabase database;
    private List<Showplace> showplaces;
    private final Fragment mContext;

    public ShowplaceAdapter(Fragment context, IDatabase database) {
        this.database = database;
        showplaces = database.getShowplaceAll();
        mContext = context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(LayoutInflater.from(mContext.getContext())
                .inflate(R.layout.item_showplace, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        //Прописать все методы!!
        holder.descr.setText(showplaces.get(position).getDescription());
        holder.title.setText(showplaces.get(position).getTitle());
        holder.endWork.setText("Время до конца");

        if (showplaces.get(position).getNumberOrder()==null){
            showplaces.get(position).setNumberOrder(position + 1);
            holder.value.setText(String.valueOf(position + 1));
            database.addShowplace(showplaces.get(position));
        }else{
            holder.value.setText(String.valueOf(showplaces.get(position).getNumberOrder()));
        }

        if (holder.mapView != null) {
            holder.mapView.onCreate(null);
            holder.mapView.getMapAsync(holder);
            holder.setLocation(showplaces.get(position).getLatLng());
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.delete(showplaces.get(position).getId());
                //Update list
            }
        });

        holder.visitThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showplaces.get(position).setPlace(Showplace.TypePlace.PLACE);
                database.addPlace(showplaces.get(position));
                //Update list
            }
        });

        holder.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeDialog dialog = new SetTimeDialog(new SetTimeDialog.NoticeDialogListener(){

                    @Override
                    public void onDialogPositiveClick(SetTimeDialog dialog) {
                        Showplace show = dialog.getSchedule();

                        showplaces.get(position).setMonday(show.getMonday());
                        showplaces.get(position).setTuesday(show.getTuesday());
                        showplaces.get(position).setWednesday(show.getWednesday());
                        showplaces.get(position).setThursday(show.getThursday());
                        showplaces.get(position).setFriday(show.getFriday());
                        showplaces.get(position).setSaturday(show.getSaturday());
                        showplaces.get(position).setSunday(show.getSunday());

                        database.addShowplace(showplaces.get(position));
                        dialog.dismiss();
                    }
                }, showplaces.get(position));
                dialog.show(mContext.getFragmentManager(), "Set time");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (showplaces==null)
            return 0;
        return showplaces.size();
    }

    @Override
    public void onItemDismiss(int position) {
        showplaces.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(showplaces, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(showplaces, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        final MapView mapView;
        private LatLng latLng = null;
        private GoogleMap map = null;

        final Button setTime;
        final TextView title;
        final TextView descr;
        final TextView endWork;
        final TextView value;
        final Button delete;
        final Button visitThere;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            mapView = itemView.findViewById(R.id.map);
            title = itemView.findViewById(R.id.title_name);
            descr = itemView.findViewById(R.id.title_description);
            endWork = itemView.findViewById(R.id.end_work);
            setTime = itemView.findViewById(R.id.but_time);
            value = itemView.findViewById(R.id.set_value);
            delete = itemView.findViewById(R.id.delete);
            visitThere = itemView.findViewById(R.id.visit_there);
        }

        void setLocation(LatLng latLng){
            this.latLng = latLng;
            if (map!=null){
                System.out.println("setPos="+latLng.toString());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.8f));
                map.addMarker(new MarkerOptions().position(latLng));
            }
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            this.map = googleMap;
            if (latLng!=null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.8f));
                map.addMarker(new MarkerOptions().position(latLng));
            }
        }
    }
}
