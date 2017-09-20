package org.mightyfrog.android.fabshowhidesample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shigehiro Soejima
 */
public class MainActivity extends AppCompatActivity {
    private int mCheckedMenuItem = R.id.recycler_view_based;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter());
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCheckedMenuItem == R.id.recycler_view_based) {
                    return;
                }

                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        final AppBarLayout appBarLayout = findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
                if (percentage > .7f && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (percentage < .7f && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.app_bar_based).setChecked(mCheckedMenuItem == R.id.app_bar_based);
        menu.findItem(R.id.recycler_view_based).setChecked(mCheckedMenuItem == R.id.recycler_view_based);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_based:
                mCheckedMenuItem = R.id.app_bar_based;
                invalidateOptionsMenu();
                break;
            case R.id.recycler_view_based:
                mCheckedMenuItem = R.id.recycler_view_based;
                invalidateOptionsMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<Integer> mList = new ArrayList<>(100);

        MyAdapter() {
            for (int i = 0; i < 100; i++) {
                mList.add(i);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);

            return new MyViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView1.setText("1: " + mList.get(position));
            holder.textView2.setText("2: " + mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView1;
        private TextView textView2;

        MyViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
        }
    }
}
