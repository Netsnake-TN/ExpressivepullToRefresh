package com.expressive.refresh.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.expressive.refresh.ExpressivePullToRefresh;
import com.expressive.refresh.demo.MainActivity;
import com.expressive.refresh.demo.R;
import com.expressive.refresh.demo.view.CircularToggle;

public class ScrollViewFragment extends Fragment implements MainActivity.StyleChangeListener {

    private static final int REFRESH_DELAY = 2000;

    private ExpressivePullToRefresh pullToRefresh;
    private CircularToggle circularToggle;
    private boolean isToggleVisible = true;
    private int mLastScrollY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh = view.findViewById(R.id.pull_to_refresh);

        if (getActivity() instanceof MainActivity) {
            circularToggle = ((MainActivity) getActivity()).getCircularToggle();
        }

        pullToRefresh.setOnRefreshListener(() ->
            pullToRefresh.postDelayed(() -> {
                pullToRefresh.setRefreshing(false);
            }, REFRESH_DELAY)
        );

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            content.append("ScrollView Content Line ").append(i + 1).append("\n\n");
            content.append("This is a sample content for the ScrollView fragment. ");
            content.append("Pull down to refresh and see the loading indicator in action.\n\n");
        }

        TextView contentText = view.findViewById(R.id.content_text);
        contentText.setText(content.toString());

        View pullToRefreshView = view.findViewById(R.id.pull_to_refresh);
        ScrollView scrollView = findScrollView(pullToRefreshView);
        if (scrollView != null) {
            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (scrollY > mLastScrollY && isToggleVisible) {
                    isToggleVisible = false;
                    if (circularToggle != null) {
                        circularToggle.hide();
                    }
                } else if (scrollY < mLastScrollY && !isToggleVisible) {
                    isToggleVisible = true;
                    if (circularToggle != null) {
                        circularToggle.show();
                    }
                }
                mLastScrollY = scrollY;
            });
        }
    }

    private ScrollView findScrollView(View view) {
        if (view instanceof ScrollView) {
            return (ScrollView) view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                ScrollView result = findScrollView(group.getChildAt(i));
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public void onStyleChanged(int style) {
        if (pullToRefresh != null) {
            pullToRefresh.setIndicatorStyle(style);
        }
    }
}