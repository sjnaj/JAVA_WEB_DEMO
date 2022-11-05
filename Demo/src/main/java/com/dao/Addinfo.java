package com.dao;

import java.time.LocalDateTime;
import java.util.*;
import com.myssm.basedao.*;

import com.store.FoodRecord;
import com.store.FruitRecord;

//仓储数据产生
public class Addinfo {
    static String[] sellers = { "小明1", "小李1", "小王1", "小孙1", "小张1", "小刘1", "小周1" };
    static String[] goods = { "薯片", "饮料", "火腿肠", "饼干", "面包", "啤酒", "方便面" };
    static Float[] prices = { 5.5f, 6.6f, 3.5f, 2.6f, 4.6f, 8.8f, 5.0f };// 可以增加浮动

    public static void main(String[] args) {
        RecordDAO dao = new RecordDAO();
        dao.Table = "foodrecords";
        dao.getConn();

        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0, 0);

        Random r = new Random();

        while (true) {
            time=time.plusSeconds(r.nextInt(5000));
            if (time.getYear() < 2022) {
                int h = time.getHour();
                if (h > 7 && h < 24) {
                    int i = r.nextInt(7);
                    dao.add(new FoodRecord(goods[i], prices[i],
                            (float) r.nextInt(1, 5), time,
                            sellers[r.nextInt(7)], "无"));
                }

            } else
                break;
        }
        // select * from fruitrecords where `time`>=STR_TO_DATE('2021-04-19
        // 00:00:00','%Y-%m-%d %H:%i:%s') and`time`<=STR_TO_DATE('2021-06-20
        // 00:00:00','%Y-%m-%d %H:%i:%s')
    }
}
