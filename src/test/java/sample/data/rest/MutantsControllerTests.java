package sample.data.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sample.data.rest.domain.entities.Mutant;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test to run the application.
 *
 * @author Esteban Bett
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MutantsControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test()
    public void mutant_ok() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isOk());

    }

    @Test
    public void dna_empty() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(new String[]{""});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isForbidden());

    }



    @Test
    public void null_dna() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(null);
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void null_mutant() throws Exception {
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void mutant_fail() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna( new String[] {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isForbidden());

        //	.andExpect(content().string(containsString("")));
    }


    @Test
    public void stats_ok() throws Exception {
        this.mvc.perform(get("/stats"))
                .andExpect(status().isOk());
        //	.andExpect(content().string(containsString("hotels")));
    }

    @Test
    public void stats_ok2() throws Exception {
        this.mvc.perform(get("/stats"))
                .andExpect(status().isOk())
            //    .andExpect(content().string(containsString("\"count_mutant_dna\": 0")))
        .andExpect(content().string("{\"count_mutant_dna\":0,\"count_human_dna\":0,\"ratio\":\"0,00\"}"));
    }

    @Test
    public void stats_ok3() throws Exception {
        Mutant mutantFail = new Mutant();
        mutantFail.setDna( new String[] {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"});

        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutantFail)));

        Mutant mutantOk = new Mutant();
        mutantOk.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutantOk)));


        this.mvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"count_mutant_dna\":1,\"count_human_dna\":1,\"ratio\":\"1,00\"}"));
    }


}
