package com.example.firebaseapp.Ui;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.R;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable horizontalDivider;
    private final Drawable verticalDivider;

    public GridItemDecoration(Resources resources) {
        horizontalDivider = resources.getDrawable(R.drawable.horizontal_divider);
        verticalDivider = resources.getDrawable(R.drawable.vertical_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (shouldDrawHorizontalDivider(parent, i, spanCount)) {
                drawHorizontalDivider(c, child);
            }
            if (shouldDrawVerticalDivider(parent, i, spanCount)) {
                drawVerticalDivider(c, child);
            }
        }
    }
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else {
            return 1;
        }
    }

    private boolean shouldDrawHorizontalDivider(RecyclerView parent, int position, int spanCount) {
        return position >= spanCount;
    }

    private boolean shouldDrawVerticalDivider(RecyclerView parent, int position, int spanCount) {
        return (position + 1) % spanCount != 0;
    }

    private void drawHorizontalDivider(Canvas canvas, View child) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left = child.getLeft() - params.leftMargin;
        int right = child.getRight() + params.rightMargin;
        int top = child.getTop() - params.topMargin;
        int bottom = top + horizontalDivider.getIntrinsicHeight();
        horizontalDivider.setBounds(left, top, right, bottom);
        horizontalDivider.draw(canvas);
    }
    private void drawVerticalDivider(Canvas canvas, View child) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left = child.getRight() + params.rightMargin;
        int right = left + verticalDivider.getIntrinsicWidth();
        int top = child.getTop() - params.topMargin;
        int bottom = child.getBottom() + params.bottomMargin;
        verticalDivider.setBounds(left, top, right, bottom);
        verticalDivider.draw(canvas);
    }
}
