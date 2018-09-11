package virtualsystems.com.br.financial;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NewBill extends Fragment implements View.OnClickListener {

    private Button btnCreateBill;
    private AutoCompleteTextView mBillName;
    private AutoCompleteTextView mBillValue;
    private Spinner categorySelect;
    private Category selectedCategory;
    private ArrayList<Category> categoriesList = new ArrayList<Category>();
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        view = inflater.inflate(R.layout.fragment_new_bill, container, false);

        mBillName = (AutoCompleteTextView) view.findViewById(R.id.bill_name);
        mBillValue = (AutoCompleteTextView) view.findViewById(R.id.bill_value);

        buildCategorySpinner();

        btnCreateBill = (Button) view.findViewById(R.id.btn_create_bill);
        btnCreateBill.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("New Bill");
    }

    private void buildCategorySpinner() {
        GatCategoryAsync taskAsync = new GatCategoryAsync(UserSession.getInstance(getContext()).getUserToken());

        try {
            categoriesList = taskAsync.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        categorySelect = (Spinner) view.findViewById(R.id.select_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), R.id.select_category, categoriesList);
        categorySelect.setAdapter(categoryAdapter);

        categorySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoriesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        });
    }

    @Override
    public void onClick(View view){
        HttpPost clientPost = new HttpPost("http://192.168.254.8/api/bill_pays");
        JSONObject requestBody = new JSONObject();
        StringEntity data = null;
        Integer statusCode = null;

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        try {
            requestBody.put("name", mBillName.getText());
            requestBody.put("value", Integer.parseInt(mBillValue.getText().toString()));
            requestBody.put("category_id", Integer.parseInt(selectedCategory.getId().toString()));
            requestBody.put("date_due", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            data = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            clientPost.setEntity(data);

            HttpResponse response = httpClient.execute(clientPost);
            String json = EntityUtils.toString(response.getEntity());
            statusCode = response.getStatusLine().getStatusCode();

            switch (statusCode){
                case 201:
                    JSONObject result = new JSONObject(json);
                    Toast.makeText(getContext(), "Created Bill", Toast.LENGTH_SHORT).show();
                    break;
                case 422:
                    Toast.makeText(getContext(), "Unprocessable Entity", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    String errorCode = statusCode.toString();
                    String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                    Toast.makeText(getContext(), "Error: " + errorCode + "\n" + errorMsg, Toast.LENGTH_SHORT).show();
                    break;
            }

            getFragmentManager().popBackStack();
        } catch (JSONException | UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GatCategoryAsync extends AsyncTask<Void, Void, ArrayList> {
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private String token;

    public GatCategoryAsync(String token) {
        this.token = token;
    }

    @Override
    protected ArrayList<Category> doInBackground(Void... params) {
        HttpResponse response = null;
        JSONObject result = null;
        String json = null;

        HttpGet clientGet = new HttpGet("http://192.168.254.8/api/categories");

        clientGet.addHeader("Content-Type", "application/json");
        clientGet.addHeader("Accept", "application/json");
        clientGet.addHeader("Authorization", "Bearer " + token);

        try {
            response = httpClient.execute(clientGet);
            json = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            result = new JSONObject(json);

            for (int i = 0; i < result.getJSONArray("data").length(); i++) {
                JSONObject data = result.getJSONArray("data").getJSONObject(i);

                categories.add(new Category(data.getString("id"), data.getString("name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categories;
    }
}
