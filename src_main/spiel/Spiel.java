package spiel;

import map.Map;
import spieler.Spieler;
import turn.Turn;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="spiel")
public class Spiel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    private String time;


    private String SpielErgebnis;
    private String Gewinner;
    private Integer maxNumberOfRounds;
    private Integer currentRound;


    private boolean isStarted;
    private boolean isFinished;



    @OneToMany(mappedBy = "spiel")
    private List<Turn> turns;

    @ManyToOne
    @JoinColumn(name = "spieler_1")
    private Spieler spieler1;
    private String spieler1Color;

    @ManyToOne
    @JoinColumn(name = "spieler_2")
    private Spieler spieler2;
    private String spieler2Color;

    @OneToOne(cascade = CascadeType.ALL)
    private Map map;


    private boolean isfull;
    private boolean canStart;


    public Spiel(){
        init();
    }

    public String getTime() {
        return time;
    }
    public boolean isCanStart() {
        return canStart;
    }

    public void setCanStart(boolean canStart) {
        this.canStart = canStart;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpielErgebnis() {
        return SpielErgebnis;
    }

    public void setSpielErgebnis(String SpielErgebnis) {
        this.SpielErgebnis = SpielErgebnis;
    }

    public String getGewinner() {
        return Gewinner;
    }

    public void setGewinner(String Gewinner) {
        this.Gewinner = Gewinner;
    }

    public Integer getMaxNumberOfRounds() {
        return maxNumberOfRounds;
    }

    public void setMaxNumberOfRounds(Integer maxNumberOfRounds) {
        this.maxNumberOfRounds = maxNumberOfRounds;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Spieler getSpieler1() {
        return spieler1;
    }

    public void setSpieler1(Spieler spieler1) {
        this.spieler1 = spieler1;
    }

    public String getSpieler1Color() {
        return spieler1Color;
    }

    public void setSpieler1Color(String spieler1Color) {
        this.spieler1Color = spieler1Color;
    }

    public Spieler getSpieler2() {
        return spieler2;
    }

    public void setSpieler2(Spieler spieler2) {
        this.spieler2 = spieler2;
    }

    public String getSpieler2Color() {
        return spieler2Color;
    }

    public void setSpieler2Color(String spieler2Color) {
        this.spieler2Color = spieler2Color;
    }

    public boolean isIsfull() {
        this.isfull = this.spieler1 != null && this.spieler2 != null;
        return isfull;
    }

    public void setIsfull(boolean isfull) {
        this.isfull = isfull;
    }


    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Spiel(Spieler spieler1, String spieler1Color) {

        this.spieler1 = spieler1;
        this.spieler1Color = spieler1Color;
        init();
    }


    private void init(){
        this.canStart = false;
        this.isfull = false;
        this.maxNumberOfRounds = 200;
        this.currentRound = 0;
        this.Gewinner = "none so far";
        this.map = null;
        this.spieler2 = null;
        this.spieler2Color = null;
        this.SpielErgebnis = "0:0";
        this.turns = new ArrayList<Turn>();

    }


    public Integer getId() {
        return id;
    }

  public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }   public void setId(Integer id) {
        this.id = id;
    }

    public void start() {
        if (this.map.getHalfmapAvalid() && this.map.getHalfmapBvalid()){
            this.setStarted(true);
            this.map.setEvalueated(true);
            this.currentRound = 1;
        }else {
            if (!this.map.getHalfmapBvalid() && this.map.getHalfmapAvalid()){
                this.setFinished(true);
                this.setGewinner(this.spieler1.getNickname() + " hat gewonnen!");
            }else if(!this.map.getHalfmapAvalid() && this.map.getHalfmapBvalid()) {
                this.setFinished(true);
                this.setGewinner(this.spieler2.getNickname() + " hat gewonnen!");
            }else {
                this.setFinished(true);
                this.setGewinner("Beide Spieler registrierten eine ungültige Karte. Das Spiel ist beendet, bevor es beginnen kann");

            }
        }
    }
}