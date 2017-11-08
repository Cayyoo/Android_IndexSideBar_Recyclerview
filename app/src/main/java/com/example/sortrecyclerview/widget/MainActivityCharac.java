package com.example.sortrecyclerview.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortrecyclerview.ClearEditText;
import com.example.sortrecyclerview.PinyinComparator;
import com.example.sortrecyclerview.R;
import com.example.sortrecyclerview.SideBar;
import com.example.sortrecyclerview.SortAdapter;
import com.example.sortrecyclerview.SortModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.sortrecyclerview.R.array.date;

/**
 * Android使用RecyclerView实现（仿微信）的联系人A-Z字母排序和过滤搜索功能:
 * 1、支持字母、汉字搜索
 * 2、全局使用一个RecyclerView，根据查询条件过滤数据源，然后更新列表并展示
 * 3、拼音解析使用了jar包，见libs目录
 * 4、本例可使用jar包、CharacterParser.java两种形式来解析汉字，详见说明
 *
 * GitHub：https://github.com/xupeng92/SortRecyclerView
 *
 * CSDN：http://blog.csdn.net/SilenceOO/article/details/75661590?locationNum=5&fps=1
 */
public class MainActivityCharac extends AppCompatActivity {

    private ClearEditText mClearEditText;

    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog;

    LinearLayoutManager manager;

    private SortAdapter adapter;
    private List<SortModel> sourceDateList;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        //不使用jar包来解析汉字
        characterParser=CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));

                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sourceDateList = filledData(getResources().getStringArray(date));

        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);

        //RecyclerView配置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        adapter = new SortAdapter(this, sourceDateList);
        mRecyclerView.setAdapter(adapter);

        //item点击事件
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivityCharac.this, ((SortModel)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
            }
        });

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }



    private CharacterParser characterParser;

    /**
     * 为ListView填充数据
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);

            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setLetters(sortString.toUpperCase());
            } else {
                sortModel.setLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList;
        } else {
            filterDateList.clear();

            for (SortModel sortModel : sourceDateList) {
                String name = sortModel.getName();

                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }

        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

}
