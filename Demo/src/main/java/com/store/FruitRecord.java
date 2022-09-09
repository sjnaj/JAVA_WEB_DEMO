package com.store;

import java.time.LocalDateTime;

public class FruitRecord extends BaseRecord {

    public String kind = "fruit";

    public FruitRecord() {
    }

    public FruitRecord(String name, Float price, Float count, LocalDateTime time, String seller,
            String remark) {
        super(name, price, count, time, seller, remark);
    }

}
