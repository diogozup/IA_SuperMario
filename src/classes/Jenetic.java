package classes;

import interfaces.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import luigi.MarioUtils;
import luigi.Request;
import luigi.RunResult;
//import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class Jenetic implements IJenetic {

    private int populationSize;
    private int randomSize;
    private int mutationSize;
    private int maxIterations;
    private double deltaMin;
    private int mutationFactor;
    private int commandsToAdd;
    private int iteration;
    private List<List<IChromosome>> historic;
    private int numBestCases;
    //private int chromosomeSize;
    //versao render modoDeJogo
    private int versao;
    private String render;
    private String modoDeJogo;
    private String ip;
    MarioUtils mu;
    RunResult rr;
    private int leap = 0; 
     
     
    public Jenetic(int populationSize, int randomSize, int mutationSize, int maxIterations, double deltaMin, int mutationFactor, int commandsToAdd, int numBestCases, int versao, String render, String modoDeJogo, String ip) {
        this.populationSize = populationSize;
        this.randomSize = randomSize;
        this.mutationSize = mutationSize;
        this.maxIterations = maxIterations;
        this.deltaMin = deltaMin;
        this.mutationFactor = mutationFactor;
        this.commandsToAdd = commandsToAdd;
        this.iteration = 0;
        this.historic = new ArrayList<>(populationSize);
        this.numBestCases = numBestCases;
       // this.chromosomeSize = chromosomeSize;
        this.versao = versao;
        this.render = render;
        this.modoDeJogo = modoDeJogo;
        this.ip = ip;
    }
    
    
    
    

//    public Jenetic(int populationSize, int randomSize, int crossoverSize, int mutationSize, int maxIterations, double deltaMin, double mutationFactor) {
//        this.populationSize = populationSize;
//        this.randomSize = randomSize;
////        this.crossoverSize = crossoverSize;
//        this.mutationSize = mutationSize;
//        this.maxIterations = maxIterations;
//        this.deltaMin = deltaMin;
//        this.mutationFactor = mutationFactor;
//        this.iteration = 0;
//        this.historic = new ArrayList<>(populationSize);
//    }

