package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by pk on 4/22/15.
 */
public class PostedViewHolder extends RecyclerView.ViewHolder {
private final TextView tvTime;;
private final TextView tvStartingPost;;
private final TextView tvEndingPost;
private final TextView tvMid1Post;
private final TextView tvMid2Post;
private final CardView cvLayout;

    private final ImageView profiles[];


    public PostedViewHolder(final View parent,ImageView profiles[], TextView tvTime,TextView tvStartingPost,TextView tvEndingPost,TextView tvMid1Post,TextView tvMid2Post,CardView cvLayout) {
        super(parent);
        this.profiles = profiles;
        this.tvTime = tvTime;
        this.tvStartingPost = tvStartingPost;
        this.tvEndingPost = tvEndingPost;
        this.tvMid1Post = tvMid1Post;
        this.tvMid2Post = tvMid2Post;
        this.cvLayout = cvLayout;

    }

    public static PostedViewHolder newInstance(View parent) {

        ImageView profiles[] = new ImageView[4];
        profiles[0] = (ImageView) parent.findViewById(R.id.ivProfile1);
        profiles[1] = (ImageView) parent.findViewById(R.id.ivProfile2);
        profiles[2] = (ImageView) parent.findViewById(R.id.ivProfile3);
        profiles[3] = (ImageView) parent.findViewById(R.id.ivProfile4);
        TextView tvTime = (TextView) parent.findViewById(R.id.tvTime);
        TextView tvStartingPost = (TextView) parent.findViewById(R.id.tvStartingPost);
        TextView tvEndingPost = (TextView) parent.findViewById(R.id.tvEndingPost);
        TextView tvMid1Post = (TextView) parent.findViewById(R.id.tvMid1Post);
        TextView tvMid2Post = (TextView) parent.findViewById(R.id.tvMid2Post);
        CardView cvLayout = (CardView) parent.findViewById(R.id.cvLayout);


        return new PostedViewHolder(parent,profiles,tvTime,tvStartingPost,tvEndingPost,tvMid1Post,tvMid2Post,cvLayout);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        try {

            UserFunctions functions = new UserFunctions(context);
            for(int i=0; i<profiles.length; i++)
            {
                profiles[i].setImageResource(R.drawable.ovaldashes);
            }


                final JSONObject oneJson = new JSONObject(Details.jsonString);

                RideObject rb = new RideObject(oneJson.toString(),functions);

                tvTime.setText(rb.SetOffTime);

                if(rb.RideStart != null)
                {
                    tvStartingPost.setText(functions.getJsonString(rb.RideStart, StaticVariables.NAME));
                }





                if(rb.RideEnd != null)
                {
                    tvEndingPost.setText(functions.getJsonString(rb.RideEnd, StaticVariables.NAME));
                }



                if(rb.Stop1 != null)
                {
                    tvMid1Post.setText(functions.getJsonString(rb.Stop1, StaticVariables.NAME));
                }else{
                    tvMid1Post.setVisibility(View.GONE);
                }


                if(rb.Stop2 != null)
                {
                    tvMid2Post.setText(functions.getJsonString(rb.Stop2, StaticVariables.NAME));
                }else{
                    tvMid2Post.setVisibility(View.GONE);
                }


                JSONArray passengers = rb.Passengers;




                AQuery aq = new AQuery(context);
                ImageOptions op=new ImageOptions();
                op.fileCache = true;
                op.memCache=true;
                op.targetWidth = 0;
                op.fallback = R.drawable.ovaldashes;

                for(int i=0; i<passengers.length(); i++)
                {
                    JSONObject oneC =  passengers.getJSONObject(i);
                    ProfileObject po = new ProfileObject(oneC.toString(),functions);
                    String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+po.ProfileUrl+".jpg";
                    aq.id(profiles[i]).image(url, op);
                }


                cvLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(StaticVariables.STARTACTIVITYINTENT);
                        it.putExtra("type","postedActivity");
                        it.putExtra(StaticVariables.JSONSTRING,oneJson.toString());
                        context.sendBroadcast(it);


                    }
                });


        }catch (Exception ex)
        {
            ex.printStackTrace();
        }




    }

}
