package turn;

import spiel.Spiel;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name="spiel_turn")
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name="spiel_id")
    private Spiel spiel;

    private Integer turnNr;

    private String action;


    private String newPositionA;
    private String newPositionB;

    private String oldPositionA;
    private String oldPositionB;

    private String inventoryA;
    private String inventoryB;


}