package com.example.slimshady.googlemap.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slimshady.googlemap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by slim shady on 06/12/2018.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> icon = new ArrayList<>();
    private ArrayList<String> hour=new ArrayList<>();

    private Context context;

    public DayAdapter(Context context, ArrayList<String> icon, ArrayList<String> time, ArrayList<String> hour) {
        this.time = time;
        this.icon = icon;
        this.context = context;
        this.hour=hour;
    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_day,parent,false);
        return new DayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayAdapter.ViewHolder holder, int position) {

       holder.timeIcon.setText(time.get(position));
       holder.hourIcon.setText(hour.get(position));

        DayAdapter.ViewHolder weatherItemHolder = (DayAdapter.ViewHolder) holder;


        Picasso.with(context).load("http://openweathermap.org/img/w/" + icon.get(position) + ".png")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(weatherItemHolder.icon);

    }

    @Override
    public int getItemCount() {
        return icon.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeIcon,hourIcon;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            timeIcon = itemView.findViewById(R.id.txtDay);
            icon=itemView.findViewById(R.id.icon_day);
            hourIcon=itemView.findViewById(R.id.hour_day);

        }
    }
}
