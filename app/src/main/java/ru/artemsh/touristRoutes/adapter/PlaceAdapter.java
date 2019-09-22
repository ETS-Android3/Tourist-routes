package ru.artemsh.touristRoutes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.createShowplace.SetTimeDialog;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.ItemTouchHelperAdapter;
import ru.artemsh.touristRoutes.model.Showplace;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private IDatabase database;
    private FragmentActivity activity;
    private List<Showplace> showplaces;

    public PlaceAdapter(FragmentActivity activity, IDatabase database) {
        this.activity = activity;
        this.database = database;
        showplaces = database.getPlaceAll();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceViewHolder holder = new PlaceViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_place, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.name.setText(showplaces.get(position).getTitle());
        holder.descr.setText(showplaces.get(position).getDescription());

        if (holder.mapView != null) {
            holder.mapView.onCreate(null);
            holder.mapView.getMapAsync(holder);
            holder.setLocation(showplaces.get(position).getLatLng());
        }

        if (showplaces.get(position).getMonday()!=null){
            holder.monday.setText(showplaces.get(position).getMonday().getButtonStr());
        }

        if (showplaces.get(position).getTuesday()!=null){
            holder.tuesday.setText(showplaces.get(position).getTuesday().getButtonStr());
        }

        if (showplaces.get(position).getWednesday()!=null){
            holder.wednesday.setText(showplaces.get(position).getWednesday().getButtonStr());
        }

        if (showplaces.get(position).getThursday()!=null){
            holder.thursday.setText(showplaces.get(position).getThursday().getButtonStr());
        }

        if (showplaces.get(position).getFriday()!=null){
            holder.friday.setText(showplaces.get(position).getFriday().getButtonStr());
        }

        if (showplaces.get(position).getSaturday()!=null){
            holder.saturday.setText(showplaces.get(position).getSaturday().getButtonStr());
        }

        if (showplaces.get(position).getSunday()!=null){
            holder.sunday.setText(showplaces.get(position).getSunday().getButtonStr());
        }

        if (showplaces.get(position).getRaiting() !=null){
            for (int i=0;i<showplaces.get(position).getRaiting();i++){
                holder.stars[i].setImageResource(R.drawable.star);
            }
        }

        holder.time.setOnClickListener(new View.OnClickListener() {
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
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }, showplaces.get(position));
                dialog.show(activity.getSupportFragmentManager(), "Set time");
            }
        });

        holder.countImages.setText(showplaces.get(position).getNamePhoto().size()+"");
        for (int i=0;i<showplaces.get(position).getItemTasks().size();i++) {
            View view = View.inflate(activity, R.layout.item_task, null);

            switch (showplaces.get(position).getItemTasks().get(i).getStatusTask()){
                case TERRIBLY:
                    ((ImageView)view.findViewById(R.id.image_view)).setImageResource(R.drawable.thumb_down);
                    break;
                case SUCCESFULLY:
                    ((ImageView)view.findViewById(R.id.image_view)).setImageResource(R.drawable.thumb_up);
                    break;
                case WAITING:
                    default:
//                        ((ImageView)view.findViewById(R.id.image_view)).setImageResource(R.drawable.schedule);
            }

            ((EditText)view.findViewById(R.id.edit_text)).setText(showplaces.get(position).getItemTasks().get(i).getTas());
            ((TextView)view.findViewById(R.id.number_element)).setText(i+".");

            holder.listTasks.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return showplaces.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        final MapView mapView;
        private LatLng latLng = null;
        private GoogleMap map = null;

        final Button time;
        final LinearLayout imagesLayout;
        final TextView countImages;
        final TextView name;
        final TextView descr;

        final Button monday;
        final Button tuesday;
        final Button wednesday;
        final Button thursday;
        final Button friday;
        final Button saturday;
        final Button sunday;

        final ImageView[] stars;

        final LinearLayout listTasks;

        final TextView endWork;

        void setLocation(LatLng latLng){
            this.latLng = latLng;
            if (map!=null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.8f));
                map.addMarker(new MarkerOptions().position(latLng));
            }
        }

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            mapView = itemView.findViewById(R.id.map);
            time = itemView.findViewById(R.id.but_time);
            imagesLayout = itemView.findViewById(R.id.images_layout);
            countImages = itemView.findViewById(R.id.count_images);
            name = itemView.findViewById(R.id.title_name);
            
            descr = itemView.findViewById(R.id.title_description);
            
            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);
            saturday = itemView.findViewById(R.id.saturday);
            sunday = itemView.findViewById(R.id.sunday);

            listTasks = itemView.findViewById(R.id.list_tasks);
            endWork = itemView.findViewById(R.id.end_work);

            stars = new ImageView[]{
                    itemView.findViewById(R.id.star1),
                    itemView.findViewById(R.id.star2),
                    itemView.findViewById(R.id.star3),
                    itemView.findViewById(R.id.star4),
                    itemView.findViewById(R.id.star5)
            };
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
