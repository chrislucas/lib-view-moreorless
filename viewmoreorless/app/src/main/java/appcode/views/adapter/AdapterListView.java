package appcode.views.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import appcode.custom.views.CustomTextView;
import appcode.custom.views.ResizableTextView;
import appcode.entities.Element;

/**
 * Created by r028367 on 01/08/2017.
 */

public class AdapterListView extends ArrayAdapter<Element> {


    private List<Element> elements;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public AdapterListView(@NonNull Context context, @LayoutRes int resource, @NonNull List<Element> objects) {
        super(context, resource, objects);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Element getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }


    public class ViewHolder {
        CustomTextView title;
        ResizableTextView message;
    }

}
