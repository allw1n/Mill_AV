package com.m_corp.millav.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.m_corp.millav.R;
import com.m_corp.millav.room.Bill;

import java.util.ArrayList;
import java.util.List;

public class PendingBillsAdapter extends RecyclerView.Adapter<PendingBillsAdapter.BillViewHolder> {

    private final Context context;
    private List<Bill> bills = new ArrayList<>();
    private OnRecyclerItemClickListener itemClickListener;

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnRecyclerItemClickListener {
        void onItemClickListener(Bill bill);
    }

    public PendingBillsAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBills(List<Bill> bills) {
        this.bills = bills;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.pending_bills_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill tempBill = bills.get(position);
        holder.bindTo(tempBill);
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final MaterialTextView viewPendingBillNumber;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            this.viewPendingBillNumber = itemView.findViewById(R.id.viewPendingBillNumber);
            itemView.setOnClickListener(this);
        }

        void bindTo(Bill bill) {
            viewPendingBillNumber.setText(String.valueOf(bill.getBillNumber()));
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClickListener(bills.get(getAdapterPosition()));
        }
    }
}
