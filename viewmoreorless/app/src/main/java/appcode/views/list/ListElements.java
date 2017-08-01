package appcode.views.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import appcode.R;
import appcode.views.adapter.AdapterListView;

public class ListElements extends AppCompatActivity {


    private ListView listView;
    private AdapterListView adapterListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_elements);
    }
}
