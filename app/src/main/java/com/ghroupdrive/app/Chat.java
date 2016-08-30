package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/16/16.
 */
public class Chat extends AppCompatActivity {

    String type;
    ArrayList<GettersAndSetters> details;
    ListView lvUserChartList;
    int counter = 0;
    ImageView ivSend;
    EditText etMessage;
    CustomAdaptor custom;
    ImageView ivProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        type = getIntent().getStringExtra("type");
        details = new ArrayList<>();
        lvUserChartList = (ListView) findViewById(R.id.lvUserChartList);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);



        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();

                if(!message.contentEquals(""))
                {
                    GettersAndSetters Details = new GettersAndSetters();
                    Details.setType((counter%2)+1);
                    Details.setMessage(message);
                    details.add(Details);
                    custom.notifyDataSetChanged();
                    counter++;
                    etMessage.setText("");
                }
            }
        });


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFunctions func = new UserFunctions(Chat.this);
                func.showUserProfileDialog();
            }
        });
        bindData();
    }




    private void bindData(){
        GettersAndSetters Details;
        if(type.contentEquals("group"))
        {
            Details = new GettersAndSetters();
            Details.setType(StaticVariables.GROUPDRIVETYPE);
            details.add(Details);
        }

         Details = new GettersAndSetters();
        Details.setType(StaticVariables.RIGHTTYPE);
        Details.setMessage("Hi there waiting for your call");
        details.add(Details);


         Details = new GettersAndSetters();
        Details.setType(StaticVariables.LEFTTYPE);
        Details.setMessage("Sure I'll call you in a short while");
        details.add(Details);
        custom =  new CustomAdaptor(details,this);
        lvUserChartList.setAdapter(custom);

    }







    private class CustomAdaptor extends BaseAdapter {
        class ViewHolder{
            TextView tvText;

        }

        private static final int LIST_ITEM_TYPE_COUNT = 3;

        private ArrayList<GettersAndSetters> _data;
        Context _c;

        public CustomAdaptor(ArrayList<GettersAndSetters> data, Context c) {
            // TODO Auto-generated constructor stub
            _data=data;
            _c=c;

        }


        @Override
        public int getItemViewType(int position) {
            int type=0;
            GettersAndSetters data=_data.get(position);
            if(data.type==0){
                //chart isme
                type=0;
            }else if(data.type==1){
                type=1;
            }else if(data.type==2){
                type=2;
            }

            return type;
        }

        public void addItem(GettersAndSetters item) {
            _data.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getViewTypeCount() {
            return LIST_ITEM_TYPE_COUNT;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return _data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final GettersAndSetters gt=_data.get(position);
            ViewHolder holder = null;


            if(convertView==null){
                LayoutInflater vi=(LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                holder = new ViewHolder();
                int type = getItemViewType(position);
                switch (type) {
                    case StaticVariables.GROUPDRIVETYPE:
                        convertView=vi.inflate(R.layout.group_drive_chat_item, null);
                        convertView.setTag(holder);
                        break;
                    case StaticVariables.RIGHTTYPE:
                        convertView=vi.inflate(R.layout.chat_right, null);
                        holder.tvText=(TextView) convertView.findViewById(R.id.tvText);
                        convertView.setTag(holder);
                        break;
                    case StaticVariables.LEFTTYPE:
                        convertView=vi.inflate(R.layout.chat_left, null);
                        holder.tvText= (TextView) convertView.findViewById(R.id.tvText);
                        convertView.setTag(holder);
                        break;

                }


            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            int type = getItemViewType(position);

            switch(type){
                case StaticVariables.LEFTTYPE:
                case StaticVariables.RIGHTTYPE:
                    holder.tvText.setText(gt.message);
                    break;


            }

            return convertView;
        }


    }
}
