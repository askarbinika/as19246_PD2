package org.example;

import ai.timefold.solver.core.api.score.ScoreExplanation;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.example.domain.RoutingSolution;
import org.example.domain.Vehicle;
import org.example.domain.Visit;
import org.example.solver.StreamCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello world!");
        LOGGER.info("Hello world from logger"); // Stāsta mazāk
        LOGGER.debug("Hello world from logger 2"); // Stāsta vairāk
        RoutingSolution problem = RoutingSolution.generateData();
        // LOGGER.info(problem.toString()); //Pirmais mēģinājums izdrukāt
        problem.print();

        SolverFactory<RoutingSolution> solverFactory = SolverFactory.create(
                new SolverConfig()
                        .withSolutionClass(RoutingSolution.class) // Pasakām, kura no mūsu izveidotajām klasēm satur solution
                        .withEntityClasses(Vehicle.class, Visit.class)
                        .withConstraintProviderClass(StreamCalculator.class)
                        .withTerminationConfig(new TerminationConfig() .withSecondsSpentLimit(10L)) // Nokonfigurējam, kad apstāties solverim
        );

        Solver<RoutingSolution> solver = solverFactory.buildSolver();
        RoutingSolution solution = solver.solve(problem);

        SolutionManager<RoutingSolution, HardSoftScore> solutionManager = SolutionManager.create(solverFactory);
        ScoreExplanation<RoutingSolution, HardSoftScore> scoreExplanation = solutionManager.explain(solution);
        LOGGER.info(scoreExplanation.getSummary());

        solution.print();



    }

}