package id.ac.itb.a8eh.radiostreaming.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import id.ac.itb.a8eh.radiostreaming.R;
import id.ac.itb.a8eh.radiostreaming.model.ProgramSchedule;

import java.util.ArrayList;
import java.util.List;

import id.ac.itb.a8eh.radiostreaming.MainActivity;
import id.ac.itb.a8eh.radiostreaming.model.ProgramSchedule;

/**
 * Created by irfan_mim on 01/09/17.
 */

public class ProgramScheduleArrayAdapter extends ArrayAdapter<ProgramSchedule> {

    private Context context;
    private List<ProgramSchedule> rentalProperties;

    //constructor, call on creation
    public ProgramScheduleArrayAdapter(Context context, int resource, ArrayList<ProgramSchedule> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = objects;
    }

    public ProgramScheduleArrayAdapter(MainActivity context, int resource, ArrayList<ProgramSchedule> rentalProperties) {
        super(context, resource, rentalProperties);


        this.context = context;
        this.rentalProperties = rentalProperties;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        ProgramSchedule property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.program_schedule_layout, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView announcer = (TextView) view.findViewById(R.id.announcer);

        title.setText(property.getTitle());
        time.setText(property.getTime());
        announcer.setText(property.getAnnouncer());

        return view;
    }
}
