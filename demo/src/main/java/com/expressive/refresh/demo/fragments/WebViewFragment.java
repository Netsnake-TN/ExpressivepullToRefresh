package com.expressive.refresh.demo.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.expressive.refresh.ExpressivePullToRefresh;
import com.expressive.refresh.demo.MainActivity;
import com.expressive.refresh.demo.R;
import com.expressive.refresh.demo.view.CircularToggle;

public class WebViewFragment extends Fragment implements MainActivity.StyleChangeListener {

    private static final String DEFAULT_URL = "https://www.google.com";

    private ExpressivePullToRefresh pullToRefresh;
    private WebView webView;
    private ProgressBar progressBar;
    private TextView errorText;
    private CircularToggle circularToggle;
    private boolean isToggleVisible = true;
    private int mLastScrollY = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pullToRefresh = view.findViewById(R.id.pull_to_refresh);
        webView = view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.progress_bar);
        errorText = view.findViewById(R.id.error_text);

        if (getActivity() instanceof MainActivity) {
            circularToggle = ((MainActivity) getActivity()).getCircularToggle();
        }

        setupWebView();

        pullToRefresh.setOnRefreshListener(() -> {
            webView.clearCache(true);
            webView.clearHistory();
            webView.reload();
        });

        webView.loadUrl(DEFAULT_URL);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.GONE);
                pullToRefresh.setRefreshing(false);
                
                webView.post(() -> {
                    webView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
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
                });
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressBar.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
                pullToRefresh.setRefreshing(false);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public void onStyleChanged(int style) {
        if (pullToRefresh != null) {
            pullToRefresh.setIndicatorStyle(style);
        }
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroyView();
    }
}
