package org.optaplanner.openshift.employeerostering.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.optaplanner.benchmark.api.PlannerBenchmark;
import org.optaplanner.benchmark.api.PlannerBenchmarkFactory;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.openshift.employeerostering.server.roster.RosterGenerator;
import org.optaplanner.openshift.employeerostering.shared.roster.Roster;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

public class OptaShiftEmployeeRosteringBenchmarkApp {

    public static void main(String[] args) {
        List<Roster> rosterList = generateRosters();

        SolverFactory<Roster> solverFactory = SolverFactory.createFromXmlResource(
                "org/optaplanner/openshift/employeerostering/server/solver/employeeRosteringSolverConfig.xml");
        PlannerBenchmarkFactory benchmarkFactory = PlannerBenchmarkFactory.createFromSolverFactory(solverFactory);
        PlannerBenchmark plannerBenchmark = benchmarkFactory.buildPlannerBenchmark(rosterList);
        plannerBenchmark.benchmark();
    }

    private static List<Roster> generateRosters() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("optashift-employee-rostering-persistence-unit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        RosterGenerator rosterGenerator = new RosterGenerator(entityManager);

        List<Roster> rosterList = new ArrayList<>();
        rosterList.add(rosterGenerator.generateRoster(10, 7, false));
        rosterList.add(rosterGenerator.generateRoster(80, (28 * 4), false));

        entityManager.close();
        entityManagerFactory.close();
        return rosterList;
    }

}
