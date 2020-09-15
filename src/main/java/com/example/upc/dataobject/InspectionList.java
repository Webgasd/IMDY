package com.example.upc.dataobject;

import java.util.List;

public class InspectionList {
    private String inspectionPosition;
    private int inspectionPositionId;
    private List<StartSelfInspection> startSelfInspectionList;

    public String getInspectionPosition(){return inspectionPosition;}

    public void setInspectionPosition(String inspectionPosition){this.inspectionPosition = inspectionPosition;}

    public int getInspectionPositionId() {
        return inspectionPositionId;
    }

    public void setInspectionPositionId(int inspectionPositionId) {
        this.inspectionPositionId = inspectionPositionId;
    }

    public List<StartSelfInspection> getStartSelfInspectionList(){return startSelfInspectionList;}

    public void  setStartSelfInspectionList(List<StartSelfInspection> startSelfInspectionList){this.startSelfInspectionList = startSelfInspectionList;}
}
