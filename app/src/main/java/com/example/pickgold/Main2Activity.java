package com.example.pickgold;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Main2Activity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private LinearLayout mLinearLayout;
    private String mPickDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mLinearLayout = (LinearLayout) findViewById(R.id.gold_detail_title);
        mFrameLayout= (FrameLayout) findViewById(R.id.gold_detail_container);
        mPickDay=getIntent().getStringExtra("pick_day");

        updateUI();

        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.gold_detail_container);
        if (fragment==null){
            fragment=GoldsListFragment.newInstance(mPickDay);
            fragmentManager.beginTransaction()
                    .add(R.id.gold_detail_container,fragment)
                    .commit();
        }
    }

    private void updateUI() {
        if (DataList.getInstance(this.getApplicationContext()).getGoldsList(mPickDay).size()<=0){
            mLinearLayout.setVisibility(View.INVISIBLE);
            mFrameLayout.setVisibility(View.INVISIBLE);
        } else {
            mFrameLayout.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
