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
public class TracksAdapter extends ArrayAdapter<Tracks> {


    public TracksAdapter(Context context, ArrayList<Tracks> tracks) {
        super(context, 0, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        Tracks tracks= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.basic_tracks_view, parent, false);
        }
        // Lookup view for data population
        TextView tvPosition = (TextView) convertView.findViewById(R.id.txt_tracks_position);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.txt_tracks_title);
        TextView tvTime = (TextView) convertView.findViewById(R.id.txt_tracks_time);

        // Populate the data into the template view using the data object
        tvPosition.setText(tracks.trackPosition);
        tvTitle.setText(tracks.trackTitle);
        tvTime.setText(tracks.trackTime);
        // Return the completed view to render on screen
        return convertView;
    }

}
