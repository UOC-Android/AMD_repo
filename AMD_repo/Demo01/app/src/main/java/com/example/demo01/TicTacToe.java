package com.example.demo01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;


public class TicTacToe extends AppCompatActivity implements View.OnClickListener { //implements View.OnClickListener is used to implement the onClick method
    //Tic Tac Toe Game
    //Atributes
    private final Button[][] board = new Button[3][3]; //3x3 bidimensional array of buttons
    // EMPTY variable to represent an empty cell
    private final String EMPTY = "";
//    // X variable to represent an X cell
//    private final String X = "X";
//    // O variable to represent an O cell
//    private final String O = "O";
//    // currentPlayer variable to keep track of the current player
//    private String currentPlayer = X;

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    //Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Construimos el identificador del boton con las posiciones de la
                // matriz bidimensional en una cadena de texto
                String buttonID = "button_" + i + j;
                // Obtenemos el identificador del boton convirtiendo la cadena de texto en un
                // numero entero mediante la funcion getResources() y getIdentifier()
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                // resID es el identificador del resucros que tenemos que pasar para encontrarlo
                // por el identificador
                board[i][j] = findViewById(resID);
                // Esta es la forma de asignar referencias a todos nuestros botones sin tene que hacerlo uno por uno
                // y también haremos que el escuchador de eventos sea el mismo para la reescritura de todos los botones
                board[i][j].setOnClickListener(this); //this es el objeto que implementa el escuchador de eventos general de la clase
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    // El metodo de clic se llamará cuando hacemos clic en cualquiera de nuestros nueve botones
    @Override
    public void onClick(View v) {
        // Verificamos si el boton en el que se hizo clic contiene una cadena de texto vacia,
        // y si ese no es el caso, significa que ya se usó antes de que haya una X o una O en
        // en esta celda
        if (!((Button) v).getText().toString().equals(EMPTY)) {
            return;
        }
        // Si el boton en el que se hizo clic contiene una cadena de texto vacia, significa que
        // no hay ninguna X o O en esta celda, por lo que podemos asignarle una X o una O, en este caso
        // dependiendo del turno del jugador, concretamente al jugador 1 le establecemoss una X y al jugador 2
        // un O
        if (player1Turn) {
            ((Button) v).setText("X"); //player1 with X
        }
        else {
            ((Button) v).setText("O"); //player2 with O
        }

        roundCount++; //increment

        // Verificamos si alguno de los jugadores ha ganado la partida o si hay un empate y si es así
        // mostramos un mensaje de victoria o empate y reiniciamos la partida
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            }
            else {
                player2Wins();
            }
        }
        // Si en nueve rondas no se ha ganado la partida, entonces hay un empate y
        // mostramos un mensaje de empate y reiniciamos la partida.
        else if (roundCount == 9) {
            draw();
        }
        // Si no se han llegado a las nueve rondas, entonces cambiamos el turno del jugador.
        else {
            player1Turn = !player1Turn;
        }
    }

    // En este metodo tenemos que revisar y contrastar todas nuestras filas y columnas,
    // es decir, las diagonales para ver si alguno de los jugadores ha ganado la partida.
    // Para ello, guardaremos en una variable la cadena de texto que contiene el boton en el que se hizo clic
    // y luego compararemos esa cadena de texto con la cadena de texto que contiene el boton en la misma
    // posición en la matriz bidimensional.
    private boolean checkForWin() {
        String[][] field = new String[3][3];
// i = fila, j = columna
//        [0][0] [0][1] [0][2]
//        [1][0] [1][1] [1][2]
//        [2][0] [2][1] [2][2]

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Guardamos en la matriz bidimensional el valor de la celda en la que se hizo clic
                field[i][j] = board[i][j].getText().toString();
            }
        }

        // Comparamos en filas si O, X o vacio
        for (int i = 0; i < 3; i++) {
            // Verificamos si en la fila i, la cadena de texto que contiene el boton en el que se hizo clic
            // es igual a la cadena de texto que contiene el boton en la misma posición en la matriz bidimensional
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals(EMPTY)) {
                return true;
            }
        }

        // Comparamos en columnas si O, X o vacio
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    // comparamos la columna 0 con lo que hay en la columna 1 y 2.
                    && field[0][i].equals(field[2][i])
                    // comparamos si el campo 0 está vacío, la cuestión es asegurarse
                    // que en las celdas no hay vacío para confirmar si hay o no ganador.
                    && !field[0][i].equals(EMPTY)) {
                return true;
            }
        }

        // Esto basicamente pasa por iterar en diagonal,
        // de arriba a abajo e izquierda y derecha en la matriz bidimensional.
        // Para así comparar la celda 0,0 con la 0,1 y 0,2 y así sucesivamente.
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals(EMPTY)) {
            return true;
        }

        return field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals(EMPTY);
    }

    // Este método se ejecuta cuando el jugador 1 ha ganado la partida.
    // Lo que hace este método es acumular y actualizar los puntos de cada ronda ganada.
    // Mostrar un mensaje de victoria y reiniciar la partida.
    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Este método se ejecuta cuando el jugador 2 ha ganado la partida.
    // Lo que hace este método es acumular y actualizar los puntos de cada ronda ganada.
    // Mostrar un mensaje de victoria y reiniciar la partida.
    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    // Este método se ejecuta cuando hay un empate.
    // Lo que hace este método es borrar las marcas del tablero y reiniciar la partida.
    // Además de mostrar un mensaje de redibujar el tablero de nuevo.
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    // Este método pinta cada celda del tablero con un espacio vacío.
    // Para así empezar de nuevo la partida. Tambien reinicia los puntos de cada jugador.
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    // Este método se ejecuta cuando se presiona el botón de reiniciar.
    // Lo que hace este método es reiniciar los puntos de cada jugador y reiniciar el tablero.
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    // Este método actualiza los puntos de cada jugador en el tablero.
    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    @Override
    // Este método se ejecuta cuando se presiona el botón de reiniciar.
    // Lo que hace este método es reiniciar los puntos de cada jugador y reiniciar el tablero.
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    // Este método se ejecuta cuando se presiona el botón de reiniciar.
    // Lo que hace este método es reiniciar los puntos de cada jugador y reiniciar el tablero.
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");

        updatePointsText();
    }
}