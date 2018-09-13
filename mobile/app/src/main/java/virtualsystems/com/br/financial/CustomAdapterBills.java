package virtualsystems.com.br.financial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CustomAdapterBills extends ArrayAdapter<Bill> {
    public CustomAdapterBills(Context context, int resource, ArrayList<Bill> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        Bill bill = getItem(position);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_bills, parent, false);
        }

        view.setLongClickable(true);

        TextView txtId = (TextView) view.findViewById(R.id.txt_bill_id);
        TextView txtName = (TextView) view.findViewById(R.id.txt_bill_name);
        TextView txtValue = (TextView) view.findViewById(R.id.txt_bill_value);
        TextView txtSlug = (TextView) view.findViewById(R.id.txt_bill_slug);

        txtId.setText(bill.getId());
        txtName.setText(bill.getName());
        txtValue.setText(bill.getValue().toString());
        txtSlug.setText(bill.getName() + " - " + numberFormat.format(bill.getValue()).toString());

        return view;
    }
}
