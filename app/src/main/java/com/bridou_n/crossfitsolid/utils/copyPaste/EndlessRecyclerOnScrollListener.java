package com.bridou_n.crossfitsolid.utils.copyPaste;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by bridou_n on 03/08/2017.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();
    private static int mVisibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    private static int mItemsPerPage = 10; // The number of items we get per page when requesting the API

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        mLinearLayoutManager = linearLayoutManager;
    }

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager,
                                           int visibleThreshold) {
        mLinearLayoutManager = linearLayoutManager;
        mVisibleThreshold = visibleThreshold;
    }

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager,
                                           int visibleThreshold, int itemsPerPage) {
        mLinearLayoutManager = linearLayoutManager;
        mVisibleThreshold = visibleThreshold;
        mItemsPerPage = itemsPerPage;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        // This means we have added items to the Rv so we can now listen to scrolls again
        if (loading && totalItemCount > previousTotal) {
            loading = false;
            previousTotal = totalItemCount;
        }

        // End has been reached
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            Log.i("ScollListener", "I have " + totalItemCount + " items in the RV");

            loading = true;
            onLoadMore(totalItemCount / mItemsPerPage + 1);
        }
    }

    public abstract void onLoadMore(int current_page);

    /**
     * This function sets the loading flag of the ScrollListener
     * It's usefull in the case you didn't load new items in the RV but are not loading anymore
     * For example when you got an error during the network call, you still want the user
     * to be able to trigger the onLoadMore callback if he scrolls afterward
     * @param state
     */
    public void setLoading(boolean state) {
        loading = state;
    }
}
