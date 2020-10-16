package com.example.upc.controller.param;

import com.example.upc.dataobject.BillReport;
import com.example.upc.dataobject.FormatOriginRecordEx;

import java.util.List;

/**
 * @author 75186
 */
public class FormatOriginRecordExListParam extends FormatOriginRecordEx {
    private String enterpriseName;
    private String areaName;
    private List<BillListParam> billList;
}
