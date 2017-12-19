package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/20.
 */

public class Select extends Activity {
    private Button btn1,btn2,btn3,btn4;
    private final static int RESPONSE=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInfo();
        setContentView(R.layout.select);
        initview();



    }
private void initview(){
    btn1 = (Button) findViewById(R.id.one);
    btn2 = (Button)findViewById(R.id.two);
    btn3 = (Button)findViewById(R.id.three);
    btn4 = (Button)findViewById(R.id.four);
    btn1.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Select.this,Select_1.class);
            startActivity(i);
            finish();
        }
    });
    btn2.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Select.this,Select_2.class);
            startActivity(i);
            finish();
        }
    });
    btn3.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Select.this,Select_3.class);
            startActivity(i);
            finish();
        }
    });
    btn4.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Select.this,Select_4.class);
            startActivity(i);
            finish();
        }
    });

}
private void getInfo(){
    SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
    String userName = sharedPreferences.getString("username","");
    final String addressCheck = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+userName;
    Log.d("address_chack",addressCheck);
    new Thread(new Runnable() {
        @Override
        public void run() {
            HttpURLConnection con = null;
            try {
                AllowX509TrustManager.allowAllSSL();
                URL url = new URL(addressCheck);
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
                String response_str = response.toString();
                Log.d("errr",response_str);
                String responseStr = response.toString();
                if (response_str.indexOf("building") != -1){
                    SharedPreferences setting = getSharedPreferences("shared",MODE_PRIVATE);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("choice_status","1");//1:已选择2：未选择
                    editor.commit();
                }
                JSONObject jsonObject = new JSONObject(responseStr);
                String errcode = jsonObject.getString("errcode") ;
                String data = jsonObject.getString("data");
                if (errcode.equals("0")){
                    JSONObject jsonObject1 = new JSONObject(data);
                    Map<String,String> params = new HashMap<String, String>();
                    SharedPreferences setting = getSharedPreferences("shared",MODE_PRIVATE);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("studentid",jsonObject1.getString("studentid"));
                    editor.putString("name",jsonObject1.getString("name"));
                    editor.putString("gender",jsonObject1.getString("gender"));
                    editor.putString("vcode",jsonObject1.getString("vcode"));
                    editor.putString("location",jsonObject1.getString("location"));
                    editor.commit();
                }else {
                    Toast.makeText(Select.this,"该用户不存在！",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Select.this,MainScreen.class);
                    startActivity(i);
                    finish();
                }

//                params.put("studentid",jsonObject1.getString("studentid"));
//                params.put("name",jsonObject1.getString("name"));
//                params.put("gender",jsonObject1.getString("gender"));
//                params.put("vcode",jsonObject1.getString("vcode"));
//                params.put("location",jsonObject1.getString("location"));
//                params.put("grade",jsonObject1.getString("grade"));
//                params.put("errcode",errcode);
//                params.put("building",jsonObject1.getString("building"));
//                params.put("room",jsonObject1.getString("room"));


//                        Log.d("dds","room");
//                Message msg = new Message();
//                msg.what = RESPONSE;
//                msg.obj = params;
//                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null){
                    con.disconnect();
                }
            }
        }
    }).start();
}


}
