package com.andresgqjob.expenseapp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.UserInfo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class UserAsPayerListAdapter extends RecyclerView.Adapter<UserAsPayerListAdapter.ViewHolder> {
    private final ArrayList<UserInfo> listdata;
    private final Context activityContext;

    public UserAsPayerListAdapter(List<UserInfo> listdata, Context context) {
        this.listdata = (ArrayList<UserInfo>) listdata;
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
        final UserInfo myListData = listdata.get(position);
        holder.txt_user.setText(myListData.name);
        holder.txt_amount.setText(myListData.amountPayed);
        holder.txt_toPayOrToReceive.setText(myListData.toPayOrToReceive);

        if (listdata.get(position).url_avatar.compareTo("") == 0) {
            holder.imageView.setImageResource(R.drawable.user_avatar);
        } else {
            new DownLoadImageTask(holder.imageView).execute(listdata.get(position).url_avatar);
        }

        holder.relativeLayout.setOnClickListener(view -> {
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView txt_user;
        public final TextView txt_amount;
        public final TextView txt_toPayOrToReceive;
        public final RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_user = itemView.findViewById(R.id.textView_description);
            this.txt_amount = itemView.findViewById(R.id.textView_amount);
            this.txt_toPayOrToReceive = itemView.findViewById(R.id.textView_date);
            this.imageView = itemView.findViewById(R.id.imageView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    private static class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
