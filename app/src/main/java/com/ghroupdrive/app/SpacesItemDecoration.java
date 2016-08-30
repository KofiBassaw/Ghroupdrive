package com.ghroupdrive.app;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by pk on 5/15/15.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
       // outRect.left = leftRight;
       // outRect.right = leftRight;
        outRect.bottom = space;
        outRect.top = space;

        // Add top margin only for the first item to avoid double space between items
       /* if(parent.getChildPosition(view) == 0)
            outRect.top = space;*/
    }
}




