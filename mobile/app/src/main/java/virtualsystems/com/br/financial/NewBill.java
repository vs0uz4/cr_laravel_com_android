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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpResponse;
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
        HttpPost clientPost = new HttpPost("http://bp-android-api.herokuapp.com/api/bill_pays");

        JSONObject requestBody = new JSONObject();
        StringEntity requestContent = null;
        Integer statusCode = null;
        String responseMessage = null;

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        try {
            requestBody.put("name", mBillName.getText());
            requestBody.put("value", Integer.parseInt(mBillValue.getText().toString()));
            requestBody.put("category_id", Integer.parseInt(selectedCategory.getId().toString()));
            requestBody.put("date_due", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

            requestContent = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            clientPost.setEntity(requestContent);

            HttpResponse response = httpClient.execute(clientPost);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

            statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode){
                case 201:
                    JSONObject result = new JSONObject(responseBody);
                    responseMessage = "Created Bill";
                    break;
                case 422:
                    responseMessage = "Unprocessable Entity";
                    break;
                default:
                    String errorCode = statusCode.toString();
                    String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                    responseMessage = "Error: " + errorCode + "\n" + errorMsg;
                    break;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show();

        getFragmentManager().popBackStack();
    }

    /**
     * Represents an asynchronous categories task used to get all
     * categories.
     */
    public class GatCategoryAsync extends AsyncTask<Void, Void, ArrayList> {
        private HttpClient httpClient = HttpClientBuilder.create().build();
        private ArrayList<Category> categories = new ArrayList<Category>();
        private String token;

        public GatCategoryAsync(String token) {
            this.token = token;
        }

        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
            HttpGet clientGet = new HttpGet("http://bp-android-api.herokuapp.com/api/categories");

            Integer statusCode = null;
            String responseMessage = null;

            clientGet.addHeader("Content-Type", "application/json");
            clientGet.addHeader("Accept", "application/json");
            clientGet.addHeader("Authorization", "Bearer " + token);

            try {
                HttpResponse response = httpClient.execute(clientGet);
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                statusCode = response.getStatusLine().getStatusCode();
                switch (statusCode) {
                    case 200:
                        JSONObject result = new JSONObject(responseBody);

                        for (int i = 0; i < result.getJSONArray("data").length(); i++) {
                            JSONObject responseData = result.getJSONArray("data").getJSONObject(i);

                            categories.add(new Category(responseData.getString("id"), responseData.getString("name")));
                        }
                        break;
                    default:
                        String errorCode = statusCode.toString();
                        String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                        responseMessage = "Error: " + errorCode + "\n" + errorMsg;

                        Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show();

                        break;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return categories;
        }
    }
}
