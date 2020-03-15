package ru.artemsh.touristRoutes.createShowplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.helper.ICallback;
import ru.artemsh.touristRoutes.helper.ICallbackRoute;
import ru.artemsh.touristRoutes.model.Showplace;

public class CreateShowplaceBottomFragment extends BottomSheetDialogFragment implements SetTimeDialog.NoticeDialogListener, OnMapReadyCallback {

    @BindView(R.id.title_name)
    EditText name;
    @BindView(R.id.descr)
    EditText descr;
    @BindView(R.id.list_tasks)
    LinearLayout listTasks;
    @BindView(R.id.map)
    MapView mapView;

    GoogleMap map;

    private static Showplace showplace = new Showplace();
    private static IDatabase iDatabase;
    private static ICallbackRoute callbackRoute;
    private static ICallback callback;


    public static CreateShowplaceBottomFragment getInstance(Showplace showplaceOld, IDatabase database, ICallback iCallback, ICallbackRoute iCallbackRoute) {
        if (showplaceOld != null){
            showplace  = showplaceOld;
        }else{
            showplace  = new Showplace();
        }
        iDatabase = database;
        callback = iCallback;
        callbackRoute = iCallbackRoute;
        return new CreateShowplaceBottomFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.create_showplace, container, false);
        ButterKnife.bind(this, view);
        name.setText(showplace.getTitle());
        descr.setText(showplace.getDescription());

        mapView.onCreate(null);
        mapView.getMapAsync(this);

        for (int i=0;i<showplace.getItemTasks().size();i++){
            View vv = View.inflate(getContext(), R.layout.item_task, null);

            switch (showplace.getItemTasks().get(i).getStatusTask()){
                case TERRIBLY:
                    ((ImageView)vv.findViewById(R.id.image_view)).setImageResource(R.drawable.thumb_down);
                    break;
                case SUCCESFULLY:
                    ((ImageView)vv.findViewById(R.id.image_view)).setImageResource(R.drawable.thumb_up);
                    break;
                case WAITING:
                default:
//                        ((ImageView)view.findViewById(R.id.image_view)).setImageResource(R.drawable.schedule);
            }
            ((EditText)vv.findViewById(R.id.edit_text)).setText(showplace.getItemTasks().get(i).getTas());
            ((TextView)vv.findViewById(R.id.number_element)).setText(i+".");

            listTasks.addView(vv);
        }

        return view;
    }

    @OnClick(R.id.save_but)
    void onClickSaveBut(){
        dismiss();
    }
    @OnClick(R.id.delete_but)
    void onClickDeleteBut(){
        if (showplace.getId()!=null){
            iDatabase.delete(showplace);
        }
        dismiss();
    }

    @OnClick(R.id.make_route_but)
    void onClickBuildRoute(){
        callbackRoute.request(showplace.getLat(), showplace.getLng());
    }

    @OnClick(R.id.but_time)
    void onCLickSetTime(){
        SetTimeDialog dialog = new SetTimeDialog(this, showplace);
        dialog.show(getFragmentManager(), "Set time");
    }

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

        dialog.dismiss();
    }

    @Override
    public void dismiss() {
        save();
        if (callback!=null)
            callback.request();
        super.dismiss();
    }

    void save(){
        showplace.setTitle(name.getText().toString());
        showplace.setDescription(descr.getText().toString());
        iDatabase.addShowplace(showplace);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setMapLocation();
    }

    private void setMapLocation() {
        if (map == null) return;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(showplace.getLatLng(), 12.8f));
        map.addMarker(new MarkerOptions().position(showplace.getLatLng()));

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public Showplace getShowplace(){
        return showplace;
    }
}
