package cl.eugcom.cajachica.Project.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.ListByUser;
import cl.eugcom.cajachica.R;

public class ListadoIngreso extends AppCompatActivity {
    public ImageButton btnBack;
    public ListView lvIncomeList;

    public Intent intentForm;
    ArrayList<ListByUser> userList = new ArrayList<>();
    ArrayList<String> listToShow = new ArrayList<>();
    ProgressDialog pd;
    public ArrayAdapter<String> adapter;
    private static final String URL_INGRESOS = "http://movil.ventascloud.cl/ConsultarListaIngreso.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_ingreso);
         pd = new ProgressDialog(this);

        //Create connection with DB
        Controller.initialVolley(getApplicationContext());

        //Init var
        initVar();

        // Load data from web service
        loadIncomes();


    }

    @Override
    protected void onResume() {
        super.onResume();
        btnBack.setOnClickListener(v ->{
            intentForm = new Intent(getApplicationContext(), MainScreenActivity.class);
            startActivity(intentForm);
            finish();
        });
    }

    public void initVar(){
        btnBack = findViewById(R.id.imgBtnAtrasListadoIngresos);
        lvIncomeList = findViewById(R.id.lvListadoIngresos);
    }

    public void loadIncomes(){
        String url = URL_INGRESOS + Controller.getUser().getId();
        pd.setMessage("Cargando...");
        pd.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response ->{
                    JSONArray json=response.optJSONArray("imagenes");
                    try {
                        for (int i=0;i<json.length();i++) {
                            ListByUser usuario = new ListByUser();

                            JSONObject jsonObject = json.getJSONObject(i);
                            usuario.setId_igreso(jsonObject.optString("id_ingreso"));
                            usuario.setId_usuario(jsonObject.optString("id"));
                            usuario.setImg_ingreso(jsonObject.optString("img_in"));
                            usuario.setNombre_foto(jsonObject.optString("nombre_img"));
                            usuario.setValor_ingreso(jsonObject.optString("valor_in"));
                            usuario.setFecha_ingreso(jsonObject.optString("fecha_in"));
                            userList.add(usuario);
                            Log.i("TAG_", String.valueOf(userList.size()));
                        }
                        //Fill the list to show
                        fillList();
                        pd.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error->{
            pd.dismiss();
                    Toast.makeText(this, "Error: Revise su conexión a internet", Toast.LENGTH_SHORT).show();
                });
        Controller.getRequestQueue().add(jsonObjectRequest);
    }
    public void fillList(){
        listToShow.clear();
        for (ListByUser l:userList) {
            listToShow.add(l.getFecha_ingreso() +"\n$ "+l.getValor_ingreso());
            //Log.i("INFO_", l.getDocumento() +": $"+l.getPrecio());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listToShow);
        lvIncomeList.setAdapter(adapter);
    }
}