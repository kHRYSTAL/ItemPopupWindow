package me.khrystal.itempopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import me.khrystal.library.ItemPopupWindow;

public class MainActivity extends AppCompatActivity {

    private ItemPopupWindow popupWindow;
    private RecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private ArrayList<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupWindow = new ItemPopupWindow(this);
        popupWindow.addActionItem(1, "回复");
        popupWindow.addActionItem(2, "点赞");
        popupWindow.addActionItem(3, "举报");

        popupWindow.setOnActionItemClickListener(new ItemPopupWindow.OnActionItemClickListener() {
            @Override
            public void onItemClick(ItemPopupWindow source, int pos, int actionId) {
                switch (actionId) {
                    case 1:
                        Toast.makeText(MainActivity.this, "回复", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "点赞", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "举报", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(getApplicationContext(), "dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDataList.add("index:" + i);
        }



        mAdapter = new SimpleAdapter(this,mDataList);
        mAdapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object data, View view, int position) {
                popupWindow.show(view);
                Toast.makeText(MainActivity.this,"click:" + position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(Object data, View view, int position) {
                //nothing todo
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }


}
