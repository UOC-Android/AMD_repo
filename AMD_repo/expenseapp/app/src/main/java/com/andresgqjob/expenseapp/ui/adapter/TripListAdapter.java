package com.andresgqjob.expenseapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.TripInfo;
import com.andresgqjob.expenseapp.ui.TripViewActivity;

import java.io.InputStream;
import java.net.URL;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.ViewHolder> {
    private final TripInfo[] listdata;
    private final Context activityContext;

    public TripListAdapter(TripInfo[] listdata, Context context) {
        this.listdata = listdata;
        this.activityContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_trip, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TripInfo myListData = listdata[position];
        holder.textView_Desc.setText(listdata[position].decription);
        holder.textView_Date.setText(listdata[position].date);

        if (listdata[position].image_url.compareTo("") == 0) {
            holder.imageView.setImageResource(R.drawable.trip);
        } else {
            new DownLoadImageTask(holder.imageView).execute(listdata[position].image_url);
        }

        holder.relativeLayout.setOnClickListener(view -> {
            Intent k = new Intent(activityContext, TripViewActivity.class);
            k.putExtra("Description", myListData.decription);
            k.putExtra("Date", myListData.date);

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
        public Button btn_Amount;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.btn_Amount = (Button) itemView.findViewById(R.id.btn_amount);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
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

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
