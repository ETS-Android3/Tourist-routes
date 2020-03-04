package ru.artemsh.touristRoutes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import ru.artemsh.touristRoutes.dialog.DeletePlaceDialog;
import ru.artemsh.touristRoutes.dialog.ToRateDialog;
import ru.artemsh.touristRoutes.helper.ICallback;
import ru.artemsh.touristRoutes.helper.ICallbackObject;
import ru.artemsh.touristRoutes.helper.ICallbackShowPlace;
import ru.artemsh.touristRoutes.helper.ItemTouchHelperAdapter;
import ru.artemsh.touristRoutes.model.Showplace;

public class ShowplaceAdapter extends RecyclerView.Adapter<ShowplaceAdapter.PlaceViewHolder> implements ItemTouchHelperAdapter {

    private IDatabase database;
    private List<Showplace> showplaces;
    private final Fragment mContext;
    private ShowplaceAdapter showplaceAdapter;

    private ICallbackShowPlace callback;

    public ShowplaceAdapter(Fragment context, IDatabase database) {
        this.database = database;
        showplaces = database.getShowplaceAll();
        mContext = context;
        showplaceAdapter = this;
    }

    public ShowplaceAdapter(Fragment context, IDatabase database, ICallbackShowPlace callback) {
        this.database = database;
        showplaces = database.getShowplaceAll();
        mContext = context;
        showplaceAdapter = this;
        this.callback = callback;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(LayoutInflater.from(mContext.getContext())
                .inflate(R.layout.item_showplace, parent, false));
    }

    public void update(Integer id, Integer position){
        for(int i=0;i<showplaces.size();i++){
            if (showplaces.get(i).getId() == id){
                showplaces.get(i).setNumberOrder(position);
                database.update(showplaces.get(i));
                return;
            }
        }
    }

    private Showplace getNumberElement(int position){
        for (int i=0;i<showplaces.size();i++){
            if (showplaces.get(i).getNumberOrder()==null)
                continue;
            if (showplaces.get(i).getNumberOrder() == position + 1)
                return showplaces.get(i);
        }
        for (int i=0;i<showplaces.size();i++){
            if (showplaces.get(i).getNumberOrder() == null){
                showplaces.get(i).setNumberOrder(position + 1);
                database.update(showplaces.get(i));
                return showplaces.get(i);
            }
        }
        int i=1;
        while (true){
            for (int a=0;a<showplaces.size();a++){
                if (position+i==showplaces.get(a).getNumberOrder()){
                    showplaces.get(a).setNumberOrder(position+1);
                    database.update(showplaces.get(a));
                    return showplaces.get(a);
                }
            }
            i++;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Showplace showplace = getNumberElement(position);
        if (showplace.getDescription()!=null){
            holder.descr.setText(showplace.getDescription());
        }else{
            holder.descr.setText("");
        }

        if (showplace.getTitle()!=null){
            holder.title.setText(showplace.getTitle());
        }else{
            holder.title.setText("");
        }

        if (showplace.getMonday()!=null){
            holder.monday.setText(showplace.getMonday().getButtonStr());
        }

        if (showplace.getTuesday()!=null){
            holder.tuesday.setText(showplace.getTuesday().getButtonStr());
        }

        if (showplace.getWednesday()!=null){
            holder.wednesday.setText(showplace.getWednesday().getButtonStr());
        }

        if (showplace.getThursday()!=null){
            holder.thursday.setText(showplace.getThursday().getButtonStr());
        }

        if (showplace.getFriday()!=null){
            holder.friday.setText(showplace.getFriday().getButtonStr());
        }

        if (showplace.getSaturday()!=null){
            holder.saturday.setText(showplace.getSaturday().getButtonStr());
        }

        if (showplace.getSunday()!=null){
            holder.sunday.setText(showplace.getSunday().getButtonStr());
        }

        holder.value.setText(String.valueOf(showplace.getNumberOrder()));
        holder.id.setText(String.valueOf(showplace.getId()));

        if (holder.mapView != null) {
            holder.mapView.onCreate(null);
            holder.mapView.getMapAsync(holder);
            holder.setLocation(showplace.getLatLng());
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletePlaceDialog deletePlaceDialog = new DeletePlaceDialog(mContext.getActivity(), new ICallback() {
                    @Override
                    public void request() {
                        database.delete(showplace);
                        callback.request(showplaceAdapter);
                        showplaces = database.getShowplaceAll();
//                        saveAll();
                        notifyDataSetChanged();
                    }
                });
                deletePlaceDialog.show();
            }
        });

        holder.visitThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToRateDialog dialog = new ToRateDialog(mContext.getActivity(), new ICallbackObject() {
                    @Override
                    public void request(Object object) {
                        showplace.setPlace(Showplace.TypePlace.PLACE);
                        showplace.setRaiting((Integer)object);
                        database.addShowplace(showplace);
                        callback.request(showplaceAdapter);
                        showplaces = database.getShowplaceAll();

//                        updateList(showplace.getNumberOrder());
                        notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });

        holder.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTimeDialog dialog = new SetTimeDialog(new SetTimeDialog.NoticeDialogListener(){

                    @Override
                    public void onDialogPositiveClick(SetTimeDialog dialog) {
                        Showplace show = dialog.getSchedule();

                        showplace.setMonday(show.getMonday());
                        showplace.setTuesday(show.getTuesday());
                        showplace.setWednesday(show.getWednesday());
                        showplace.setThursday(show.getThursday());
                        showplace.setFriday(show.getFriday());
                        showplace.setSaturday(show.getSaturday());
                        showplace.setSunday(show.getSunday());

                        database.update(showplace);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }, showplace);
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
        this.notifyItemRemoved(position-1);
        this.notifyDataSetChanged();
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
        callback.request(showplaceAdapter);
    }

    public void setPosition(String idStr, int position) {
        int id = Integer.parseInt(idStr);
        for (int i=0;i<showplaces.size();i++){
            if (showplaces.get(i).getId()==id){
                showplaces.get(i).setNumberOrder(position);
                database.update(showplaces.get(i));
            }
        }
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
        final TextView id;
        final Button delete;
        final Button visitThere;

        final Button monday;
        final Button tuesday;
        final Button wednesday;
        final Button thursday;
        final Button friday;
        final Button saturday;
        final Button sunday;

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
            id = itemView.findViewById(R.id.id);

            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);
            saturday = itemView.findViewById(R.id.saturday);
            sunday = itemView.findViewById(R.id.sunday);
        }

        void setLocation(LatLng latLng){
            this.latLng = latLng;
            if (map!=null){
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
