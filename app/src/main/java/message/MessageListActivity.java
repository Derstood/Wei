package message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bean.Message;
import util.ThisAPP;

import com.ff.wei.MapActivity;
import com.ff.wei.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity{
    private ThisAPP thisApp;
    Message[]  comingMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        thisApp = (ThisAPP)getApplication();
        //临时设置自己的id为me
        //Log.d("testInfo",thisApp.getSelfID());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.messageList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent();
                intent.setAction("Chat_Action");
                intent.putExtra("chosenMessage",comingMessages[i]);
                startActivity(intent);
            }
        });

        //本地测试数据
        //**************************
        Message[] alist = new Message[12];
        for(int i=0 ;i<12;i+=4)
        {
            alist[i] = new Message(new Timestamp(System.currentTimeMillis()),"GroupA","me",null,"conent content"+i,null);
            alist[i+1]=new Message(new Timestamp(System.currentTimeMillis()),null,"UserA","me","conent content"+(i+1),null);
            alist[i+2]=new Message(new Timestamp(System.currentTimeMillis()),"GroupA","UserA",null,"conent content"+(i+2),null);
            alist[i+3]=new Message(new Timestamp(System.currentTimeMillis()),null,"me","UserA","conent content"+(i+3),null);
        }
        //***************************

        listView.setAdapter(new MessageListApapter(alist,getApplicationContext()));
    }



    private class MessageListApapter extends BaseAdapter{

        private Message[] data;
        private Context mContext;
        private int messageNum;
        public MessageListApapter(Message[] data, Context mContext)
        {
            comingMessages=new Message[data.length];//只取收到的消息
            int j =0;
            for(int i=0;i<data.length;i++)
            {
                if(data[i].getFrom()!="me")
                {
                    comingMessages[j++]=data[i];
                }
            }
            messageNum = j;
            this.data = comingMessages;
            this.mContext = mContext;
        }

        @Override
        public int getCount() { return messageNum; }

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

            if(data[i].getFrom()!="me")
            {
                LinearLayout messageListView = (LinearLayout)View.inflate(mContext,R.layout.message_list_item, null);
                LinearLayout nameAndContent = (LinearLayout) messageListView.getChildAt(2);
                TextView mesName= (TextView) nameAndContent.getChildAt(0);
                TextView mesContent = (TextView) nameAndContent.getChildAt(1);
                ImageView showType =(ImageView) messageListView.getChildAt(0);
                if(data[i].getGroup()!=null&&data[i].getTo()==null)//群聊
                {

                    showType.setBackgroundColor(getResources().getColor(R.color.HuaweiGreen));
                    mesName.setText(data[i].getGroup());
                    mesContent.setText(data[i].getFrom()+": "+data[i].getContent());
                }
                else if(data[i].getGroup()==null && data[i].getTo()!=null)//私信
                {
                    showType.setBackgroundColor(getResources().getColor(R.color.LightHuaweiGreen));
                    mesName.setText(data[i].getFrom());
                    mesContent.setText(data[i].getContent());
                }
                else
                    {
                    Log.e("错误！！：","既不是群聊 也不是私信");
                }

                return messageListView;
            }

            return null;


        }
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,MapActivity.class);
        startActivity(intent);
        Log.d("testInfo","测试back键");
    }
}

