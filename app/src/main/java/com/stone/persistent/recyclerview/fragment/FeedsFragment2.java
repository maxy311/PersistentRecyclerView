package com.stone.persistent.recyclerview.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stone.persistent.recyclerview.R;
import com.stone.persistent.recyclerview.adapter.FeedsListAdapter;
import com.stone.persistent.recyclerview.widget.GridItemDecoration;
import com.stone.persistent.recyclerview.widget.PersistentStaggeredGridLayoutManager;

import library2.ChildRecyclerView;

public class FeedsFragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds_list2, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChildRecyclerView childRecyclerView = view.findViewById(R.id.child_recycler_view);
        childRecyclerView.setLayoutManager(new PersistentStaggeredGridLayoutManager(2));
        childRecyclerView.addItemDecoration(new GridItemDecoration(dp2px(8f)));
        childRecyclerView.setAdapter(new FeedsListAdapter(getActivity()));
    }

    public Float dp2px(Float dpValue) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        return dpValue * density + 0.5f;
    }
}
