package com.app_nccaa.nccaa.Model;


public class Certificate_Exam_Model {

    private String id;
    private String type;
    private String attemptNumber;
    private String dateStart;
    private String dateEnd;
    private String registrationIsAvailable;
    private String registrationStart;
    private String registrationEnd;
    private String lateStart;
    private String lateEnd;
    private String regularFee;
    private String lateFee;
    private String retakeFee;
    private String psiFilled;
    private String receiptId;
    private String receiptPaid;
    private String resultsAvailable;
    private String testingCenterUrl;
    private String bookingMade;
    private String bookingStatus;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTestingCenterUrl() {
        return testingCenterUrl;
    }

    public void setTestingCenterUrl(String testingCenterUrl) {
        this.testingCenterUrl = testingCenterUrl;
    }

    public String getBookingMade() {
        return bookingMade;
    }

    public void setBookingMade(String bookingMade) {
        this.bookingMade = bookingMade;
    }

    public String getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(String attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

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

    public String getRegistrationIsAvailable() {
        return registrationIsAvailable;
    }

    public void setRegistrationIsAvailable(String registrationIsAvailable) {
        this.registrationIsAvailable = registrationIsAvailable;
    }

    public String getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }

    public String getRegistrationEnd() {
        return registrationEnd;
    }

    public void setRegistrationEnd(String registrationEnd) {
        this.registrationEnd = registrationEnd;
    }

    public String getLateStart() {
        return lateStart;
    }

    public void setLateStart(String lateStart) {
        this.lateStart = lateStart;
    }

    public String getLateEnd() {
        return lateEnd;
    }

    public void setLateEnd(String lateEnd) {
        this.lateEnd = lateEnd;
    }

    public String getRegularFee() {
        return regularFee;
    }

    public void setRegularFee(String regularFee) {
        this.regularFee = regularFee;
    }

    public String getLateFee() {
        return lateFee;
    }

    public void setLateFee(String lateFee) {
        this.lateFee = lateFee;
    }

    public String getRetakeFee() {
        return retakeFee;
    }

    public void setRetakeFee(String retakeFee) {
        this.retakeFee = retakeFee;
    }

    public String getPsiFilled() {
        return psiFilled;
    }

    public void setPsiFilled(String psiFilled) {
        this.psiFilled = psiFilled;
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

    public String getResultsAvailable() {
        return resultsAvailable;
    }

    public void setResultsAvailable(String resultsAvailable) {
        this.resultsAvailable = resultsAvailable;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }
}
