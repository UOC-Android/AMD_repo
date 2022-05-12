package com.andresgqjob.expenseapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.ExpenseInfo;
import com.andresgqjob.expenseapp.model.UserInfo;
import com.andresgqjob.expenseapp.ui.ExpenseActivity;

import java.util.ArrayList;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder> {
    private final ExpenseInfo[] listdata;
    private final Context activityContext;
    ArrayList<UserInfo> users;

    public ExpenseListAdapter(ExpenseInfo[] listData, Context context, ArrayList<UserInfo> users) {
        this.listdata = listData;
        this.users = users;
        this.activityContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_expense, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExpenseInfo myListData = listdata[position];
        holder.textView_Desc.setText(listdata[position].description);
        holder.textView_Date.setText(listdata[position].date);
        holder.textView_Amount.setText("" + listdata[position].totalAmount + " €");
        holder.relativeLayout.setOnClickListener(view -> {
            Intent k = new Intent(activityContext, ExpenseActivity.class);
            k.putExtra("Description", myListData.description);
            k.putExtra("Date", myListData.date);
            k.putExtra("Amount", myListData.totalAmount);
            k.putExtra("Users", users);
            activityContext.startActivity(k);
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public TextView textView_Amount;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.textView_Amount = (TextView) itemView.findViewById(R.id.textView_amount);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }
}