//-----------------------------------------------------------------------------------------------------------------------
//    public Jenetic(int populationSize, int randomSize, int mutationSize, int maxIterations, double deltaMin, int mutationFactor, int numBestCases, int chromosomeSize, int versao, String render, String modoDeJogo) {
//        this.populationSize = populationSize;
//        this.randomSize = randomSize;
//        this.mutationSize = mutationSize;
//        this.maxIterations = maxIterations;
//        this.deltaMin = deltaMin;
//        this.mutationFactor = mutationFactor;
//        this.iteration = 0;
//        this.historic = new ArrayList<>(populationSize);
//        this.numBestCases = numBestCases;
//        this.chromosomeSize = chromosomeSize;
//        this.versao = versao;
//        this.render = render;
//        this.modoDeJogo = modoDeJogo;
//    }

    @Override
    public List<IChromosome> initialize() {
        return Stream.generate(() -> new Chromosome())
                .limit(populationSize)
                .collect(Collectors.toList());
    }

    private IChromosome getRandomChromosome(List<IChromosome> parents) {
        return parents.get(new Random().nextInt(parents.size()));
    }

    @Override
    public List<IChromosome> random() {
        return Stream.generate(() -> ((IChromosome) new Chromosome()))
                .limit(randomSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<IChromosome> mutate(List<IChromosome> parents) {
        return Stream.generate(() -> getRandomChromosome(parents).mutate(mutationFactor, commandsToAdd))
                .limit(mutationSize)
                .collect(Collectors.toList());
    }

    @Override
    public List<IChromosome> heredity(List<IChromosome> parents) {
        List<IChromosome> tempList = new ArrayList<>();

        for (int i = 0; i < numBestCases; i++) {
            IChromosome chromosomeTemp = parents.get(i).heredity();
            tempList.add(chromosomeTemp);
        }
        return tempList;
    }

    @Override
    public List<IChromosome> selection(List<IChromosome> parents) {
        return parents.stream().sorted((x, y) -> Double.compare(y.getFitness(), x.getFitness()))
                .limit(populationSize)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canStop() {
        return iteration >= maxIterations;
    }

    //---- enviarPedidoServidor
    private List<IChromosome> serverSendRequest(List<IChromosome> chromosomesToServer) {
        Request req;
        
        

        for (int i = 0; i < chromosomesToServer.size(); i++) {
            System.out.println(i);
            Integer[] chromosomeToServer = new Integer[chromosomesToServer.get(i).getGenes().size()];

            Object[] genesTest = chromosomesToServer.get(i).getGenes().toArray();

            for(int j = 0 ; j < chromosomesToServer.get(i).getGenes().size();j++){
                chromosomeToServer[j] = ((Gene) genesTest[j]).getValue();
            }
            
            
            
            req = new Request(chromosomeToServer, "SuperMarioBros-v" + versao, render, modoDeJogo);
        

        mu = new MarioUtils(ip);
       rr = mu.goMarioGo(req);
        System.out.println(rr.toString());
        chromosomesToServer.get(i).setScore(rr.getScore());
        chromosomesToServer.get(i).setReward(rr.getReward());
        chromosomesToServer.get(i).setReasonFinish(rr.getReason_finish());
        chromosomesToServer.get(i).setCommandsUsed(rr.getCommands_used());
        chromosomesToServer.get(i).setTimeLeft(rr.getTime_left());
        
        }
        return chromosomesToServer;
    }

    
    
    
    
    
    @Override
    public int run() {
        List<IChromosome> population = initialize();
        historic.add(population);

        iteration = 0;

        do {
            population = serverSendRequest(population);
            
            //aplica operadores apenas aos melhores
            List<IChromosome> best = population.stream()
                    .sorted((x, y) -> Double.compare(y.getFitness(), x.getFitness()))
                    .limit(5)
                    .collect(Collectors.toList());

            List<IChromosome> offspring = mutate(best);
            //offspring.addAll(cross(best));
            //offspring.addAll(heredity(best)); ja tem o best
            offspring.addAll(random());

            best.addAll(offspring);

            System.out.println(best.size());

            population = offspring;

            historic.add(population);

            population = best;
            iteration++;
            System.out.println("--------------------------------- Acabou esta geracao:" + iteration);
        } while (!canStop());

        System.out.println("------------- ENVIOU LEADERBOARD ----------------");
        mu.submitToLeaderboard(rr, "PandaPanda", "forever");
        return iteration;
    }

    public void writeResults() {
        try {
            ArrayList<String> header = new ArrayList<>();
            header.add("Fitness");
            ArrayList<String> header2 = new ArrayList<>();
            header2.add("X");
            Files.deleteIfExists(Paths.get("fitnesses"));
            Files.deleteIfExists(Paths.get("avgFitnesses"));
            Files.deleteIfExists(Paths.get("coordinates"));
            Files.write(Paths.get("fitnesses"), header, StandardOpenOption.CREATE);
            Files.write(Paths.get("avgFitnesses"), header, StandardOpenOption.CREATE);
            Files.write(Paths.get("coordinates"), header2, StandardOpenOption.CREATE);
            Files.write(Paths.get("fitnesses"), historic.stream().map(x -> getFittest(x)).map(x -> x.getFitness() + "").collect(Collectors.toList()), StandardOpenOption.APPEND);
            Files.write(Paths.get("avgFitnesses"), historic.stream().map(x -> getAverageFitness(x)).map(x -> x.toString()).collect(Collectors.toList()), StandardOpenOption.APPEND);
            Files.write(Paths.get("coordinates"), historic.stream().map(x -> getFittest(x)).map(x -> x.getGenes().get(0).toString()).collect(Collectors.toList()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IChromosome getFittest(List<IChromosome> population) {
        return population.stream().sorted((x, y) -> Double.compare(y.getFitness(), x.getFitness())).findFirst().get();
    }

    @Override
    public double getAverageFitness(List<IChromosome> population) {
        return population.stream().mapToDouble(IChromosome::getFitness).average().getAsDouble();
    }

    @Override
    public IChromosome getBestSolution() {
        return historic.stream().map(x -> getFittest(x)).reduce((x, y) -> x.getFitness() > y.getFitness() ? x : y).get();
    }



    

//--------------------------------------------------------- ESCREVER FILE(docTxt) ---------------------------------------------------------
    public static void Mario_Save_To_DocTexto(Integer[] solution, String file_path) throws IOException {
        File file = new File(file_path);
        FileWriter fw = new FileWriter(file);
        String string = "";
        try {
            for (Integer integer : solution) {
                string = string.concat(integer + " ");
            }
            fw.write(string + " ");
            fw.flush();
            fw.close();
            System.out.println("Sucess_Save_Texto");
        } catch (IOException e) {
            System.err.println("Erro_Save_Texto!");
        }
    }

    
    //--------------------------------------------------------- LER FILE ---------------------------------------------------------
    public static Integer[] Mario_Load_From_DocTexto(String path_file) throws IOException {
        File file = new File(path_file);
        FileReader fr = new FileReader(file);
        Integer[] sol = null;
        int tamanho = 0;
        int num;
        try {
            int i;
            int j = 0;
            while ((i = fr.read()) != -1) {
                num = Character.getNumericValue((char) i);
                if (num >= 0 && num < 10) {
                    tamanho++;
                }
            }
            fr = new FileReader(file);
            sol = new Integer[tamanho];
            while ((i = fr.read()) != -1) {
                num = Character.getNumericValue((char) i);
                if (num >= 0 && num < 10) {
                    sol[j] = Character.getNumericValue((char) i);
                    j++;
                }
            }
            System.out.println("Sucess_Read_Texto");
        } catch (IOException e) {
            System.err.println("Erro_Read_Texto!");
        }
        return sol;
    }
    
    










































}//END Jenetic









































