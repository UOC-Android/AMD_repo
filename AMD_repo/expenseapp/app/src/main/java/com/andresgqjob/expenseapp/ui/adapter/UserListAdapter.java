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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.UserInfo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private final ArrayList<UserInfo> listData;
    private Context activityContext;

    public UserListAdapter(List<UserInfo> listData, Context context) {
        this.listData = (ArrayList<UserInfo>) listData;
        this.activityContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserInfo myListData = listData.get(position);

        if (listData.get(position).url_avatar.compareTo("") == 0) {
            holder.imageView.setImageResource(R.drawable.user_avatar);
        } else {
            new DownLoadImageTask(holder.imageView).execute(listData.get(position).url_avatar);
        }

        holder.relativeLayout.setOnClickListener(view -> {
            //Toast.makeText(view.getContext(),"click on item: "+myListData.decription,Toast.LENGTH_LONG).show();
            //listdata[holder.getAdapterPosition()].setDescription("KAKAK");
            //notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
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
