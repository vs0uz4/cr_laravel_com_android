package virtualsystems.com.br.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Bills extends Fragment implements View.OnClickListener{
    private Button btnNewBill;
    private ListView listBill;
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Bill> bills = new ArrayList<Bill>();
    private CustomAdapterBills caBills;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        btnNewBill = (Button) view.findViewById(R.id.btn_new_bill);
        btnNewBill.setOnClickListener(this);

        listBill = (ListView) view.findViewById(R.id.list_bills);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("List of Bills");

        try {
            findBills();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new NewBill());
        ft.commit();
    }

    public void findBills() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://bp-android-api.herokuapp.com/api/bill_pays");

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

                    bills.add(new Bill(responseData.getString("id"), responseData.getString("name"), responseData.getDouble("value")));
                }

                caBills = new CustomAdapterBills(getContext(), 0, bills);
                listBill.setAdapter(caBills);

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
