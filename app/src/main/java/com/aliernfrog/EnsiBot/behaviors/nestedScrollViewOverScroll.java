package com.aliernfrog.EnsiBot.behaviors;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

public class nestedScrollViewOverScroll extends AppBarLayout.ScrollingViewBehavior {
    private float mOverScroll;

    public nestedScrollViewOverScroll() {
    }

    public nestedScrollViewOverScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        mOverScroll = 0;
        return true;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (dyUnconsumed == 0) return;

        mOverScroll -= dyUnconsumed;

        float newPos = mOverScroll;
        if (newPos > 200) newPos = 200;
        if (newPos < -200) newPos = -200;
        float scale = Math.abs((float) newPos)/3000;
        float newScale = 1+scale;

        ViewGroup viewGroup = (ViewGroup) target;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setTranslationY(newPos);
            view.setScaleY(newScale);
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        ViewGroup viewGroup = (ViewGroup) target;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.animate().scaleY(1).translationY(0).start();
        }
    }
}
