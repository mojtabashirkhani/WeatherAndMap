package com.example.slimshady.googlemap.Slide_Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.slimshady.googlemap.R;

/**
 * Created by slim shady on 06/06/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter (Context context){
        this.context=context;
    }
    public  int[] slide_images ={
   R.drawable.ic_no_locations
            ,R.drawable.art_storm
           , R.drawable.icons_question_mark
    };

    public String[] headers={
            "نقشه","آب و هوا","اطلاعات"

    };
    public String[] description={
            "در این نقشه امکاناتی نظیر جستجو ، مسیر یابی ، یافتن فاصله و زمان تا مکان مورد نظر ، نشان دادن اطلاعات مربوط به مکان انتخاب شده ، نشان دادن مکان هایی نظیر فروشگاه ها و پمپ بنزین ها و ... که پرکاربرد هستند و به راحتی میتوان این مکان ها را با دسترسی سریع در شعاع پنج کیلومتر پیدا کرد همچنین این امکان وجود دارد که تمامی مکان های موجود در نقشه در نزدیکی مکان مورد نظر را به صورت لیست مشاهده و انتخاب کرد",
            "در قسمت آب و هوا امکاناتی نظیر پیش بینی هوا در ساعات آینده و همچنین روز های آینده وجود دارد و همچنین میتوان اطلاعات آب و هوای مکان دلخواه خود را مشاهده کنید",
            "برای استفاده ی بهتر از این برنامه بهتر است قسمت راهنمای برنامه را مطالعه کنید . همچنین در این برنامه سعی شده که ظاهر مناسبی داشته باشد در عین حال از سرعت کاربر برای رسیدن به اطلاعات مورد نظرش نکاهد"

    };

    @Override
    public int getCount() {
        return headers.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_view,container,false);

        ImageView slideImageView=view.findViewById(R.id.img_pager);
        TextView slideHeaderTextView=view.findViewById(R.id.txtTitle);
        TextView slideDescTextView=view.findViewById(R.id.txtDesc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeaderTextView.setText(headers[position]);
        slideDescTextView.setText(description[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
