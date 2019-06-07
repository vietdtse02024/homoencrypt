package example.com.homomorphiccrypto.Screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
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

    @BindView(R.id.lnlButton)
    LinearLayout lnlButton;

    @BindView(R.id.lnlResult)
    LinearLayout lnlResult;

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @BindView(R.id.tvResult)
    TextView tvResult;

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
        toolbar.setTitle("Bỏ phiếu");
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
        ApiHelper.getPersonData(getActivity(), new iNWCallback() {
            @Override
            public void onSuccess(String result) {
                d.dismiss();
                lnlQuestion.setVisibility(View.VISIBLE);
                try {
                    if ("EXISTED".equals(result)) {
                        lnlButton.setVisibility(View.GONE);
                        lnlResult.setVisibility(View.VISIBLE);
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
                        String typeData = obj.getString("type");
                        if (arrayDatas.length() > 0 && "DATA".equals(typeData)) {
                            lnlButton.setVisibility(View.VISIBLE);
                            lnlResult.setVisibility(View.GONE);
                        } else {
                            lnlButton.setVisibility(View.GONE);
                            lnlResult.setVisibility(View.VISIBLE);
                            tvResult.setText("Đã hết thời gian bỏ phiếu.");
                        }
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

        tvQuestion.setText(person.getDescription());
        final int questionId = person.getId();
        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVotingData(questionId, 1);
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVotingData(questionId, 0);
            }
        });
    }

    private void sendVotingData(int questionId, int value) {
        try {
            JSONObject obj = new JSONObject();

            Elgamal elgamal = new Elgamal();
            obj.put("questionId", questionId);
            obj.put("value", elgamal.encrypt(value));
            ApiHelper.sendVotingData(getActivity(), obj.toString(), new iNWCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("", "SEND VOTING SUCCESS");
                    lnlButton.setVisibility(View.GONE);
                    lnlResult.setVisibility(View.VISIBLE);
                    if("TIMEOUT".equals(result)) {
                        tvResult.setText("Đã hết thời gian bỏ phiếu.");
                    } else {
                        tvResult.setText("Bạn đã bỏ phiếu thành công.");
                    }
                }

                @Override
                public void onFailed(String error) {
                    Log.e("", "SEND VOTING FAILED: " + error);
                }
            });
        } catch (Exception ex) {
            Log.e("", "SEND VOTING FAILED: " + ex.getMessage(), ex);
        }
    }
}
