package com.expressive.refresh.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expressive.refresh.ExpressivePullToRefresh;
import com.expressive.refresh.demo.MainActivity;
import com.expressive.refresh.demo.R;
import com.expressive.refresh.demo.view.CircularToggle;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements MainActivity.StyleChangeListener {

    private static final int REFRESH_DELAY = 2000;

    private ExpressivePullToRefresh pullToRefresh;
    private RecyclerView recyclerView;
    private CircularToggle circularToggle;
    private boolean isToggleVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh = view.findViewById(R.id.pull_to_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

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
            items.add("RecyclerView Item " + (i + 1));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new SimpleAdapter(items));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && isToggleVisible) {
                    isToggleVisible = false;
                    if (circularToggle != null) {
                        circularToggle.hide();
                    }
                } else if (dy < 0 && !isToggleVisible) {
                    isToggleVisible = true;
                    if (circularToggle != null) {
                        circularToggle.show();
                    }
                }
            }
        });
    }

    @Override
    public void onStyleChanged(int style) {
        if (pullToRefresh != null) {
            pullToRefresh.setIndicatorStyle(style);
        }
    }

    private static class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

        private final List<String> items;

        SimpleAdapter(List<String> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_simple, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text_view);
            }
        }
    }
}