package com.example.hospitalmanagement;

public class AssistantNotification {
        private String recipientUid;
        private String message;
        private long timestamp;

        public AssistantNotification() {
            // Default constructor required for Firestore
        }

        public AssistantNotification(String recipientUid, String message) {
            this.recipientUid = recipientUid;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getRecipientUid() {
            return recipientUid;
        }

        public void setRecipientUid(String recipientUid) {
            this.recipientUid = recipientUid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

