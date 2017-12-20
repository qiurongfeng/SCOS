package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
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

public class Check extends Activity{
    public static final int RESPONSE=1;
    private TextView mName,mNumber,mSex,mLocation,mGrade,mBuliding,mRoom,mVcode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        initView();
        isLogin();
        setUI();
//        isChoose();


    }

//    private void isChoose(){   //s数据造假
//        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
//        String choice_status = sharedPreferences.getString("choice_status", "");
//        if (choice_status.equals("1")) {
//            String building = sharedPreferences.getString("building","");
//            String room = sharedPreferences.getString("room","");
//            mBuliding.setText("楼号："+building);
//            mRoom.setText("房间号："+room);
//        }
//    }
    private void isLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        String loginStatus= sharedPreferences.getString("login_status","2");
        if (loginStatus.equals("2")){
            Toast.makeText(Check.this,"请先登陆",Toast.LENGTH_LONG).show();
            Intent i = new Intent(Check.this,LoginOrRegister.class);
            startActivity(i);
            finish();
        }
    }

    private void setUI(){
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
                        JSONObject jsonObject = new JSONObject(responseStr);
                        String errcode = jsonObject.getString("errcode") ;
                        String data = jsonObject.getString("data");
                        JSONObject jsonObject1 = new JSONObject(data);
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("studentid",jsonObject1.getString("studentid"));
                        params.put("name",jsonObject1.getString("name"));
                        params.put("gender",jsonObject1.getString("gender"));
                        params.put("vcode",jsonObject1.getString("vcode"));
                        params.put("location",jsonObject1.getString("location"));
                        params.put("grade",jsonObject1.getString("grade"));
                        params.put("errcode",errcode);
                        if (response_str.indexOf("building") != -1){
                            params.put("building",jsonObject1.getString("building"));
                            params.put("room",jsonObject1.getString("room"));
                        }

//                        Log.d("dds","room");
                        Message msg = new Message();
                        msg.what = RESPONSE;
                        msg.obj = params;
                        handler.sendMessage(msg);
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
    private Handler handler = new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case RESPONSE:
                Map<String,String> info = (Map)msg.obj;
                  mName.setText("姓名："+info.get("name"));
                  mNumber.setText("学号："+info.get("studentid"));
                  mSex.setText("性别："+info.get("gender"));
                  mLocation.setText("校区："+info.get("location"));
                  mGrade.setText("年级："+ info.get("grade"));
                  mVcode.setText("验证码：" + info.get("vcode") );
                  if (info.get("building") !=null){
                      mBuliding.setText("楼号："+info.get("building"));
                      mRoom.setText("房间号："+info.get("room"));
                  }else {
                      mBuliding.setText("楼号：您还未选择，请到选宿舍界面选择！");
                      mBuliding.setTextColor(Color.parseColor("#FFEF1115"));
                      mRoom.setText("房间号：您还未选择，请到选宿舍界面选择！");
                      mRoom.setTextColor(Color.parseColor("#FFEF1115"));
                      SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
                      String choice_status = sharedPreferences.getString("choice_status", "");
                      if (choice_status.equals("1")) {
                          String building = sharedPreferences.getString("building","");
                          String room = sharedPreferences.getString("room","");
                          mBuliding.setText("楼号："+building);
                          mRoom.setText("房间号："+room);
                      }
                  }


          }
      }
    };
    private void initView(){
        mName = findViewById(R.id.name);
        mBuliding = findViewById(R.id.building);
        mVcode = findViewById(R.id.vcode);
        mGrade = findViewById(R.id.grade);
        mLocation = findViewById(R.id.location);
        mNumber  = findViewById(R.id.number);
        mSex = findViewById(R.id.sex);
        mRoom = findViewById(R.id.room);
    }
}
