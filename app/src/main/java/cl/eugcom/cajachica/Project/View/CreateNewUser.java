package cl.eugcom.cajachica.Project.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import cl.eugcom.cajachica.Project.Model.DatePickerFragment;
import cl.eugcom.cajachica.R;

public class CreateNewUser extends AppCompatActivity {
    public EditText etNombre, etApellido, etFechaNac, etPass1, etPass2, etCorreo;
    public Switch swAdmin;
    public Button btnRegistrar, btnLimpiar;
    public ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        // Init var
        initVar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        btnRegistrar.setOnClickListener(v ->{
            if(validateForm()){
                if(etPass1.getText().toString().trim().equals(etPass2.toString().trim())){

                }else{
                    etPass2.setError("Las contraseñas no coinciden");
                }
            }
        });
        btnLimpiar.setOnClickListener(v ->{
            clearForm();
        });
        etFechaNac.setOnClickListener(v -> {
            showDatePickerDialog();
        });
    }

    public void initVar(){
        etNombre = findViewById(R.id.etNewNombre);
        etApellido = findViewById(R.id.etNewApellido);
        etFechaNac = findViewById(R.id.etNewFechaNac);
        etPass1 = findViewById(R.id.etNewPassword);
        etPass2 = findViewById(R.id.etNewPassword2);
        etCorreo = findViewById(R.id.etCorreo);
        swAdmin = findViewById(R.id.swTipoUsuario);
        btnRegistrar = findViewById(R.id.btnNewRegistrar);
        btnLimpiar = findViewById(R.id.btnNewLimpiar);
        btnBack = findViewById(R.id.btnNewBack);
    }
    public boolean validateForm(){
        boolean res = false;
        if(etNombre.getText().toString().trim().isEmpty()){
            etNombre.setError("Ingrese un nombre");
        }else{
            if(etApellido.getText().toString().trim().isEmpty()){
                etApellido.setError("Ingrese un apellido");
            }else{
                if(etFechaNac.getText().toString().trim().isEmpty()){
                    etFechaNac.setError("Ingrese la fecha de nacimiento");
                }else{
                    if(etCorreo.getText().toString().trim().isEmpty()){
                        etCorreo.setError("Ingrese un correo");
                    }else{
                        etNombre.setError("");
                        etApellido.setError("");
                        etFechaNac.setError("");
                        etCorreo.setError("");
                        res = true;
                    }
                }
            }
        }
        return res;
    }
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 because January is zero
            final String selectedDate = day + " / " + (month + 1) + " / " + year;
            etFechaNac.setText(selectedDate);
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void clearForm(){
        etNombre.setText("");
        etApellido.setText("");
        etFechaNac.setText("");
        etPass1.setText("");
        etPass2.setText("");
        swAdmin.setChecked(false);
    }
    public void saveUser(){
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String fechaNAc = etFechaNac.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String pass1 = etPass1.getText().toString().trim();
        String pass2 = etPass2.getText().toString().trim();
        String tipoUser = (swAdmin.isChecked())? "1":"2";
        /*String url = ConstantValues.URL_REGISTRO_USUARIO+"id= "+"&nombre="+nombre+"&apellido="+ apellido +
                "&fechaNacimiento=" + fechaNac+"&correo="+ correo+"&password="+ pass1
                +"&confirmarClave="+ pass2+"&id_tipo_user="+tipoUser;*/
    }
}