package com.rickdane.farmersmarkets.archive;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.rickdane.farmersmarkets.archive.DrawView;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 2/16/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewContainer extends LinearLayout {

    public ViewContainer(final Context context) {
        super(context);

        Button btn = new Button(context);
        btn.setId(1);
        btn.setText("Change Color");
        this.addView(btn);

        Button btn1 = new Button(context);
        btn1.setId(1);
        btn1.setText("Clear");
        this.addView(btn1);

        final DrawView drawView = new DrawView(context);
        this.addView(drawView);

        //prepare change color button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.changeColor();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.clearCanvas();
            }
        });

        drawView.requestFocus();
    }

}
