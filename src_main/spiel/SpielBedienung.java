package spiel;

import spielmanager.ExceptionManager;
import map.Map;
import map.MapHilfe;
import map.MapBedienung;
import spieler.Spieler;
import spieler.SpielerBedienung;
import turn.Turn;
import turn.TurnHilfe;
import turn.TurnBedienung;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpielBedienung {


    @Autowired
    SpielRepository spielRepository;
    
    @Autowired
    TurnBedienung turnBedienung;

    @Autowired
    SpielerBedienung spielerBedienung;

    @Autowired
    MapBedienung mapBedienung;

    private Log logger = LogFactory.getLog(ExceptionManager.class);

    public Object getAllSpiele() {
        List<Spiel> spiele = new ArrayList<>();
        spielRepository.findAll().forEach(spiele::add);
        return spiele;
    }

    public Spiel createSpiel(Spieler spieler) {
        if (spieler.getId() == null  || spielerBedienung.getOne(spieler.getId()) == null){
           spieler = spielerBedienung.addSpieler(spieler);
        }
        // init Spiel.
        Spiel spiel = new Spiel(spieler, "blue");
        spiel.setMap(new Map());
        spiel = spielRepository.save(spiel);
        return spiel;
    }

    public Spiel registerMap(Integer spiel_id, Integer spieler_id, MapHilfe mapHilfe) throws Exception {

        Spiel spiel = spielRepository.findOne(spiel_id);
        if (spiel == null){
            throw new Exception("Das Spiel, dass Sie registrieren möchten, existiert nicht!");
        }
        logger.info("Das Spiel wurde gefunden, hinzufügen der Halfmap zu " + spiel.getId());
        boolean isHalfMapA;

        String whose = null;
        whose = spiel.getSpieler1().getId() == spieler_id ? "p1" :(spiel.getSpieler2().getId() == spieler_id? "p2" : null);
        if (whose == null ) throw new Exception("Sie können keine Karte registrieren. Sie müssen sich registrieren, um sich registrieren zu können");

        Map spielMap = spiel.getMap();
        logger.info("Der Spieler ist gültig");

        spielMap.initMap((whose.equals("p1")),
                mapHilfe.getWaterCoordinates(),
                mapHilfe.getMountainCoordinates(),
                mapHilfe.getGrassCoordinates(),
                mapHilfe.getCastleCoordinates(),
                mapHilfe.getTreasureCoordinates());

        logger.info("Spielkarte wurde initialisiert");
        if (spielMap.getHalfmapA() != null && spielMap.getHalfmapB() != null) spiel.start();
        if (spielMap.getHalfmapA() != null && spielMap.getHalfmapB() != null){
            Turn initial = spiel.start();
            turnBedienung.addTurn(initial);
            spiel.getTurns().add(initial);
        }
        mapBedienung.save(spielMap);
        return spielRepository.save(spiel);
    }

    public Spiel joinSpiel(Integer spiel_id, Spieler spieler) throws Exception {

        Spiel spiel = spielRepository.findOne(spiel_id);
        if (spiel == null){
            throw new Exception("Das Spiel, dem Sie beitreten möchten, existiert nicht");
        }
        logger.info("Spiel wurde gefunden, Spiel beitreten " + spiel.getId());
        if (spieler.getId() == null || spielerBedienung.getOne(spieler.getId()) == null){
            spieler = spielerBedienung.addSpieler(spieler);
            logger.info("Neuer Spieler wurde mit dieser ID registriert: " + spieler.getId());
        }

        if (!(spiel.getSpieler2() == null) && !(spiel.getSpieler1() == null)){
            throw new Exception("Das Spiel, an dem Sie teilnehmen möchten, ist bereits voll");
        }else if(spiel.getSpieler2() == null) {
           spiel.setSpieler2(spieler);
           spiel.setSpieler2Color("red");
           spiel.isIsfull();
           spiel = spielRepository.save(spiel);
        }else {
            throw new Exception("Es gibt ein Problem mit einem speziellen Spiel. Bitte versuchen Sie es mit einem anderen");
        }

        return spiel;

    }

public Spiel registerMove(Integer spiel_id, Integer spieler_id, TurnHilfe turn) throws Exception {
    Spiel spiel = spielRepository.findOne(spiel_id);
    if (spiel == null){
        throw new Exception("Zug existiert nicht, Spiel kann nicht registrieren");
    }
    if (!spiel.isStarted()) throw new Exception("Spiel hat noch nicht begonnen");
    if (spiel.isFinished()) throw new Exception("Spiel ist fertig");

    logger.info("Registrierung von Spielzug im Spiel : " + spiel_id);


    String whose = spiel.getSpieler1().getId() == spieler_id ? "p1" :(spiel.getSpieler2().getId() == spieler_id? "p2" : null);

    if (whose == null) throw new Exception("Man kann keinen Zug registrieren, wenn man sich nicht in der Spielerliste befindet");

    Turn newTurn = spiel.addTurn(whose.equals("p1"),turn);
    turnBedienung.addTurn(newTurn);
    spielRepository.save(spiel);
    return spiel;





}
}

