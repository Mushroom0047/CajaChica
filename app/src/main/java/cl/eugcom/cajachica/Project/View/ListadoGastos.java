package cl.eugcom.cajachica.Project.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cl.eugcom.cajachica.Project.Controller.Controller;
import cl.eugcom.cajachica.Project.Model.ListByUser;
import cl.eugcom.cajachica.R;

public class ListadoGastos extends AppCompatActivity {
    public ImageButton btnBack;
    public ListView lvSpendList;
    public Intent intentForm;
    ArrayList<ListByUser> userList = new ArrayList<>();
    ArrayList<String> listToShow = new ArrayList<>();
    ProgressDialog pd;
    public ArrayAdapter<String> adapter;
    private static final String URL_GASTOS = "http://movil.ventascloud.cl/ConsultarListaGasto.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_gastos);
        pd = new ProgressDialog(this);

        //Create connection with DB
        Controller.initialVolley(getApplicationContext());

        //Init var
        initVar();

        // Load data from web service
        loadSpend();
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
        btnBack = findViewById(R.id.imgBtnAtrasListadoGastos);
        lvSpendList = findViewById(R.id.lvListadoGastos);
    }

    public void loadSpend(){
        String url = URL_GASTOS + Controller.getUser().getId();
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
                            usuario.setId_gasto(jsonObject.optString("id_gasto"));
                            usuario.setId_usuario(jsonObject.optString("id"));
                            usuario.setImg_gasto(jsonObject.optString("img_ga"));
                            usuario.setNombre_foto(jsonObject.optString("nombre_foto"));
                            usuario.setValor_gasto(jsonObject.optString("valor_ga"));
                            usuario.setFecha_gasto(jsonObject.optString("fecha_ga"));
                            userList.add(usuario);
                            //Log.i("TAG_", String.valueOf(userList.size()));
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
            listToShow.add(l.getFecha_gasto() +"\n$ "+l.getValor_gasto());
            //Log.i("INFO_", l.getDocumento() +": $"+l.getPrecio());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listToShow);
        lvSpendList.setAdapter(adapter);
    }
}