package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.source.code.data.MainScreenData;
import utils.CommonAdapter;

public class MainScreen extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CommonAdapter<MainScreenData, MainScreenHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initView();
        initData();
        initUI();

    }

    private void initUI() {
        String vaule = getIntent().getStringExtra("key");
        if (TextUtils.isEmpty(vaule) || !vaule.equals(SCOSEntry.FROM_ENTRY)){
            mRecyclerView.setVisibility(View.GONE);
        }else {
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void initData() {
        adapter.append(new MainScreenData(R.mipmap.dc,"点菜"));
        adapter.append(new MainScreenData(R.mipmap.dd,"查看订单"));
        adapter.append(new MainScreenData(R.mipmap.wd,"登录/注册"));
        adapter.append(new MainScreenData(R.mipmap.bz,"系统帮助"));
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CommonAdapter<MainScreenData, MainScreenHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_main_screen;
            }

            @Override
            public MainScreenHolder holderInstance(View itemView, int viewType) {
                return new MainScreenHolder(itemView);
            }

            @Override
            public void fillView(MainScreenHolder holder, final MainScreenData data, final int position) {
                //设置图片
                holder.imageView.setImageResource(data.getRes());
                //设置标题
                holder.textView.setText(data.getName());
                //设置点击事件
                holder.clickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClick(data,position);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
    }

    private void onItemClick(MainScreenData data, int position) {
        //        0点菜，1查看订单，2登录/注册，3系统帮助。
        switch (position){
            case 0:
                //跳转点菜页面
                break;

            case 1:
                //跳转查看订单页面
                break;

            case 2:
                //跳转登录/注册页面
                /**显式页面跳转*/
                Intent it = new Intent(this,LoginOrRegister.class);
                startActivity(it);
                break;

            case 3:
                //跳转系统帮助页面

                break;
        }
    }



    public class MainScreenHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout clickView;

        public MainScreenHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv);
            textView = itemView.findViewById(R.id.tv);
            clickView = itemView.findViewById(R.id.click_view);
        }
    }
}
