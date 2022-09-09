package com.store;

import java.io.Serializable;

public  class BaseInventory implements Serializable {
    public String kind;
    protected Integer id;
    protected String name;
    protected Float purchasePrice;
    protected Float salePrice;
    protected Float stock;
    protected String remark;

    /**
     * @param id
     * @param name
     * @param purchasePrice
     * @param salePrice
     * @param stock
     * @param remark
     */
    public BaseInventory(Integer id, String name, Float purchasePrice, Float salePrice, Float stock,
            String remark) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.stock = stock;
        this.remark = remark;
    }

    /**
     * 
     */
    public BaseInventory() {
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the purchasePrice
     */
    public Float getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * @param purchasePrice the purchasePrice to set
     */
    public void setPurchasePrice(Float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * @return the salePrice
     */
    public Float getSalePrice() {
        return salePrice;
    }

    /**
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(Float salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * @return the stock
     */
    public Float getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(Float stock) {
        this.stock = stock;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
