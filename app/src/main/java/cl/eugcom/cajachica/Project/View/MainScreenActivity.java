package cl.eugcom.cajachica.Project.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import cl.eugcom.cajachica.Project.Controller.ConstantValues;
import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.R;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {
public TextView tvUserName, tvTotalIncome, tvTotalSpend, tvDif, tvIngresoEd, tvGastosEd, tvDiferenciaEd, tvFechaActual;
public ImageButton btnUserMenu;
public Button btnNewIncome, btnNewSpend, btnListIncome, btnListSpend, btnReloadInfo, btnAdmin;
public Intent intentMainMenu;
private ProgressDialog pd;
double i, g, d;
String ingreso = "", gasto = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // Check network
        if(Controller.isNetworkConnected(getApplicationContext())){
            Toast.makeText(this, "Conectado !", Toast.LENGTH_SHORT).show();
            // Update details
            updateDetails();

            // Update list of category
            Controller.fillIncomeCategory(this);
            Controller.fillSpendCategory(this);
        }else{
            Toast.makeText(this, "ups! no tienes internet", Toast.LENGTH_SHORT).show();
        }
        //Init var
        initVar();



        //Update date
        Date d = new Date();
        CharSequence s  = DateFormat.format("d MMMM yyyy ", d.getTime());
        tvFechaActual.setText(s.toString());

        //Update button for ADMIN
        if(Controller.getUser().getTipoUser().equals("1")){
            btnAdmin.setVisibility(View.VISIBLE);
            //Update user name
            tvUserName.setText("Bienvenido "+Controller.getUser().getNombre()+"\nADMINISTRADOR");
        }else{
            btnAdmin.setVisibility(View.INVISIBLE);
            tvUserName.setText("Bienvenido "+Controller.getUser().getNombre());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Changes of Intents
        btnUserMenu.setOnClickListener(this);
        btnNewSpend.setOnClickListener(this);
        btnNewIncome.setOnClickListener(this);
        btnListSpend.setOnClickListener(this);
        btnListIncome.setOnClickListener(this);
        btnReloadInfo.setOnClickListener(this);
        btnAdmin.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imgBtnDetalleUsuario:
                intentMainMenu = new Intent(getApplicationContext(), CuentaUsuario.class);
                startActivity(intentMainMenu);
                finish();
                break;
            case R.id.btnNuevoIngreso:
                intentMainMenu = new Intent(getApplicationContext(), FormIngreso.class);
                startActivity(intentMainMenu);
                break;
            case R.id.btnNuevoGasto:
                intentMainMenu = new Intent(getApplicationContext(), FormGasto.class);
                startActivity(intentMainMenu);
                break;
            case R.id.btnListadoIngresos:
                intentMainMenu = new Intent(getApplicationContext(), ListadoIngreso.class);
                startActivity(intentMainMenu);
                break;
            case R.id.btnListadoGastos:
                intentMainMenu = new Intent(getApplicationContext(), ListadoGastos.class);
                startActivity(intentMainMenu);
                break;
            case R.id.btnReloadInfo:
                updateDetails();
                break;
            case R.id.btnAdmin:
                intentMainMenu = new Intent(getApplicationContext(), CreateNewUser.class);
                startActivity(intentMainMenu);
                break;
        }
    }



    public void initVar(){
        tvUserName = findViewById(R.id.tvSaludoUsuario);
        tvTotalIncome = findViewById(R.id.tvIngresosDetalle);
        tvTotalSpend = findViewById(R.id.tvGastosDetalle);
        btnUserMenu = findViewById(R.id.imgBtnDetalleUsuario);
        btnNewIncome = findViewById(R.id.btnNuevoIngreso);
        btnNewSpend = findViewById(R.id.btnNuevoGasto);
        btnListIncome = findViewById(R.id.btnListadoIngresos);
        btnListSpend = findViewById(R.id.btnListadoGastos);
        tvDif = findViewById(R.id.tvDiferenciaDetalle);
        tvIngresoEd = findViewById(R.id.tvIngresosEdit);
        tvGastosEd = findViewById(R.id.tvGastosEdit);
        tvDiferenciaEd = findViewById(R.id.tvDiferenciaEdit);
        btnReloadInfo = findViewById(R.id.btnReloadInfo);
        tvFechaActual = findViewById(R.id.tvFechaActual);
        btnAdmin = findViewById(R.id.btnAdmin);
    }

    public String removeParenthesis(String txt){
        String r1, rFinal;
        r1 = txt.replace("[", "");
        rFinal = r1.replace("]", "");
        return rFinal;
    }
    
    public void updateDetails(){
        // get Income total
        String urlIncome = ConstantValues.URL_SUMADOR_INGRESOS+Controller.getUser().getId();
        StringRequest srIncome = new StringRequest(Request.Method.GET, urlIncome, response -> {
                ingreso = removeParenthesis(response);

            // Get Spend total
            String urlSpend = ConstantValues.URL_SUMADOR_GASTOS+Controller.getUser().getId();
            StringRequest srSpend = new StringRequest(Request.Method.GET, urlSpend, response1 -> {
                gasto = removeParenthesis(response1);

                fillDetails(ingreso, gasto);

            }, error -> {
                Log.e("TAG_", "error: "+error.toString());
                Toast.makeText(MainScreenActivity.this, "Error: Revise su conexión a internet", Toast.LENGTH_SHORT).show();
            });
            Controller.getRequestQueue().add(srSpend);

        }, error -> {
            Log.e("TAG_", "error: "+error.toString());
            Toast.makeText(this, "No se puede conectar", Toast.LENGTH_SHORT).show();
        });

        //Call both request (income & spend)
        Controller.getRequestQueue().add(srIncome);

    }
    public void fillDetails(String ing, String gas){
        Locale locale = new Locale("es", "CL");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        // Fill incomes
        if(ing.equals("null")){
            i = 0;
            tvIngresoEd.setText(currencyFormatter.format(i));
        }else{
            i = Double.parseDouble(ing);
            tvIngresoEd.setText(currencyFormatter.format(i));
        }
        // Fill Spends
        if(gas.equals("null")){
            g = 0;
            tvGastosEd.setText(currencyFormatter.format(g));
        }else{
            g = Double.parseDouble(gas);
            tvGastosEd.setText(currencyFormatter.format(g));
        }
        d = i-g;
        tvDiferenciaEd.setText(currencyFormatter.format(d));
    }

    private void showDialogOption() {
        final CharSequence[] opciones = {"Si","No"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿ Desea cerrar la sesion?")
                .setPositiveButton(opciones[0], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete sharedPref info
                        SharedPreferences p = getApplicationContext().getSharedPreferences("datos", MODE_PRIVATE);
                        SharedPreferences.Editor et = p.edit();
                        et.clear().apply();

                        // Change to login menu
                        intentMainMenu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentMainMenu);
                        finish();
                    }
                })
                .setNegativeButton(opciones[1], new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}