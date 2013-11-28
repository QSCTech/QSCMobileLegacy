package com.myqsc.mobile2.homework;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myqsc.mobile2.R;
import com.myqsc.mobile2.fragment.MySwipeExitActivity;
import com.myqsc.mobile2.uti.AwesomeFontHelper;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by richard on 13-9-24.
 */
public class HomeworkActivity extends MySwipeExitActivity {

    TextView iconLeft, iconMiddle, iconRight;
    TextView nameLeft, nameMiddle, nameRight;

    int selected = 0;
    HomeworkHelper homeworkHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        homeworkHelper = new HomeworkHelper(this, (LinearLayout) findViewById(R.id.homework_activity_linear));

        InitTextViews();
        setColor();
        homeworkHelper.getMineHomework();
    }

    /**
     * 根据选中的图标设置字体颜色
     */
    private void setColor() {
        switch (selected) {
            case 0:
                iconLeft.setTextColor(getResources().getColor(R.color.blue_text));
                nameLeft.setTextColor(getResources().getColor(R.color.blue_text));
                iconMiddle.setTextColor(getResources().getColor(R.color.gray_text));
                nameMiddle.setTextColor(getResources().getColor(R.color.gray_text));
                iconRight.setTextColor(getResources().getColor(R.color.gray_text));
                nameRight.setTextColor(getResources().getColor(R.color.gray_text));
                break;
            case 1:
                iconLeft.setTextColor(getResources().getColor(R.color.gray_text));
                nameLeft.setTextColor(getResources().getColor(R.color.gray_text));
                iconMiddle.setTextColor(getResources().getColor(R.color.blue_text));
                nameMiddle.setTextColor(getResources().getColor(R.color.blue_text));
                iconRight.setTextColor(getResources().getColor(R.color.gray_text));
                nameRight.setTextColor(getResources().getColor(R.color.gray_text));
                break;
            case 2:
                iconLeft.setTextColor(getResources().getColor(R.color.gray_text));
                nameLeft.setTextColor(getResources().getColor(R.color.gray_text));
                iconMiddle.setTextColor(getResources().getColor(R.color.gray_text));
                nameMiddle.setTextColor(getResources().getColor(R.color.gray_text));
                iconRight.setTextColor(getResources().getColor(R.color.blue_text));
                nameRight.setTextColor(getResources().getColor(R.color.blue_text));
                break;
        }
    }

    /**
     * 初始化各个textview，设置字体
     */
    private void InitTextViews() {
        iconLeft = (TextView) findViewById(R.id.homework_activity_layout_left)
                .findViewById(R.id.icon);
        iconMiddle = (TextView) findViewById(R.id.homework_activity_layout_middle)
                .findViewById(R.id.icon);
        iconRight = (TextView) findViewById(R.id.homework_activity_layout_right)
                .findViewById(R.id.icon);

        nameLeft = (TextView) findViewById(R.id.homework_activity_layout_left)
                .findViewById(R.id.name);
        nameMiddle = (TextView) findViewById(R.id.homework_activity_layout_middle)
                .findViewById(R.id.name);
        nameRight = (TextView) findViewById(R.id.homework_activity_layout_right)
                .findViewById(R.id.name);

        AwesomeFontHelper.setFontFace(iconLeft, this);
        AwesomeFontHelper.setFontFace(iconMiddle, this);
        AwesomeFontHelper.setFontFace(iconRight, this);

        findViewById(R.id.homework_activity_layout_left)
                .setOnClickListener(onClickListener);
        findViewById(R.id.homework_activity_layout_middle)
                .setOnClickListener(onClickListener);
        findViewById(R.id.homework_activity_layout_right)
                .setOnClickListener(onClickListener);
    }

    /**
     * 点击监听器，用来切换3个功能
     */
    final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.homework_activity_layout_left:
                    if (selected != 0) {
                        selected = 0;
                        setColor();
                    }
                    homeworkHelper.getMineHomework();
                    break;
                case R.id.homework_activity_layout_middle:
                    if (selected != 1) {
                        selected = 1;
                        setColor();
                    }
                    homeworkHelper.getAllHomework();
                    break;
                case R.id.homework_activity_layout_right:
                    if (selected != 2) {
                        selected = 2;
                        setColor();
                    }
                    homeworkHelper.initAddViews();
                    break;
            }
        }
    };
}
