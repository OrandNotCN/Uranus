package gaia.uranus.module.welcome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import gaia.uranus.R;

/**
 * ClassName:
 * Author:oubowu
 * Fuction:
 * CreateDate:2015/11/29 15:59
 * UpdateUser:
 * UpdateDate:
 */
public class FloatLeafLayout extends RelativeLayout {

    private static final String TAG = FloatLeafLayout.class.getSimpleName();

    // 树叶种类的数组
    private Drawable mLeafs[];

    // 补间器种类的数组
    private Interpolator mInterpolator[];

    // view宽度
    private int mWidthSize;

    // view高度
    private int mHeightSize;

    // 动画合集
    private ArrayList<AnimatorSet> mAnimatorSets;

    // view是否被销毁了，用于清除子线程
    private boolean mIsDestoryed;

    private int[] mFallTime;

    public FloatLeafLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        // 四张不同形状的叶子
        mLeafs = new Drawable[]{getResources().getDrawable(R.mipmap.leaf_1),
                getResources().getDrawable(R.mipmap.leaf_2),
                getResources().getDrawable(R.mipmap.leaf_3),
                getResources().getDrawable(R.mipmap.leaf_4)};

        // 四个不同的补间器
        mInterpolator = new Interpolator[]{new AccelerateDecelerateInterpolator(),
                new AccelerateInterpolator(),
                new DecelerateInterpolator(),
                new LinearInterpolator()};
        mFallTime =new int[]{7000,8000,9000};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidthSize = measure(widthMeasureSpec), mHeightSize = measure(heightMeasureSpec));
        if (getChildCount() == 0) {
            addTree(mWidthSize, mHeightSize);
        }
    }

    private int measure(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = dip2px(getContext(), 300);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    // 添加树的图片
    private void addTree(int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.tree, options);
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        int inSampleSize = 1;
        if (outWidth > reqWidth || outHeight > reqHeight) {
            final int widthRatio = outWidth / reqWidth;
            final int heightRatio = outHeight / reqHeight;
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        options.inSampleSize = inSampleSize == 0 ? 1 : inSampleSize;
        options.inJustDecodeBounds = false;
        ImageView mTree = new ImageView(getContext());
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tree, options);
        mTree.setBackgroundDrawable(new BitmapDrawable(bitmap));
        addView(mTree, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 播放落叶,播放15片
    public void playLeaf() {

        new Thread() {
            @Override
            public void run() {
                if (mIsDestoryed) return;
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addLeaf();
                    }
                });
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addLeaf();
                    }
                });
            }
        }.start();

    }


    // 添加落叶
    public void addLeaf() {

        ImageView mLeaf = new ImageView(getContext());
        Random random = new Random();
        // 设置随机一片落叶
        mLeaf.setImageDrawable(mLeafs[random.nextInt(4)]);

        // 随机设置落叶的起点x坐标
        float leafX = random.nextInt(mWidthSize);

        float leafY;
        // 根据x坐标算出y坐标，因为树叶的范围呈三角形，并且约占高度一半，所以要控制y坐标
        if (leafX > mWidthSize / 2) {
            leafY = mHeightSize * 1.0f / mWidthSize * leafX - mHeightSize / 2;
        } else {
            leafY = -mHeightSize * 1.0f / mWidthSize * leafX + mHeightSize / 2;
        }

        // 设置落叶起点，添加到布局
        ViewCompat.setX(mLeaf, leafX);
        ViewCompat.setY(mLeaf, leafY);
        addView(mLeaf);

        // 设置树叶刚开始出现的动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mLeaf, "alpha", 0.1f, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mLeaf, "scaleX", 0.1f, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mLeaf, "scaleY", 0.1f, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setDuration(300);

        // 树叶落下经过的第二个点
        final PointF pointF1 = new PointF(leafX + random.nextInt((int) (mWidthSize - leafX)), leafY + random.nextInt((int) (mHeightSize - leafY)));
        // 树叶落下经过的第三个点
        final PointF pointF2 = new PointF(leafX + random.nextInt((int) (mWidthSize - leafX)), leafY + random.nextInt((int) (mHeightSize - leafY)));
        // 树叶落下的起点
        final PointF pointF0 = new PointF(ViewCompat.getX(mLeaf), ViewCompat.getY(mLeaf));
        // 树叶落下的终点
        final PointF pointF3 = new PointF(random.nextInt(mWidthSize), mHeightSize);

        // 通过自定义的贝塞尔估值器算出途经的点的想x，y坐标
        final BazierTypeEvaluator bazierTypeEvaluator = new BazierTypeEvaluator(pointF1, pointF2);
        // 设置值动画
        ValueAnimator bazierAnimator = ValueAnimator.ofObject(bazierTypeEvaluator, pointF0, pointF3);
        bazierAnimator.setTarget(mLeaf);
        bazierAnimator.addUpdateListener(new BazierUpdateListener(mLeaf));
        bazierAnimator.setDuration(mFallTime[random.nextInt(mFallTime.length)]);

        // 将以上动画添加到动画集合
        AnimatorSet allSet = new AnimatorSet();
        allSet.play(set).before(bazierAnimator);
        // 随机设置一个补间器
        allSet.setInterpolator(mInterpolator[random.nextInt(4)]);
        allSet.addListener(new AnimatorEndListener(mLeaf));
        allSet.start();

        if (mAnimatorSets == null)
            mAnimatorSets = new ArrayList<>();
        mAnimatorSets.add(allSet);

        Log.e(TAG, "添加树叶，当前child个数: " + getChildCount());

    }

    // 值动画更新监听
    private class BazierUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        View target;

        public BazierUpdateListener(View target) {
            BazierUpdateListener.this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 获取坐标，设置落叶的位置
            final PointF pointF = (PointF) animation.getAnimatedValue();
            ViewCompat.setX(target, pointF.x);
            ViewCompat.setY(target, pointF.y);
            ViewCompat.setAlpha(target, 1 - animation.getAnimatedFraction());
        }
    }

    // 动画更新适配器，用于动画停止的时候移除落叶
    private class AnimatorEndListener extends AnimatorListenerAdapter {

        View target;

        public AnimatorEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(target);
            Log.e(TAG, "child:" + getChildCount());
            if (mIsDestoryed) return;
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addLeaf();
                }
            });
        }
    }

    // 销毁的时候做清理工作
    public void onDestroy() {
        Log.e(TAG, "Activity被销毁了");
        mIsDestoryed = true;
        if (mAnimatorSets == null) return;
        for (int i = 0; i < mAnimatorSets.size(); i++) {
            mAnimatorSets.get(i).cancel();
        }
        mAnimatorSets.clear();
    }

}
