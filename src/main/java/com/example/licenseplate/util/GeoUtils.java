package com.example.licenseplate.util;

public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0; // Bán kính Trái Đất (km)

    /**
     * Tính khoảng cách giữa 2 điểm tọa độ (lat, lon) theo km.
     * @param lat1 vĩ độ điểm 1
     * @param lon1 kinh độ điểm 1
     * @param lat2 vĩ độ điểm 2
     * @param lon2 kinh độ điểm 2
     * @return khoảng cách tính bằng km
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Chuyển độ sang radian
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        // Công thức Haversine
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(latRad1) * Math.cos(latRad2) *
                   Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

  
}
