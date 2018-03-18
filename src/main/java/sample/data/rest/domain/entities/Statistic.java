package sample.data.rest.domain.entities;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Esteban Bett
 */
public class Statistic implements Serializable {
    private Long count_mutant_dna;

    private Long count_human_dna;


    public Statistic() {
    }

    public Statistic(Long count_mutant_dna, Long count_human_dna) {
        this.count_mutant_dna = count_mutant_dna;
        this.count_human_dna = count_human_dna;
    }

    public Long getCount_mutant_dna() {
        return count_mutant_dna;
    }

    public void setCount_mutant_dna(Long count_mutant_dna) {
        this.count_mutant_dna = count_mutant_dna;
    }

    public Long getCount_human_dna() {
        return count_human_dna;
    }

    public void setCount_human_dna(Long count_human_dna) {
        this.count_human_dna = count_human_dna;
    }

    public String getRatio() {
        Double number =  Double.valueOf(count_mutant_dna) / Float.valueOf((count_human_dna == 0? 1: count_human_dna));

        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.DOWN); // this is the key step
        return format.format(number);
    }


    @Override
    public String toString() {
        return "Statistic{" +
                "count_mutant_dna=" + count_mutant_dna +
                ", count_human_dna=" + count_human_dna +
                ", ratio=" + getRatio() +
                '}';
    }
}
