package cl.eugcom.cajachica.Project.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.service.controls.Control;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import VolleySingleton.VolleySingleton;
import cl.eugcom.cajachica.Project.Model.Category;
import cl.eugcom.cajachica.Project.Model.User;

public class Controller {
    private static User user = new User();
    private static VolleySingleton volley;
    private static RequestQueue frequestQueue;

    // Online list of category
    private static List<Category> incomesList = new ArrayList<>();
    private static List<Category> spendsList = new ArrayList<>();
    // Local list of category
    public static List<String> listIncome = new ArrayList<>();
    public static List<String> listSpend = new ArrayList<>();

    // Init volley variable method
    public static void initialVolley(Context context){
        volley = VolleySingleton.getInstance(context);
        frequestQueue = volley.getmRequestQueue();
    }

    //Check connection
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            return false;
        }
        return true;
    }
    // Volley get
    public static VolleySingleton getVolley(){
        return volley;
    }
    // Request get
    public static RequestQueue getRequestQueue(){
        return frequestQueue;
    }

    // User
    public static void setUser(User u){
        user = u;
    }
    public static User getUser(){
        return user;
    }

    // Fill the local list of categorys
    public static void fillCategorysOnline(Context context){
        initialVolley(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                ConstantValues.URL_LISTA_CATEGORIA_INGRESOS+getUser().getId(),
                null,
                response -> {
                    JSONArray json=response.optJSONArray("imagenes");
                    try {
                        for (int i=0;i<json.length();i++) {
                            JSONObject jsonObject = json.getJSONObject(i);
                            Category ci = new Category();
                            ci.createIn(jsonObject.optString("id_categoria"), jsonObject.optString("nombre_categoria"));
                            incomesList.add(ci);
                            listIncome.add(ci.getNombre_in());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                            Request.Method.GET,
                            ConstantValues.URL_LISTA_CATEGORIA_GASTO+getUser().getId(),
                            null,
                            response1 -> {
                                JSONArray json1=response1.optJSONArray("imagenes");
                                try {
                                    for (int i=0;i<json1.length();i++) {
                                        JSONObject jsonObject1 = json1.getJSONObject(i);
                                        Category cg =new Category();
                                        cg.createGa(jsonObject1.optString("id_categoria_ga"), jsonObject1.optString("nombre_categoria_ga"));
                                        spendsList.add(cg);
                                        listSpend.add(cg.getNombre_ga());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> {
                    } );
                    Controller.getRequestQueue().add(jsonObjectRequest2);
                }, error -> {
        } );
        Controller.getRequestQueue().add(jsonObjectRequest);
    }
}
