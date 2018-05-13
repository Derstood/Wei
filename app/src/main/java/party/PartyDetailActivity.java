package party;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ff.wei.R;

public class PartyDetailActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.party_detail);
        TextView midTitle= (TextView) findViewById(R.id.title_MID);
        Button back= (Button) findViewById(R.id.title_L);
        back.setOnClickListener(this);
        midTitle.setText(" ");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_L:
                finish();
                break;
        }
    }
}
