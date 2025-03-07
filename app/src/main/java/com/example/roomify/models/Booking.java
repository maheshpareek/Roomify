    package com.example.roomify.models;

    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;

    public class Booking {
        private String id;
        private String userId;
        private String roomId;
        private String roomNumber;
        private Date checkInDate;
        private Date checkOutDate;
        private double totalPrice;
        private String status; // "pending", "confirmed", "checked_in", "checked_out", "cancelled"
        private Date createdAt;
        private Map<String, Object> paymentDetails;
        private String guestName;
        private int numberOfGuests;
        private String specialRequests;

        // Empty constructor required for Firestore
        public Booking() {
            createdAt = new Date();
            paymentDetails = new HashMap<>();
        }

        public Booking(String userId, String roomId, String roomNumber, Date checkInDate,
                       Date checkOutDate, double totalPrice) {
            this.userId = userId;
            this.roomId = roomId;
            this.roomNumber = roomNumber;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalPrice = totalPrice;
            this.status = "pending";
            this.createdAt = new Date();
            this.paymentDetails = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public Date getCheckInDate() {
            return checkInDate;
        }

        public void setCheckInDate(Date checkInDate) {
            this.checkInDate = checkInDate;
        }

        public Date getCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(Date checkOutDate) {
            this.checkOutDate = checkOutDate;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Map<String, Object> getPaymentDetails() {
            return paymentDetails;
        }

        public void setPaymentDetails(Map<String, Object> paymentDetails) {
            this.paymentDetails = paymentDetails;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public void setNumberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
        }

        public String getSpecialRequests() {
            return specialRequests;
        }

        public void setSpecialRequests(String specialRequests) {
            this.specialRequests = specialRequests;
        }

        // Add payment information
        public void addPaymentDetail(String key, Object value) {
            if (this.paymentDetails == null) {
                this.paymentDetails = new HashMap<>();
            }
            this.paymentDetails.put(key, value);
        }

        // Convert Booking object to a Map for Firestore
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("roomId", roomId);
            result.put("roomNumber", roomNumber);
            result.put("checkInDate", checkInDate);
            result.put("checkOutDate", checkOutDate);
            result.put("totalPrice", totalPrice);
            result.put("status", status);
            result.put("createdAt", createdAt);
            result.put("paymentDetails", paymentDetails);

            if (guestName != null) result.put("guestName", guestName);
            if (numberOfGuests > 0) result.put("numberOfGuests", numberOfGuests);
            if (specialRequests != null) result.put("specialRequests", specialRequests);

            return result;
        }
    }