package example.com.homomorphiccrypto.BaseClasses;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.Unbinder;
import example.com.homomorphiccrypto.Adapter.PersonAdapter;
import example.com.homomorphiccrypto.Adapter.ResultAdapter;
import example.com.homomorphiccrypto.HomomotphicCrypto.ApiCrypter;
import example.com.homomorphiccrypto.Main.HomeActivity;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class BaseFragment extends Fragment {
    protected View rootView;
    protected RecyclerView recyclerView;
    protected KProgressHUD d;
    protected Unbinder unbinder;
    protected List<Person> personList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        d = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    protected void getPersonDataFromServer() {
        d.show();
        ApiHelper.getPersonData(getActivity(), new iNWCallback() {
            @Override
            public void onSuccess(String result) {
                d.dismiss();

                try {
                    if ("EXISTED".equals(result)) {
                        ((HomeActivity) getActivity()).navigationView.setSelectedItemId(R.id.nav_result);
                    } else {
                        JSONObject obj = new JSONObject(result);
                        JSONArray arrayDatas = (JSONArray) obj.get("data");
                        for (int i = 0; i < arrayDatas.length(); i++) {
                            JSONObject jb = arrayDatas.getJSONObject(i);
                            Person person = new Person();
                            person.setId(jb.getInt("ID"));
                            person.setDescription(jb.getString("CONTENT"));
                            personList.add(person);
                        }
                        PersonAdapter adapter = new PersonAdapter(getActivity(), personList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(String error) {
                d.dismiss();
                Log.e("", "Get person data failed: " + error);
            }
        });
    }

    protected void getResultDataFromServer() {
        d.show();
        ApiHelper.getResultData(getActivity(), new iNWCallback() {
            @Override
            public void onSuccess(String result) {
                d.dismiss();
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray arrayDatas = (JSONArray) obj.get("data");
                    int totalCount = obj.getInt("total");
                    /*for (int i = 0; i < arrayDatas.length(); i++) {
                        JSONObject jb = arrayDatas.getJSONObject(i);
                        *//*personList.add(new Person(jb.getInt("PERSON_ID"), jb.getString("FULL_NAME") + "-" + jb.getString("BIRTH_DATE"),
                                jb.getString("ADDRESS"), jb.getString("IMAGE_URL"), jb.getInt("RESULT") + "/" + totalCount));*//*
                    }*/
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                ResultAdapter adapter = new ResultAdapter(getActivity(), personList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailed(String error) {
                d.dismiss();
                Log.e("", "Get person data failed: " + error);
            }
        });
    }

    public String getMacAdress(Context ctx) {
        WifiManager m_wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        String m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
        return m_wlanMacAdd;
    }
}
