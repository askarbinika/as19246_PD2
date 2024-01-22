package org.example.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {
    private String vehicleId;
    private Double startOfWork;
    private Double endOfWork;

    @PlanningListVariable // Mēs vēlamies katrai mašīnai piekārtot mainīgo sarakstu
    private List<Visit> visits = new ArrayList<>();

    private Location depot;

    public Double getTotaltime() { //Aprēķinām kopējo patērēto laiku
        Double totalTime = 0.0;
        Location prevLoc = this.getDepot();
        for (Visit visit : this.getVisits()) {
            totalTime = totalTime +
                    prevLoc.timeTo(visit.getLocation());
            prevLoc = visit.getLocation();
        }
        totalTime = totalTime + prevLoc.timeTo(this.getDepot());
        return totalTime;
    }

    public Map<Visit, Double> getCurrentTimes() {
        Map<Visit, Double> currentTimes = new HashMap<>();
        Location currentTimeLocation = this.getDepot(); // Mašīna uzsāk maršrutu no depo
        Double currentTime = this.getStartOfWork(); // Laika atskaiti sāk no darba laika sākuma

        for (Visit visit : this.getVisits()) { // Iet cauri visām piekārtotajām vizītēm
            Double travelTime = currentTimeLocation.timeTo(visit.getLocation()); // Nosaka laiku, kas nepieciešams ceļā uz vizīti
            currentTime += travelTime; // Pieskaita pašreizējam laikam

            Double visitTime = currentTime;
            currentTimes.put(visit, visitTime);

            // pāriet uz nākamo lokāciju
            currentTimeLocation = visit.getLocation();
            currentTime += visit.getLocation().timeTo(currentTimeLocation);
        }

        // Maršruta beigas - atgriežas depo
        currentTime += currentTimeLocation.timeTo(this.getDepot());
        currentTimes.put(this.getDepot(), currentTime);

        return currentTimes;
    }


    public Boolean isWorkHoursConstraintBroken() {
        Map<Visit, Double> currentTimes = getCurrentTimes();

        for (Map.Entry<Visit, Double> entry : currentTimes.entrySet()) {
            Visit visit = entry.getKey();
            Double visitTime = entry.getValue();

            // Pārbauda, vai vizīte nav ārpus mašīnas darba laika
            if (!(visitTime >= this.getStartOfWork() && visitTime <= this.getEndOfWork())) {
                return true;
            }
        }
        return false;
    }
    public Boolean isDeliveryTimeConstraintBroken() {
        Map<Visit, Double> currentTimes = getCurrentTimes();

        for (Map.Entry<Visit, Double> entry : currentTimes.entrySet()) {
            Visit visit = entry.getKey();
            Double visitTime = entry.getValue();

            System.out.println("Visit ID: " + visit.getVisitId() + ", TimeStart: " + visit.getTimeStart() + ", TimeEnd: " + visit.getTimeEnd());

            //Pagaidām.. Noignorējam null vizītes
            if (visit.getTimeStart() == null || visit.getTimeEnd() == null) {
                continue;
            }

            System.out.println("Visit ID: " + visit.getVisitId() + ", TimeStart: " + visit.getTimeStart() + ", TimeEnd: " + visit.getTimeEnd());

            // Pārbauda, vai vizīte nav ārpus klienta rezervācijas laika
            if (!(visitTime >= visit.getTimeStart() && visitTime <= visit.getTimeEnd())) {
                return true;
            }
        }
        return false;
    }




    public Double getTotaldistance() {
        Double totalDistance = 0.0;
        Location prevLoc = this.getDepot();
        for (Visit visit : this.getVisits()) {
            totalDistance = totalDistance +
                    prevLoc.distanceTo(visit.getLocation());
            prevLoc = visit.getLocation();
        }
        totalDistance = totalDistance + prevLoc.distanceTo(this.getDepot());
        return totalDistance;
    }

    public String toString() {
        return this.getVehicleId();
    }
}



