package com.andresgqjob.testcrud;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeVH extends RecyclerView.ViewHolder
{
    public final TextView txtName;
    public final TextView txtPos;
    public final TextView txtOpt;

    public EmployeeVH(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.txt_name);
        txtPos  = itemView.findViewById(R.id.txt_pos);
        txtOpt  = itemView.findViewById(R.id.txt_opt);
    }
}

