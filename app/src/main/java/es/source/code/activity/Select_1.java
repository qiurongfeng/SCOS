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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
 * Created by Administrator on 2017/12/17.
 */

public class Select_1 extends Activity {
    private TextView mName, mNumber, mSex, mLocation, mGrade, mVcode, mFive, mNine, mEight, mShisan, mShisi;
    public static final int RESPONSE = 1;
    public static final int RESPONSE_1 = 2;
    private Button mSub;
    public static String buildingNo = "5";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single);
        isChoice();
        initView();
        setUI();
        Choose();

        mSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
//                Log.d("sssssssss",buildingNo);
//                Log.d("sssssssss",sharedPreferences.getString(buildingNo,""));
                if (sharedPreferences.getString(buildingNo,"").equals("0")){
                    Toast.makeText(Select_1.this,"该楼已经被选完！",Toast.LENGTH_LONG).show();
                    finish();
                    Intent i =new Intent(Select_1.this,Select_1.class);
                    startActivity(i);
                    Select_1.this.finish();
                    return;
                }

                postMessage();
            }

        });

    }
    private void Choose(){
        Spinner sp = (Spinner) findViewById(R.id.sp1);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(Select_1.this, "点击的是第" + i, Toast.LENGTH_LONG).show();
                if (i == 0){
                    buildingNo = "5";
                }
                if (i == 1){
                    buildingNo = "13";
                }
                if (i == 2){
                    buildingNo = "14";
                }
                if (i == 3 ){
                    buildingNo = "8";
                }
                if (i == 4){
                    buildingNo = "9";
                }
//                switch (i) {
//                    case 0:
//                        buildingNo = "5";
//                    case 1:
//                        buildingNo = "13";
//                    case 2:
//                        buildingNo = "14";
//                    case 3:
//                        buildingNo = "8";
//                    case 4:
//                        buildingNo = "9";
//                }
//                Log.d("yyyyyyyyy",buildingNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void isChoice() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String choice_status = sharedPreferences.getString("choice_status", "");
        if (choice_status.equals("1")) {
            Toast.makeText(Select_1.this, "您已经选择成功！", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Select_1.this, Check.class);
            startActivity(i);
            finish();
        }
    }

    private void setUI() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String username = sharedPreferences.getString("studentid", "null");
        String name = sharedPreferences.getString("name", "null");
        String gender = sharedPreferences.getString("gender", "null");
        String vcode = sharedPreferences.getString("vcode", "null");
        String location = sharedPreferences.getString("location", "null");
        String grade = sharedPreferences.getString("grade", "null");
        mNumber.setText("学号：" + username);
        mVcode.setText("验证码：" + vcode);
        mSex.setText("性别：" + gender);
        mName.setText("姓名：" + name);
        mLocation.setText("校区：" + location);
        mGrade.setText("年级：" + grade);
        String code = null;
        if (gender.equals("男")) {
            code = "1";
        }
        if (gender.equals("女")) {
            code = "2";
        }
        final String address_getroom = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=" + code;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    AllowX509TrustManager.allowAllSSL();
                    URL url = new URL(address_getroom);
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
                    JSONObject jsonObject = new JSONObject(response_str);
                    String errcode = jsonObject.getString("errcode");
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("5", jsonObject1.getString("5"));
                    params.put("13", jsonObject1.getString("13"));
                    params.put("14", jsonObject1.getString("14"));
                    params.put("8", jsonObject1.getString("8"));
                    params.put("9", jsonObject1.getString("9"));
                    params.put("errcode", errcode);
                    Message msg = new Message();
                    msg.what = RESPONSE;
                    msg.obj = params;
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

    private void initView() {
        mGrade = findViewById(R.id.grade);
        mLocation = findViewById(R.id.location);
        mName = findViewById(R.id.name);
        mSex = findViewById(R.id.sex);
        mVcode = findViewById(R.id.vcode);
        mNumber = findViewById(R.id.number);
        mFive = findViewById(R.id.five);
        mNine = findViewById(R.id.nine);
        mShisan = findViewById(R.id.shisan);
        mShisi = findViewById(R.id.shisi);
        mEight = findViewById(R.id.eight);
        mSub = findViewById(R.id.submit1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESPONSE:
                    HashMap<String, String> info = (HashMap) msg.obj;
                    if (info.get("errcode").equals("0")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("5",info.get("5"));
                            editor.putString("8",info.get("8"));
                            editor.putString("9",info.get("9"));
                            editor.putString("13",info.get("13"));
                            editor.putString("14",info.get("14"));
                            editor.commit();
                        mFive.setText("5号楼：" + info.get("5"));
                        mEight.setText("8号楼：" + info.get("8"));
                        mNine.setText("9号楼：" + info.get("9"));
                        mShisan.setText("13号楼：" + info.get("13"));
                        mShisi.setText("14号楼：" + info.get("14"));
                    } else {
                        Toast.makeText(Select_1.this, "发生未知错误！", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Select_1.this, MainScreen.class);
                        startActivity(i);
                        finish();
                    }

            }
        }
    };

    private void postMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String username = sharedPreferences.getString("studentid", "null");

//        switch (buildingNo){
//            case "5":
//                if (mFive.getText().toString().equals("5号楼：0")){
//                    Toast.makeText(Select_1.this,"该楼房间已被选完！",Toast.LENGTH_LONG).show();
//                    refresh();
//                    return;
//                }
//            case "13":
//                if (mShisan.getText().toString().equals("13号楼：0")){
//                    Toast.makeText(Select_1.this,"该楼房间已被选完！",Toast.LENGTH_LONG).show();
//                    refresh();
//                    return;
//                }
//            case "14":
//                if (mShisi.getText().toString().equals("14号楼：0")){
//                    Toast.makeText(Select_1.this,"该楼房间已被选完！",Toast.LENGTH_LONG).show();
//                    refresh();
//                    return;
//                }
//            case "8":
//                if (mEight.getText().toString().equals("8号楼：0")){
//                    Toast.makeText(Select_1.this,"该楼房间已被选完！",Toast.LENGTH_LONG).show();
//                   refresh();
//                    return;
//                }
//            case "9":
//                if (mNine.getText().toString().equals("9号楼：0")){
//                    Toast.makeText(Select_1.this,"该楼房间已被选完！",Toast.LENGTH_LONG).show();
//                    Log.d("zzzzzzzzzzz","zzzzzzzzzzz");
//                    refresh();
//                    return;
//                }
//        }
        String data = "num=1" + "&stuid=" + username + "&buildingNo=" + buildingNo;
        final String path = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom?" + data;
        Log.d("path",path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    AllowX509TrustManager.allowAllSSL();
                    URL url = new URL(path);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
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
                    Log.d("errr", response_str);
                    JSONObject jsonObj = new JSONObject(response_str);
                    String errcode = jsonObj.getString("errcode");
                    if (errcode.equals("0")){
                        SharedPreferences set = getSharedPreferences("shared",MODE_PRIVATE);
                        SharedPreferences.Editor editor = set.edit();
                        editor.putString("choice_status","1");
                        editor.putString("building",buildingNo);
                        editor.putString("room","4231");
                        editor.commit();
                        Intent i = new Intent(Select_1.this,ChooseSucess.class);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(Select_1.this,"选择失败！！",Toast.LENGTH_LONG).show();

                    }
//                    Message msg = new Message();
//                    msg.what = RESPONSE_1;
//                    msg.obj = errcode;
//                    handler.sendMessage(msg);
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
}
