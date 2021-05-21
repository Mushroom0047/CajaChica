package cl.eugcom.cajachica.Project.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.Category;
import cl.eugcom.cajachica.Project.Model.DatePickerFragment;
import cl.eugcom.cajachica.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormIngreso extends AppCompatActivity implements View.OnClickListener{
    public Spinner spinCatIncome;
    public Button btnNewCat, btnAttachPhoto, btnSave, btnClear;
    public ImageButton btnBack;
    public EditText etValue, etDate, etDescription;
    public Intent intentForm;
    public ImageView imgCamaraIng;

    private static final String URL_REG = "http://movil.ventascloud.cl/RegistroIngreso.php?";
    private static final int COD_SELECCIONADO = 10;
    private static final int REQUEST_PERMISION_CAMERA = 101;
    public Bitmap bitmap;
    private final int PHOTO_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ingreso);
        //Set adapter
        Controller.initialVolley(getApplicationContext());

        //Inicialize variables
        initVar();
        spinCatIncome.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Controller.listIncome));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Go Back to main menu
        btnBack.setOnClickListener(this);
        // Go to Add new category menu
        btnNewCat.setOnClickListener(this);
        // Attach photo
        btnAttachPhoto.setOnClickListener(this);
        // Save Form
        btnSave.setOnClickListener(this);
        // Clear Form
        btnClear.setOnClickListener(this);
        // Set date
        etDate.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnNuevaCatIngreso:
                intentForm = new Intent(getApplicationContext(), CategoryMenu.class);
                startActivity(intentForm);
            break;
            case R.id.btnGuardarIngreso:
                if(!validateForm()){
                    //sendPostRequest();
                }else{
                    Toast.makeText(getApplicationContext(), "Faltan datos", Toast.LENGTH_LONG).show();
                }
            break;
            case R.id.btnLimpiarIngreso:
                clearForm();
            break;
            case R.id.imgBtnAtrasNuevoIngreso:
                intentForm = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(intentForm);
                finish();
            break;
            case R.id.etFechaIngreso:
                showDatePickerDialog();
            break;
            case R.id.btnAdjFotoIngreso:
                showDialogOption();
            break;
        }
    }

    public void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                etDate.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void initVar(){
        spinCatIncome = findViewById(R.id.spinnerCategoriaIngreso);
        etValue = findViewById(R.id.etValorIngreso);
        etDate = findViewById(R.id.etFechaIngreso);
        etDescription = findViewById(R.id.etDescripsionIngreso);
        btnNewCat = findViewById(R.id.btnNuevaCatIngreso);
        btnAttachPhoto = findViewById(R.id.btnAdjFotoIngreso);
        btnSave = findViewById(R.id.btnGuardarIngreso);
        btnClear = findViewById(R.id.btnLimpiarIngreso);
        btnBack = findViewById(R.id.imgBtnAtrasNuevoIngreso);
        imgCamaraIng = findViewById(R.id.imgCamaraIngreso);
    }

    public boolean validateForm(){
        if(!etValue.getText().toString().trim().isEmpty()
        && !etDate.getText().toString().trim().isEmpty()
        && !spinCatIncome.getSelectedItem().toString().equals("Seleccione")){
            return false;
        }else{
            return true;
        }
    }

    public void clearForm(){
        etValue.setText("");
        etDate.setText("");
        etDescription.setText("");
        spinCatIncome.setSelection(0);
        imgCamaraIng.setImageBitmap(null);
    }

    private void openCamara(){
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if(intent.resolveActivity(getPackageManager())!=null){
           startActivityForResult(intent, PHOTO_CODE);
       }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgCamaraIng.setImageBitmap(bitmap);
                    break;
                case COD_SELECCIONADO:
                    Uri miPath=data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imgCamaraIng.setImageURI(miPath);
                    Cursor cursor = getApplicationContext().getContentResolver().query(miPath, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),miPath);
                        imgCamaraIng.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cursor.close();
                    break;
            }
            bitmap=redimensionarImagen(bitmap,600,800);
        }
    }

    private void showDialogOption() {
        final CharSequence[] opciones = {"Tomar foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if(opciones[i].equals("Tomar foto")){
                //Check sdk version
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(FormIngreso.this, CAMERA)==PackageManager.PERMISSION_GRANTED){
                        openCamara();
                    }else{
                        ActivityCompat.requestPermissions(FormIngreso.this, new String[]{CAMERA}, REQUEST_PERMISION_CAMERA);
                    }
                }else{
                    openCamara();
                }
            }
            if (opciones[i].equals("Elegir de Galeria")){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONADO);
            }else{
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            openCamara();
        }else{
            Toast.makeText(this, "Necesita habilitar los permisos", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }

   /* public void sendPostRequest(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REG, response -> {
            Toast.makeText(getApplicationContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
            if (response.trim().equalsIgnoreCase("registra")){
                Toast.makeText(getApplicationContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(getApplicationContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String nombre =etDescription.getText().toString().trim();
                String id=Controller.getUser().getId();
                String id_categoria=idCategoria;
                String Valor=valor.getText().toString();
                String fecha=Fecha.getText().toString();
                String imagen=convertirImgString(bitmap);
                String fechaHora=fechaAct.getText().toString();


                Map<String,String> parametros=new HashMap<>();
                parametros.put("id",id);
                parametros.put("id_categoria",id_categoria);
                parametros.put("img_in",imagen);
                parametros.put("nombre_img",nombre);
                parametros.put("valor_in",Valor);
                parametros.put("fecha_in",fecha);
                parametros.put("fecha_hora", fechaHora);

                return parametros;
            }
        };
        Controller.getRequestQueue().add(stringRequest);
    }*/
}