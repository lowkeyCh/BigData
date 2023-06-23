package com.example.web;

import com.alibaba.fastjson2.JSON;
import com.example.Service.GetAvg;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/AvgServlet")
public class CourseAvgServlet extends HttpServlet {

    private GetAvg getAvg  = new GetAvg();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader br = request.getReader();
        String params = br.readLine();

        List<Map<String,Object>> avg = (List<Map<String, Object>>) JSON.parseObject(params, GetAvg.class);

        response.getWriter().write(String.valueOf(avg));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
