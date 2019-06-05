package example.com.homomorphiccrypto.Screen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.Adapter.PersonAdapter;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
import example.com.homomorphiccrypto.HomomotphicCrypto.ApiCrypter;
import example.com.homomorphiccrypto.HomomotphicCrypto.Elgamal;
import example.com.homomorphiccrypto.Main.HomeActivity;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class VotingScreen extends BaseFragment {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.btGetQuestion)
    Button btGetQuestion;
    @BindView(R.id.btYes)
    Button btYes;
    @BindView(R.id.btNo)
    Button btNo;
    @BindView(R.id.lnlQuestion)
    LinearLayout lnlQuestion;
    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.voting_screen, container, false);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, rootView);
        initLayout();
        return rootView;
    }

    private void initLayout() {
        toolbar.setTitle("Bầu cử tri");
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

        lnlQuestion.setVisibility(View.GONE);

        btGetQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVotingData();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.add(Menu.NONE, 222, Menu.NONE, "Xong").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 222) {
//            sendVotingData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getVotingData() {
        d.show();
        personList = new ArrayList<>();
        ApiHelper.getPersonData(getActivity(), new iNWCallback() {
            @Override
            public void onSuccess(String result) {
                d.dismiss();
                try {
                    if ("EXISTED".equals(result)) {
                        ((HomeActivity) getActivity()).navigationView.setSelectedItemId(R.id.nav_result);
                    } else {
                        JSONObject obj = new JSONObject(result);
                        Log.e("TAG", "json: " + obj.toString());
                        JSONArray arrayDatas = (JSONArray) obj.get("data");
                        for (int i = 0; i < arrayDatas.length(); i++) {
                            JSONObject jb = arrayDatas.getJSONObject(i);
                            Person person = new Person();
                            person.setId(jb.getInt("ID"));
                            person.setDescription(jb.getString("CONTENT"));
                            displayQuestion(person);
                        }
                        Log.e("TAG", "" + personList.size());
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

    private void displayQuestion(Person person){
        lnlQuestion.setVisibility(View.VISIBLE);
        tvQuestion.setText(person.getDescription());
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer(1);
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAnswer(0);
            }
        });
    }

    private void sendAnswer(int b){
        //call api
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn đã thực hiện xong");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
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
