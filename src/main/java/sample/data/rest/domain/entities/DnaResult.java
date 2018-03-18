package sample.data.rest.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Esteban Bett
 */
@Entity
@Table(name ="dna_results")
public class DnaResult {
    @Id
    @Column(name = "dna_dna")
    private String dna;

    @Column(name = "dna_is_mutant")
    private Boolean isMutant;

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public Boolean getIsMutant() {
        return isMutant;
    }

    public void setIsMutant(Boolean isMutant) {
        this.isMutant = isMutant;
    }

    @Override
    public String toString() {
        return "DnaResult{" +
                "dna='" + dna + '\'' +
                ", isMutant='" + isMutant + '\'' +
                '}';
    }
}
