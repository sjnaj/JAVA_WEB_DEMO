package com.store;

public class FoodInventory extends BaseInventory {
    public String kind = "food";

    /**
     * @param name
     * @param purchasePrice
     * @param salePrice
     * @param stock
     * @param remark
     */
    public FoodInventory(Integer id, String name, Float purchasePrice, Float salePrice, Float stock, String remark) {
        super(id,name, purchasePrice, salePrice, stock, remark);
    }

    /**
     * 
     */
    public FoodInventory() {
    }

}
