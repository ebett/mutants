package sample.data.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.data.rest.domain.MutantService;
import sample.data.rest.domain.entities.Mutant;
import sample.data.rest.domain.entities.Statistic;

/**
 * @author Esteban Bett
 */
@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET,
                // RequestMethod.HEAD,
                RequestMethod.POST,
                //  RequestMethod.PUT,
                //  RequestMethod.DELETE,
                RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"})
@RestController
public class MutantsController {

    private final MutantService mutantService;

    @Autowired
    public MutantsController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @RequestMapping("/mutant")
    @PostMapping
    public ResponseEntity isMutant(@RequestBody Mutant mutant) {
        return mutantService.isMutant(mutant.getKey()) ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @RequestMapping("/stats")
    @GetMapping
    public ResponseEntity<Statistic> stats() {
        return new ResponseEntity<>(this.mutantService.getStatistic(), HttpStatus.OK );
    }
}
