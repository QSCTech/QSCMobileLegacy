package com.myqsc.qscmobile2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.myqsc.qscmobile2.R;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by richard on 13-9-10.
 */
public class LoadFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.loading_image);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView,
                "rotation", -360f);
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatMode(Animation.INFINITE);
        objectAnimator.start();
        return view;
    }
}
