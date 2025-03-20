    package com.example.roomify.models;

    public class ActivityItem {
        public static final int TYPE_BOOKING = 0;
        public static final int TYPE_PAYMENT = 1;
        public static final int TYPE_USER = 2;
        public static final int TYPE_ROOM = 3;

        private String title;
        private String subtitle;
        private int type;

        public ActivityItem(String title, String subtitle, int type) {
            this.title = title;
            this.subtitle = subtitle;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public int getType() {
            return type;
        }
    }