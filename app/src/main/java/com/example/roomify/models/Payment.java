package com.example.roomify.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment {
    private String id;
    private String bookingId;
    private String userId;
    private double amount;
    private String paymentMethod;  // "credit_card", "debit_card", "cash", "bank_transfer"
    private String status;         // "pending", "completed", "failed", "refunded"
    private Date paymentDate;
    private String transactionId;
    private Map<String, Object> paymentDetails;
    private String notes;

    // Empty constructor required for Firestore
    public Payment() {
        paymentDate = new Date();
        paymentDetails = new HashMap<>();
    }

    public Payment(String bookingId, String userId, double amount, String paymentMethod) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "pending";
        this.paymentDate = new Date();
        this.paymentDetails = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Map<String, Object> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Map<String, Object> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Add payment details
    public void addPaymentDetail(String key, Object value) {
        if (this.paymentDetails == null) {
            this.paymentDetails = new HashMap<>();
        }
        this.paymentDetails.put(key, value);
    }

    // Convert Payment object to a Map for Firestore
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("bookingId", bookingId);
        result.put("userId", userId);
        result.put("amount", amount);
        result.put("paymentMethod", paymentMethod);
        result.put("status", status);
        result.put("paymentDate", paymentDate);

        if (transactionId != null) result.put("transactionId", transactionId);
        if (paymentDetails != null) result.put("paymentDetails", paymentDetails);
        if (notes != null) result.put("notes", notes);

        return result;
    }
}