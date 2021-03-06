package com.iiifi.shop.modules.syscenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iiifi.shop.activity.R;
import com.iiifi.shop.modules.syscenter.view.HomeView;
import com.wyt.searchbox.custom.IOnSearchClickListener;

/**
 * Created by donglinghao on 2016-01-28.
 */
public class HomeFragment extends Fragment implements  IOnSearchClickListener {

    private View homeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (homeView == null){
            homeView = inflater.inflate(R.layout.home_fragment,container,false);
        }
        ViewGroup parent = (ViewGroup) homeView.getParent();
        if (parent != null){
            parent.removeView(homeView);
        }
        //编译页面
        new HomeView(this,homeView);
       // HomeView.build(this,homeView);
        return homeView;
    }
    @Override
    public void OnSearchClick(String keyword) {
        Toast.makeText(getContext(),keyword,Toast.LENGTH_SHORT).show();
    }

}
