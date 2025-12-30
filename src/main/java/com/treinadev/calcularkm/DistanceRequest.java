package com.treinadev.calcularkm;

import lombok.Data;

@Data
public class DistanceRequest {
    public double userLat;
    public double userLng;
    public double targetLat;
    public double targetLng;
    
}
