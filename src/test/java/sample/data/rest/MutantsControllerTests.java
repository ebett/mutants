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
// Separate profile for web tests to avoid clashing databases
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
    public void testMutant1() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isOk());

    }

    @Test
    public void testMutant2() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(new String[]{""});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isForbidden());

    }


    @Test
    public void testMutant3() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna(new String[]{""});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isForbidden());

        //	.andExpect(content().string(containsString("hotels")));
    }

    @Test
    public void testMutant4() throws Exception {
        Mutant mutant = new Mutant();
        mutant.setDna( new String[] {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"});
        this.mvc.perform(post("/mutant").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutant)))
                .andExpect(status().isForbidden());

        //	.andExpect(content().string(containsString("hotels")));
    }


    @Test
    public void testStats1() throws Exception {
        this.mvc.perform(get("/stats"))
                .andExpect(status().isOk());
        //	.andExpect(content().string(containsString("hotels")));
    }

    @Test
    public void testStats2() throws Exception {
        this.mvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("count_mutant_dna")));
    }


}
