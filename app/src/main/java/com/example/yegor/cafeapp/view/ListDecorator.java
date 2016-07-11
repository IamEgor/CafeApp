package com.example.yegor.cafeapp.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListDecorator extends RecyclerView.ItemDecoration {

    private int space;

    public ListDecorator(int space) {
        this.space = space;
    }

    public ListDecorator(float space) {
        this.space = (int) space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) == 0)
            outRect.top = space;
    }

}
