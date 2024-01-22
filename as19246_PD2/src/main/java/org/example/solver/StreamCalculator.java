package org.example.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import org.example.domain.Vehicle;
import org.example.domain.Visit;

public class StreamCalculator implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                isWorkHoursConstraintBroken(constraintFactory),
                isDeliveryTimeConstraintBroken(constraintFactory),
                totalTime(constraintFactory)
        };
    }
    public Constraint everyVisit(ConstraintFactory constraintFactory){
        return constraintFactory
                .forEach(Visit.class)
                .penalize(HardSoftScore.ONE_SOFT, visit -> 1)
                .asConstraint("everyVisit");
    }
    public Constraint totalTime(ConstraintFactory constraintFactory){
        return constraintFactory //Ko mums vajag, lai izrēķinātu totalDistance
                .forEach(Vehicle.class) // atgriež plūsmu ar vehicle objektiem
                .filter(vehicle -> vehicle.getTotaltime()>0)
                .penalize(HardSoftScore.ONE_SOFT, vehicle -> (int) Math.round(vehicle.getTotaltime())) // Hard Soft Score vajag Int, tādēļ pareizinam mūsu decimālskaitli ar 1000 un noapaļojam , pārvēršam par veselu skaitli.
                .asConstraint("totalTime");

    }
    public Constraint isWorkHoursConstraintBroken(ConstraintFactory constraintFactory){
        return constraintFactory
                .forEach(Vehicle.class)
                .filter(Vehicle::isWorkHoursConstraintBroken) //Izfiltrē visus tos, kuri ir pārkāpti
                .penalize(HardSoftScore.ofHard(1000000)) // Uzliek Hard
                .asConstraint("isWorkHoursConstraintBroken");

    }
    public Constraint isDeliveryTimeConstraintBroken(ConstraintFactory constraintFactory){
        return constraintFactory
                .forEach(Vehicle.class) // atgriež plūsmu ar vehicle objektiem
                .filter(Vehicle::isDeliveryTimeConstraintBroken) //Izfiltrē visus tos, kuri ir pārkāpti
                .penalize(HardSoftScore.ofHard(500000)) // Uzliek Hard
                .asConstraint("isDeliveryTimeConstraintBroken");

    }


}
