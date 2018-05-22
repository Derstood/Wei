package login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ff.wei.MapActivity;
import com.ff.wei.R;

import org.w3c.dom.Document;

import message.MessageActivity;
import message.MessageListActivity;
import util.ThisAPP;

public class LoginActivity extends Activity implements View.OnClickListener {

    EditText user;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        user= (EditText) findViewById(R.id.login_user);
        password= (EditText) findViewById(R.id.login_password);
        ImageView avatar=(ImageView)findViewById(R.id.avatar);
        avatar.setVisibility(View.INVISIBLE);
        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        TextView gotoRegisster=(TextView) findViewById(R.id.bt_goto_register);
        gotoRegisster.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.login:
                //登录操作
                if(login(user.getText()+"",password.getText()+"")){
                    ThisAPP app=(ThisAPP)getApplication();
                    app.setSelfID(user.getText()+"");
                    finish();
                    Log.d("testInfo","logined");
                }else{
                    //登录失败
                }

                break;
            case R.id.bt_goto_register:
                //转注册页面
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                Log.d("testInfo","startAct");
                break;
            case R.id.avatar:
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 111);
        }
    }
    public boolean login(String username,String password){

                String request="<login>\n" +
                "<userID>"+username+"</userID>\n" +
                "<password>"+ password+"</password>\n" +
                "</login>";

                Log.d("testInfo",request);
        return true;
    }

}
