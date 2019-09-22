package demo;

import classes.Gene;
import classes.Jenetic;
import static classes.Jenetic.Mario_Save_To_DocTexto;
import interfaces.IChromosome;
import interfaces.IJenetic;
import java.io.IOException;
import luigi.MarioUtils;
import luigi.Request;
import luigi.RunResult;



public class IA {

    public static void main(String[] args) throws IOException {

        //------------------------------------------------------ LER FICHEIRO -----------------------------------------------------
        Integer[] solution = Jenetic.Mario_Load_From_DocTexto("./files/solution100.txt");

        int maxIterations = 50;
        
        IJenetic jen = new Jenetic(10, 4, 4, maxIterations, 0.000000, 40, 200, 2, 0, "true", "forever", "192.168.1.5");
        int iterations = jen.run();         //""""

        jen.writeResults();

        System.out.println("Stopped after" + iterations + "iterations");

        IChromosome best = jen.getBestSolution();
        System.out.println("Best solution" + best + " ,fitness: " + best.getFitness());

        Integer[] boa = new Integer[best.getGenes().size()];

        for (int i = 0; i < best.getGenes().size(); i++) {
            boa[i] = ((Gene) best.getGenes().get(i)).getValue();
        }

        Mario_Save_To_DocTexto(boa, "./files/solution100.txt");

    
    }// END main

}// END Classe IA
