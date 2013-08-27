package com.myqsc.qscmobile2;

import java.util.ArrayList;
import java.util.List;

import com.myqsc.qscmobile2.fragment.CardFragment;
import com.myqsc.qscmobile2.fragment.EmptyFragment;
import com.myqsc.qscmobile2.fragment.MyFragmentPagerAdapter;
import com.myqsc.qscmobile2.fragment.cardlist.FunctionListFragment;
import com.myqsc.qscmobile2.login.UserSwitchFragment;
import com.myqsc.qscmobile2.uti.BroadcastHelper;
import com.myqsc.qscmobile2.uti.LogHelper;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import ViewPager.ZoomOutPageTransformer;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends FragmentActivity {

	final List<Fragment> fragmentList = new ArrayList<Fragment>();
	MyFragmentPagerAdapter adapter = null;
	ViewPager vPager = null;
	View fragmentView = null, gestureView = null;
	FragmentManager manager = null;
	DisplayMetrics dm = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		vPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
//		vPager.setPageTransformer(true, new ZoomOutPageTransformer());
		
		fragmentView = findViewById(R.id.activity_main_frame);
		gestureView = findViewById(R.id.activity_main_gesture);
		manager = getSupportFragmentManager();
		
		IntentFilter intentFilter = new IntentFilter(BroadcastHelper.BROADCAST_ONABOUTUS_CLICK);
		registerReceiver(new aboutusReceiver(), intentFilter);
		
		FragmentTransaction transaction = manager.beginTransaction();
		
		transaction.add(R.id.activity_main_frame, new EmptyFragment());
		transaction.commit();
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (manager.getBackStackEntryCount() != 0){
                ObjectAnimator animator = ObjectAnimator.ofFloat(fragmentView, "translationX", 0, dm.widthPixels)
                        .setDuration(300);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        manager.popBackStack();
                        ObjectAnimator.ofFloat(fragmentView, "translationX", 0)
                                .setDuration(1)
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animator.start();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
	protected void onStart() {
		super.onStart();
		
		final FunctionListFragment functionListFragment = new FunctionListFragment();
		
		final CardFragment cardFragment = new CardFragment();
		
		fragmentList.add(new UserSwitchFragment(this));
		fragmentList.add(functionListFragment);
		fragmentList.add(cardFragment);
		
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
		
		vPager.setAdapter(adapter);
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				getThisProcessMemeryInfo();
				handler.postDelayed(this, 1000);
			}
		});
		
		vPager.setOnTouchListener(onTouchListener);
		gestureView.setOnTouchListener(onTouchListener);
	}
	
	public void getThisProcessMemeryInfo() {
        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        int pid = android.os.Process.myPid();
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(new int[] {pid});
        LogHelper.i("内存使用：" + (int)memoryInfoArray[0].getTotalPrivateDirty() / 1024 + "mb");
    }

	@Override
	protected void onStop() {
		super.onStop();
		fragmentList.clear();
	}
	
	private class aboutusReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			FragmentTransaction transaction = manager.beginTransaction();
			
			AboutUsFragment fragment = new AboutUsFragment();
			transaction.replace(R.id.activity_main_frame, fragment);
			transaction.setCustomAnimations(R.anim.push_down_in, R.anim.push_down_out);
			findViewById(R.id.activity_main_frame).setVisibility(View.VISIBLE);
			transaction.addToBackStack(null);
			transaction.commit();

            ObjectAnimator animator = ObjectAnimator.ofFloat(fragmentView, "translationX", dm.widthPixels, 0f)
                    .setDuration(300);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
		}
	}
	
	final OnTouchListener onTouchListener = new OnTouchListener() {
		int beingScroll = 0;
		
		final int STATE_IDLE = 0;
		final int STATE_DRAGING = 1;
		final int STATE_SETTING = 2;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				LogHelper.d(event.toString());
				if (manager.getBackStackEntryCount() != 0 && event.getX() < 20) {
					beingScroll = STATE_DRAGING;
					return true;
				}
				break;
				
			case MotionEvent.ACTION_MOVE:
				LogHelper.d(event.toString());
				if (beingScroll == STATE_DRAGING){
					ObjectAnimator.ofFloat(fragmentView, "translationX", event.getX()).setDuration(0).start();
					gestureView.getBackground().setAlpha((int) (event.getX() / dm.widthPixels * 255));
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				LogHelper.d(event.toString());
				if (beingScroll == STATE_DRAGING){
					if ((int) event.getX() < dm.widthPixels / 3){
                        beingScroll = STATE_SETTING;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(fragmentView, "translationX",
                                0f);
                        animator.setDuration(100);
                        animator.setInterpolator(new DecelerateInterpolator());
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                beingScroll = STATE_IDLE;
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
						animator.start();

					} else {
                        beingScroll = STATE_SETTING;
                        ObjectAnimator animator = ObjectAnimator.ofFloat(fragmentView, "translationX",
                                (float) event.getX(), dm.widthPixels)
                                .setDuration(100);
                        animator.setInterpolator(new DecelerateInterpolator());
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                manager.popBackStack();
                                ObjectAnimator.ofFloat(fragmentView, "translationX",
                                        0).setDuration(1).start();
                                beingScroll = STATE_IDLE;
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        animator.start();
					}
                    return true;
				}
			default:
				break;
			}
			return false;
		}
	}; 
	
	
}
