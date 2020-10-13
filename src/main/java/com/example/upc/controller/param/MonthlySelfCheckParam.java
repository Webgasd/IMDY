package com.example.upc.controller.param;

import com.example.upc.common.InsertGroup;
import com.example.upc.common.SearchGroup;
import com.example.upc.dataobject.MonthlyAdditionalAnswer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class MonthlySelfCheckParam {

    private Integer id;

    private Integer enterpriseId;

    @NotNull(groups={SearchGroup.class},message = "搜索日期不能为空")
    @DateTimeFormat(pattern="yyyy")
    @JsonFormat(pattern="yyyy",timezone = "GMT+8")
    private Date searchTime;


    @NotNull(groups={InsertGroup.class},message = "检查日期不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date checkTime;

    @NotNull(groups={InsertGroup.class},message = "检查人员不能为空")
    private String checkStaff;

    @NotNull(groups={InsertGroup.class},message = "陪同检查人员不能为空")
    private String accompanyStaff;

    List<MonthlySelfCheckOptCategoryParam> monthlySelfCheckOptCategoryParamList;

    private String checkContent;

    private String existedProblem;

    private String rectifySituation;

    private String lastRecifySituation;

}
