package com.example.upc.controller.param;

import com.example.upc.dataobject.BillReport;
import com.example.upc.dataobject.FormatOriginRecordEx;

public class BillReportParam extends FormatOriginRecordEx {
    private String billList;

    public String getBillList() {
        return billList;
    }

    public void setBillList(String billList) {
        this.billList = billList;
    }
}
