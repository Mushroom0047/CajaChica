package cl.eugcom.cajachica.Project.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.DatePickerFragment;
import cl.eugcom.cajachica.R;

import static android.Manifest.permission.CAMERA;

public class FormGasto extends AppCompatActivity implements View.OnClickListener {
    public Spinner spinCatSpend;
    public Button btnNewCat, btnAttachPhoto, btnSave, btnClear;
    public ImageButton btnBack;
    public EditText etValue, etDate, etDescription;
    Intent intentGasto;
    public ImageView imgCamaraGas;

    private static final int COD_SELECCIONADO = 10;
    private static final int REQUEST_PERMISION_CAMERA = 101;
    public Bitmap bitmap;
    private final int PHOTO_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_gasto);
        //Inicialize variables
        initVar();


        // Fill the spinner with categorys
        spinCatSpend.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Controller.listSpend));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fill category list
        Controller.fillSpendCategory(getApplicationContext());

        // Go back
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

    public void initVar(){
        spinCatSpend = findViewById(R.id.spinnerCategoriaGasto);
        btnNewCat = findViewById(R.id.btnNuevaCatGasto);
        btnAttachPhoto = findViewById(R.id.btnAdjFotoGasto);
        btnSave = findViewById(R.id.btnGuardarGasto);
        btnClear = findViewById(R.id.btnLimpiarGasto);
        btnBack = findViewById(R.id.imgBtnAtrasNuevoGasto);
        etValue = findViewById(R.id.etValorGasto);
        etDate = findViewById(R.id.etFechaGasto);
        etDescription = findViewById(R.id.etDescripsionGasto);
        imgCamaraGas = findViewById(R.id.imgCamaraGasto);
    }

    public boolean validateForm(){
        if(!etValue.getText().toString().trim().isEmpty()
                && !etDate.getText().toString().trim().isEmpty()
                && !spinCatSpend.getSelectedItem().toString().equals("Seleccione")){
            return false;
        }else{
            return true;
        }
    }

    public void clearForm(){
        etValue.setText("");
        etDate.setText("");
        etDescription.setText("");
        spinCatSpend.setSelection(0);
        imgCamaraGas.setImageBitmap(null);
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
    private void openCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, PHOTO_CODE);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnNuevaCatGasto:
                intentGasto = new Intent(getApplicationContext(), CategoryMenu.class);
                startActivity(intentGasto);
                break;
            case R.id.btnGuardarGasto:
                if(!validateForm()){
                    Toast.makeText(getApplicationContext(), "Valido", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Faltan datos", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnLimpiarGasto:
                clearForm();
                break;
            case R.id.imgBtnAtrasNuevoGasto:
                intentGasto = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(intentGasto);
                break;
            case R.id.etFechaGasto:
                showDatePickerDialog();
                break;
            case R.id.btnAdjFotoGasto:
                showDialogOption();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imgCamaraGas.setImageBitmap(bitmap);
                    break;
                case COD_SELECCIONADO:
                    Uri miPath=data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imgCamaraGas.setImageURI(miPath);
                    Cursor cursor = getApplicationContext().getContentResolver().query(miPath, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),miPath);
                        imgCamaraGas.setImageBitmap(bitmap);
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
        builder.setTitle("Elige una Opci??n");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if(opciones[i].equals("Tomar foto")){
                //Check sdk version
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ActivityCompat.checkSelfPermission(FormGasto.this, CAMERA)== PackageManager.PERMISSION_GRANTED){
                        openCamara();
                    }else{
                        ActivityCompat.requestPermissions(FormGasto.this, new String[]{CAMERA}, REQUEST_PERMISION_CAMERA);
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
}