package cl.eugcom.cajachica.Project.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
            Log.i("TAG_", info.toString()+"|"+ info.isConnected() + "|"+ info.isAvailable());
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


    // Request User
    public static void setUser(User u){
        user = u;
    }
    public static User getUser(){
        return user;
    }

    //Send request
    public static void sendCategoryRequest(){

    }
    // Check if income exist
    public static Boolean ifExistIncome(String income){
        boolean result = false;
        for(int i=0;i<listIncome.size();i++){
            if(listIncome.get(i).equals(income)){
                result = true;
                break;
            }
        }
        return result;
    }
    // Check if spend exist
    public static Boolean ifExistSpend(String spend){
        boolean result = false;
        for(int i=0;i<listSpend.size();i++){
            if(listSpend.get(i).equals(spend)){
                result = true;
                break;
            }
        }
        return result;
    }

    public static void fillIncomeCategory(Context context) {
        initialVolley(context);
        String[] data = {"", ""};
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                ConstantValues.URL_LISTA_CATEGORIA_INGRESOS + getUser().getId(),
                null,
                response -> {
                    JSONArray json = response.optJSONArray("imagenes");
                    try {
                        listIncome.clear();
                        incomesList.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsonObject = json.getJSONObject(i);
                            data[0] = jsonObject.optString("id_categoria");
                            data[1] = jsonObject.optString("nombre_categoria");
                            Category ci = new Category();
                            ci.createIn(data[0], data[1]);
                            incomesList.add(ci);
                            listIncome.add(ci.getNombre_in());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
        });
        Controller.getRequestQueue().add(jsonObjectRequest);
    }
    public static void fillSpendCategory(Context context) {
        initialVolley(context);
        String[] data = {"", ""};
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                ConstantValues.URL_LISTA_CATEGORIA_GASTO + getUser().getId(),
                null,
                response -> {
                    JSONArray json = response.optJSONArray("imagenes");
                    try {
                        listSpend.clear();
                        spendsList.clear();
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsonObject = json.getJSONObject(i);
                            data[0] = jsonObject.optString("id_categoria_ga");
                            data[1] = jsonObject.optString("nombre_categoria_ga");
                            Category cg = new Category();
                            cg.createGa(data[0], data[1]);
                            spendsList.add(cg);
                            listSpend.add(cg.getNombre_ga());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
        });
        Controller.getRequestQueue().add(jsonObjectRequest);
    }
}
