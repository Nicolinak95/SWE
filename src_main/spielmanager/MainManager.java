package spielmanager;


import spiel.Spiel;
import spiel.SpielBedienung;
import map.MapHilfe;
import spieler.Spieler;
import spieler.SpielerBedienung;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class MainManager {


    @Autowired
    SpielerBedienung spielerBedienung;

    @Autowired
    SpielBedienung spielBedienung;

    private Log logger = LogFactory.getLog(ExceptionManager.class);


    /**
     *  TODO: /spieler post: registerPlayerRequest: return player && created.
     *  TODO: /game post: send halfmap request : response true | false
     *  TODO: /game/player/[id] get:  PlayerStateRequest
     *  TODO: /game/player/[id] post: send halfMap Request
     *  TODO: /game/player/1/move post: player Move Request
     *  TODO: /game
     *
     */


    /**
     * POST: /spieler
     * adds new user specified in req. body
     * @param player
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/spieler", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addNewSpieler(@Valid @RequestBody Spieler spieler ) throws Exception {
        logger.info("Spieleranfrage hinzugefügt");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ResponseEntity<Spieler> response = new ResponseEntity<Spieler>(spielerBedienung.addSpieler(spieler),headers, HttpStatus.ACCEPTED);
        return response;
    }

    /**
     * GET: /players
     * @return all Players as json
     */
    @GetMapping(value="/spielers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllSpielers(){
        logger.info("getAllSpielers request erhalten");
        return new ResponseEntity<>(
                spielerBedienung.getAllSpielers(),

                HttpStatus.OK);
    }


    /**
     * GET: /games
     * @return all games as json
     */
    @GetMapping(value="/spiele", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllSpiele(){
        logger.info("getAllSpiele request erhalten");
        return new ResponseEntity<>(spielBedienung.getAllSpiele(), HttpStatus.OK);
    }

    @PostMapping(value="/spiel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createSpiel(@Valid @RequestBody Spieler spieler) throws Exception{
        logger.info("createSpiel request von " + spieler.getNickname());
        Spiel spiel = spielBedienung.createSpiel(spieler);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ResponseEntity<Spiel> response = new ResponseEntity<Spiel>(spiel,headers, HttpStatus.ACCEPTED);
        return response;
    }

    @PostMapping(value="/spiel/{spiel_id}/spieler/{spieler_id}/halfmap")
    public ResponseEntity registerHalfMap(@RequestBody MapHilfe mapHilfe,
                                          @PathVariable final Integer spiel_id,
                                          @PathVariable final Integer spieler_id) throws Exception{
       logger.info("Die Half Map von Spieler : " + spieler_id + " von Spiel : " + spiel_id );

       Spiel spiel = spielBedienung.registerMap(spiel_id, spieler_id, mapHilfe );
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
       ResponseEntity<Spiel> response = new ResponseEntity<Spiel>(spiel,headers, HttpStatus.ACCEPTED);
       return response;
    }

    @PostMapping(value="/spiel/{spiel_id}/join", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity joinSpiel(@Valid @RequestBody Spieler spieler, @PathVariable final Integer spiel_id) throws Exception {
        logger.info("Spielanforderung beitreten" + spieler.getNickname());
        Spiel spiel = spielBedienung.joinSpiel(spiel_id, spieler);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ResponseEntity<Spiel> response = new ResponseEntity<Spiel>(spiel, headers, HttpStatus.ACCEPTED);
        return response;
    }




    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleException(HttpServletRequest req, Exception e) {
        logger.error("Request : " + req.getRequestURL() + " raised " + e);
        String[] pc = {"Email darf nicht leer sein", "Vorname darf nicht leer sein", "Nachname darf nicht leer sein"};
        ErrorMessage exceptionResponse = new ErrorMessage("Ungültige parameter", "Body hat alle 3: Vorname, Nachname, Email Eingabenwerte.", pc);
        return new ResponseEntity<ErrorMessage>(exceptionResponse, new HttpHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }



}
