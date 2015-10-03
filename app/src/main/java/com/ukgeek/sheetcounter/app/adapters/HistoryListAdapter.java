package com.ukgeek.sheetcounter.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.database.Speech;

import java.util.List;

/**
 * Created by voronsky on 03.10.15.
 */
public class HistoryListAdapter extends ArrayAdapter<Speech> {

    private Context mContext;
    private int mLayoutResourceId;

    public HistoryListAdapter(Context context, int layoutResourceId, List<Speech> speeches) {
        super(context, layoutResourceId,speeches);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
    }

    private class ViewHolder {
        final TextView badWordHistory;

        private ViewHolder(View view) {
            badWordHistory = (TextView) view.findViewById(R.id.badWordHistory);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = inflater.inflate(mLayoutResourceId, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Speech speech = getItem(position);
        holder.badWordHistory.setText(speech.getBadWord());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Speech getItem(int position) {
        return super.getItem(super.getCount() - position - 1);
    }
}
