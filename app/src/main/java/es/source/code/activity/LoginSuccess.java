package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2017/12/19.
 */

public class LoginSuccess extends Activity{
    private Button mLookup,mLoginout;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success);
        mLoginout = findViewById(R.id.login_out);
        mLookup = findViewById(R.id.lookup);
        mLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginSuccess.this,Check.class);
                startActivity(i);
                finish();
            }
        });
        mLoginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences  = (SharedPreferences) getSharedPreferences("shared",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(LoginSuccess.this,LoginOrRegister.class);
                startActivity(i);
                finish();
            }
        });



    }


}
