package com.example.yegor.cafeapp.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridDecorator extends RecyclerView.ItemDecoration {

    private int space;

    public GridDecorator(int space) {
        this.space = space;
    }

    public GridDecorator(float space) {
        this.space = (int) space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.top = space / 2;
        outRect.bottom = space / 2;

        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        } else {
            outRect.left = space / 2;
            outRect.right = space;
        }
    }

}
