package com.codepath.welldone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.welldone.helper.AddressUtil;
import com.codepath.welldone.helper.DateTimeUtil;
import com.codepath.welldone.model.Pump;

import java.io.IOException;
import java.util.Random;

public class PumpRowView extends RelativeLayout {

    public static final int TARGET_DETAILS_HEIGHT = 850;
    public static final int ANIMATE_IN_DURATION_MILLIS = 300;
    public static final int ANIMATE_OUT_DURATION_MILLIS = 500;

    public Button newReportButton;
    private ViewGroup detailsContainer;

    public ViewHolder viewHolder;

    public PumpRowView(Context context, AttributeSet attrs){
        super(context, attrs);
        View.inflate(context, R.layout.row_pump, this);
        newReportButton = (Button)findViewById(R.id.btnNewReport);
        detailsContainer = (ViewGroup)findViewById(R.id.vgDetailsContainer);
        detailsContainer.setVisibility(View.GONE);

        viewHolder = new ViewHolder();
        populateViewHolder();
    }

    public void toggleExpandedState() {
        boolean expanded = detailsContainer.getVisibility() == View.VISIBLE;
        if (expanded) {
            DropDownAnim anim = new DropDownAnim(detailsContainer, TARGET_DETAILS_HEIGHT, false);
            anim.setDuration(ANIMATE_OUT_DURATION_MILLIS);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    detailsContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            detailsContainer.startAnimation(anim);
        }
        else {
            detailsContainer.setVisibility(View.VISIBLE);
            DropDownAnim anim = new DropDownAnim(detailsContainer, TARGET_DETAILS_HEIGHT, true);
            anim.setDuration(ANIMATE_IN_DURATION_MILLIS);
            detailsContainer.startAnimation(anim);
        }
    }

    private void populateViewHolder() {
        viewHolder.ivPump = (ImageView)findViewById(R.id.ivPump);
        viewHolder.tvLastUpdated = (TextView)findViewById(R.id.tvPumpLastUpdated);
        viewHolder.tvPriority = (TextView)findViewById(R.id.tvPriority);
        viewHolder.tvStatus = (TextView)findViewById(R.id.tvPumpStatus);
        viewHolder.tvLocation = (TextView)findViewById(R.id.tvPumpLocation);
    }

    public void updateSubviews(Pump pump) {
        viewHolder.tvLastUpdated.setText(
                DateTimeUtil.getRelativeTimeofTweet(pump.getUpdatedAt().toString()));
        viewHolder.tvStatus.setText(pump.getCurrentStatus());
        viewHolder.tvPriority.setText(String.format("Priority Level %d", pump.getPriority()));
        setPumpToRandomImage();
        setupLocationLabel(pump);
    }

    private void setPumpToRandomImage() {
        String filename = String.format("pump%d.png", 1 + Math.abs(new Random().nextInt()) % 4);
        try {
            viewHolder.ivPump.setImageDrawable(Drawable.createFromStream(getContext().getAssets().open(filename), null));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupLocationLabel(Pump pump) {
        try {
            viewHolder.tvLocation.setText(AddressUtil.stripCountryFromAddress(pump.getAddress()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void clearTextViews() {
        viewHolder.tvLocation.setText("");
        viewHolder.tvStatus.setText("");
        viewHolder.tvPriority.setText("");
        viewHolder.tvLastUpdated.setText("");
    }

    static class ViewHolder {
        ImageView ivPump;
        TextView tvLastUpdated;
        TextView tvLocation;
        TextView tvStatus;
        TextView tvPriority;
    }
}
