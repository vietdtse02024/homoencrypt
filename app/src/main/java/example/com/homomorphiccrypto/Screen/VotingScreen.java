package example.com.homomorphiccrypto.Screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
import example.com.homomorphiccrypto.HomomotphicCrypto.ApiCrypter;
import example.com.homomorphiccrypto.HomomotphicCrypto.Elgamal;
import example.com.homomorphiccrypto.Main.HomeActivity;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class VotingScreen extends BaseFragment {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.voting_screen, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, rootView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        initLayout();
        getVotingData();
        return rootView;
    }

    private void initLayout() {
        toolbar.setTitle("Bầu cử tri");
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 222, Menu.NONE, "Xong").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 222) {
            sendVotingData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getVotingData() {
        d.show();
        personList = new ArrayList<>();
        getPersonDataFromServer();
    }


    private void sendVotingData() {
        try {
            String params = new Gson().toJson(personList).toString();
            Log.e("Origin params: ", params);
            Elgamal elgamal = new Elgamal();
            /* Encrypt */

            /*ApiHelper.sendVotingData(getActivity(), params, new iNWCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("", "SEND VOTING SUCCESS");
                    //change page to result
                    ((HomeActivity) getActivity()).navigationView.setSelectedItemId(R.id.nav_result);
                }

                @Override
                public void onFailed(String error) {
                    Log.e("", "SEND VOTING FAILED: " + error);
                }
            });*/
        } catch (Exception ex) {
            Log.e("", "SEND VOTING FAILED: " + ex.getMessage(), ex);
        }
    }
}
