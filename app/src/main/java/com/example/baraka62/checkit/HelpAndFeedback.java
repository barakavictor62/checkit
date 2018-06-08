package com.example.baraka62.checkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by baraka62 on 11/3/2017.
 */

public class HelpAndFeedback extends ListFragment implements AdapterView.OnItemClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle my_data = getArguments();
        ((Home)getActivity()).setActionBarTitle(my_data.getString("storename"));
        return inflater.inflate(R.layout.help_and_feedback, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.help_and_feedback,android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String clicked = adapterView.getItemAtPosition(i).toString();
        switch (clicked){
            case "Send Feedback":
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"checkit@example.com"});
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                getContext().startActivity(Intent.createChooser(intent,"Send"));
                break;
            case "Help":
                Toast.makeText(getActivity(),"Help is on the way",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
