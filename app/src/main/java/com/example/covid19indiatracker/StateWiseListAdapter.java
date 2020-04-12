package com.example.covid19indiatracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateWiseListAdapter extends RecyclerView.Adapter<StateWiseListAdapter.StateWiseListViewHolder> {

    List<StatewisePojo> mStateWiseList;
    Context mContext;

    public StateWiseListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public StateWiseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.list_item_view, parent, false);
        return new StateWiseListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StateWiseListViewHolder holder, int position) {
        StatewisePojo statewisePojo = mStateWiseList.get(position);
        holder.tvStateName.setText(statewisePojo.stateName);
        holder.tvConfirmed.setText(statewisePojo.confirmed);
        holder.tvActive.setText(statewisePojo.active);
        holder.tvRecovered.setText(statewisePojo.recovered);
        holder.tvDeceased.setText(statewisePojo.deceased);
        if(Integer.parseInt(statewisePojo.confirmedIncrease) >= 0) {
            String str = "+ " + statewisePojo.confirmedIncrease;
            holder.tvConfirmedIncrease.setText(str);
        }else{
            String str = "- " + statewisePojo.confirmedIncrease.substring(1);
            holder.tvConfirmedIncrease.setText(str);
        }
        if(Integer.parseInt(statewisePojo.activeIncrease) >= 0) {
            String str = "+ " + statewisePojo.activeIncrease;
            holder.tvActiveIncrease.setText(str);
        }else{
            String str = "- " + statewisePojo.activeIncrease.substring(1);
            holder.tvActiveIncrease.setText(str);
        }
        if(Integer.parseInt(statewisePojo.recoveredIncrease) >= 0) {
            String str = "+ " + statewisePojo.recoveredIncrease;
            holder.tvRecoveredIncrease.setText(str);
        }else{
            String str = "- " + statewisePojo.recoveredIncrease.substring(1);
            holder.tvRecoveredIncrease.setText(str);
        }
        if(Integer.parseInt(statewisePojo.deceasedIncrease) >= 0) {
            String str = "+ " + statewisePojo.deceasedIncrease;
            holder.tvDeceasedIncrease.setText(str);
        }else{
            String str = "- " + statewisePojo.deceasedIncrease.substring(1);
            holder.tvDeceasedIncrease.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        if(mStateWiseList == null) return 0;
        return mStateWiseList.size();
    }

    class StateWiseListViewHolder extends RecyclerView.ViewHolder{
        TextView tvStateName, tvConfirmed, tvActive, tvRecovered, tvDeceased,
                tvConfirmedIncrease, tvActiveIncrease, tvRecoveredIncrease, tvDeceasedIncrease;
        public StateWiseListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStateName = itemView.findViewById(R.id.tvStateName);
            tvConfirmed = itemView.findViewById(R.id.tvConfirmed);
            tvActive = itemView.findViewById(R.id.tvActive);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);
            tvDeceased = itemView.findViewById(R.id.tvDeceased);
            tvConfirmedIncrease = itemView.findViewById(R.id.tvConfirmedIncrease);
            tvActiveIncrease = itemView.findViewById(R.id.tvActiveIncrease);
            tvRecoveredIncrease = itemView.findViewById(R.id.tvRecoveredIncrease);
            tvDeceasedIncrease = itemView.findViewById(R.id.tvDeceasedIncrease);
        }
    }

    public void setStateWiseList(List<StatewisePojo> stateWiseList){
        mStateWiseList = stateWiseList;
        notifyDataSetChanged();
    }
}
