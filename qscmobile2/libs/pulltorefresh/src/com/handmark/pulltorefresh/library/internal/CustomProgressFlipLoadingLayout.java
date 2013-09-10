package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 * Uses a custom icon to show loading state instead of the indetermindate ProgressBar.
 * 
 * @author Will Hou (will@ezi.am)
 * @date Nov 18, 2012
 */
public class CustomProgressFlipLoadingLayout extends FlipLoadingLayout {
    
    private ImageView mCustomImageView;
    private Animation mSpinAnimation;

    public CustomProgressFlipLoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        
        mSpinAnimation = new RotateAnimation(-359, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mSpinAnimation.setInterpolator(new LinearInterpolator());
        mSpinAnimation.setRepeatMode(Animation.INFINITE);
        mSpinAnimation.setRepeatCount(Animation.INFINITE);
        mSpinAnimation.setDuration(500);

        mCustomImageView = (ImageView) findViewById(R.id.pull_to_refresh_progress_custom);
        
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrCustomProgressDrawable)) {
            Drawable src = attrs.getDrawable(R.styleable.PullToRefresh_ptrCustomProgressDrawable);
            mCustomImageView.setImageDrawable(src);
        } else {
            throw new IllegalArgumentException("Need to provide a custom drawable in XML to use CustomFlipLoadingLayout");
        }
    }

    @Override
    protected void refreshingImpl() {
        super.refreshingImpl();
        mHeaderProgress.setVisibility(View.GONE);
        if (mCustomImageView != null) {
            mCustomImageView.setVisibility(View.VISIBLE);
            mCustomImageView.startAnimation(mSpinAnimation);
        }
    }
    
    @Override
    protected void resetImpl() {
        super.resetImpl();
        mHeaderProgress.setVisibility(View.GONE);
        if (mCustomImageView != null) {
            mCustomImageView.clearAnimation();
            mCustomImageView.setVisibility(View.GONE);
        }
    }
}