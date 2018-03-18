package sample.data.rest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.data.rest.domain.entities.DnaResult;

/**
 * @author Esteban Bett
 */
public interface DnaResultRepository extends JpaRepository<DnaResult, String> {


}
