package com.andresgqjob.expenseapp.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.PayerInfo;
import com.andresgqjob.expenseapp.model.UserInfo;
import com.andresgqjob.expenseapp.ui.adapter.PayerListAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;


public class ExpenseActivity extends AppCompatActivity {
    EditText txtAmount;
    EditText txtDate;
    EditText txtDescription;
    TextView lblWarning;
    Button btnAddPayer;
    Button btnSave;
    ArrayList<UserInfo> users;
    ArrayList<PayerInfo> payers = new ArrayList<>();
    PayerListAdapter adapter;
    Spinner payerSpinner;
    Integer totalAmount;
    ProgressBar progressBar;

    int spinnerCurrentIndexSelected = 0;

    //To debug
    boolean savedCorrectly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        txtAmount      = findViewById(R.id.txtf_amount);
        txtDate        = findViewById(R.id.txtf_date);
        txtDescription = findViewById(R.id.txtf_description);
        btnAddPayer    = findViewById(R.id.btn_add_payer);
        lblWarning     = findViewById(R.id.lbl_warning);
        lblWarning.setVisibility(View.INVISIBLE);
        progressBar    = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        btnSave = findViewById(R.id.btn_expense_save);
        btnSave.setOnClickListener(v -> {//Disaparador que detecta pulsar por el usuario y guarda el gasto
            boolean showWarning = false;//y si hay algun campo vacio
            String infoWarning = "";//da un mensaje de error

            if (payers.isEmpty()) {//Si no hay nadie que pague
                showWarning = true;//muestra un mensaje de error
                infoWarning = "Antes de guardar, tienes que agregar al menos un payer";//
            } else {//per si hay al menos un pago
                int totalAmount = 0;//(Total de pagos var)
                for (PayerInfo payer : payers) {//se recorren los pagos
                    totalAmount += payer.amount;//y suma el total
                }
                String sTotalAmount = txtAmount.getText().toString();//luego se obtiene el total de los pagos para mostrarlos en el label

                try {//Intenta convertir el total de los pagos a entero
                    int number = Integer.parseInt(sTotalAmount);//Convierte el total de los pagos a entero

                    if (totalAmount != number) {//Si el total de los pagos no es igual al total de los pagos
                        lblWarning.setVisibility(View.VISIBLE);//se muestra un mensaje de error
                        infoWarning = "Cuidao, la suma de todos los payers (" + totalAmount + "€)";
                        infoWarning += "tiene que ser (" + number + "€)";
                        showWarning = true;//(Mostrar mensaje de error)
                    } else {//Si el total de los pagos es igual al total de los pagos
                        savedCorrectly = true;//se guardar correctamente
                        finish();//se cierra la actividad
                    }
                } catch (NumberFormatException ex) {//Si no se puede convertir el total de los pagos a entero
                    ex.printStackTrace();//se imprime el error
                }
            }

            if (showWarning) {//Si hay algun campo vacio
                new AlertDialog.Builder(ExpenseActivity.this)//se crea un dialogo de alerta
                        .setTitle("Error guardando el gasto")//Titulo del dialogo
                        .setMessage(infoWarning)//Mensaje del dialogo
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {//Boton aceptar
                            // Continue with delete operation
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)//Icono del dialogo
                        .show();//Mostrar el dialogo
            } else {//Si no hay algun campo vacio
                progressBar.setVisibility(View.VISIBLE);//se muestra la barra de progreso
                btnSave.setEnabled(false);//se desactiva el boton de guardar
                doConnection();//y se hace la conexion
            }
        });

        Bundle extras = getIntent().getExtras();//Obtiene los extras

        if (extras != null) {//Si hay extras
            String description = extras.getString("Descripción");//se obtiene la descripcion
            String date = extras.getString("Fecha");// se obtiene la fecha
            totalAmount = extras.getInt("Monto");//y se obtiene el total de los pagos

            txtDescription.setText(description);//Añade la descripcion
            txtDate.setText(date);//Añade la fecha
            txtAmount.setText(MessageFormat.format("{0}", totalAmount));//Añade el total de los pagos

            users = extras.getParcelableArrayList("Users");//Finalmente se obtienen a los usuarios
        }

