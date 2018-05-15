package party;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ff.wei.R;

public class PartyDetailActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Log.d("testInfo","intent:"+intent.getStringExtra("actID"));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.party_detail);
        TextView midTitle= (TextView) findViewById(R.id.title_MID);
        Button back= (Button) findViewById(R.id.title_L);
        Button join=(Button)findViewById(R.id.joinParty);
        back.setOnClickListener(this);
        join.setOnClickListener(this);
        midTitle.setText(" ");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_L:
                finish();
                break;
            case R.id.joinParty:
                //加入活动
                Log.d("testInfo","joinParty");
                break;
        }
    }
}
