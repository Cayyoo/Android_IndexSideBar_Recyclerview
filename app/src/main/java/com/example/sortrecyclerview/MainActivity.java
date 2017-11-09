package com.example.sortrecyclerview;

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

import com.example.sortrecyclerview.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Android使用RecyclerView实现（仿微信）的联系人A-Z字母排序和过滤搜索功能:
 * 1、支持字母、汉字搜索
 * 2、全局使用一个RecyclerView，根据查询条件过滤数据源，然后更新列表并展示
 * 3、拼音解析使用了jar包，见libs目录
 * 4、本例可使用jar包(PinyinUtils.java类)、CharacterParser.java两种形式来解析汉字，详见说明
 *
 * GitHub：https://github.com/xupeng92/SortRecyclerView
 *
 * CSDN：http://blog.csdn.net/SilenceOO/article/details/75661590?locationNum=5&fps=1
 *
 *
 * 其他参考项目：
 * 浅谈android中手机联系人字母索引表的实现:
 * http://blog.csdn.net/u013064109/article/details/52013744
 */
public class MainActivity extends AppCompatActivity {

    private ClearEditText mClearEditText;

    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog;

    LinearLayoutManager manager;

    private SortAdapter adapter;
    private List<SortModel> sourceDataList;

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
        sourceDataList = filledData(getResources().getStringArray(R.array.dataArray));

        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator);

        //RecyclerView配置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        adapter = new SortAdapter(this, sourceDataList);
        mRecyclerView.setAdapter(adapter);

        //item点击事件
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((SortModel)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
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


    /**
     * 为RecyclerView填充数据
     *
     * @param data
     * @return
     */
    private List<SortModel> filledData(String[] data) {
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(data[i]);

            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(data[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            //正则表达式，判断首字母是否是英文字母
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
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDataList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDataList = sourceDataList;
        } else {
            filterDataList.clear();

            for (SortModel sortModel : sourceDataList) {
                String name = sortModel.getName();

                if (name.indexOf(filterStr.toString()) != -1
                        || PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDataList.add(sortModel);
                }

            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDataList, pinyinComparator);
        adapter.updateList(filterDataList);
    }

}
