package com.discoget.test.discoget_core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Steven on 9/18/2016.
 */
public class ItemMatchesAdapter extends ArrayAdapter<ItemMatches> {


    public ItemMatchesAdapter(Context context, ArrayList<ItemMatches> matches) {
        super(context, 0, matches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        ItemMatches matches = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.basic_matches_view, parent, false);
        }
        // Lookup view for data population
        TextView tvUsername = (TextView) convertView.findViewById(R.id.basic_match_row_username);
        TextView tvListType = (TextView) convertView.findViewById(R.id.basic_match_row_listtype);


        // Populate the data into the template view using the data object
        tvUsername.setText("This item is in " + matches.username +"'s ");
        tvListType.setText(matches.listType);


        // Return the completed view to render on screen
        return convertView;
    }

}
