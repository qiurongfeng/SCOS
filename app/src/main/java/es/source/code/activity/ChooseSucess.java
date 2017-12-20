package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ChooseSucess extends Activity {
    private Button mLookup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_sucess);
        mLookup = findViewById(R.id.lookup);
        mLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseSucess.this,Check.class);
                startActivity(i);
                finish();
            }
        });
    }

}
