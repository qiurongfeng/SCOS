package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/17.
 */

public class Select_1 extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single);
        isChoice();

    }

    private void isChoice(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        String choice_status = sharedPreferences.getString("choice_status","");
        if (choice_status.equals("1")){
            Toast.makeText(Select_1.this,"您已经选择成功！",Toast.LENGTH_LONG).show();
            Intent i = new Intent(Select_1.this,Check.class);
            startActivity(i);
            finish();
        }
    }
    private void setUI(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared",MODE_PRIVATE);
        String username = sharedPreferences.getString("studentid","");
        String name = sharedPreferences.getString("name","");
        String gender = sharedPreferences.getString("gender","");
        String vcode = sharedPreferences.getString("vcode","");
        String location = sharedPreferences.getString("location","");
    }
}
