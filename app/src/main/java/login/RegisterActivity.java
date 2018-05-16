package login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ff.wei.R;

public class RegisterActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        EditText user= (EditText) findViewById(R.id.login_user);
        EditText password= (EditText) findViewById(R.id.login_password);
        Button login=(Button)findViewById(R.id.login);
        login.setText("注册");
        login.setOnClickListener(this);
        TextView gotoRegisster=(TextView) findViewById(R.id.bt_goto_register);
        gotoRegisster.setText("已有账号？去登录");
        gotoRegisster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                //注册操作
                Log.d("testInfo","registered");
                break;
            case R.id.bt_goto_register:
                //转登录页面
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
        }
    }

}
