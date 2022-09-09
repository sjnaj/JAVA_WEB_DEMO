package com.store;

public class FruitInventory extends BaseInventory {
    public String kind = "fruit";

    /**
     * 
     * @param name
     * @param purchasePrice
     * @param salePrice
     * @param stock
     * @param remark
     */
    public FruitInventory(Integer id, String name, Float purchasePrice, Float salePrice, Float stock,
            String remark) {
        super(id,name, purchasePrice, salePrice, stock, remark);
    }

    /**
     * 
     */
    public FruitInventory() {
    }

}
