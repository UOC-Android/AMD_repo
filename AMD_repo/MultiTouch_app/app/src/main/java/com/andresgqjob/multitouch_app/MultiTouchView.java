package com.andresgqjob.multitouch_app;

// getX: Devuelve la coordenada X de la posición de la vista en la que se encuentra el puntero.
// getY: Devuelve la coordenada Y de la posición de la vista en la que se encuentra el puntero.
// getPointerCount: Devuelve el número de puntos de contacto que tiene el evento.
// getPointerId: Devuelve el identificador de un punto de contacto.
// getAction: Devuelve el tipo de acción de evento.
// getActionIndex: Devuelve el índice del punto de contacto que generó el evento.
// ACTION_DOWN: El usuario toca la pantalla.
// ACTION_MOVE: El usuario mueve el dedo sobre la pantalla.
// ACTION_UP: El usuario suelta el dedo de la pantalla.
// ACTION_CANCEL: El usuario toca la pantalla y luego lo suelta.
// ACTION_POINTER_DOWN: El usuario toca un punto de contacto.
// ACTION_POINTER_UP: El usuario suelta un punto de contacto.
// ACTION_OUTSIDE: El usuario toca

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

// La clase MultiTouchView hereda a su vez de la clase View, esta representa el bloque de construcción
// básico para los componentes de la interfaz de usuario. Una Vista ocupa un área rectangular en la
// pantalla y es responsable del dibujo y manejo de eventos. View es la clase base para los widgets
// que se utilizan para crear componentes de interfaz de usuario interactivos (botones, campos de texto...)
// La subclase ViewGroup es la clase base para diseños, que son contenedores invisibles que contienen
// otras vistas (u otros grupos de vistas) y definen sus propiedades de diseño.
// onTouchListener es la interfaz que invoca una devolución de llamada cuando se envía un evento
// táctil a esta vista.
public class MultiTouchView extends View implements View.OnTouchListener {
    private static final int SIZE_CIRCLE = 200;
    private static final int SIZE_TEXT = 190;
    // La clase Paint contiene la información de estilo y color sobre
    // cómo dibujar geometrías, texto y mapas de bits.
    private final Paint paint;
    private final Paint paintText;
    private final int[] colors = {Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW};
    // CountDownTimer: clase que permite crear un temporizador que cuenta atrás.
    // Las llamadas a onTick(long) se sincronizan con este objeto para que nunca se produzca una
    // llamada a onTick(long) antes de que se complete el callback anterior. Esto solo es relevante
    // cuando la implementación de onTick(long) requiere una cantidad de tiempo para ejecutarse que
    // es significativa en comparación con el intervalo de cuenta atrás.
    CountDownTimer cTimer = null;
    ArrayList<Vector2D> map = new ArrayList<>();
    TextView lbl_info = null;
    Button btn_restart = null;
    private int colorIndex = 0;
    private int playerSelected = -1;

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 0, 0);

        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(SIZE_TEXT);
        // setOnTouchListener: Registra un callback para que se invoque cuando se envíe un evento táctil a esta vista.
        setOnTouchListener(this);
    }

    public void initView(MainActivity activity) {
        btn_restart = activity.findViewById(R.id.btn_restart);
        lbl_info = activity.findViewById(R.id.lbl_info);
        lbl_info.setText("");
        btn_restart.setVisibility(INVISIBLE);
        btn_restart.setOnClickListener(v -> {
            map.clear();
            playerSelected = -1;
            invalidate();
            btn_restart.setVisibility(INVISIBLE);
            lbl_info.setText("");
        });
    }

    // La clase Canvas contiene las llamadas "dibujar". Para dibujar algo,
    // se necesita 4 componentes básicos: un mapa de bits para contener los píxeles,
    // un lienzo para albergar las llamadas de dibujo (escribiendo en el mapa de bits),
    // una primitiva de dibujo (por ejemplo, Rect, ruta, texto, mapa de bits)
    // y una pintura (para dibujar). describir los colores y estilos para el dibujo).
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas); // Se llama cuando la vista debe mostrar su contenido.

        if (playerSelected == -1) {
            for (int i = 0; i < map.size(); i++) {
                int x = map.get(i).posX;
                int y = map.get(i).posY;
                int c = map.get(i).color;
                paint.setColor(c);
                canvas.drawCircle(x, y, SIZE_CIRCLE, paint);
            }
        } else {
            int theOthers = 2;
            String text = "";
            for (int i = 0; i < map.size(); i++) {
                int x = map.get(i).posX;
                int y = map.get(i).posY;
                if (i == playerSelected) {
                    paint.setColor(Color.RED);
                    text = "1";
                } else {
                    paint.setColor(Color.BLACK);
                    text = "" + theOthers;
                    theOthers++;
                }
                canvas.drawCircle(x, y, SIZE_CIRCLE, paint);
                canvas.drawText(text, x - (float) (SIZE_TEXT * 0.25), y + (float) (SIZE_TEXT * 0.25), paintText);
            }
        }
    }

    // onTouch: Se llama cuando se envíe un evento táctil a esta vista.
    // MotionEvent: Objeto utilizado para informar eventos de movimiento (ratón, lápiz, dedo).
    // Los eventos de movimiento pueden contener movimientos absolutos o relativos y otros datos,
    // según el tipo de dispositivo.
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (playerSelected != -1) {
            return true;
        }
        // getActionIndex: Para ACTION_POINTER_DOWN o ACTION_POINTER_UP como lo devuelve getActionMasked(),
        // esto devuelve el índice de puntero asociado. El índice se puede usar con
        // getPointerId(int), getX(int), getY(int), getPressure(int)y getSize(int) para obtener
        // información sobre el puntero que ha subido o bajado.
        int pointerIndex = event.getActionIndex();
        // getPointerId: Devuelve el identificador de puntero asociado con un índice de datos de puntero
        // particular en este evento. El identificador le indica el número de puntero real
        // asociado con los datos, teniendo en cuenta los punteros individuales que
        // suben y bajan desde el inicio del gesto actual.
        int pointerID = event.getPointerId(pointerIndex);
        // getActionMasked: Devuelve la acción enmascarada que se está realizando, sin información
        // de índice de puntero. Úse getActionIndex() para devolver el índice
        // asociado con las acciones del puntero.
        int maskedAction = event.getActionMasked();
        Log.i("touch_andres", "" + pointerID);
        switch (maskedAction) {
            // ACTION_POINTER_DOWN: Constante para getActionMasked(): Un puntero no primario ha caído.
            // Úse getActionIndex() para recuperar el índice del puntero que cambió.
            // El índice está codificado en los ACTION_POINTER_INDEX_MASK bits de la acción
            // desenmascarada devuelta por getAction().
            case MotionEvent.ACTION_POINTER_DOWN:
                // ACTION_DOWN: Constante para getActionMasked(): Ha comenzado un gesto presionado, el movimiento
                // contiene la ubicación de posicion inicial.
                // Este también es un buen momento para verificar el estado del botón para distinguir
                // los clics de botón secundarios y terciarios y manejarlos adecuadamente.
                // Úse getButtonState() para recuperar el estado del botón.
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX(pointerIndex);
                int y = (int) event.getY(pointerIndex);
                Vector2D newPoint = new Vector2D(x, y, pointerID, colors[colorIndex % colors.length]);
                colorIndex++;
                map.add(newPoint);

                if (map.size() > 1) {
                    if (cTimer != null) {
                        cTimer.cancel(); // Cancelar la cuenta atrás.
                    }
                    lbl_info.setText("Calculating player positions in...3");
                    cTimer = new CountDownTimer(3000, 1000) {
                        // La devolución de llamada disparada a intervalos regulares.
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000);
                            lbl_info.setText("Calculating player positions in..." + seconds);
                        }

                        // La devolucion de llamada se activa cuando se acaba el tiempo.
                        public void onFinish() {
                            btn_restart.setVisibility(VISIBLE);
                            lbl_info.setText("");

                            int min = 0;
                            int max = map.size();
                            Random random = new Random();
                            playerSelected = random.nextInt(max + min) + min;

                            invalidate();
                            if (cTimer != null) {
                                cTimer.cancel();
                            }
                        }
                    };
                    cTimer.start(); // Comienza la cuenta atrás.
                }
            }
            break;
            // ACTION_MOVE: Constante para getActionMasked(): Ha ocurrido un cambio durante un gesto de presión
            // (entre ACTION_DOWN y ACTION_UP). El movimiento contiene el punto más reciente, así
            // como cualquier punto intermedio desde el último evento de bajada o movimiento.
            case MotionEvent.ACTION_MOVE: {
                int size = event.getPointerCount();
                for (int i = 0; i < size; i++) {
                    boolean found = false;
                    int index = 0;
                    while (!found && index < map.size()) {
                        if (map.get(index).ID == event.getPointerId(i)) {
                            map.get(index).posX = (int) event.getX(i);
                            map.get(index).posY = (int) event.getY(i);
                            found = true;
                        }
                        index++;
                    }
                }
            }
            break;
            // ACTION_UP: Constante para getActionMasked(): Ha finalizado un gesto presionado, el movimiento
            // contiene la ubicación de liberación final, así como cualquier punto intermedio desde
            // el último evento de bajada o movimiento.
            case MotionEvent.ACTION_UP:
                // ACTION_POINTER_UP: Constante para getActionMasked(): un puntero no primario ha subido.
                // Úselo getActionIndex()para recuperar el índice del puntero que cambió.
                // El índice está codificado en los ACTION_POINTER_INDEX_MASK bits de la acción
                // desenmascarada devuelta por getAction().
            case MotionEvent.ACTION_POINTER_UP:
                // ACTION_CANCEL: Constante para getActionMasked(): El gesto actual ha sido abortado. No recibirás
                // más puntos en él. Debe tratar esto como un evento ascendente, pero no realizar
                // ninguna acción que normalmente haría.
            case MotionEvent.ACTION_CANCEL:
                lbl_info.setText("");
                if (cTimer != null) {
                    cTimer.cancel();
                }
                boolean found = false;
                int index = 0;
                while (!found && index < map.size()) {
                    if (map.get(index).ID == pointerID) {
                        map.remove(index);
                        found = true;
                    }
                    index++;
                }
                break;
            default:
                break;
        }
        // Invalidar toda la vista. Si la vista está visible, se llamará a onDraw en algún momento
        // en el futuro. Esto debe llamarse desde un subproceso de interfaz de usuario. Para llamar
        // desde un subproceso que no sea de interfaz de usuario, llamar a postInvalidate().
        invalidate();

        return true;
    }
}
