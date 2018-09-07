package virtualsystems.com.br.financial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterCategories extends ArrayAdapter<Category> {
    private Context context;

    public CustomAdapterCategories(Context context, int resource, ArrayList<Category> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        Category category = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_categories, parent, false);
        }

        TextView txtId = (TextView) view.findViewById(R.id.txt_category_id);
        TextView txtName = (TextView) view.findViewById(R.id.txt_category_name);

        txtId.setText(category.getId());
        txtName.setText(category.getName());

        return view;

    }
}
