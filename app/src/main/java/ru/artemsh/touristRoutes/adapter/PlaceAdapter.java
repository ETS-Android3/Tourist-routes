package ru.artemsh.touristRoutes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.artemsh.touristRoutes.R;
import ru.artemsh.touristRoutes.database.IDatabase;
import ru.artemsh.touristRoutes.model.Showplace;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private IDatabase database;
    private List<Showplace> showplaces;
    private final Context mContext;

    public PlaceAdapter(Context context, IDatabase database) {
        this.database = database;
        showplaces = database.getPlaceAll();
        mContext = context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_showplace, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        //Прописать все методы!!
        holder.descr.setText(showplaces.get(position).getDescription());
        holder.title.setText(showplaces.get(position).getTitle());
        holder.endWork.setText("Время до конца");
        holder.setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Показ время до конца", Toast.LENGTH_SHORT).show();
            }
        });
        holder.fullText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Весь текст", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (showplaces==null)
            return 0;
        return showplaces.size();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder{

        final View map;
        final Button setTime;
        final TextView title;
        final TextView descr;
        final TextView fullText;
        final TextView endWork;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            map = (View) itemView.findViewById(R.id.map);
            title = (TextView) itemView.findViewById(R.id.title_name);
            descr = (TextView) itemView.findViewById(R.id.title_name);
            fullText = (TextView) itemView.findViewById(R.id.title_name);
            endWork = (TextView) itemView.findViewById(R.id.title_name);
            setTime = (Button) itemView.findViewById(R.id.but_time);
        }
    }
}
