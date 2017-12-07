package spieler;

import spiel.Spiel;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="spieler")
public class Spieler {

    // TODO: add attr. and methods

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotBlank(message="nickname cannot be blank")
    private String nickname;

    @NotBlank(message="matrikelnummer cannot be blank")
    private String matrNr;


    @OneToMany
    private List<Spiel> attendedGames;

    public Spieler(){}

    public Spieler(String nickname, String matrNr) {
        this.nickname = nickname;
        this.matrNr = matrNr;
    }



    // Getter and Setters
    public Integer getId() {
        return id;

    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getMatrNr() {
        return matrNr;
    }
    public void setMatrNr(String matrNr) {
        this.matrNr = matrNr;
    }



}
