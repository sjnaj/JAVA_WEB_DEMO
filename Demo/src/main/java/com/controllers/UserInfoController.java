package com.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class UserInfoController {
    private String index(HttpServletRequest request) throws IOException {
        return "userinfo";
    }
}
