package message;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bean.Message;
import util.ThisAPP;

import com.ff.wei.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends Activity implements View.OnClickListener {

    private ActionBar actionBar;
    private EditText editText ;
    private Button sendButton ;
    private RecyclerView rv ;

    List<Message> GroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        editText = (EditText)findViewById(R.id.inputbox);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        Message chosenMessage = (Message) intent.getSerializableExtra("chosenMessage");

//        if(chosenMessage==null)
//            return;
//        actionBar = getSupportActionBar();
//        if(chosenMessage.getGroup()==null&&chosenMessage.getTo()!=null)
//            actionBar.setTitle(chosenMessage.getTo());
//        else if(chosenMessage.getGroup()!=null&&chosenMessage.getTo()==null)
//            actionBar.setTitle(chosenMessage.getGroup());
//        actionBar.show();



        //********向服务器查数据************
        //提交时间段和用户ID、gruopID


        //**********本地测试数据（私聊）****************
        Message[] PriavateTemp = new Message[12];
        List<Message> priavateList = new ArrayList<>();
        for(int i=0 ;i<12;i+=4)
        {
            priavateList.add(new Message(new Timestamp(System.currentTimeMillis()),null,"me","UserA","conentcontent1",null));
            priavateList.add(new Message(new Timestamp(System.currentTimeMillis()),null,"me","UserA","conentcontent2",null));
            priavateList.add(new Message(new Timestamp(System.currentTimeMillis()),null,"UserA",null,"conentcontent3",null));
            priavateList.add(new Message(new Timestamp(System.currentTimeMillis()),null,"me","UserA","conentcontent4",null));
        }
        //***************************

        //**********本地测试数据（群聊）****************
        Message[] GroupTemp = new Message[12];
        GroupList =new ArrayList<>();
            GroupList.add(new Message(new Timestamp(System.currentTimeMillis()),"GroupA","me",null,"conentcontent1",null));
            GroupList.add(new Message(new Timestamp(System.currentTimeMillis()),"GroupA","UserA",null,"conentcontent2",null));
            GroupList.add(new Message(new Timestamp(System.currentTimeMillis()),"GroupA","UserB",null,"conentcontent3",null));
            GroupList.add(new Message(new Timestamp(System.currentTimeMillis()),"GroupA","UserA",null,"conentcontent4",null));
        //***************************

        rv.setAdapter(new ChatAdapter(GroupList));
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,MessageListActivity.class);
        startActivity(intent);
        Log.d("testInfo","测试back键");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendButton:
                String str=editText.getText()+"";
                GroupList.add(new Message(new Timestamp(System.currentTimeMillis()),"groupA", ThisAPP.getSelfID(),null,str,null));
                rv.setAdapter(new ChatAdapter(GroupList));
                editText.setText("");
                break;
        }
    }
}
