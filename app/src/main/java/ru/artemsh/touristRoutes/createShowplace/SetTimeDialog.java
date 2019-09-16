package ru.artemsh.touristRoutes.createShowplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.model.Showplace;

public class SetTimeDialog extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(SetTimeDialog dialog);
    }

    public SetTimeDialog(NoticeDialogListener mListener, Showplace showplace) {
        this.mListener = mListener;

        monStart.setText(showplace.getMonday().getStartWork());
        monFinish.setText(showplace.getMonday().getFinishWork());

        tueStart.setText(showplace.getTuesday().getStartWork());
        tueFinish.setText(showplace.getTuesday().getFinishWork());

        wedStart.setText(showplace.getWednesday().getStartWork());
        wedFinish.setText(showplace.getWednesday().getFinishWork());

        thuStart.setText(showplace.getThursday().getStartWork());
        thuFinish.setText(showplace.getThursday().getFinishWork());

        friStart.setText(showplace.getFriday().getStartWork());
        friFinish.setText(showplace.getFriday().getFinishWork());

        satStart.setText(showplace.getSaturday().getStartWork());
        satFinish.setText(showplace.getSaturday().getFinishWork());

        sunStart.setText(showplace.getSunday().getStartWork());
        sunFinish.setText(showplace.getSunday().getFinishWork());
    }

    NoticeDialogListener mListener;

    @BindView(R.id.mon_start)
    TextView monStart;
    @BindView(R.id.mon_finish)
    TextView monFinish;

    @BindView(R.id.tue_start)
    TextView tueStart;
    @BindView(R.id.tue_finish)
    TextView tueFinish;

    @BindView(R.id.wed_start)
    TextView wedStart;
    @BindView(R.id.wed_finish)
    TextView wedFinish;

    @BindView(R.id.thu_start)
    TextView thuStart;
    @BindView(R.id.thu_finish)
    TextView thuFinish;

    @BindView(R.id.fri_start)
    TextView friStart;
    @BindView(R.id.fri_finish)
    TextView friFinish;

    @BindView(R.id.sat_start)
    TextView satStart;
    @BindView(R.id.sat_finish)
    TextView satFinish;

    @BindView(R.id.sun_start)
    TextView sunStart;
    @BindView(R.id.sun_finish)
    TextView sunFinish;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_set_time, null);
        builder.setView(view);
        ButterKnife.bind(this, view);
        return builder.create();
    }

    public Showplace getSchedule(){
        Showplace showplace = new Showplace();

        showplace.setMonday(new Showplace.WorkTime(monStart.getText().toString(), monFinish.getText().toString()));
        showplace.setTuesday(new Showplace.WorkTime(tueStart.getText().toString(), tueFinish.getText().toString()));
        showplace.setWednesday(new Showplace.WorkTime(wedStart.getText().toString(), wedFinish.getText().toString()));
        showplace.setThursday(new Showplace.WorkTime(thuStart.getText().toString(), thuFinish.getText().toString()));
        showplace.setFriday(new Showplace.WorkTime(friStart.getText().toString(), friFinish.getText().toString()));
        showplace.setSaturday(new Showplace.WorkTime(satStart.getText().toString(), satFinish.getText().toString()));
        showplace.setSunday(new Showplace.WorkTime(sunStart.getText().toString(), sunFinish.getText().toString()));

        return showplace;
    }


    @OnClick(R.id.save)
    void save(){
        mListener.onDialogPositiveClick(this);
    }


    @OnClick({R.id.mon,R.id.tue,R.id.thu,R.id.wed,R.id.fri,R.id.sun,R.id.sat})
    void setnullTime(View view){
        switch (view.getId()){
            case R.id.mon:
                monStart.setText(getResources().getText(R.string.dont_work));
                monFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.tue:
                tueStart.setText(getResources().getText(R.string.dont_work));
                tueFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.wed:
                wedStart.setText(getResources().getText(R.string.dont_work));
                wedFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.thu:
                thuStart.setText(getResources().getText(R.string.dont_work));
                thuFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.fri:
                friStart.setText(getResources().getText(R.string.dont_work));
                friFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.sat:
                satStart.setText(getResources().getText(R.string.dont_work));
                satFinish.setText(getResources().getText(R.string.dont_work));
                break;
            case R.id.sun:
                sunStart.setText(getResources().getText(R.string.dont_work));
                sunFinish.setText(getResources().getText(R.string.dont_work));
                break;
        }
    }

    @OnClick({R.id.mon_start,R.id.mon_finish,R.id.tue_start,R.id.tue_finish,R.id.wed_start,R.id.wed_finish,
            R.id.thu_start,R.id.thu_finish,R.id.fri_start,R.id.fri_finish,R.id.sat_start,R.id.sat_finish,R.id.sun_start,R.id.sun_finish})
    void setTime(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                ((TextView) view).setText(i+":"+i1);
            }
        }, 24, 60, true);
        timePickerDialog.show();
//        timePickerDialog.set
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
