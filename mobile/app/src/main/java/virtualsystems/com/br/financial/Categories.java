package virtualsystems.com.br.financial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
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
        registerForContextMenu(listCategory);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        Integer itemId = (Integer) view.getId();

        menu.setHeaderTitle("Options");
        menu.add(0, itemId, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if (item.getTitle() == "Delete"){
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Integer itemSelected = Integer.parseInt(String.valueOf(caCategories.getItem(acmi.position).getId()));

            deleteCategory(itemSelected);
        }

        return true;
    }

    public void findCategories() throws IOException, JSONException {
        HttpGet clientGet = new HttpGet("http://bp-android-api.herokuapp.com/api/categories");

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

                    categories.add(new Category(responseData.getString("id"), responseData.getString("name")));
                }

                caCategories = new CustomAdapterCategories(getContext(), 0, categories);
                listCategory.setAdapter(caCategories);

                break;
            default:
                String errorCode = statusCode.toString();
                String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                responseMessage  = "Error: " + errorCode + "\n" + errorMsg;

                Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show();

                break;
        }
    }

    public void deleteCategory(Integer Id) {
        HttpDelete clientDelete = new HttpDelete("http://bp-android-api.herokuapp.com/api/categories/" + Id.toString());

        Integer statusCode = null;
        String responseMessage = null;

        clientDelete.addHeader("Content-Type", "application/json");
        clientDelete.addHeader("Accept", "application/json");
        clientDelete.addHeader("Authorization", "Bearer " + UserSession.getInstance(getContext()).getUserToken());

        try {
            HttpResponse response = httpClient.execute(clientDelete);

            statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
                case 204:
                    updateUI(categories);
                    findCategories();
                    responseMessage = "Category Deleted";
                    break;
                case 404:
                    responseMessage = "Resource not Found";
                    break;
                case 500:
                    responseMessage = "Resource can not be Deleted";
                    break;
                default:
                    String errorCode = statusCode.toString();
                    String errorMsg = response.getStatusLine().getReasonPhrase().toString();
                    responseMessage  = "Error: " + errorCode + "\n" + errorMsg;
                    break;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show();
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
