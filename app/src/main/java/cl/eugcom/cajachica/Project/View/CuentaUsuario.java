package cl.eugcom.cajachica.Project.View;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import cl.eugcom.cajachica.Project.Controller.ConstantValues;
import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.R;

public class CuentaUsuario extends AppCompatActivity {
    public EditText etName, etEmail, etPassword;
    public Button btnSave, btnLogOut, btnDelete;
    public ImageButton btnBack;
    public Intent intentUser;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_usuario);

        // Init
        initVar();

        // Fill data
        fillFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setOnClickListener(v -> onBackPressed());
        btnDelete.setOnClickListener(v -> showDialogOption());
    }

    public void initVar(){
        etName = findViewById(R.id.etCuentaNombre);
        etEmail = findViewById(R.id.etCuentaCorreo);
        etPassword = findViewById(R.id.etCuentaPAssword);
        btnBack = findViewById(R.id.imgBtnCuentaBack);
        btnSave = findViewById(R.id.btnCuentaGuardar);
        btnLogOut = findViewById(R.id.btnCuentaCerrar);
        btnDelete = findViewById(R.id.btnCuentaEliminar);
    }

    public void fillFields(){
        etName.setText(Controller.getUser().getNombre());
        etEmail.setText(Controller.getUser().getCorreo());
        etPassword.setText(Controller.getUser().getPassword());
    }

    private void showDialogOption(){
        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();
        final CharSequence[] opciones = {"Si","No"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿ Desea eliminar su cuenta ?")
                .setPositiveButton(opciones[0], (dialog, id) -> {
                    // Delete sharedPref info
                    SharedPreferences p = getApplicationContext().getSharedPreferences("datos", MODE_PRIVATE);
                    SharedPreferences.Editor et = p.edit();
                    et.clear().apply();

                    // Delete method
                    deleteAcount();

                    // Change to login menu
                    intentUser = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentUser);
                    finish();
                })
                .setNegativeButton(opciones[1], (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });
        builder.show();
    }
    public void deleteAcount(){
        String url = ConstantValues.URL_ELIMINAR_USUARIO+Controller.getUser().getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    if (response.trim().equalsIgnoreCase("eliminaeliminaeliminaeliminaelimina1")){
                        Toast.makeText(this,"Cuenta eliminada",Toast.LENGTH_SHORT).show();
                    }else{
                        if (response.trim().equalsIgnoreCase("noExiste")){
                            Toast.makeText(this,"No se encuentra la persona ",Toast.LENGTH_SHORT).show();
                            Log.i("RESPUESTA: ",""+response);
                        }else{
                            Toast.makeText(this,"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                            //  Log.i("RESPUESTA: ",""+response);
                        }
                    }
                }, error -> {
        });
        Controller.getRequestQueue().add(stringRequest);
    }
}