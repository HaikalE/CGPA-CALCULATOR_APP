package com.example.projectkelbaru1;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ButtonAdapter extends BaseAdapter {
    private Context mContext;

    public ButtonAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 4; // Number of buttons
    }

    @Override
    public Object getItem(int position) {
        return null; // Not used
    }

    @Override
    public long getItemId(int position) {
        return 0; // Not used
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            // Create a new button if it doesn't exist
            button = new Button(mContext);
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            // Reuse the existing button
            button = (Button) convertView;
        }

        // Set button text and other properties
        button.setText("Button " + (position + 1));

        return button;
    }
}
