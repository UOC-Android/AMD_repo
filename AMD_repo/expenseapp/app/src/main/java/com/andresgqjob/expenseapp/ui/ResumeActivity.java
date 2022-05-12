package com.andresgqjob.expenseapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.ExpenseInfo;
import com.andresgqjob.expenseapp.model.UserInfo;
import com.andresgqjob.expenseapp.ui.adapter.TripListAdapter;
import com.andresgqjob.expenseapp.ui.adapter.UserAsPayerListAdapter;

public class ResumeActivity extends AppCompatActivity {
    public ArrayList<UserInfo> users = new ArrayList<>();
    public ArrayList<ExpenseInfo> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        //expenses hardcoded-->
        ExpenseInfo exp1 = new ExpenseInfo("Expense1", "(10/17/2021)",  101, null);
        expenses.add(exp1);

        CreateUserListFromExpenses();

        RecyclerView list_users = findViewById(R.id.list_users);
        UserAsPayerListAdapter adapter = new UserAsPayerListAdapter(users, this);
        list_users.setHasFixedSize(true);
        list_users.setLayoutManager(new LinearLayoutManager(this));
        list_users.setAdapter(adapter);
    }

    public void CreateUserListFromExpenses(){

    }
}