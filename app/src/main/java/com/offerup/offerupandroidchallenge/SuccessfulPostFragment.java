package com.offerup.offerupandroidchallenge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SuccessfulPostFragment extends Fragment {


    @OnClick(R.id.done_button)
    public void showHomeScreen(){
        getFragmentManager().beginTransaction().replace(R.id.container_layout,new MainFragment()).commit();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.post_successful, container, false);
        ButterKnife.bind(this,result);
        return  result;
    }
}
