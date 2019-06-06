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

    }

    protected void getResultDataFromServer() {

    }

    public String getMacAdress(Context ctx) {
        WifiManager m_wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        String m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
        return m_wlanMacAdd;
    }
}
