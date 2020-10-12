package com.example.upc.controller.param;

import com.example.upc.dataobject.MonthlySelfcheckOpt;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MonthlySelfCheckOptCategoryParam {
    private Integer categoryId;

    private String categoryName;

    private Integer pageNumber;

    List<MonthlySelfcheckOptParam> optList;
}
