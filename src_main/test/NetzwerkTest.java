package test;

import spielmanager.MainManager;
import spiel.Spiel;
import spiel.SpielBedienung;
import map.MapHilfe;
import map.MapBedienung;
import spieler.Spieler;
import spieler.SpielerBedienung;
import turn.TurnBedienung;
import turn.TurnHilfe;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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


    @Test
    public void getSpieler() throws Exception {
         Spieler spieler = new Spieler("nickname", "12345");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/spieler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(spieler))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/spielers/1")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(MockMvcResultHandlers.print());
    }
   
    /**
     *
     * Die Erstellung eines neuen Spiels sollte erfolgreich sein, wenn der Benutzer gültig ist.
     * und ein anderer Benutzer sollte in der Lage sein, dem Spiel beizutreten, indem er Benutzerinformationen eingibt.
     *
     *
     * @throws Exception
     */
    @Test
    public void joinSpielSuccess() throws Exception {

        Spieler spieler1 = new Spieler("nickname1", "12345");
        Spieler spieler2 = new Spieler("nickname2", "12345");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(spieler1))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel/1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(spieler2))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

    }
    /**
     *
     * Der Spieler sollte in der Lage sein, die Karte einem erstellten Spiel zu übermitteln
     *
     * @throws Exception
     */
    @Test
    public void submitMap() throws Exception {

        Spieler spieler1 = new Spieler("nickname1", "12345");
        MapHilfe mapHilfe = new MapHilfe();
        mapHilfe.setCastleCoordinates("d3");
        mapHilfe.setTreasureCoordinates("b1");
        String[] waters = { "a2", "a7", "c5", "c4"};
        mapHilfe.setWaterCoordinates(waters);
        String[] mountains = { "d7", "b2", "b3" };
        String[] grass = { "a1", "d3", "d2", "b1", "c1", "d1", "c2", "a3", "c3", "a4", "b4", "d4", "a5", "b5", "d5", "a6", "b6", "c6", "d6", "b7", "c7", "a8", "b8", "c8", "d8" };
        mapHilfe.setGrassCoordinates(grass);
        mapHilfe.setMountainCoordinates(mountains);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(spieler1))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel/1/spieler/1/halfmap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mapHilfe))
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }


     /**
     *
     * Der Spieler sollte einen Zug einreichen können
     *
     * @throws Exception
     */
    @Test
    public void submitMove() throws Exception {

        TurnHilfe turnHilfe = new TurnHilfe();
        turnHilfe.setCurrentPosition("h3");
        turnHilfe.setInventory(false);
        turnHilfe.setNewPosition("h4");
        turnHilfe.setTurnNumber(1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/spiel/1/spieler/1/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(turnHilfe))
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
