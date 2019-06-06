package example.com.homomorphiccrypto.Network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.UUID;

import example.com.homomorphiccrypto.HomomotphicCrypto.InfoDevices;

public class ApiHelper {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void getPersonData(Activity activity, final iNWCallback callback){
        if(!isNetworkAvailable(activity)){
            callback.onFailed("No internet access");
            return;
        }
        String deviceId = new InfoDevices().getInfo();
        String URL  = ApiEndPoint.URL_GET_QUESTION_DATA + "?deviceId=" + deviceId;
        AndroidNetworking.get(URL)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailed(anError.getErrorDetail());
                    }
                });
    }

    public static void getResultData(Activity activity, final iNWCallback callback){
        if(!isNetworkAvailable(activity)){
            callback.onFailed("No internet access");
            return;
        }

        String URL  = ApiEndPoint.URL_GET_RESULT_DATA;
        AndroidNetworking.get(URL)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailed(anError.getErrorDetail());
                    }
                });
    }

    public static void sendVotingData(Activity activity, String params, final iNWCallback callback){
        if(!isNetworkAvailable(activity)){
            callback.onFailed("No internet access");
            return;
        }
        String deviceId = new InfoDevices().getInfo();
        String URL  = ApiEndPoint.URL_SEND_VOTING_DATA;
        AndroidNetworking.post(URL)
                .addBodyParameter("params", params)
                .addBodyParameter("deviceId", deviceId)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        callback.onFailed(anError.getErrorDetail());
                    }
                });
    }
}
