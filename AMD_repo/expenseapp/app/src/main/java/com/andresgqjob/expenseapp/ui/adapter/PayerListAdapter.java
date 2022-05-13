package com.andresgqjob.expenseapp.ui.adapter;

import static java.lang.System.*;

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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class PayerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<PayerInfo> listData;//lista de datos
    private final ExpenseActivity activity;//actividad que contiene el recycler view

    public PayerListAdapter(List<PayerInfo> payers, ExpenseActivity activity) {//
        this.listData = (ArrayList<PayerInfo>) payers;
        this.activity = activity;
    }

    @NonNull
    @Override
    //crea la vista sin personalizar
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {//segun el tipo de vista se crea una vista
            case 0: {//si es la vista vacia
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());//crea un inflador
                View listItem = layoutInflater.inflate(R.layout.list_item_empty, parent, false);//crea la vista

                return new ViewHolderEmpty(listItem);//retorna el view holder
            }
            case 1://si es la vista con datos
            default: {//si es la vista con datos
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());//crea un inflador
                View listItem = layoutInflater.inflate(R.layout.list_item_payer, parent, false);//crea la vista

                return new ViewHolderGeneral(listItem);//retorna el view holder
            }
        }
    }

    @Override
    //segun el tipo de vista se carga la vista
    public int getItemViewType(int position) {//segun la posicion del elemento se carga la vista
        if (listData.isEmpty()) {//si la lista esta vacia
            return 0;//retorna la vista vacia
        }
        return 1;//retorna la vista con datos
    }

    @Override
    //Indicamos el número de elementos de la lista
    public int getItemCount() {//retorna la cantidad de elementos
        if (listData.isEmpty()) {//si la lista esta vacia
            return 1;//retorna 1
        }
        return listData.size();//retorna la cantidad de elementos
    }

    @Override
    //Personalizamos el viewholder
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {//segun la posicion del elemento se carga la vista
        switch (viewHolder.getItemViewType()) {//segun el tipo de vista se carga una vista específica
            case 0: {//si es la vista vacia
                ViewHolderEmpty holder = (ViewHolderEmpty) viewHolder;//castea el view holder
                holder.textViewInfo.setText("The list of payers is empty, to fill it use the button 'Add Payer' ");//carga el texto
            }
            break;

            case 1: {//si es la vista con datos
                ViewHolderGeneral holder = (ViewHolderGeneral) viewHolder;//castea el view holder
                final PayerInfo myListData = listData.get(position);//obtiene el elemento de la lista
                holder.textViewDesc.setText(listData.get(position).name);//carga el nombre del elemento
                holder.textViewDate.setText(MessageFormat.format("{0} {1}", listData.get(position).email, listData.get(position).amount));//carga el email y el monto del elemento

                if (listData.get(position).imageUrl.compareTo("") == 0) {//si no tiene imagen
                    holder.imageView.setImageResource(R.drawable.trip);//carga la imagen por defecto
                } else {//si tiene imagen
                    new DownLoadImageTask(holder.imageView).execute(listData.get(position).imageUrl);//carga la imagen
                }

                holder.btnAmount.setText(MessageFormat.format("{0} €", listData.get(position).amount));//carga el monto del elemento
                holder.btnAmount.setOnClickListener(v -> {//si se presiona el boton de monto
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);//crea un dialogo
                    builder.setTitle("Amount for " + myListData.name + ":");//carga el titulo
                    View viewInflated = LayoutInflater.from(activity).inflate(R.layout.amount_input, null, false);//crea la vista
                    final EditText input = viewInflated.findViewById(R.id.input);//obtiene el input
                    input.setText(MessageFormat.format("{0}", myListData.amount));//carga el monto
                    builder.setView(viewInflated);//carga la vista

                    builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {//si se presiona el boton de aceptar
                        dialog.dismiss();//cierra el dialogo
                        String str = input.getText().toString();//obtiene el monto
                        try {//si se puede parsear el monto
                            int number = Integer.parseInt(str);//lo parsea
                            out.println(number);//envia el monto
                            myListData.amount = number;//carga el monto
                            int pos = holder.getAdapterPosition();//obtiene la posicion del elemento
                            PayerListAdapter.this.notifyItemChanged(pos);//notifica la vista
                            activity.updateLabelWarning();//actualiza el label de advertencia
                        } catch (NumberFormatException ex) {//si no se puede parsear el monto
                            ex.printStackTrace();//imprime el error
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());//si se presiona el boton de cancelar
                    builder.show();//muestra el dialogo
                });

                holder.btnDelete.setOnClickListener(v -> new AlertDialog.Builder(activity)//si se presiona el boton de eliminar
                        .setTitle("Do you really want to delete the payer " + myListData.name + "?")//carga el titulo
                        .setMessage("")//carga el mensaje
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {//si se presiona el boton de aceptar
                            // Continue with delete operation
                            int pos = holder.getAdapterPosition();//obtiene la posicion del elemento
                            listData.remove(pos);//elimina el elemento
                            notifyItemRemoved(pos);//notifica la vista
                            activity.updateLabelWarning();//actualiza el label de advertencia
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())//si se presiona el boton de cancelar
                        .setIcon(android.R.drawable.ic_dialog_alert)//carga el icono
                        .show());//muestra el dialogo

                holder.relativeLayout.setOnClickListener(view -> {//si se presiona el elemento
                });
            }
            break;

            default:
                throw new IllegalStateException("Unexpected value: " + viewHolder.getItemViewType());//lanza una excepcion
        }
    }

    // Returns the total count of items in the list
    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {//clase que contiene la vista
        public final ImageView imageView;//imagen
        public final TextView textViewDesc;//texto
        public final TextView textViewDate;//texto
        public final Button btnAmount;//boton de monto
        public final Button btnDelete;//boton de eliminar
        public final RelativeLayout relativeLayout;

        public ViewHolderGeneral(View itemView) {//constructor
            super(itemView);//carga la vista
            this.imageView    = itemView.findViewById(R.id.imageView);//carga la imagen
            this.textViewDesc = itemView.findViewById(R.id.textView_description);//carga la descripcion
            this.textViewDate = itemView.findViewById(R.id.textView_date);//carga la fecha
            this.btnAmount    = itemView.findViewById(R.id.btn_amount);//carga el boton de monto
            this.btnDelete    = itemView.findViewById(R.id.btn_delete);//carga el boton de eliminar
            relativeLayout    = itemView.findViewById(R.id.relativeLayout);//carga el elemento
        }
    }

    // Returns the total count of items in the list
    public static class ViewHolderEmpty extends RecyclerView.ViewHolder {//clase que contiene la vista
        public final TextView textViewInfo;//texto

        //public final ImageView imageView;//imagen
        public ViewHolderEmpty(View itemView) {//constructor
            super(itemView);//carga la vista
            this.textViewInfo = itemView.findViewById(R.id.text_info);//carga el texto
        }
    }

    // Returns the total count of items in the list
    private static class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {//clase que descarga la imagen
        ImageView imageView;//imagen

        //private final WeakReference<ImageView> imageViewReference;//imagen
        public DownLoadImageTask(ImageView imageView) {//constructor
            this.imageView = imageView;//carga la imagen
        }

        // Download the image file in background and set bitmap to the image view
        protected Bitmap doInBackground(String... urls) {//descarga la imagen
            String urlOfImage = urls[0];//carga la url
            Bitmap logo = null;//imagen
            try {//intento
                InputStream is = new URL(urlOfImage).openStream();//abre el stream
                logo = BitmapFactory.decodeStream(is);//carga la imagen
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();//imprime la excepcion
            }
            return logo;//retorna la imagen
        }

        @Override
        // Once image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap result) {//cuando se descarga la imagen
            imageView.setImageBitmap(result);//carga la imagen
        }
    }
}
