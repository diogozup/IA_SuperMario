package classes;

import interfaces.IChromosome;
import interfaces.IGene;
import java.awt.image.CropImageFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome implements IChromosome {

    private int score; //Indice para o modo: Level
    private int reward; //Indice para o modo: Forever
    private String reasonFinish;
    private int commandsUsed;
    private int timeLeft;
    private int chromosomeSize = 5000;
//    private int chromosomeSize = 500; << size cromossoma original

    private int buttonSize = 10;

    private List<IGene> genes; //vem por valor maximo por default de 5 espacos no array

//Contrutor vazio chromosome a instanciar array list
    public Chromosome() {
        this.genes = new ArrayList<>();
        for (int i = 0; i < chromosomeSize / buttonSize; i++) {
            int umGene = gerarRandomGene();
            for (int j = 0; j < buttonSize; j++) {
                genes.add(new Gene(umGene));
            }
        }
    }
    //Contrutor para preencher comandos de leap 5 genes

    public Chromosome(int chromosomeSize) {
        this.genes = new ArrayList<>();
        for (int i = 0; i < chromosomeSize / buttonSize; i++) {
            int umGene = gerarRandomGene();
            for (int j = 0; j < buttonSize; j++) {
                genes.add(new Gene(umGene));
            }
        }
    }

//Construtor chromossome
    public Chromosome(Chromosome other) {
        this.score = other.score;
        this.timeLeft = other.timeLeft;
        this.reward = other.reward;
        this.reasonFinish = other.reasonFinish;
        this.commandsUsed = other.commandsUsed;

        this.genes = new ArrayList<>();

        for (int i = 0; i < other.getGenes().size(); i++) {
            this.genes.add((IGene) (other.getGenes().get(i).clone()));
        }

    }

    //---------------------------------------------------- versoes de mutate ------------------
    private IChromosome mutateIfDeath(IChromosome chromosomeDeath, int mr) {

                IChromosome c = new Chromosome((Chromosome) chromosomeDeath);
        
        int deathPosition = c.getCommandsUsed();

        int newRandomValue;

        for (int i = 0; i < mr / buttonSize; i++) {
            newRandomValue = this.gerarRandomGene();
            for (int j = 0; j < buttonSize; j++) {
                c.getGenes().get(deathPosition - j).setValue(newRandomValue);
            }
            deathPosition -= buttonSize;
        }
        return c;
    }

    // metodo que acresta comandos
    public IChromosome addRandomChromosomes(IChromosome chromosome, int commandsToAdd) {
        
        IChromosome c = new Chromosome((Chromosome) chromosome);
        
        do {
            int randomValue = this.gerarRandomGene();

            //adiciona o mesmo gene 
            for (int j = 0; j < buttonSize; j++) {
                c.getGenes().add(new Gene(randomValue));
            }

        } while ((commandsToAdd -= buttonSize) > 0);

        return c;
    }

//   mutate     param numero cromossomas que recebe quantos comandos tem q mutar
    @Override
    public IChromosome mutate(int mr, int commandsToAdd) {

        IChromosome c = new Chromosome(this);

        if (this.getReasonFinish().equals("win")) {
                System.out.println("\n\n\t\t ***** WIIIIIIIIN !!!! ******");
            //codigo caso WIN -> acrescentar 50 comandos
        } else if (this.getReasonFinish().equals("death")) {
            //codigo caso morte
            return mutateIfDeath(c, mr);

          //  if (this.getTimeLeft() == 0) {
                //codigo morte falta tempo -> altera ultimos 1k commandos
            //}
        } else if (this.getReasonFinish().equals("no_more_commands")) {
            //codigo caso NO_MORE_COMMANDS -> acrescentar 50 comandos
            return addRandomChromosomes(c, commandsToAdd);
        }
        
        return null;
    }

    @Override
    public IChromosome cross(IChromosome other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IChromosome heredity() {
        return new Chromosome(this);
    }

    @Override
    public List<IGene> getGenes() {
        return this.genes;
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValid(IChromosome ch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getFitness() {
        return getReward();
    }

    // GET E SET
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getReasonFinish() {
        return reasonFinish;
    }

    public void setReasonFinish(String reasonFinish) {
        this.reasonFinish = reasonFinish;
    }

    public int gerarRandomGene() {
        return new Random().nextInt((4 - 3) + 1 + 3);
    }

    /**
     *
     * @return
     */
    public int getCommandsUsed() {
        return commandsUsed;
    }

    public void setCommandsUsed(int commandsUsed) {
        this.commandsUsed = commandsUsed;
    }

    @Override
    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

}
