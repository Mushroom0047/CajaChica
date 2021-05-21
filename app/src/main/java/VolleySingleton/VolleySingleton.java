package VolleySingleton;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mVolley = null;

    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }
    public static VolleySingleton getInstance(Context context){
        if(mVolley == null){
            mVolley = new VolleySingleton(context);
        }
        return mVolley;
    }

    public RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }
}
