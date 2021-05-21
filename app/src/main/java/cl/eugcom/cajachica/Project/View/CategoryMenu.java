package cl.eugcom.cajachica.Project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Go back
        btnBack.setOnClickListener(v -> {
            finish();
        });
        // Save category
        btnSave.setOnClickListener(v -> {
            if(validateForm()){
                category = new Category();
                String catvalue = etCategoryName.getText().toString().trim();
                if(chkSpend.isChecked()){
                   
                }
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
        }else{
            return true;
        }
    }
}