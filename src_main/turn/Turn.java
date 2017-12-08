package turn;

import spiel.Spiel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="spiel_turn")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="spiel_id")
    private Spiel spiel;

    private Integer turnNr;


    private String newPositionA;
    private String newPositionB;

    private String oldPositionA;
    private String oldPositionB;

    private Boolean inventoryA;
    private Boolean inventoryB;

    public Turn(){}




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Spiel getSpiel() {
        return spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public Integer getTurnNr() {
        return turnNr;
    }

    public void setTurnNr(Integer turnNr) {
        this.turnNr = turnNr;
    }

    public String getNewPositionA() {
        return newPositionA;
    }

    public void setNewPositionA(String newPositionA) {
        this.newPositionA = newPositionA;
    }

    public String getNewPositionB() {
        return newPositionB;
    }

    public void setNewPositionB(String newPositionB) {
        this.newPositionB = newPositionB;
    }

    public String getOldPositionA() {
        return oldPositionA;
    }

    public void setOldPositionA(String oldPositionA) {
        this.oldPositionA = oldPositionA;
    }

    public String getOldPositionB() {
        return oldPositionB;
    }

    public void setOldPositionB(String oldPositionB) {
        this.oldPositionB = oldPositionB;
    }

    public boolean getInventoryA() {
        return inventoryA;
    }

    public void setInventoryA(boolean inventoryA) {
        this.inventoryA = inventoryA;
    }

    public boolean getInventoryB() {
        return inventoryB;
    }

    public void setInventoryB(boolean inventoryB) {
        this.inventoryB = inventoryB;
    }
}
