package virtualsystems.com.br.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class NewCategory extends Fragment implements View.OnClickListener {

    private Button btnCreateCategory;
    private AutoCompleteTextView mCategoryName;
    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);

        mCategoryName = (AutoCompleteTextView) view.findViewById(R.id.category_name);

        btnCreateCategory = (Button) view.findViewById(R.id.btn_create_category);
        btnCreateCategory.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("New Category");
    }

    @Override
    public void onClick(View view){
        HttpPost clientPost = new HttpPost("http://bp-android-api.herokuapp.com/api/categories");

        JSONObject requestBody = new JSONObject();
        StringEntity requestContent = null;
        Integer statusCode = null;
        String responseMessage = null;

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        try {
            requestBody.put("name", mCategoryName.getText());

            requestContent = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            clientPost.setEntity(requestContent);

            HttpResponse response = httpClient.execute(clientPost);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

            statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode){
                case 201:
                    JSONObject result = new JSONObject(responseBody);
                    responseMessage = "Created Category";
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
}
