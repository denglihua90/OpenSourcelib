package com.dlh.opensourcelib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlh.opensourcelib.R;


/**
 * @TODO: 左右两边各一个TextView 底部有分割线
 * @AUTHOR: dlh
 * @DATE: 2016/3/21
 */
public class TextLableContentView extends LinearLayout {
    private TextView leftTv;
    private TextView rightTv;
    private View bottomView;

    private LinearLayout linearLayout;

    public TextLableContentView(Context context) {
        super(context);
        initView(context);
    }

    public TextLableContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_lable_content_view, this);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear);
        leftTv = (TextView) view.findViewById(R.id.left_tv);
        rightTv = (TextView) view.findViewById(R.id.right_tv);
        bottomView = view.findViewById(R.id.bottom_line);
    }

    public void setLeftText(String text) {
        leftTv.setText(text);
    }

    public void setLeftText(int text) {
        leftTv.setText(text);
    }

    public void setLeftText(CharSequence text) {
        leftTv.setText(text);
    }

    public void setLeftTextColor(int color) {
        leftTv.setTextColor(color);
    }

    public void setLeftSize(float size) {
        leftTv.setTextSize(size);
    }

    public void setRightTvText(String text) {
        rightTv.setText(text);
    }

    public void setRightTvText(int text) {
        rightTv.setText(text);
    }

    public void setRightTvText(CharSequence text) {
        rightTv.setText(text);
    }

    public void setRightTextColor(int color) {
        rightTv.setTextColor(color);
    }

    public void setRightTvSize(float size) {
        rightTv.setTextSize(size);
    }

    public void setBottomViewBg(int resid) {
        bottomView.setBackgroundResource(resid);
    }

    public void setBottomViewVisibility(int resid) {
        bottomView.setVisibility(resid);
    }

    public TextView getLeftTv() {
        return leftTv;
    }

    public void setLeftTv(TextView leftTv) {
        this.leftTv = leftTv;
    }

    public TextView getRightTv() {
        return rightTv;
    }

    public void setRightTv(TextView rightTv) {
        this.rightTv = rightTv;
    }

    public View getBottomView() {
        return bottomView;
    }

    public void setBottomView(View bottomView) {
        this.bottomView = bottomView;
    }
}
