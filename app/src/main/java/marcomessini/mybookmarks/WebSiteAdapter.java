package marcomessini.mybookmarks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marcomessini on 20/01/15.
 */
public class WebSiteAdapter extends ArrayAdapter<WebSite> {
private final Context context;
private final ArrayList<WebSite> valuesWS;

public WebSiteAdapter(Context context, ArrayList<WebSite> valuesWS) {
        super(context, R.layout.custom_layout_site, valuesWS);
        this.context = context;
        this.valuesWS = valuesWS;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_layout_site, parent, false);
        CheckedTextView checkedTextView=(CheckedTextView) rowView.findViewById(R.id.checkWS);
        //controllo se il sito è aggiornato
        int check=valuesWS.get(position).check;
        if(check==1){
            int colorUp= Color.parseColor("#f46e6e");
            checkedTextView.setBackgroundColor(colorUp);
            //background rosso chiaro
        }
        else{
            //grigio
            checkedTextView.setBackgroundColor(-12303292);
        }
        TextView textView = (TextView) rowView.findViewById(R.id.website);
        TextView textView1 = (TextView) rowView.findViewById(R.id.websiteurl);
        textView.setText(valuesWS.get(position).name);
        textView1.setText(valuesWS.get(position).URL);

        return rowView;
        }
}
