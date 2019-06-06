package example.com.homomorphiccrypto.Screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.Adapter.ResultAdapter;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
import example.com.homomorphiccrypto.HomomotphicCrypto.Elgamal;
import example.com.homomorphiccrypto.Model.Person;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class ResultScreen extends BaseFragment {
    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.result_screen, container, false);
        setHasOptionsMenu(false);
        unbinder = ButterKnife.bind(this, rootView);
        initLayout();
        getResultData();
        return rootView;
    }

    private void initLayout() {

    }


    private void getResultData() {
        d.show();
        ApiHelper.getResultData(getActivity(), new iNWCallback() {
            @Override
            public void onSuccess(String result) {
                d.dismiss();
                try {
                    if("WAITING".equals(result)) {
                        tvResult.setText("Đang tổng hợp kết quả. Vui lòng đợi.");
                    } else {
                        JSONObject obj = new JSONObject(result);
                        //JSONArray arrayDatas = (JSONArray) obj.get("data");
                        int totalCount = obj.getInt("total");
                        BigInteger voteResult = new BigInteger(obj.getString("data"));
                        Elgamal elgamal = new Elgamal();
                        Double resultDecrypt = elgamal.decrypt(voteResult.doubleValue());
                        tvResult.setText("Kết quả: " + resultDecrypt.intValue() + " đồng ý/tổng số: " + totalCount);
                        JSONArray arrayDatas = (JSONArray) obj.get("array");
                        for (int i = 0; i < arrayDatas.length(); i++) {
                            JSONObject jb = arrayDatas.getJSONObject(i);
                            tvQuestion.setText(jb.getString("CONTENT"));
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
}
