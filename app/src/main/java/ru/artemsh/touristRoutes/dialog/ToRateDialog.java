package ru.artemsh.touristRoutes.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.helper.ICallback;
import ru.artemsh.touristRoutes.helper.ICallbackObject;

public class ToRateDialog extends Dialog implements View.OnClickListener {

    private Button ok, back;
    private Activity activity;
    private ICallbackObject callback;
    private ImageView[] stars;
    private Integer countStars = 0;

    public ToRateDialog(@NonNull Activity activity, ICallbackObject callback) {
        super(activity);
        this.activity = activity;
        this.callback = callback;
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_to_rate);
        stars = new ImageView[]{
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5),
        };
        for (int i=0;i<stars.length;i++){
            int finalI = i;
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int a = 0; a < stars.length; a++){
                        stars[a].setImageResource(R.drawable.star_empty);
                    }
                    for (int a = 0; a < finalI +1; a++){
                        stars[a].setImageResource(R.drawable.star);
                    }
                    countStars = finalI + 1;
                }
            });
        }
        ok = (Button) findViewById(R.id.but_ok);
        back = (Button) findViewById(R.id.but_back);
        ok.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.but_ok:
                if (callback != null)
                    callback.request(countStars);
                dismiss();
                break;
            case R.id.but_back:
                dismiss();
                break;
        }
    }
}
