package org.example.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import ai.timefold.solver.core.api.domain.variable.NextElementShadowVariable;
import ai.timefold.solver.core.api.domain.variable.PreviousElementShadowVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@PlanningEntity
@Getter @Setter @NoArgsConstructor
public class Visit {
    private String visitId;
    @Setter
    @Getter
    private Double timeStart; // Klienta vēlamais laiks "no"
    private Double timeEnd; // Klienta vēlamā laiks "līdz"
    private Location location;

    @InverseRelationShadowVariable(sourceVariableName = "visits")
    private Vehicle vehicle;
    @NextElementShadowVariable(sourceVariableName = "visits")
    private Visit next;
    @PreviousElementShadowVariable(sourceVariableName = "visits")
    private Visit prev;

    private Integer volume;
    public String toString() {
        return this.getVisitId();
    }


}
