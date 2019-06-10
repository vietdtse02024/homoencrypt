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

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.anychart.graphics.vector.SolidFill;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.homomorphiccrypto.BaseClasses.BaseFragment;
import example.com.homomorphiccrypto.HomomotphicCrypto.Elgamal;
import example.com.homomorphiccrypto.Network.ApiHelper;
import example.com.homomorphiccrypto.Network.iNWCallback;
import example.com.homomorphiccrypto.R;

public class ResultScreen extends BaseFragment {
    @BindView(R.id.tvResult)
    TextView tvResult;

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @BindView(R.id.any_chart_view)
    AnyChartView anyChartView;

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
                        tvResult.setVisibility(View.VISIBLE);
                        tvResult.setText("Đang tổng hợp kết quả. Vui lòng đợi.");
                        anyChartView.setVisibility(View.GONE);
                    } else {
                        JSONObject obj = new JSONObject(result);
                        //String voteResult = obj.getString("data");
                        int total = obj.getInt("total");
                        Elgamal elgamal = new Elgamal();
                        /*JSONArray voteResults = (JSONArray) obj.get("data");
                        for (int i = 0; i < voteResults.length(); i++) {
                            JSONObject jb = voteResults.getJSONObject(i);
                            String [] value = jb.getString("VALUE").split(",");
                            BigInteger x = new BigInteger(value[0]);
                            BigInteger y = new BigInteger(value[1]);
                            voteResult = voteResult.multiply(y.divide(x.pow(elgamal.secretKey)));
                        }*/
                        String [] value = obj.getString("data").split(",");
                        int resultDecrypt = elgamal.decrypt( new BigInteger(value[0]),  new BigInteger(value[1]));
                        tvResult.setVisibility(View.GONE);
                        anyChartView.setVisibility(View.VISIBLE);
                        //tvResult.setText("Kết quả: " + resultDecrypt.intValue() + " đồng ý/tổng số: " +  voteResults.length());
                        Pie pie = AnyChart.pie();
                        List<DataEntry> data = new ArrayList<DataEntry>();
                        data.add(new ValueDataEntry("Đồng ý", resultDecrypt));
                        data.add(new ValueDataEntry("Không đồng ý", total - resultDecrypt));

                        pie.data(data);

                        pie.title("Kết quả bỏ phiếu");
                        pie.labels().position("outside");
                        pie.legend().title().enabled(true);
                        pie.legend().title()
                                .text(" ");
                        pie.palette().itemAt(0, new SolidFill("#2AD62A", 1d));
                        pie.palette().itemAt(1, new SolidFill("#F1683C", 1d));
                        pie.legend()
                                .position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                        anyChartView.setChart(pie);

                        JSONArray questions = (JSONArray) obj.get("question");
                        for (int i = 0; i < questions.length(); i++) {
                            JSONObject jb = questions.getJSONObject(i);
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
