package com.oxandon.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.oxandon.calendar.R;
import com.oxandon.calendar.adapter.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 日历控件
 * Created by peng on 2017/8/3.
 */

public class CalendarView extends LinearLayout {
    private final CalendarAdapter calendarAdapter = new CalendarAdapter();

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        inflate(context, R.layout.layout_calendar_view, this);
        //星期标头
        GridView weekView = (GridView) findViewById(R.id.weekView);
        ListAdapter weekAdapter = buildWeekAdapter(context);
        weekView.setNumColumns(weekAdapter.getCount());
        weekView.setAdapter(weekAdapter);
        //月份列表
        RecyclerView bodyView = (RecyclerView) findViewById(R.id.bodyView);
        bodyView.setLayoutManager(new LinearLayoutManager(context));
        bodyView.setAdapter(calendarAdapter);
        initDecoration(bodyView);
    }

    public void initDecoration(RecyclerView bodyView) {
        GroupListener groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                Date date = calendarAdapter.getDates().get(position);
                return TimeUtil.dateText(date.getTime(), TimeUtil.YY_M);
            }
        };
        StickyDecoration decoration = StickyDecoration.Builder
                .init(groupListener)
                .setGroupBackground(Color.parseColor("#48BDFF"))       //背景色
                .setGroupHeight(ViewUtils.dp2px(getContext(), 35))     //高度
                .setDivideColor(Color.parseColor("#CCCCCC"))           //分割线颜色
                .setDivideHeight(ViewUtils.dp2px(getContext(), 1))     //分割线高度 (默认没有分割线)
                .setGroupTextColor(Color.WHITE)                        //字体颜色
                .setGroupTextSize(ViewUtils.sp2px(getContext(), 15))   //字体大小
                .setTextSideMargin(ViewUtils.dp2px(getContext(), 10))  //边距   靠左时为左边距  靠右时为右边距
                .setTextAlign(Paint.Align.CENTER)                      //居中显示
                .build();
        bodyView.addItemDecoration(decoration);
    }

    public void func() {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date date = new Date();
        calendar.setTime(date);
        for (int i = 0; i < 50; i++) {
            dates.add(calendar.getTime());
            int month = calendar.get(Calendar.MONTH);
            month -= 1;
            calendar.set(Calendar.MONTH, month);
        }
        calendarAdapter.update(dates);

    }

    public List<Date> getData() {
        return calendarAdapter.getDates();
    }

    private ListAdapter buildWeekAdapter(Context context) {
        String[] from = new String[]{"week"};
        int[] to = new int[]{R.id.tvWeekDay};
        String[] strings = new String[]{
                "日", "一", "二", "三", "四", "五", "六"
        };
        List<Map<String, String>> weeks = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(from[0], strings[i]);
            weeks.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(context, weeks, R.layout.layout_week_view, from, to);
        return adapter;
    }
}