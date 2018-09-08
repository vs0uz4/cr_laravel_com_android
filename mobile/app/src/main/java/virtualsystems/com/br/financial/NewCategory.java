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
        HttpPost clientPost = new HttpPost("http://192.168.254.8/api/categories");
        JSONObject requestBody = new JSONObject();
        StringEntity data = null;

        clientPost.addHeader("Content-Type", "application/json");
        clientPost.addHeader("Accept", "application/json");
        clientPost.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());


        try {
            requestBody.put("name", mCategoryName.getText());
            data = new StringEntity(requestBody.toString());
            clientPost.setEntity(data);

            HttpResponse response = httpClient.execute(clientPost);
            String json = EntityUtils.toString(response.getEntity());

            JSONObject result = new JSONObject(json);

            Toast.makeText(getContext(), "Created", Toast.LENGTH_SHORT).show();

            getFragmentManager().popBackStack();
        } catch (JSONException | UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
