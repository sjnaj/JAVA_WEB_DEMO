package com.store;

import java.time.LocalDateTime;

public class FoodRecord extends BaseRecord {
    public String kind = "food";

    /**
     * @param name
     * @param price
     * @param count
     * @param time
     * @param seller
     * @param remark
     */
    public FoodRecord(String name, Float price, Float count, LocalDateTime time, String seller, String remark) {
        super(name, price, count, time, seller, remark);
    }

    /**
     * 
     */
    public FoodRecord() {
    }
}
