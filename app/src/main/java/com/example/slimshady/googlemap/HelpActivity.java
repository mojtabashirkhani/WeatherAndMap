package com.example.slimshady.googlemap;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slimshady.googlemap.Slide_Adapter.HelpAdapter;

public class HelpActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private HelpAdapter helpAdapter;
    private TextView[] mDots;
   // private Button skip;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

       // skip=(Button)findViewById(R.id.skip);
        viewPager=(ViewPager) findViewById(R.id.view_pager_help);
        linearLayout=(LinearLayout)findViewById(R.id.dots_help);
        helpAdapter= new HelpAdapter(HelpActivity.this);
        viewPager.setAdapter(helpAdapter);
        buildDots(0);
       viewPager.addOnPageChangeListener(viewPagerChange);

    }

    private void buildDots(int position) {

        mDots=new TextView[2];
        linearLayout.removeAllViews();

        for(int i =0; i<mDots.length; i++){

            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextColor(getResources().getColor(R.color.white_transparent));
            mDots[i].setTextSize(35);

            linearLayout.addView(mDots[i]);
        }

        if(mDots.length>0){

            mDots[position].setTextColor(getResources().getColor(R.color.viewBg));
        }
    }

    ViewPager.OnPageChangeListener viewPagerChange= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {


            buildDots(position);
            mCurrentPage=position;

           /* if(position==mDots.length-1){

                skip.setEnabled(true);
                skip.setText("Finish");
                Toast.makeText(getApplicationContext(),"لطفا بر روی"+" Finish "+"کلیک کنید",Toast.LENGTH_SHORT).show();
            }
            else {
                skip.setText("Skip");
                skip.setEnabled(true);
            }*/

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
