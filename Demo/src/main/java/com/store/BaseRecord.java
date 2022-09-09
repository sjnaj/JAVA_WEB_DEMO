package com.store;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseRecord  implements Serializable {
        public String kind;
        protected Integer id;
        protected String name;

        protected Float price;
        protected Float count;
        protected LocalDateTime time;
        protected String seller;

        protected Float total;

        protected String remark;

    public BaseRecord() {
    }

    public BaseRecord(String name, Float price, Float count, LocalDateTime time, String seller,
            String remark) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.time = time;
        this.seller = seller;
        this.total =Float.parseFloat(String.format("%.2f", price*count));//保留两位小数
        this.remark = remark;
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

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getcount() {
        return count;
    }

    public void setcount(Float count) {
        this.count = count;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime( LocalDateTime time) {
        this.time = time;
    }

    /**
     * @return the seller
     */
    public String getSeller() {
        return seller;
    }

    /**
     * @param seller the seller to set
     */
    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * @return the total
     */
    public Float getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Float total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t\t" + name + "\t\t" + price + "\t\t" + count + "\t\t" + time + "\t\t" + seller + "\t\t" + remark;
    }
}
