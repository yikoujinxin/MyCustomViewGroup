package com.example.mycustomviewgroup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ImageLayout extends ViewGroup {

    private static final String TAG = "ImageLayout";

    // 用于处理多行的排列
    // 记录所有行
    private List<List<View>> allLines = new ArrayList<>();
    // 记录每一行的高度
    private List<Integer> lineHeights = new ArrayList<>();

    private int mHorizontalSpacing = dp2px(5);  // 横向间距
    private int mVerticalSpacing = dp2px(5);    // 纵向间距

    // 屏幕宽度
    private int screenWidth;

    private Context context;

    // 容纳imageView的容器
    private ImageLayout imageLayout;
    public void setImageLayout(ImageLayout imageLayout, int screenWidth) {
        this.imageLayout = imageLayout;
        this.screenWidth = screenWidth;
    }

    // 图片管理类
    private ImageMgr imageMgr;
    public void setImageMgr(ImageMgr imageMgr) {
        this.imageMgr = imageMgr;
    }

    public ImageLayout(Context context) {
        super(context);
        this.context = context;
    }
    public ImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void clearMeasureParams(){
        allLines.clear();
        lineHeights.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 防止多次执行onMeasure，需要清空操作
        clearMeasureParams();

        // 保存一行的imageView
        List<View> lineViews = new ArrayList<>();
        int lineWidthUsed = 0; // 记录该行已经使用的宽度
        int lineHeight = 0; // 记录一行最大的高度

        // 父容器的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        // 父亲给的宽度
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 子imageView要求的父亲的宽高
        int parenNeededWidth = 0;
        int parenNeededHeight = 0;

        // 子view的数量
        int childCount = getChildCount();
        for (int i=0; i<childCount; i++){
            // 获取每一个子view
            View childView = getChildAt(i);
            LayoutParams childViewLayoutParams = childView.getLayoutParams();

            // 通过子view的layoutParams，获取子view的measureSpec
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childViewLayoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingBottom + paddingTop, childViewLayoutParams.height);

            // 进行度量子view
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // 获取子view的度量宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            // 当前view的宽度，加上已经使用的宽度及间距，如果大于父亲的宽度，则换行
            if (childMeasuredWidth + lineWidthUsed + mHorizontalSpacing > selfWidth){
                // 换行了，把之前一行保存的view，添加到allLines中，并添加高度
                allLines.add(lineViews);
                lineHeights.add(lineHeight);

                // 此时需要的父亲的宽高
                parenNeededHeight = parenNeededHeight + lineHeight + mVerticalSpacing;
                parenNeededWidth = Math.max(parenNeededWidth, lineWidthUsed+mHorizontalSpacing);

                // 清空上一行的数据到初始态
                lineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }

            // 将子view添加到该行
            lineViews.add(childView);
            // 当前该行的宽度，及最大高度
            lineWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpacing;
            lineHeight = Math.max(lineHeight, childMeasuredHeight);

            // 最后一个view时
            if (i == childCount - 1){
                allLines.add(lineViews);
                lineHeights.add(lineHeight);

                // 此时需要的父亲的宽高
                parenNeededHeight = parenNeededHeight + lineHeight + mVerticalSpacing;
                parenNeededWidth = Math.max(parenNeededWidth, lineWidthUsed+mHorizontalSpacing);
            }
        }

        // 度量父容器
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 根据mode进行判断
        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parenNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parenNeededHeight;

        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean b, int l, int i1, int i2, int i3) {
        // 一共几行
        int size = allLines.size();

        // padding
        int currentLeft = getPaddingLeft();
        int currentTop = getPaddingTop();

        for (int i = 0; i< size; i++){
            // 获取每一行的子view
            List<View> lineViews = allLines.get(i);
            // 获取每一行的高
            Integer lineHeight = lineHeights.get(i);

            // 遍历每一个子view
            for (int j=0; j< lineViews.size(); j++){
                View view = lineViews.get(j);
                int left = currentLeft;
                int top = currentTop;
                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();

                view.layout(left, top, right, bottom);

                currentLeft = right + mHorizontalSpacing;
            }

            currentLeft = getPaddingLeft();
            currentTop = currentTop + lineHeight + mVerticalSpacing;
        }
    }

    // 往容器中添加一个imageView
    public void addImage(int resID, String uuid){
        boolean flag = imageMgr.isExistImage(uuid);
        if (flag){
            Toast.makeText(context, "添加失败，uuid重复了，已存在", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, ImageView> allImage = imageMgr.getAllImage();
        // 当前所有的图片数量
        int size = allImage.size();
        // 达到5个，按照九宫格
        if (size>=4){
            Collection<ImageView> values = allImage.values();
            for (ImageView imageView : values){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth/3 - dp2px(10) , screenWidth/3 - dp2px(10));
                imageView.setLayoutParams(params);
            }

            ImageView view = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth/3 - dp2px(10) , screenWidth/3 - dp2px(10));
            view.setLayoutParams(params);
            view.setVisibility(VISIBLE);
            view.setImageBitmap(BitmapFactory.decodeResource(getResources(), resID, null));

            imageMgr.addImage(uuid, view);
            imageLayout.addView(view);
            return;
        }

        ImageView view = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( screenWidth/2 - ImageLayout.dp2px(10) ,  screenWidth/2 - ImageLayout.dp2px(10));
        view.setLayoutParams(params);
        view.setVisibility(VISIBLE);
        view.setImageBitmap(BitmapFactory.decodeResource(getResources(), resID, null));

        imageMgr.addImage(uuid, view);
        imageLayout.addView(view);
    }

    // 从容器中移除指定的imageView
    public void removeImage(String uuid){
        if (imageMgr.isExistImage(uuid)){
            Map<String, ImageView> allImage = imageMgr.getAllImage();
            int size = allImage.size();
            // 四宫格
            if (size <= 5){
                Collection<ImageView> values = allImage.values();
                for (ImageView view : values){
                    LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = screenWidth/2 - ImageLayout.dp2px(20);
                    layoutParams.height = layoutParams.width;
                    view.setLayoutParams(layoutParams);
                }
            }

            // 从容器中移除指定的imageView
            imageLayout.removeView(imageMgr.getImageView(uuid));
            imageMgr.deleteImage(uuid);
            Log.d(TAG, "删除UUID对应的imageView成功！");
        } else {
            Log.d(TAG, "删除UUID对应的imageView失败！uuid不存在，uuid为："+uuid);
            Toast.makeText(context, "删除UUID对应的imageView失败！uuid不存在", Toast.LENGTH_SHORT).show();
        }
    }

    // 删除所有的view
    public void removeAllImage(){
        imageLayout.removeAllViews();
        imageMgr.deleteAllImage();
    }

    // 转换
    public static int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
