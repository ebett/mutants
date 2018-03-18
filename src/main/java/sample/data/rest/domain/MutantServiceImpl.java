package sample.data.rest.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import sample.data.rest.domain.entities.DnaResult;
import sample.data.rest.domain.entities.Statistic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Esteban Bett
 */
@Service("mutantService")
public class MutantServiceImpl implements MutantService {

    private final Logger logger = LoggerFactory.getLogger(MutantServiceImpl.class);

    private final DnaResultRepository dnaResultRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String queryStatistics = "select (select count(*) from dna_results d where d.dna_is_mutant=true) " +
            "as count_mutant_dna, (select count(*) from dna_results d where d.dna_is_mutant=false) as count_human_dna";

    private int ocurrences = 0;

    @Autowired
    public MutantServiceImpl(DnaResultRepository dnaResultRepository) {
        this.dnaResultRepository = dnaResultRepository;
        logger.info("Mutant service created.");
    }

    @Cacheable(value = "isMutant", key = "#dnaKey")
    @Override
    public boolean isMutant(final String dnaKey) {
        logger.info("check isMutant start");

        if (dnaKey == null) {
            return false;
        }

        final String[] dna = dnaKey.split("@");

        if (!validateInput(dnaKey, dna)) return false;

        logger.debug("DNA Key: {}", dnaKey);
        final int n = dna.length;
        ocurrences = 0;
        boolean match = false;

        logger.info("Verifying rows ...");
        for (int i = 0; i < n && !match; i++) {
            logger.debug("row: {}", dna[i]);
            if (verifyLine(dna[i])) {
                match = true;
            }
        }

        if(match) {
            saveRecord(dnaKey, match);
            return match;
        }

        char[] col = new char[n]; //stores column characters
        char[][] M = new char[n][n]; //stores 2d matrix

        logger.info("Verifying columns ...");
        for (int i = 0; i < n && !match; i++) {
            for (int j = 0; j < n; j++) {
                col[j] = dna[j].charAt(i);
                M[i][j] = col[j];
            }

            final String column = new String(col);
            logger.debug("column: {}", column);

            if (verifyLine(column)) {
                match = true;
            }
        }

        if(match) {
            saveRecord(dnaKey, match);
            return match;
        }

        logger.info("Verifying diagonals NE to SW ...");
        match = printDiagonalsNEtoSW(M);

        if(match) {
            saveRecord(dnaKey, match);
            return match;
        }

        logger.info("Verifying diagonals NW to SE ...");
        match = printDiagonalsNWtoSE(M);

        saveRecord(dnaKey, match);

        return match;
    }

    private final void saveRecord(final String dnaKey, boolean isMutant) {
        DnaResult dnaResult = new DnaResult();
        dnaResult.setDna(dnaKey);
        dnaResult.setIsMutant(isMutant);
        dnaResultRepository.save(dnaResult);
    }

    /**
     * Validate input
     *
     * @param dnaKey
     * @param dna
     * @return
     */
    private boolean validateInput(final String dnaKey, final String[] dna) {
        if (dna == null || dna.length == 0) {
            logger.warn("Invalid argument dnaKey: {}", dnaKey);
            return false;
        }
        Integer rowCount = null;
        for (final String row : dna) {
            if (row.length() < 4) {
                logger.warn("Invalid argument dnaKey. Invalid row: {}", row);
                return false;
            }
            if (rowCount == null) {
                rowCount = row.length();
            } else if (rowCount != row.length()) {
                logger.warn("Invalid argument dnaKey. DNA rows have different length");
                return false;
            }
        }
        return true;
    }


    /**
     * Gets DNA counters
     * @return statistics
     */
    @Override
    public Statistic getStatistic() {
        final Statistic statistic = jdbcTemplate.queryForObject(queryStatistics, new RowMapper<Statistic>() {
            @Override
            public Statistic mapRow(ResultSet resultSet, int i) throws SQLException {
                return new Statistic(resultSet.getLong(1), resultSet.getLong(2));
            }
        });

        logger.debug("{}", statistic);

        return statistic;
    }


    /**
     * Checks if the string s have more than one match
     * @param s string line
     * @return true or false
     */
    private boolean verifyLine(String s) {
        if (s.indexOf("AAAA") >= 0) {
            if (++ocurrences > 1) {
                return true;
            }
        }
        if (s.indexOf("GGGG") >= 0) {
            if (++ocurrences > 1) {
                return true;
            }
        }
        if (s.indexOf("TTTT") >= 0) {
            if (++ocurrences > 1) {
                return true;
            }
        }
        if (s.indexOf("CCCC") >= 0) {
            if (++ocurrences > 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * Verifies in all diagonals NE to SW
     * @param m matrix of characters
     * @return true or false
     */
    private boolean printDiagonalsNEtoSW(char[][] m) {
        int xStart = 0;
        int yStart = 1;

        while (true) {
            int xLoop, yLoop;
            if (xStart < m.length) {
                xLoop = xStart;
                yLoop = 0;
                xStart++;
            } else if (yStart < m[0].length) {
                xLoop = m.length - 1;
                yLoop = yStart;
                yStart++;
            } else
                break;

            int dsize = m[0].length - yLoop;
            if(xLoop > 2 && dsize > 3){
                char[] diagonal = new char[dsize];
                for (int c=0; xLoop >= 0 && yLoop < m[0].length; xLoop--, yLoop++) {
                    diagonal[c++] = m[xLoop][yLoop];
                }
                final String sdiagonal = new String(diagonal);
                logger.debug("diagonal: {}", sdiagonal);

                if (verifyLine(sdiagonal)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Verifies in all diagonals NW to SE
     * @param m matrix of characters
     * @return true or false
     */
    private boolean printDiagonalsNWtoSE(char[][] m) {

        int xStart = m.length - 1;
        int yStart = 1;

        while (true) {
            int xLoop, yLoop;
            if (xStart >= 0) {
                xLoop = xStart;
                yLoop = 0;
                xStart--;
            } else if (yStart < m[0].length) {
                xLoop = 0;
                yLoop = yStart;
                yStart++;
            } else
                break;

            int dsize = m[0].length - yLoop;
         //   if(xLoop > 3 && dsize > 3) {
                char[] diagonal = new char[dsize];
                for (int c = 0; xLoop < m.length && yLoop < m[0].length; xLoop++, yLoop++) {
                    diagonal[c++] = m[xLoop][yLoop];
                }
                final String sdiagonal = new String(diagonal);
                logger.debug("diagonal: {}", sdiagonal);

                if (verifyLine(sdiagonal)) {
                    return true;
                }
         //   }
        }
        return false;
    }
}
