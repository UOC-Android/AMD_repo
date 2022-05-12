package com.andresgqjob.expenseapp.ui.adapter;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.PayerInfo;
import com.andresgqjob.expenseapp.ui.ExpenseActivity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class PayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<PayerInfo> listData;
    private final ExpenseActivity activity;

    public PayerListAdapter(ArrayList<PayerInfo> payers, ExpenseActivity activity) {
        this.listData = payers;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View listItem = layoutInflater.inflate(R.layout.list_item_empty, parent, false);

                return new ViewHolderEmpty(listItem);
            }
            case 1:
            default: {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View listItem = layoutInflater.inflate(R.layout.list_item_payer, parent, false);

                return new ViewHolderGeneral(listItem);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listData.size() == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        if (listData.size() == 0) {
            return 1;
        }
        return listData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case 0: {
                ViewHolderEmpty holder = (ViewHolderEmpty) viewHolder;
                holder.textView_Info.setText("The list of payers is empty, to fill it use the button 'Add Payer' ");
            }
            break;
            case 1: {
                ViewHolderGeneral holder = (ViewHolderGeneral) viewHolder;
                final PayerInfo myListData = listData.get(position);
                holder.textView_Desc.setText(listData.get(position).name);
                holder.textView_Date.setText(listData.get(position).email + " " + listData.get(position).amount);

                if (listData.get(position).image_url.compareTo("") == 0) {
                    holder.imageView.setImageResource(R.drawable.trip);
                } else {
                    new DownLoadImageTask(holder.imageView).execute(listData.get(position).image_url);
                }

                holder.btn_amount.setText("" + listData.get(position).amount + " €");
                holder.btn_amount.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Amount for " + myListData.name + ":");
                    View viewInflated = LayoutInflater.from(activity).inflate(R.layout.amount_input, (ViewGroup) null, false);
                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                    input.setText("" + myListData.amount);
                    builder.setView(viewInflated);

                    builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        String str = input.getText().toString();
                        try {
                            int number = Integer.parseInt(str);
                            System.out.println(number);
                            myListData.amount = number;
                            int pos = holder.getAdapterPosition();
                            PayerListAdapter.this.notifyItemChanged(pos);
                            activity.updateLabelWarning();
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }

                    });
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                    builder.show();
                });

                holder.btn_delete.setOnClickListener(v -> new AlertDialog.Builder(activity)
                        .setTitle("Do you really want to delete the payer " + myListData.name + "?")
                        .setMessage("")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Continue with delete operation
                            int pos = holder.getAdapterPosition();
                            listData.remove(pos);
                            notifyItemRemoved(pos);
                            activity.updateLabelWarning();
                        })

                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show());

                holder.relativeLayout.setOnClickListener(view -> {
                    //Toast.makeText(view.getContext(),"click on item: "+myListData.name,Toast.LENGTH_LONG).show();
                    //listdata[holder.getAdapterPosition()].setDescription("KAKAK");
                    //notifyItemChanged(holder.getAdapterPosition());
                });
            }
        }
    }

    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView_Desc;
        public TextView textView_Date;
        public Button btn_amount;
        public Button btn_delete;
        public RelativeLayout relativeLayout;

        public ViewHolderGeneral(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView_Desc = (TextView) itemView.findViewById(R.id.textView_description);
            this.textView_Date = (TextView) itemView.findViewById(R.id.textView_date);
            this.btn_amount = (Button) itemView.findViewById(R.id.btn_amount);
            this.btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
        }
    }

    public static class ViewHolderEmpty extends RecyclerView.ViewHolder {
        public TextView textView_Info;

        public ViewHolderEmpty(View itemView) {
            super(itemView);
            this.textView_Info = (TextView) itemView.findViewById(R.id.text_info);

        }
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
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
