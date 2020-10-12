package com.example.upc.controller.param;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class MonthlySelfcheckOptParam {
    private Integer optId;

    private String optTopic;

    private Integer optIndex;

    private String optAnswer;

    private String operator;

    private String operatorIp;

    private Date operatorTime;
}
