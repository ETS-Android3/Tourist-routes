package ru.artemsh.touristRoutes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.helper.ICallback;

public class DeletePlaceDialog extends Dialog implements View.OnClickListener {

    private Button yes, no;
    private Activity activity;
    private ICallback callback;

    public DeletePlaceDialog(@NonNull Activity activity, ICallback callback) {
        super(activity);
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_place);
        yes = (Button) findViewById(R.id.but_yes);
        no = (Button) findViewById(R.id.but_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_yes:
                if (callback != null)
                    callback.request();
                dismiss();
                break;
            case R.id.but_no:
                dismiss();
                break;
        }
    }
}