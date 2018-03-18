package sample.data.rest.domain;

import sample.data.rest.domain.entities.Statistic;

/**
 * @author Esteban Bett
 */
public interface MutantService {

    boolean isMutant(String dnaKey);

    Statistic getStatistic();
}
