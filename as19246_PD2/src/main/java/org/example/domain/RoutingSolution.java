package org.example.domain;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@PlanningSolution
@Getter @Setter @NoArgsConstructor// Automatiski uzliek visam getters, setters utml
public class RoutingSolution {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingSolution.class);

    private String solutionId;
    @PlanningScore
    private HardSoftScore score;
    @PlanningEntityCollectionProperty // Te glabāsies mainīgās lietas
    private List<Vehicle> vehicleList = new ArrayList<>();
    @ProblemFactCollectionProperty // Lietas, kas nemainīsies
    @ValueRangeProvider // Šajā vietā optimizators varēs meklēt vērtības, kuras piešķirt plānošanas mainīgajam, ja tādu vietu būtu daudz, tad varētu piešķirt savus id.
    private List<Visit> visitList = new ArrayList<>();
    @ProblemFactCollectionProperty // Jo Lokācijas arī ir nemainīgas
    private List<Location> locationList = new ArrayList<>();

    public void print(){
        this.getVehicleList().forEach( vehicle -> {
            LOGGER.info(vehicle.getVehicleId() + "(" + vehicle.getStartOfWork() + "-" + vehicle.getEndOfWork() + ")");
            vehicle.getVisits().forEach(visit -> {
                Map<Visit, Double> currentTimes = vehicle.getCurrentTimes();
                Double visitTime = currentTimes.get(visit);

                LOGGER.info("       " + visit.getVisitId() + " ("
                        + visit.getTimeStart() + " - " + visit.getTimeEnd() + ") "
                        + "Visited at " + visitTime);

            });
        });
    }


    public static RoutingSolution generateData(){
    RoutingSolution problem = new RoutingSolution();
    problem.setSolutionId("P1");

    Vehicle v1 = new Vehicle();
        v1.setVehicleId("AA0000");
        v1.setStartOfWork(8.00);
        v1.setEndOfWork(15.00);
    Location depotLoc = new Location(0.0, 0.0);
        v1.setDepot(depotLoc);

    Vehicle v2 = new Vehicle();
        v2.setVehicleId("BB1111");
        v2.setStartOfWork(14.00);
        v2.setEndOfWork(23.00);
        v2.setDepot(depotLoc);

    Visit a1 = new Visit();
        a1.setVisitId("Klients1");
        a1.setTimeStart(8.00);
        a1.setTimeEnd(12.00);
    Location a1Loc = new Location(0.0, 4.0);
        a1.setLocation(a1Loc);

    Visit a2 = new Visit();
        a2.setVisitId("Klients2");
        a2.setTimeStart(10.00);
        a2.setTimeEnd(13.00);
    Location a2Loc = new Location(4.0, 4.0);
        a2.setLocation(a2Loc);

    Visit a3 = new Visit();
        a3.setVisitId("Klients3");
        a3.setTimeStart(12.00);
        a3.setTimeEnd(14.00);
    Location a3Loc = new Location(3.0, 1.0);
        a3.setLocation(a3Loc);

    Visit a4 = new Visit();
        a4.setVisitId("Noliktava1");
        a4.setTimeStart(14.00);
        a4.setTimeEnd(15.00);
    Location stockLoc = new Location(4.0, 0.0);
        a4.setLocation(stockLoc);

    Visit a5 = new Visit();
        a5.setTimeStart(15.00);
        a5.setTimeEnd(17.00);
        a5.setVisitId("Noliktava2");
        a5.setLocation(stockLoc);



        problem.getVehicleList().addAll(List.of(v1, v2));
        problem.getLocationList().addAll(List.of(depotLoc, a1Loc, a2Loc, a3Loc, stockLoc));
        problem.getVisitList().addAll(List.of(a1, a2, a3, a4, a5));

        return problem; }


}
