package com.example.pickgold;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;


public class DayRecordFragment extends Fragment {

    private RecyclerView mDayRecordRecyclerView;
    private DayRecordAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        mDayRecordRecyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view_id);
        mDayRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        List<DayRecord> list = dataList.getDayRecordList();

        if (mAdapter == null) {
            mAdapter = new DayRecordAdapter(list);
            mDayRecordRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class DayRecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPickDay;
        private TextView mCountGoldsNumber;
        private TextView mTimes;
        private DayRecord mDayRecord;

        public DayRecordHolder(View itemView) {
            super(itemView);

            mPickDay = (TextView) itemView.findViewById(R.id.day_record_pick_day);
            mCountGoldsNumber = (TextView) itemView.findViewById(R.id.day_record_numbers);
            mTimes = (TextView) itemView.findViewById(R.id.day_record_times);

            itemView.setOnClickListener(this);
        }


        public void bindDayRecord(DayRecord dayRecord) {
            mDayRecord=dayRecord;
            mPickDay.setText(dayRecord.getPickDay());
            mCountGoldsNumber.setText(String.valueOf(Double.parseDouble(dayRecord.getCountGoldsNumbers().toString())));
            mTimes.setText(String.valueOf(dayRecord.getTimes()));
        }

        @Override
        public void onClick(View view) {
            Intent intent =new Intent(getActivity(),Main2Activity.class);
            intent.putExtra("pick_day",mDayRecord.getPickDay());
            startActivity(intent);
        }
    }

    private class DayRecordAdapter extends RecyclerView.Adapter<DayRecordHolder> {

        private List<DayRecord> mDayRecordList;

        public DayRecordAdapter(List<DayRecord> dayRecords) {
            mDayRecordList = dayRecords;
        }

        @Override
        public DayRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.day_record_list, parent, false);
            return new DayRecordHolder(view);
        }

        @Override
        public void onBindViewHolder(DayRecordHolder holder, int position) {
            DayRecord dayRecord = mDayRecordList.get(position);
            holder.bindDayRecord(dayRecord);
        }

        @Override
        public int getItemCount() {
            return mDayRecordList.size();
        }
    }
}
