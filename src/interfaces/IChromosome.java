package interfaces;

import java.util.List;


public interface IChromosome
{
    /**
     * Applies the mutation operator to this {@link IChromosome}
     * @param mr the mutation rate (indicates how much the chromosome can mutate)
     * @return a new chromosome that results from the mutation of this chromosome
     */
    IChromosome mutate(int mr, int commandsToAdd);
    /**
     * Applies the crossover operator to this {@link IChromosome}
     * @param other the other chromosome used in the crossover operator
     * @return a new chromosome that results from the crossover of the two chromosomes
     */
    IChromosome cross(IChromosome other);
    /**
     * Generates a new chromosome that is a copy of this.
     * @return a new chromosome, that is a copy of this
     */
    IChromosome heredity();
    /**
     * Returns the list of genes that compose this chromosome
     * @return the list of genes
     */
    List<IGene> getGenes();
    /**
     * Determines if this chromosome is a valid solution within the solution space
     * @return true if the chromosome is a valid solution within the solution space
     */
    boolean isValid();
    /**
     * Determines if a given chromosome is a valid solution within the solution space
     * @param ch the chromosome to validate
     * @return true if a given chromosome is a valid solution within the solution space
     */
    boolean isValid(IChromosome ch);
    /**
     * Calculates the fitness of this chromosome 
     * @return the fitness of this chromosome
     */
    double getFitness();

    public void setScore(int score);

    public void setReward(int reward);

    public void setReasonFinish(String reason_finish);

    public void setCommandsUsed(int commandsUsed);

    public void setTimeLeft(int timeLeft);

    public int getTimeLeft();

    public int getCommandsUsed();



}
