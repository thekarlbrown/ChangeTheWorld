package com.thekarlbrown.changetheworld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is an implementation of a Toolbar that splits up items evenly at the top without traditional buttons
 * Note: In the future, I will use traditional toolbars
 */
public class SplitToolbar extends Toolbar {

    public SplitToolbar(Context context) {
        super(context);
    }

    public SplitToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SplitToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(@NonNull View child, ViewGroup.LayoutParams params) {
        if (child instanceof ActionMenuView) {
            params.width = LayoutParams.MATCH_PARENT;
        }
        super.addView(child, params);
    }
}