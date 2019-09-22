package interfaces;

import java.util.List;

/**
 * Created by Davide on 28/02/2018.
 */
public interface IJenetic {
    /**
     * Initializes a random population, with a pre-determined size
     * @return The list of {@link IChromosome} that represents the new random population
     */
    List<IChromosome> initialize();

    
    /**
     * Creates a new group of offsprings, applying mutation to the parents
     * @param parents a list of parents to which apply mutation
     * @return The new list of offsprings
     */
    List<IChromosome> mutate(List<IChromosome> parents);

    /**
     * Creates a new group of offsprings, applying heredity to the parents
     * @param parents a list of parents to which apply heredity
     * @return The new list of offsprings
     */
    List<IChromosome> heredity(List<IChromosome> parents);

    /**
     * Creates a new group of random offsprings, to maintain diversity
     * @return a new group of random offsprings
     */
    List<IChromosome> random();

    /**
     * Creates a new population based on the selection of the fittest individuals of the parents list
     * @param parents a list of parents from which to select
     * @return The list of the fittest
     */
    List<IChromosome> selection(List<IChromosome> parents);

    /**
     * Determines if the genetic algorithm may stop
     * @return true if the algorithm reached the termination condition, false otherwise
     */
    boolean canStop();

    /**
     * Starts the genetic algorithm
     * @return
     */
    int run();

    /**
     * Finds the fittest individual in a given population
     * @param population the population in which to search for the fittest individual
     * @return The fittest individual
     */
    IChromosome getFittest(List<IChromosome> population);

    /**
     * Calculates the average fitness of a given population
     * @param population the population for which to find the average fitness
     * @return the average fitness of the population
     */
    double getAverageFitness(List<IChromosome> population);

    /**
     * Finds the best solution created by the Genetic Algorithm
     * @return the best solution created by the Genetic Algorithm
     */
    IChromosome getBestSolution();

    /**
     * Writes results to file (e.g. fitness of each population, average fitness, best solution in each population)
     */
    void writeResults();
}
