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

    private RecyclerView mCrimeRecyclerView;
    private GoldsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.golds_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        GoldsList goldsList=GoldsList.getInstance(getActivity().getApplicationContext());
        List<Golds> list = goldsList.getGoldsList();

        if (mAdapter == null) {
            mAdapter = new GoldsAdapter(list);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder{

        private TextView mPickDay;
        private TextView mPickTime;
        private TextView mNumbers;


        public CrimeHolder(View itemView) {
            super(itemView);

            mPickDay = (TextView) itemView.findViewById(R.id.pick_day);
            mPickTime = (TextView) itemView.findViewById(R.id.pick_time);
            mNumbers = (TextView) itemView.findViewById(R.id.golds_number);
        }

        public void bindCrime(Golds golds) {
            mPickDay.setText(golds.getPickDay());
            mPickTime.setText(golds.getPickTime());
            mNumbers.setText(String.valueOf(golds.getFormatNumber()));
        }
    }

    private class GoldsAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Golds> mGoldsList;

        public GoldsAdapter(List<Golds> crimes) {
            mGoldsList = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.fragment_golds_list, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Golds golds = mGoldsList.get(position);
            holder.bindCrime(golds);
        }

        @Override
        public int getItemCount() {
            return mGoldsList.size();
        }
    }
}
