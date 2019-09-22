package classes;

import interfaces.IGene;
import java.util.Random;

public class Gene implements IGene {
 private int value;
    
 @Override
    public boolean isValid() {
      
        if (value >= 0 && value <= 11) {
            return true;
        } else {
            return false;
        }
    }

    
    
    public int getValue() {
        return value;
    }

        @Override
    public void setValue(int value) {
        this.value = value;
    }

    public Gene(int value) {
        this.value = value;
    }

    @Override
    public Object clone(){
        return new Gene(this.value);
    }
    
    public Gene() {
        this.value = new Random().nextInt((4-3)+1+3);// gerar valores apenas entre 3 e 4
    }
    
}


























//  Gene                    -> 1 valor aritmetico
//  Chromossome             -> Array de Gene
//  Jenetic                 -> Array de Chromossome
//  random                  -> [3,3,2,1,5] 
//  mutate                  -> [1,2] -> [5,5]           
//  cross                   -> junta 2 ararys de genes(junta primeiros 50 do primeiro e finais 50 do segundo e viceversa)                   >> not needed for TP
//  heredit                 -> escolhe melhores arrays de chromossoma

//  fitness                 return > reward -> forever | > level -> score
