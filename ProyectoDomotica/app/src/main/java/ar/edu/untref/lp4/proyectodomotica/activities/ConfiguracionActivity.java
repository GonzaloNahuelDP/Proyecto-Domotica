package ar.edu.untref.lp4.proyectodomotica.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Locale;

import ar.edu.untref.lp4.proyectodomotica.R;
import ar.edu.untref.lp4.proyectodomotica.controladores.ControladorBaseDatos;

public class ConfiguracionActivity extends ActionBarActivity {

    private ImageButton bVaciarBD;
    private ImageButton bDomotica;
    private ImageButton bEspanol;
    private ImageButton bIngles;
    private boolean lleno = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        preferences = getSharedPreferences("du_preferences", Context.MODE_PRIVATE);

        bVaciarBD = (ImageButton) findViewById(R.id.boton_vaciar_bd);
        bDomotica = (ImageButton) findViewById(R.id.boton_domotica);
        bEspanol = (ImageButton) findViewById(R.id.boton_espanol);
        bIngles = (ImageButton) findViewById(R.id.boton_ingles);

        bEspanol.setImageResource(R.drawable.bandera_spa);
        bIngles.setImageResource(R.drawable.bandera_ing);
        bVaciarBD.setImageResource(R.drawable.tacho);
        bDomotica.setImageResource(R.drawable.info_domotica);

        baseLlena();

        // Comportamiento boton domotica
        bDomotica.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage(R.string.mensaje_domotica)
                        .setTitle(getString(R.string.domotica))
                        .setCancelable(true)
                        .setNeutralButton(getString(R.string.entendido),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        // Comportamiento boton vaciar datos
        bVaciarBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (baseLlena()) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfiguracionActivity.this);
                    alertDialog.setTitle(R.string.eliminar_datos_bd);
                    alertDialog.setMessage(R.string.nombre_habitacion);
                    alertDialog.setMessage(R.string.mensaje_confirmacion);

                    alertDialog.setPositiveButton(getString(R.string.aceptar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    ControladorBaseDatos.limpiarBD();
                                    bVaciarBD.setImageResource(R.drawable.tacho);
                                    lleno = false;
                                    Toast.makeText(ConfiguracionActivity.this, R.string.datos_bd, Toast.LENGTH_SHORT).show();

                                }
                            });

                    alertDialog.setNegativeButton(getString(R.string.cancelar),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
            }
        });

        // Comportamiento boton español
        bEspanol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resources res = ConfiguracionActivity.this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(Locale.getDefault().getLanguage());
                res.updateConfiguration(conf, dm);

                preferences.edit().putString("idioma", "espaniol").apply();
                Toast.makeText(ConfiguracionActivity.this, getString(R.string.cambio_idioma), Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        // Comportamiento boton ingles
        bIngles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Resources res = ConfiguracionActivity.this.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();
                conf.locale = new Locale(Locale.US.getLanguage());
                res.updateConfiguration(conf, dm);

                preferences.edit().putString("idioma", "ingles").apply();
                Toast.makeText(ConfiguracionActivity.this, getString(R.string.cambio_idioma_ingles), Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    // Consulta si hay alguna habitacion agregada
    private boolean baseLlena() {

        if (ControladorBaseDatos.getTodasHabitaciones().size() > 0) {

            bVaciarBD.setImageResource(R.drawable.tacho_lleno);
            lleno = true;

        } else {

            bVaciarBD.setImageResource(R.drawable.tacho);
            lleno = false;
        }

        return lleno;

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
