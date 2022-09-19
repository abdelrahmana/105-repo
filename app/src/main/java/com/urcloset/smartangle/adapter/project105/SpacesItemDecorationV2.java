package com.urcloset.smartangle.adapter.project105;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecorationV2 extends RecyclerView.ItemDecoration {
    private int space;
    private int spaceH;

    public SpacesItemDecorationV2(int space,int spaceH) {
        this.space = space;
        this.spaceH=spaceH;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = spaceH;

        // Add top margin only for the first item to avoid double space between items
      /*  if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }*/
    }
}
