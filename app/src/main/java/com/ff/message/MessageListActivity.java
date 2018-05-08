package com.ff.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ff.Bean.Message;
import com.ff.wei.R;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.messageList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent();
                intent.setAction("Chat_Action");
                intent.putExtra("i",i);
                startActivity(intent);
            }
        });

        Message[] alist = new Message[100];
        for(int i=0 ;i<100;i++)
        {
            Message temp = new Message();
            temp.setType(i%2);
            temp.setName("test name "+i);
            temp.setContent("test content"+i);
            alist[i] = temp;
        }


        //SimpleAdapter simpleAdapter= new SimpleAdapter(getApplicationContext(),);
        //ArrayAdapter<Message> adapter=new ArrayAdapter<Message>(getApplicationContext(), R.layout.message_list_item, alist);
        listView.setAdapter(new MessageListApapter(alist,getApplicationContext()));
        //setContentView(listView);




    }

    private class MessageListApapter extends BaseAdapter{

        private Message[] data;
        private Context mContext;
        public MessageListApapter(Message[] data, Context mContext)
        {
            this.data = data;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            //显示每条消息
            LinearLayout messageListView = (LinearLayout)View.inflate(mContext,R.layout.message_list_item, null);
            LinearLayout nameAndContent = (LinearLayout) messageListView.getChildAt(1);
            TextView mesName= (TextView) nameAndContent.getChildAt(0);
            TextView mesContent = (TextView) nameAndContent.getChildAt(1);
            mesName.setText(data[i].getName());
            mesContent.setText(data[i].getContent());


            return messageListView;

        }
    }
}

