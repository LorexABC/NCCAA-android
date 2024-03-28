package com.app_nccaa.nccaa.Model;

import java.util.ArrayList;

public class CityModel {

    private String name;
    private String cycle;
    private String id;
    private String code;
    private String group;
    private String type;
    private String value;
    private boolean isChecked;

    private String creditsNeeded;
    private String creditsCompleted;
    private String creditsLeft;
    private String anesthesiaCreditsLeft;
    private String isCurrent;
    private String receiptId;
    private String receiptPaid;
    private ArrayList<CityModel> retirementPlan2;



    public ArrayList<CityModel> getRetirementPlan2() {
        return retirementPlan2;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public void setRetirementPlan2(ArrayList<CityModel> retirementPlan2) {
        this.retirementPlan2 = retirementPlan2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreditsNeeded() {
        return creditsNeeded;
    }

    public void setCreditsNeeded(String creditsNeeded) {
        this.creditsNeeded = creditsNeeded;
    }

    public String getCreditsCompleted() {
        return creditsCompleted;
    }

    public void setCreditsCompleted(String creditsCompleted) {
        this.creditsCompleted = creditsCompleted;
    }

    public String getCreditsLeft() {
        return creditsLeft;
    }

    public void setCreditsLeft(String creditsLeft) {
        this.creditsLeft = creditsLeft;
    }

    public String getAnesthesiaCreditsLeft() {
        return anesthesiaCreditsLeft;
    }

    public void setAnesthesiaCreditsLeft(String anesthesiaCreditsLeft) {
        this.anesthesiaCreditsLeft = anesthesiaCreditsLeft;
    }

    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptPaid() {
        return receiptPaid;
    }

    public void setReceiptPaid(String receiptPaid) {
        this.receiptPaid = receiptPaid;
    }



    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
