package com.myqsc.mobile2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.myqsc.mobile2.fragment.ZoomOutPageTransformer;
import com.myqsc.mobile2.uti.BroadcastHelper;
import com.myqsc.mobile2.uti.Utility;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by richard on 3/27/2014.
 */
public class UserGuideActivity extends Activity {
    private ViewPager mViewPager = null;
    private WeakReference<Bitmap> weakReference[];
    private Handler handler = new Handler();
    AssetManager assetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        assetManager = getAssets();
        weakReference = new WeakReference[4];

        final PagerAdapter myPagerAdapter = new myViewPagerAdapter(this);
        mViewPager = (ViewPager) findViewById(R.id.activity_guide_viewpager);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setBackgroundDrawable(getResources().getDrawable(R.drawable.vpage_back));
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserGuideActivity.this, "即将开始……", Toast.LENGTH_SHORT).show();
                        }
                    });
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 5000);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SharedPreferences preferences = getSharedPreferences(Utility.PREFERENCE, 0);
        preferences.edit().putBoolean(BroadcastHelper.BROADCAST_GUIDE, false).commit();
    }

    private Bitmap getBitmap(int i) {
        if (weakReference[i] == null || weakReference[i].get() == null) {
            try {
                final Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open("guide_" + (i + 1) + ".png"));
                weakReference[i] = new WeakReference(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return weakReference[i].get();
    }

    private class myViewPagerAdapter extends PagerAdapter {
        final Context context;

        public myViewPagerAdapter(final Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(getBitmap(position));
            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
