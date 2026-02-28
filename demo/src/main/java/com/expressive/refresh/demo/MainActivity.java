package com.expressive.refresh.demo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.expressive.refresh.demo.view.CircularToggle;
import com.expressive.refresh.demo.adapters.DemoPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private CircularToggle tog;
    private DemoPagerAdapter pagerAdapter;

    private int mCurrentStyle = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tog = (CircularToggle) findViewById(R.id.circular_toggle);
        tog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mCurrentStyle = (mCurrentStyle + 1) % 2;
               notifyFragmentsOfStyleChange(mCurrentStyle);
               tog.toggle();
           }
        });
        setupViewPager();
    }

    private void setupViewPager() {
        pagerAdapter = new DemoPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case DemoPagerAdapter.TAB_RECYCLER:
                    tab.setText(R.string.tab_recycler);
                    break;
                case DemoPagerAdapter.TAB_LISTVIEW:
                    tab.setText(R.string.tab_listview);
                    break;
                case DemoPagerAdapter.TAB_WEBVIEW:
                    tab.setText(R.string.tab_webview);
                    break;
                case DemoPagerAdapter.TAB_SCROLLVIEW:
                    tab.setText(R.string.tab_scrollview);
                    break;
            }
        }).attach();
    }

    private void notifyFragmentsOfStyleChange(int style) {
        for (int i = 0; i < pagerAdapter.getItemCount(); i++) {
            androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag("f" + i);
            if (fragment instanceof StyleChangeListener) {
                ((StyleChangeListener) fragment).onStyleChanged(style);
            }
        }
    }

    public CircularToggle getCircularToggle() {
        return tog;
    }

    public interface StyleChangeListener {
        void onStyleChanged(int style);
    }
}
