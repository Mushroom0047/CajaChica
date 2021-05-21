package cl.eugcom.cajachica.Project.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.eugcom.cajachica.Project.Controller.ConstantValues;
import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.User;
import cl.eugcom.cajachica.R;

public class MainActivity extends AppCompatActivity  {
public EditText etEmail, etPassword;
public TextView tvAvisos;
public Button btnIngresar;
@SuppressLint("UseSwitchCompatOrMaterialCode")
public Switch swKeepSesion;
private ProgressDialog pd;
public String name, id, passw, id_tipo, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(this);
        //Check sharedPref & init auto
        SharedPreferences p = getApplicationContext().getSharedPreferences("datos", MODE_PRIVATE);
        if(!p.getString("id", "").equals("")){
            name = p.getString("name", "");
            id = p.getString("id", "");
            passw = p.getString("password", "");
            id_tipo = p.getString("id_tipo", "");
            email = p.getString("correo", "");

            createUser(id, name, passw, id_tipo, email);
            changeToAct();
        }

        //Initialize variables
        initVar();

        //Create connection with DB
        Controller.initialVolley(getApplicationContext());

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmptyFields()){
                    makerRequest();
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void initVar(){
        btnIngresar = findViewById(R.id.btn_ingresar);
        etEmail = findViewById(R.id.etCorreo);
        etEmail.setText("");
        etPassword = findViewById(R.id.etPassword);
        etPassword.setText("");
        tvAvisos = findViewById(R.id.tvAvisos);
        tvAvisos.setTextColor(Color.RED);
        swKeepSesion = findViewById(R.id.swMantenerInicio);
        //swKeepSesion.setVisibility(View.INVISIBLE);
    }

    public boolean validateEmptyFields(){
        if(!etEmail.getText().toString().trim().isEmpty()){
            if(!etPassword.getText().toString().trim().isEmpty()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void changeToAct(){
        // Change to activity
        Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
        startActivity(intent);
    }


    public void makerRequest(){
        String correo = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String url = ConstantValues.URL_LOGIN+"correo=" + correo + "&password=" + pass;
        pd.setMessage("Buscando usuario...");
        pd.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("datos");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        //tvLogin.setText(jsonObject.optString("nombre"));

                        //Save data
                        name = jsonObject.optString("nombre");
                        id = jsonObject.optString("id");
                        email = jsonObject.optString("correo");
                        id_tipo = jsonObject.optString("id_tipo_user");
                        passw = jsonObject.optString("password");

                        // Save data on shared
                        if(swKeepSesion.isChecked()){
                            Log.i("TAG_", id + name + id_tipo + email + passw);
                            SharedPreferences prefe = getApplicationContext().getSharedPreferences("datos", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefe.edit();
                            editor.putString("id", id);
                            editor.putString("name", name);
                            editor.putString("password", passw);
                            editor.putString("id_tipo", id_tipo);
                            editor.putString("email", email);
                            editor.apply();
                        }

                        // If the user exist will be create
                        createUser(id, name, passw, id_tipo, email);
                        pd.dismiss();

                        // change to activity
                        changeToAct();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    pd.dismiss();
                    // Server error
                    if(error instanceof ServerError){
                        tvAvisos.setText("Error en el Servidor");
                    }else if(error instanceof NoConnectionError){
                        tvAvisos.setText("Error de conexión");
                    }else{
                        Toast.makeText(getApplicationContext(), "Usuario no encontrado", Toast.LENGTH_LONG).show();
                        tvAvisos.setText("");
                    }
                });
        Controller.getRequestQueue().add(jsonObjectRequest);
    }
    public void createUser(String id, String nombre, String pass, String id_tipo, String correo){
        User usuario = new User();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setPassword(pass);
        usuario.setTipoUser(id_tipo);
        usuario.setNombre(nombre);
        Controller.setUser(usuario);
        // Inform with toast
        Toast.makeText(getApplicationContext(), "Bienvenido "+usuario.getNombre(), Toast.LENGTH_SHORT).show();
    }

}