        //Si no hay extras
        ArrayAdapter<UserInfo> adapterSpinner = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, users);//
        payerSpinner = findViewById(R.id.payer_spinner);//se obtiene el spinner de los pagos
        payerSpinner.setAdapter(adapterSpinner);//se añade el adapter al spinner
        payerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//se crea un listener para el spinner
            @Override//y cuando se selecciona un item
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {//se obtienen los parametros
                spinnerCurrentIndexSelected = i;//y se obtiene el indice del spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//Cuando no se selecciona nada
                spinnerCurrentIndexSelected = -1;//se obtiene el indice del spinner
            }
        });

        btnAddPayer.setOnClickListener(v -> {//Disaparador que detecta pulsar por el usuario y se crea un listener para el boton de añadir pagos
            UserInfo user = users.get(spinnerCurrentIndexSelected);//se obtiene el usuario seleccionado
            boolean userAdded = false;//Variable para saber si el usuario ya esta en la lista
            for (PayerInfo payer : payers) {//se recorre la lista de pagos
                if (payer.name.compareTo(user.name) == 0) {//y si el nombre del usuario es igual al nombre del usuario seleccionado
                    userAdded = true;//pues el usuario ya esta en la lista
                    break;//y entonces termina el ciclo for
                }
            }

            if (!userAdded) {//Si el usuario no esta en la lista
                int amount = 0;//Variable para almacenar el monto
                if (payers.isEmpty()) {//si la lista de pagos esta vacia
                    amount = totalAmount;//entonces el monto es el total de los pagos
                }

                PayerInfo newPayer = new PayerInfo("", user.name, "", amount);//Se crea un nuevo pagador
                payers.add(newPayer);//y se añade el nuevo pagador a la lista

                if (payers.size() == 1) {//Si la lista de pagos tiene un solo elemento
                    adapter.notifyItemChanged(0);//se notifica que el elemento cambio
                } else {//y si la lista de pagos tiene mas de un elemento
                    adapter.notifyItemInserted(payers.size() - 1);//entonces se notifica que se añadió un elemento
                }
                updateLabelWarning();//Actualiza la etiqueta de advertencia
            } else {//Si el usuario ya esta en la lista
                new AlertDialog.Builder(ExpenseActivity.this)//se crea un dialogo de alerta
                        .setTitle("El usuario " + user.name + " ya ha sido agregado")//un titulo del dialogo
                        .setMessage("")//un mensaje del dialogo
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {//y tambien se crea un boton de aceptar
                            // Continue with delete operation
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)//se crea un icono del dialogo
                        .show();//y finalmente se muestra el dialogo
            }
        });

        //Crea un listener para el boton de eliminar pagos
        String path1 = "https://m.media-amazon.com/images/M/MV5BNzUxNjM4ODI1OV5BMl5BanBnXkFtZTgwNTEwNDE2OTE@._V1_SX150_CR0,0,150,150_.jpg";
        String path2 = "https://m.media-amazon.com/images/M/MV5BMTUyMDU1MTU2N15BMl5BanBnXkFtZTgwODkyNzQ3MDE@._V1_SX150_CR0,0,150,150_.jpg";
        String path3 = "https://m.media-amazon.com/images/M/MV5BMTk1MjM5NDg4MF5BMl5BanBnXkFtZTcwNDg1OTQ4Nw@@._V1_SX150_CR0,0,150,150_.jpg";
        String path4 = "https://m.media-amazon.com/images/M/MV5BMjExNjY5NDY0MV5BMl5BanBnXkFtZTgwNjQ1Mjg1MTI@._V1_SX150_CR0,0,150,150_.jpg";

        RecyclerView recyclerView = findViewById(R.id.payer_list);//Obtiene el recycler view
        adapter = new PayerListAdapter(payers, this);//Crea un nuevo adaptador
        recyclerView.setHasFixedSize(true);//Establece que el recycler view tiene un tamaño fijo
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//Establece que el recycler view es de tipo linear
        recyclerView.setAdapter(adapter);//Establece el adaptador del recycler view
    }

    public void updateLabelWarning() {//Actualiza la etiqueta de advertencia
        if (payers.isEmpty()) {//Si la lista de pagos esta vacia
            lblWarning.setVisibility(View.INVISIBLE);//La etiqueta de advertencia se oculta
        } else {//Si la lista de pagos no esta vacia
            int totAmount = 0;//Variable para almacenar el monto total
            for (PayerInfo payer : payers) {//se recorre la lista de pagos
                totAmount += payer.amount;//y se suma el monto de cada pagador
            }
            String sTotalAmount = txtAmount.getText().toString();//Obtiene el monto total

            try {//Intenta
                int number = Integer.parseInt(sTotalAmount);//convertir el monto total a un entero
                if (totAmount != number) {//Si el monto total no es igual al monto total
                    lblWarning.setVisibility(View.VISIBLE);//La etiqueta de advertencia se muestra
                    String info = "Cuidao, la suma de todos los payers (" + totAmount + "€)";//Variable para almacenar la informacion
                    info += "\ntiene que ser (" + number + "€)";//Agrega la informacion
                    lblWarning.setText(info);//Establece la informacion
                } else {//Si el monto total es igual al monto total
                    lblWarning.setVisibility(View.INVISIBLE);//La etiqueta de advertencia se oculta
                }
            } catch (NumberFormatException ex) {//Captura la excepcion
                ex.printStackTrace();//Imprime la traza de la excepcion
            }
        }
    }

    public void doConnection() {//Realiza la conexion
        final Handler handler = new Handler(Looper.getMainLooper());//Crea un nuevo manejador de bucle principal
        handler.postDelayed(() -> {//Ejecuta una accion despues de un tiempo
            progressBar.setVisibility(View.INVISIBLE);//La barra de progreso se oculta
            btnSave.setEnabled(true);//El boton de guardar se habilita

            if (savedCorrectly) {//Si se guardo correctamente
                Toast.makeText(ExpenseActivity.this, "Gasto guardao correctamente", Toast.LENGTH_LONG).show();//Muestra un mensaje de exito
            } else {//Si no se guardo correctamente
                Toast.makeText(ExpenseActivity.this, "Error al intentar guardar el gasto", Toast.LENGTH_LONG).show();//Muestra un mensaje de error
                savedCorrectly = true;//Se establece que se guardo correctamente
            }
        }, 1500); //Tiempo de espera en 1,5 segundos
    }
}