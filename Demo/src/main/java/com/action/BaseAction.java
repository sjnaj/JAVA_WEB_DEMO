package com.action;

import java.util.List;

public interface BaseAction<T> {
    List<T> getList();


    T getByid(Integer id);

    int getCount(String keyword);

    T getByFloatField(String field, Float f);

    T getByIntField(String field, Integer i);

    T getByStringField(String field, String str);

    void update(T value);

    void del(Integer fid);

    void add(T value);
}
