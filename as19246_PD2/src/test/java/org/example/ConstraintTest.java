package org.example;

import ai.timefold.solver.test.api.score.stream.ConstraintVerifier;
import org.example.domain.Location;
import org.example.domain.RoutingSolution;
import org.example.domain.Vehicle;
import org.example.domain.Visit;
import org.example.solver.StreamCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ConstraintTest {
    Vehicle vehicle = new Vehicle();
    Visit visit1 = new Visit();
    Visit visit2 = new Visit();
    Location depot = new Location(0.0,0.0);
    Location a1Loc = new Location(4.0,0.0);
    Location a2Loc = new Location(4.0, 4.0);

    public ConstraintTest(){
        visit1.setVehicle(vehicle);
        visit1.setLocation(a1Loc);
        visit1.setTimeStart(9.0);
        visit1.setTimeEnd(10.0);
        visit2.setVehicle(vehicle);
        visit2.setLocation(a2Loc);
        visit2.setTimeStart(12.0);
        visit2.setTimeEnd(15.0);
        visit1.setNext(visit2);
        visit2.setPrev(visit1);
        vehicle.getVisits().addAll(List.of(visit1, visit2));
        vehicle.setDepot(depot);
        vehicle.setStartOfWork(8.0);
        vehicle.setEndOfWork(22.0);
    }
    ConstraintVerifier<StreamCalculator, RoutingSolution> constraintVerifier= ConstraintVerifier.build(
            new StreamCalculator(), RoutingSolution.class, Vehicle.class, Visit.class
    );
    @Test
    void workHoursTest1(){
        constraintVerifier.verifyThat(StreamCalculator::isWorkHoursConstraintBroken)
                .given(vehicle, visit1)
                .penalizes(0);
    }
    @Test
    void deliveryTimeTest1(){
        constraintVerifier.verifyThat(StreamCalculator::isDeliveryTimeConstraintBroken)
                .given(vehicle, visit1)
                .penalizes(0);
    }
}
