package com.ukgeek.sheetcounter.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.database.SpeechItem;

import java.util.List;

/**
 * Created by voronsky on 03.10.15.
 */
public class HistoryListAdapter extends ArrayAdapter<SpeechItem> {

    private Context mContext;
    private int mLayoutResourceId;

    public HistoryListAdapter(Context context, int layoutResourceId, List<SpeechItem> speeches) {
        super(context, layoutResourceId, speeches);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
    }

    private class ViewHolder {
        final TextView tvItemHistory;

        private ViewHolder(View view) {
            tvItemHistory = (TextView) view.findViewById(R.id.tv_item_history);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SpeechItem speech = getItem(position);
        holder.tvItemHistory.setText(speech.getPhrase());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SpeechItem getItem(int position) {
        return super.getItem(position);
    }
}
