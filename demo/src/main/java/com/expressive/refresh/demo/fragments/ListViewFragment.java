package com.expressive.refresh.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.expressive.refresh.ExpressivePullToRefresh;
import com.expressive.refresh.demo.MainActivity;
import com.expressive.refresh.demo.R;
import com.expressive.refresh.demo.view.CircularToggle;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends Fragment implements MainActivity.StyleChangeListener {

    private static final int REFRESH_DELAY = 2000;

    private ExpressivePullToRefresh pullToRefresh;
    private ListView listView;
    private CircularToggle circularToggle;
    private boolean isToggleVisible = true;
    private int mLastFirstVisibleItem = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh = view.findViewById(R.id.pull_to_refresh);
        listView = view.findViewById(R.id.list_view);

        if (getActivity() instanceof MainActivity) {
            circularToggle = ((MainActivity) getActivity()).getCircularToggle();
        }

        pullToRefresh.setOnRefreshListener(() ->
            pullToRefresh.postDelayed(() -> {
                pullToRefresh.setRefreshing(false);
            }, REFRESH_DELAY)
        );

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            items.add("ListView Item " + (i + 1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_simple, R.id.text_view, items);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLastFirstVisibleItem < firstVisibleItem) {
                    if (isToggleVisible) {
                        isToggleVisible = false;
                        if (circularToggle != null) {
                            circularToggle.hide();
                        }
                    }
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    if (!isToggleVisible) {
                        isToggleVisible = true;
                        if (circularToggle != null) {
                            circularToggle.show();
                        }
                    }
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public void onStyleChanged(int style) {
        if (pullToRefresh != null) {
            pullToRefresh.setIndicatorStyle(style);
        }
    }
}