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
 * Created by slim shady on 06/16/2018.
 */

public class HelpAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public HelpAdapter(Context context) {
        this.context = context;
    }


    public  int[] slide_images_help ={
            R.drawable.ic_no_locations
            ,R.drawable.art_storm

    };

    public String[] headers_help={
            "آب و هوا","نقشه"

    };
    public String[] description_help={
            "در این قسمت برای زیبایی بیشتر برخی کلمات انگلیسی دیده میشود نظیر شرایط آب و هوا که البته آیکون مورد نظر به خوبی این را نشان میدهد .همچنین در نمودار هایی که در قسمت های پیشبینی هوا به صورت روزانه و ساعتی وجود دارد ،نشان دهنده ی دما میباشد. راجع به عکس هایی که مشاهده میکنید برای شلوغ نشدن صفحه تعبیه شده است و عکس قطره های آب نشان دهنده رطوبت هواست و عکس دیگر در سمت چپ تصویر سرعت باد را نشان میدهد و دو عکس دیگر که خورشید را نشان میدهد مربوط به  طلوع و غروب آفتاب میباشد.همچنین دو دمایی که علاوه بر دمای فعلی نشان داده میشود مربوط به بیشترین و کمترین مقدار دما میباشد",
            "در این قسمت آیکون هایی کنار صفحه مشاهده میکنید که هر کدام کار به خصوصی انجام میدهند به طور مثال آیکون جی پی اس که مکان فعلی کاربر را نشان میدهد، آیکون جهت که مسیر بین مکان فعلی کاربر و مکان انتخاب شده را نشان میدهد هم چنین با کلیک کردن بر روی مکان انتخاب شده، میتوانید مسافت و مدت زمان رسیدن به آن را بیابید. آیکون نقشه که مکان های نزدیک کاربر را به صورت لیست نشان میدهد و آیکون اینفو که اطلاعات مکان انتخاب شده نظیر شماره تماس - وب سایت - آدرس و ... را نشان میدهد. همچنین آیکون خورشید مکانی که انتخاب کردید یا با کلیک کردن بر روی نقشه یا جستجوی شهر مورد، نظر میتوانید اطلاعات آب و هوای آن مکان را بیابید"


    };



    @Override
    public int getCount() {
        return headers_help.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.help_slide,container,false);

        ImageView slideImageView=view.findViewById(R.id.img_pager_help);
        TextView slideHeaderTextView=view.findViewById(R.id.txtTitle_help);
        TextView slideDescTextView=view.findViewById(R.id.txtDesc_help);

        slideImageView.setImageResource(slide_images_help[position]);
        slideHeaderTextView.setText(headers_help[position]);
        slideDescTextView.setText(description_help[position]);

        container.addView(view);


        return view;
    }
}
