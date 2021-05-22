package cl.eugcom.cajachica.Project.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import cl.eugcom.cajachica.Project.Controller.ConstantValues;
import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.Category;
import cl.eugcom.cajachica.R;

public class CategoryMenu extends AppCompatActivity {
public ImageButton btnBack;
public EditText etCategoryName;
public Button btnSave;
public CheckBox chkIncome, chkSpend;
Intent intent;
public Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_menu);
        // Initialize variables
        initVar();
        Controller.initialVolley(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Go back
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        // Save category
        btnSave.setOnClickListener(v -> {
            if(validateForm()){
                if(chkIncome.isChecked()){
                    if(Controller.ifExistIncome(etCategoryName.getText().toString().trim())){
                        //Toast.makeText(this, "La categoria ya existe !", Toast.LENGTH_LONG).show();
                        etCategoryName.setError("La categoria ya existe");
                    }else{
                        sendAddCatRequest();
                    }
                }
                if(chkSpend.isChecked()){
                    if(Controller.ifExistSpend(etCategoryName.getText().toString().trim())){
                        //Toast.makeText(this, "La categoria ya existe !", Toast.LENGTH_LONG).show();
                        etCategoryName.setError("La categoria ya existe");
                    }else{
                        sendAddCatRequest();
                    }
                }

            }else{
                etCategoryName.setError("Ingrese una categoria");
            }
        });
        chkIncome.setOnClickListener(v ->{
            chkSpend.setChecked(false);
        });
        chkSpend.setOnClickListener(v ->{
            chkIncome.setChecked(false);
        });
    }
    public void initVar(){
        btnBack = findViewById(R.id.imgBtnAtrasCategory);
        etCategoryName = findViewById(R.id.etNombreCat);
        btnSave = findViewById(R.id.btnGuardarCategoria);
        chkIncome = findViewById(R.id.chkIngreso);
        chkSpend = findViewById(R.id.chkGasto);
    }
    public boolean validateForm(){
        if(etCategoryName.getText().toString().trim().isEmpty()){
            etCategoryName.setError("Ingrese categoria");
            etCategoryName.requestFocus();
            return false;
        }else if(chkSpend.isChecked() || chkIncome.isChecked()){
            return true;
        }else{
            return false;
        }
    }

    public void sendAddCatRequest(){
        String cat = etCategoryName.getText().toString().trim();
        String url = "";
        if(chkIncome.isChecked()){
            url = ConstantValues.URL_REGISTRAR_CATEGORIA_INGRESO+""+"&nombre_categoria="+cat+"&id="+Controller.getUser().getId();
        }
        if(chkSpend.isChecked()){
            url = ConstantValues.URL_REGISTRAR_CATEGORIA_GASTO+""+"&nombre_categoria_ga="+cat+"&id="+Controller.getUser().getId();
        }
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                response->
                {
                    Log.i("TAG_", "Categoria ingresada !");
                }, error ->{
            //Log.i("TAG_", "Error response");
            Toast.makeText(this, "Categoria ingresada", Toast.LENGTH_LONG).show();
            etCategoryName.setText("");
        });
        Controller.getRequestQueue().add(jsonObject);
    }
}