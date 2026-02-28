# Expressive Pull to Refresh

[![Platform](https://img.shields.io/badge/platform-Android-blue.svg)](https://developer.android.com/)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg)](https://android-arsenal.com/api?level=26)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![JitPack](https://img.shields.io/badge/JitPack-1.0.0-green.svg)](https://jitpack.io/#Netsnake-TN/ExpressivepullToRefresh)

A Material Design 3 Expressive Pull-to-Refresh component for Android. Provides two distinct indicator styles with smooth animations, haptic feedback, and seamless integration for all scrollable views.

![Material Design 3](https://img.shields.io/badge/Material-Design%203-6200EE)
![Java](https://img.shields.io/badge/Java-8-orange)

---

## 🎨 Features

- **Two Indicator Styles**
  - `STYLE_UNCONTAINED` - Minimal indicator without background
  - `STYLE_CONTAINED` - Full indicator with background and shadow

- **Material Design 3** - Full support for Material 3 theming and dynamic colors

- **Nested Scrolling** - Compatible with:
  - `RecyclerView`
  - `ListView`
  - `WebView`
  - `ScrollView`

- **Haptic Feedback** - Tactile response when refresh threshold is reached

- **Smooth Animations** - Decelerate interpolation for natural motion

- **Runtime Style Switching** - Change indicator styles dynamically

---

## 🎬 Preview

<div align="center">
  <img src="demo.gif" alt="Demo" />
</div>

---

## 📦 Installation

### JitPack Dependency

1. Add JitPack repository to your root `build.gradle` or `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency to your app-level `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Netsnake-TN:ExpressivepullToRefresh:1.0.0'
}
```

### Required Dependencies

This library uses **Material Components 1.14+** for the `LoadingIndicator` component. Make sure your project includes:

```gradle
dependencies {
    implementation 'com.google.android.material:material:1.14.0'
    implementation 'androidx.core:core:1.16.0'
}
```

### Requirements

- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 36
- **Compile SDK:** 36

---

## 🚀 Usage

### XML Layout

Wrap your scrollable view with `ExpressivePullToRefresh`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.expressive.refresh.ExpressivePullToRefresh
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/pull_to_refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.expressive.refresh.ExpressivePullToRefresh>
```

### Set Up Refresh Listener

```java
ExpressivePullToRefresh pullToRefresh = findViewById(R.id.pull_to_refresh);

pullToRefresh.setOnRefreshListener(() -> {
    // Perform your refresh operation here
    loadDataFromServer();
    
    // Stop refreshing when done
    pullToRefresh.setRefreshing(false);
});
```

### Simulate Refresh State

```java
// Start refreshing programmatically
pullToRefresh.setRefreshing(true);

// Stop refreshing
pullToRefresh.setRefreshing(false);

// Check if currently refreshing
boolean isRefreshing = pullToRefresh.isRefreshing();
```

---

## 🎯 Indicator Styles

### Set Style Programmatically

```java
// Uncontained style (no background)
pullToRefresh.setIndicatorStyle(ExpressivePullToRefresh.STYLE_UNCONTAINED);

// Contained style (with background and shadow)
pullToRefresh.setIndicatorStyle(ExpressivePullToRefresh.STYLE_CONTAINED);

// Get current style
int currentStyle = pullToRefresh.getIndicatorStyle();
```

### Set Style in XML

```xml
<com.expressive.refresh.ExpressivePullToRefresh
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/pull_to_refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:type="uncontained">

    <!-- Your scrollable content -->

</com.expressive.refresh.ExpressivePullToRefresh>
```

Available values:
- `app:type="uncontained"` - Minimal indicator
- `app:type="contained"` - Full indicator with background

---

## 🎨 Customization

### Material 3 Theming

The component automatically adapts to your app's Material 3 theme:

- **Uncontained:** Uses `colorPrimary` from your theme
- **Contained:** Uses `colorOnPrimaryContainer` from your theme

Enable dynamic colors for automatic adaptation to system themes:

```java
// In your Activity or Application class
DynamicColors.applyToActivitiesIfAvailable(this);
```

### Theme Example

```xml
<style name="AppTheme" parent="Theme.Material3Expressive.DayNight.NoActionBar">
    <item name="colorPrimary">#6750A4</item>
    <item name="colorPrimaryContainer">#EADDFF</item>
    <item name="colorOnPrimaryContainer">#21005D</item>
</style>
```

---

## 📚 Complete Example

### RecyclerView with Pull-to-Refresh

**Layout (fragment_recycler_view.xml):**

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.expressive.refresh.ExpressivePullToRefresh
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/pull_to_refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:clipToPadding="false"
        android:padding="8dp" />

</com.expressive.refresh.ExpressivePullToRefresh>
```

**Fragment (RecyclerViewFragment.java):**

```java
public class RecyclerViewFragment extends Fragment {

    private ExpressivePullToRefresh pullToRefresh;
    private RecyclerView recyclerView;
    private MyDataAdapter adapter;
    private List<MyData> items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pullToRefresh = view.findViewById(R.id.pull_to_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyDataAdapter(items);
        recyclerView.setAdapter(adapter);

        // Setup Pull-to-Refresh
        pullToRefresh.setOnRefreshListener(this::refreshData);
    }

    private void refreshData() {
        // Simulate network delay
        pullToRefresh.postDelayed(() -> {
            // Load new data
            loadData();
            
            // Stop refreshing
            pullToRefresh.setRefreshing(false);
        }, 2000);
    }

    private void loadData() {
        items.clear();
        for (int i = 0; i < 30; i++) {
            items.add(new MyData("Item " + (i + 1)));
        }
        adapter.notifyDataSetChanged();
    }
}
```

---

## 🔧 API Reference

### Methods

| Method | Description |
|--------|-------------|
| `setOnRefreshListener(OnRefreshListener listener)` | Set callback for refresh events |
| `setRefreshing(boolean refreshing)` | Programmatically start/stop refresh |
| `setRefreshing(boolean refreshing, boolean notify)` | Start/stop with optional callback trigger |
| `isRefreshing()` | Check current refresh state |
| `setIndicatorStyle(int style)` | Set indicator style (UNCONTAINED/CONTAINED) |
| `getIndicatorStyle()` | Get current indicator style |

### Constants

| Constant | Value | Description |
|----------|-------|-------------|
| `STYLE_UNCONTAINED` | 0 | Indicator without background |
| `STYLE_CONTAINED` | 1 | Indicator with background |

### Callback Interface

```java
public interface OnRefreshListener {
    void onRefresh();
}
```

---

## 📄 License

```
Copyright 2026

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
