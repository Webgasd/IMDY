package com.example.upc.service;

import com.example.upc.controller.param.PageQuery;
import com.example.upc.controller.param.PageResult;
import com.example.upc.controller.searchParam.TypeSearchParam;
import com.example.upc.dataobject.FormatOrigin;

public interface FormatOriginService {
    PageResult getPage (PageQuery pageQuery, TypeSearchParam typeSearchParam);
    void insert(FormatOrigin formatOrigin);
    void delete(int foId);
    void update(FormatOrigin formatOrigin);
}
