package example.com.homomorphiccrypto.Screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.Adapter.ResultAdapter;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
import example.com.homomorphiccrypto.HomomotphicCrypto.InfoDevices;
import example.com.homomorphiccrypto.HomomotphicCrypto.StringCipher;
import example.com.homomorphiccrypto.Main.MainApplication;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class ResultScreen extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.result_screen, container, false);
        setHasOptionsMenu(false);
        unbinder = ButterKnife.bind(this, rootView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        initLayout();
        getResultData();
        return rootView;
    }

    private void initLayout() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }


    private void getResultData() {
        d.show();
        //generateFakeData();
        personList = new ArrayList<>();
        getResultDataFromServer();
    }
}
