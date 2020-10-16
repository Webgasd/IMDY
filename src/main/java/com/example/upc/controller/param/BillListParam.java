package com.example.upc.controller.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BillListParam {
    Integer billId;

    private String name;

    private String picture;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date date;
}
