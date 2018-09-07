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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Categories extends Fragment implements View.OnClickListener {

    private Button btnNewCategory;
    private ListView listCategory;
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private CustomAdapterCategories caCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle save){
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        btnNewCategory = (Button) view.findViewById(R.id.btn_new_category);
        btnNewCategory.setOnClickListener(this);

        listCategory = (ListView) view.findViewById(R.id.list_categories);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("List of Categories");

        try {
            findCategories();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view){
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new NewCategory());
        ft.commit();
    }

    public void findCategories() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://192.168.254.8/api/categories");

        clientGet.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());
        HttpResponse response = httpClient.execute(clientGet);

        String json = EntityUtils.toString(response.getEntity());
        JSONObject result = new JSONObject(json);
        JSONArray dataArray = result.getJSONArray("data");

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject data = dataArray.getJSONObject(i);

            categories.add(new Category(data.getString("id"), data.getString("name")));
        }

        caCategories = new CustomAdapterCategories(getContext(), 0, categories);
        listCategory.setAdapter(caCategories);
    }

    public void updateUI(ArrayList<Category> items){
        caCategories.clear();

        if (items != null){
            for (Object obj : items) {
                caCategories.insert((Category) obj, caCategories.getCount());
            }
        }

        caCategories.notifyDataSetChanged();
    }
}
