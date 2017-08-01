package appcode;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import appcode.R;
import appcode.entities.Element;

/**
 * Created by r028367 on 01/08/2017.
 */

public class App {

    public static List<Element> getElements(Context context) {
        List<Element> elements = new ArrayList<>();
        String message = context.getResources().getString(R.string.example_text_info);
        for(int i=0; i<15; i++) {
            Element element = new Element();
            element.setTitle("Titulo: " + (i+1));
            element.setMessage(message);
        }
        return elements;
    }

}
