package org.mightyfrog.android.fabshowhidesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import org.mightyfrog.android.fabshowhidesample.databinding.ActivityMainBinding
import org.mightyfrog.android.fabshowhidesample.databinding.ViewHolderBinding
import kotlin.math.abs

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {
    private var checkedMenuItem = R.id.recycler_view_based

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        val fab = binding.fab
        val recyclerView = binding.recyclerView
        val appbar = binding.appBar

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter()

        // remove RecyclerView.OnScrollListener & AppBarLayout.OnOffsetChangedListener to use
        // ScrollAwareFABBehavior (see also activity_main.xml).
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (checkedMenuItem == R.id.recycler_view_based) {
                    return
                }
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        })
        appbar.addOnOffsetChangedListener(
            OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percentage = abs(verticalOffset) / appBarLayout.totalScrollRange
                    .toFloat()
                if (percentage > .7f && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (percentage < .7f && fab.visibility != View.VISIBLE) {
                    fab.show()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.app_bar_based).isChecked =
            checkedMenuItem == R.id.app_bar_based
        menu.findItem(R.id.recycler_view_based).isChecked =
            checkedMenuItem == R.id.recycler_view_based
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_based -> {
                checkedMenuItem = R.id.app_bar_based
                invalidateOptionsMenu()
            }
            R.id.recycler_view_based -> {
                checkedMenuItem = R.id.recycler_view_based
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                ViewHolderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 100
    }

    private class MyViewHolder(val binding: ViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.position = position
            binding.executePendingBindings()
        }
    }
}
