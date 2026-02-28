package com.expressive.refresh.demo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.expressive.refresh.demo.fragments.ListViewFragment;
import com.expressive.refresh.demo.fragments.RecyclerViewFragment;
import com.expressive.refresh.demo.fragments.ScrollViewFragment;
import com.expressive.refresh.demo.fragments.WebViewFragment;

public class DemoPagerAdapter extends FragmentStateAdapter {

    public static final int TAB_RECYCLER = 0;
    public static final int TAB_LISTVIEW = 1;
    public static final int TAB_WEBVIEW = 2;
    public static final int TAB_SCROLLVIEW = 3;

    public DemoPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case TAB_RECYCLER:
                return new RecyclerViewFragment();
            case TAB_LISTVIEW:
                return new ListViewFragment();
            case TAB_WEBVIEW:
                return new WebViewFragment();
            case TAB_SCROLLVIEW:
                return new ScrollViewFragment();
            default:
                return new RecyclerViewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
