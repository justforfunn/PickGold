package com.example.pickgold;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class GoldsListFragment extends Fragment {

    private RecyclerView mGoldsRecyclerView;
    private GoldsAdapter mAdapter;
    private static final String ARGUMENTS_PICK_DAY="pick_day";

    /**
     * 实例化fragment
     * @param pickDay 捡金币的日期
     * @return goldsListFragment
     */
    public static GoldsListFragment newInstance(String pickDay){
        GoldsListFragment goldsListFragment=new GoldsListFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARGUMENTS_PICK_DAY,pickDay);
        goldsListFragment.setArguments(bundle);
        return goldsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mGoldsRecyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view_id);
        mGoldsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        DataList dataList = DataList.getInstance(getActivity().getApplicationContext());
        List<Golds> list;
        String pickDay=getArguments().getString(ARGUMENTS_PICK_DAY);
        if (pickDay!=null){
            list=dataList.getGoldsList(pickDay);
        }else {
            list = dataList.getGoldsList();
        }

        if (mAdapter == null) {
            mAdapter = new GoldsAdapter(list);
            mGoldsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class GoldsHolder extends RecyclerView.ViewHolder{

        private TextView mPickDay;
        private TextView mPickTime;
        private TextView mNumbers;
        private TextView mOwner;


        public GoldsHolder(View itemView) {
            super(itemView);

            mPickDay = (TextView) itemView.findViewById(R.id.pick_day);
            mPickTime = (TextView) itemView.findViewById(R.id.pick_time);
            mNumbers = (TextView) itemView.findViewById(R.id.golds_number);
            mOwner = (TextView) itemView.findViewById(R.id.golds_owner);
        }

        public void bindGolds(Golds golds) {
            mPickDay.setText(golds.getPickDay());
            mPickTime.setText(golds.getPickTime());
            mOwner.setText(golds.getOwner());
            mNumbers.setText(String.valueOf(Double.parseDouble(golds.getNumber().toString())));
        }
    }

    private class GoldsAdapter extends RecyclerView.Adapter<GoldsHolder> {

        private List<Golds> mGoldsList;

        public GoldsAdapter(List<Golds> goldsList) {
            mGoldsList = goldsList;
        }

        @Override
        public GoldsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_golds_list, parent, false);
            return new GoldsHolder(view);
        }

        @Override
        public void onBindViewHolder(GoldsHolder holder, int position) {
            Golds golds = mGoldsList.get(position);
            holder.bindGolds(golds);
        }

        @Override
        public int getItemCount() {
            return mGoldsList.size();
        }
    }
}
