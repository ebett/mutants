package sample.data.rest.domain.entities;

/**
 * @author Esteban Bett
 */
public class Mutant {

    private String[] dna;

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

    public String getKey(){
        if (dna == null){
            return null;
        }
        StringBuilder dnaKey = new StringBuilder();
        for(int i= 0; i<dna.length; i++){
            dnaKey.append(dna[i]);
            if(i+1 < dna.length) dnaKey.append('@');
        }
        return dnaKey.toString();
    }
}
