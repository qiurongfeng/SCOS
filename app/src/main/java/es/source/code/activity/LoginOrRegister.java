package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A login screen that offers login via email/password.
 */
public class LoginOrRegister extends Activity {
    private EditText mUsername,mPassword;
    private Button mSignin,mReturn;
    public static final int RESPONSE=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogin();
        setContentView(R.layout.activity_login_or_register);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mSignin = (Button)findViewById(R.id.username_sign_in_button);
        mReturn = (Button)findViewById(R.id.username_return_button);

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comfirm();
            }
        });
        mReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginOrRegister.this,MainScreen.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void Comfirm(){
            final String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username=" + username + "&&password="+password;
        Log.d("ScosQ", address);
        //开启一个查询线程，将像该地址发送请求，以GET的方式
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    AllowX509TrustManager.allowAllSSL();
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("login", str);
                    }

                    String responseStr = response.toString();
                    JSONObject jsonObject = new JSONObject(responseStr);
                    String errcode = jsonObject.getString("errcode") ;
                    Log.d("login_json", errcode);
                    String[] info = {errcode,username};
                    Message msg =new Message();
                    msg.what = RESPONSE;
                    msg.obj=info;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RESPONSE:
                    String[] str = (String[]) msg.obj;
                    String response_str = str[0];
                    String username = str[1];
                   if (Integer.valueOf(response_str) ==  0){
                       SharedPreferences settings = (SharedPreferences)getSharedPreferences("shared", MODE_PRIVATE);
                       SharedPreferences.Editor editor = settings.edit();
                       editor.putString("login_status", "1");//1表示登陆成功2表示未登陆
                       editor.putString("username",username);
                       editor.commit();
                       Toast.makeText(LoginOrRegister.this,"登陆成功！",Toast.LENGTH_LONG).show();
                       Intent i = new Intent(LoginOrRegister.this,LoginSuccess.class);
                       startActivity(i);
                       finish();
                   }else{
                       Toast.makeText(LoginOrRegister.this,"用户名或者密码错误！",Toast.LENGTH_LONG).show();
                   }
            }
        }
    };
    private void isLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String loginStatus = sharedPreferences.getString("login_status","2");
        Log.d("qq",loginStatus);
        if (loginStatus.equals("1")){
            Intent i = new Intent(LoginOrRegister.this,LoginSuccess.class);
            startActivity(i);
            finish();
        }
    }


}