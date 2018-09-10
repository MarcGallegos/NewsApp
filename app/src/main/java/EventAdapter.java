import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter<NewsEvent> {

    public EventAdapter(@NonNull Context context,@NonNull ArrayList<NewsEvent> events){
        super(context,0,events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //store convertView as variable of View type named listEventView
        View listEventView=convertView;

        //check if existing view is in use else inflate view
        if (listEventView==null){
            listEventView=LayoutInflater.from(getContext()).inflate(
                    R.layout.list_segment,parent,false);
        }

        //Get event data at this position in index
        NewsEvent currentNewsEvent=getItem(position);

        //Bind to TextView with ID of "segment_title"
        TextView segmentTitleView=listEventView.findViewById(R.id.segment_title);
        //Get view from adapter and set view with segment_title
        segmentTitleView.setText(currentNewsEvent.getSegmentTitle());

        //Bind to TextView with ID of "section_name"
        TextView sectionNameView=listEventView.findViewById(R.id.section_name);
        //Get view from adapter and set view with section_name
        sectionNameView.setText(currentNewsEvent.getSectionName());

        //Bind to TextView with ID of "author"
        TextView authorView=listEventView.findViewById(R.id.author);
        //Get view from adapter and set view with time
        authorView.setText(currentNewsEvent.getAuthor());

        //Bind TextView with ID of "date"
        TextView dateView=listEventView.findViewById(R.id.date);
        //Get view from adapter and set view with date
        dateView.setText(currentNewsEvent.getDate());

        return listEventView;
    }
}
