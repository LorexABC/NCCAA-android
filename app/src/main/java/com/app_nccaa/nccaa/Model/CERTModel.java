package com.app_nccaa.nccaa.Model;

public class CERTModel {

    private String id;
    private String type;
    private String examDateStart;
    private String examDateEnd;
    private String paidDate;
    private String paidAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExamDateStart() {
        return examDateStart;
    }

    public void setExamDateStart(String examDateStart) {
        this.examDateStart = examDateStart;
    }

    public String getExamDateEnd() {
        return examDateEnd;
    }

    public void setExamDateEnd(String examDateEnd) {
        this.examDateEnd = examDateEnd;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(String paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public String getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(String attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityCode() {
        return universityCode;
    }

    public void setUniversityCode(String universityCode) {
        this.universityCode = universityCode;
    }

    private String paymentPeriod;
    private String attemptNumber;
    private String receiptId;
    private String universityName;
    private String universityCode;

}
