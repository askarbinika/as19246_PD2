package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Location extends Visit {
    private Double lat;
    private Double lon;

    public Double timeTo(Location location){
        return Math.sqrt(Math.pow(this.lat - location.lat, 2) + Math.pow(this.lon - location.lon,2));
    }
    public Double distanceTo(Location location){
        return Math.sqrt(Math.pow(this.lat - location.lat, 2) + Math.pow(this.lon - location.lon,2));
    }




}
