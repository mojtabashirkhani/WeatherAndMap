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

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<String> icon = new ArrayList<>();

    private Context context;

    public HourlyAdapter(Context context, ArrayList<String> time, ArrayList<String> icon ) {
        this.time = time;
        this.context = context;
        this.icon=icon;
    }

    @Override
    public HourlyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hourly,parent,false);
        return new HourlyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyAdapter.ViewHolder holder, int position) {
        holder.timeIcon.setText(time.get(position));

        ViewHolder weatherItemHolder = (ViewHolder) holder;


        Picasso.with(context).load("http://openweathermap.org/img/w/" + icon.get(position) + ".png")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(weatherItemHolder.icon);

    }

    @Override
    public int getItemCount() {
        return time.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeIcon;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            timeIcon = itemView.findViewById(R.id.hour);
            icon=itemView.findViewById(R.id.icon_hour);


        }
    }
}
