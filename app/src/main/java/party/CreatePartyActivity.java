package party;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ff.wei.R;

public class CreatePartyActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_party);
        TextView midTitle= (TextView) findViewById(R.id.title_MID);
        midTitle.setText("创建活动");

    }

    @Override
    public void onClick(View v) {

    }
}
