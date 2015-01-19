package marcomessini.mybookmarks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by marcomessini on 19/01/15.
 */
public class GroupsAdapter extends ArrayAdapter<Group> {
        private final Context context;
        private final ArrayList<Group> values;

        public GroupsAdapter(Context context, ArrayList<Group> values) {
            super(context, R.layout.custom_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.title);
            TextView textView1 = (TextView) rowView.findViewById(R.id.description);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
            textView.setText(values.get(position).name);
            textView1.setText(""+values.get(position).NSiti);

            return rowView;
        }
    }

