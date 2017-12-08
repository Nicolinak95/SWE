package test;

import spielmanager.MainManager;
import spiel.Spiel;
import spiel.SpielBedienung;
import map.MapBedienung;
import spieler.Spieler;
import spieler.SpielerBedienung;
import turn.TurnBedienung;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RunWith(SpringJUnit4ClassRunner.class)
public class NetzwerkTest {


    private MockMvc mockMvc;


    @InjectMocks
    private MainManager manager;

    @Mock
    private SpielerBedienung spielerBedienung;
    @Mock
    private SpielBedienung spielBedieung;
    @Mock
    private TurnBedienung turnBedienung;
    @Mock
    private MapBedienung mapBedienung;


    @Before
    public void setup() throws Exception{

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(manager).build();
    }


    /**
     * Für eine erfolgreiche registration des Spielers, müssen nickname und matrikelnr ausgefüllt sein
     *      * wenn 202 zurückgegeben wird, dann wird Spieler akzeptiert als json obj.
     */
    @Test
    public void registerNewPlayerSuccess() throws Exception{

        Spieler spieler = new Spieler("nickname", "12345");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/spieler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(spieler))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    /**
     * Wenn registernewplayer schreibt: Im Text sind keine gültigen Werte eingetragen
     * "nickname"
     * "matrNr"
     *
     *  Antwort sollte 418 sein mit json error Nachricht
     *
     */
    @Test
    public void registerNewPlayerFail() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.post("/spieler")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isIAmATeapot())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }


    /**
     * Get: /spielers
     * Fragt nach alles registrierten Spielern
     *
     * Antwort status : 200 und alle Spieler als json.
     * @throws Exception
     */

    @Test
    public void getAllUsersTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/spielers")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    /**
     * Get: /spiele
     * Fragt nach allen registrierten Spielen
     *
     * Antwort status : 200 und alle Spiele als json.
     * @throws Exception
     */
    @Test
    public void getAllSpiele() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/spiele").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    /**
     *  Das Spiel wird nicht starten, wenn nicht alle Spieler Infromationen im RequestBody gegeben sind.
     * @throws Exception
     */
    @Test
    public void createNewSpielFail() throws Exception {

        mockMvc.perform(
                 MockMvcRequestBuilders.post("/spiel")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{}")
        ).andExpect(MockMvcResultMatchers.status().isIAmATeapot())
                 .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

    }



    /**
     *  Das neue Spiel wird erstellt, sollten alle Spieler richtig angemeldet sein.
     *  
     *  Antwort status: 202 und responsebody als json
     *
     * @throws Exception
     */
    @Test
    public void startNewSpielSuccess() throws Exception {

        Spieler spieler = new Spieler("nickname", "12345");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(spieler))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }











    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
