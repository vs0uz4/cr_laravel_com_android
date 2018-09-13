package virtualsystems.com.br.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class BillsTotal extends Fragment {
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private Double billTotals = 0.0;
    private TextView txtBillsTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_bills_total, container, false);

        txtBillsTotal = (TextView) view.findViewById(R.id.txt_bills_total);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Totals of Bills");

        try {
            findBills();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void findBills() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://192.168.254.8/api/bill_pays");

        Integer statusCode = null;
        String responseMessage = null;

        clientGet.addHeader("Content-Type", "application/json");
        clientGet.addHeader("Accept", "application/json");
        clientGet.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        HttpResponse response = httpClient.execute(clientGet);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
            case 200:
                JSONObject result = new JSONObject(responseBody);

                for (int i = 0; i < result.getJSONArray("data").length(); i++) {
                    JSONObject responseData = result.getJSONArray("data").getJSONObject(i);

                    billTotals = billTotals + responseData.getDouble("value");

                    if (txtBillsTotal != null) {
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                        txtBillsTotal.setText(numberFormat.format(billTotals).toString());
                    }
                }

                break;
            default:
                String errorCode = statusCode.toString();
                String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                responseMessage  = "Error: " + errorCode + "\n" + errorMsg;

                Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
