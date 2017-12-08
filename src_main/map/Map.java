package map;

import spiel.Spiel;
import turn.TurnHilfe;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name="map")
public class Map {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    private String coordinatesOfTreasureA;
    private String coordinatesOfTreasureB;
    private String coordinatesOfCastleA;
    private String coordinatesOfCastleB;
    private Character[][] halfmapA;
    private Character[][] halfmapB;
    private Boolean isHalfmapAvalid;
    private Boolean isHalfmapBvalid;


    private Boolean evalueated;



    @OneToOne(cascade = CascadeType.ALL)
    private Spiel spiel;


    public Map(){
        this.evalueated = false;
    }



    public boolean initMap(boolean isHalfmapA,
                            String[] waterCoordinates,
                            String[] mountainCoordinates,
                            String[] grassCoordinates,
                            String castleCoordinates,
                            String treasureCoordinates){

        if (isHalfmapA){
            this.isHalfmapAvalid = true;
        }else {
            this.isHalfmapBvalid= true;
        }
        /**
         * checks if the amount of terrain types are ok.
         */
        if (waterCoordinates.length > 4
                || mountainCoordinates.length > 3
                || grassCoordinates.length < 5){
             setHalfmapInvalid(isHalfmapA) ;
        }

        Character[][] halfmap = new Character[4][8];
        for (String f : waterCoordinates){
            int[] fCoordinate = getCoordinatesFromString(f);
            System.out.println(fCoordinate[0] + " - " + fCoordinate[1]);
            if (fCoordinate == null) break;
            halfmap[fCoordinate[0]][fCoordinate[1]] = 'w';
        }
        for (String f : mountainCoordinates){
            int[] fCoordinate = getCoordinatesFromString(f);
            if (fCoordinate == null) break;
            halfmap[fCoordinate[0]][fCoordinate[1]] = 'm';
        }
        for (String f : grassCoordinates){
            int[] fCoordinate = getCoordinatesFromString(f);
            if (fCoordinate == null) break;
            halfmap[fCoordinate[0]][fCoordinate[1]] = 'g';
        };


        for (int i = 0; i < halfmap.length; i++){
            for (int j = 0 ; j < halfmap[0].length; j++){
                System.out.println(halfmap[i][j]);
                if (halfmap[i][j] == null) setHalfmapInvalid(isHalfmapA);
            }
        }

        int[] cCoordinate = getCoordinatesFromString(castleCoordinates);
        int[] tCoordinate = getCoordinatesFromString(treasureCoordinates);

        /**
         * check if treasure or mountain is for sure in grass
         */
        char cGround = halfmap[cCoordinate[0]][cCoordinate[1]];
        char tGround = halfmap[tCoordinate[0]][tCoordinate[1]];
        if (tGround != 'g' || cGround != 'g') return setHalfmapInvalid(isHalfmapA);

        // set treasure and castle position.
        halfmap[cCoordinate[0]][cCoordinate[1]] = 'c';
        halfmap[tCoordinate[0]][tCoordinate[1]] = 't';


        if (isHalfmapA){
            this.halfmapA = halfmap;
            this.setCoordinatesOfCastleA(castleCoordinates);
            this.setCoordinatesOfTreasureA(treasureCoordinates);
        }else {
            this.halfmapB = halfmap;
            this.setCoordinatesOfCastleB(castleCoordinates);
            this.setCoordinatesOfTreasureB(treasureCoordinates);
        }

        return true;

    }

    private boolean setHalfmapInvalid(boolean isHalfmapA){
        if (isHalfmapA){
            this.isHalfmapAvalid = false;
            return this.isHalfmapAvalid;
        }else {
            this.isHalfmapBvalid = false;
            return isHalfmapBvalid;
        }
    }
    

    /**
     * Pr�ft ob der Spielzug vom Spieler in erlaubt ist
     * @param isSpielerA
     * @param turn
     * @return true if _ is valid move
     */
    public boolean validateTurn(boolean isSpielerA, TurnHilfe turn) throws Exception {

        int[] oldCoordinates = getCoordinatesFromString(turn.getCurrentPosition());
        int[] newCoordinates = getCoordinatesFromString(turn.getNewPosition());

        if (oldCoordinates == null || newCoordinates == null) return false;
        System.out.println("coordinates exists");

        int oldXIndex = "abcdefgh".indexOf(turn.getCurrentPosition().charAt(0));
        if (isSpielerA && oldXIndex > 3 && !turn.isInventory()) return false;
        System.out.println("p1 test");
        if (!isSpielerA && oldXIndex <= 3 && !turn.isInventory()) return false;
        System.out.println("p2 test");

        int diff = Math.abs(oldCoordinates[0] - newCoordinates[0]);
        int diffY = Math.abs(oldCoordinates[1] - newCoordinates[1]);
        if (turn.isInventory()){
            System.out.println("inventory");
            if (diff <= 1 || diff == 3){
                boolean isOnWater;
                boolean isTreasureFound;
                boolean isOnMountain;
                if (isSpielerA){
                    isOnWater = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 'w';
                    isOnMountain = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 'm';
                    isTreasureFound = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 't';
                    if (isOnWater) return false;
                    return true;
                }else {
                    isOnWater = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 'w';
                    isOnMountain = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 'm';
                    isTreasureFound = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 't';
                    if (isOnWater) return false;
                    return true;
                }
            }else {
                return false;
            }
        }else  {
            System.out.println("noInventory");
            if (diff <= 1 && diff <= 1){
                System.out.println("noInventory");
                boolean isOnWater;
                boolean isTreasureFound;
                boolean isOnMountain;
                if (isSpielerA){
                    System.out.println("Spieler A");
                    isOnWater = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 'w';
                    isOnMountain = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 'm';
                    isTreasureFound = this.halfmapB[newCoordinates[0]][newCoordinates[1]] == 't';
                    if (isOnWater) return false;
                    System.out.println("not on water");
                    return true;
                }else {
                    isOnWater = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 'w';
                    isOnMountain = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 'm';
                    isTreasureFound = this.halfmapA[newCoordinates[0]][newCoordinates[1]] == 't';
                    if (isOnWater) return false;
                    return true;
                }
            }else {
                return false;
            }
        }

    }


    private int[] getCoordinatesFromString(String c){

        if (c.length() != 2){
           return null;
        }
        int[] coordinates = new int[2];

        char[] cc = c.toCharArray();

        int xIndex = "abcdefgh".indexOf(cc[0]);
        int yIndex = "12345678".indexOf(cc[1]);
        if (xIndex == -1 || yIndex == -1) return null;

        coordinates[0] = xIndex <= 3 ? xIndex : xIndex -4;
        coordinates[1] = yIndex;

        return coordinates;

    }



   public Spiel getSpiel() {
        return spiel;
   }

   public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
   }public Integer getId() {
        return id;
    }

    public String getCoordinatesOfTreasureA() {
        return coordinatesOfTreasureA;
    }

    public void setCoordinatesOfTreasureA(String coordinatesOfTreasureA) {
        this.coordinatesOfTreasureA = coordinatesOfTreasureA;
    }

    public String getCoordinatesOfTreasureB() {
        return coordinatesOfTreasureB;
    }

    public void setCoordinatesOfTreasureB(String coordinatesOfTreasureB) {
        this.coordinatesOfTreasureB = coordinatesOfTreasureB;
    }

    public String getCoordinatesOfCastleA() {
        return coordinatesOfCastleA;
    }

    public void setCoordinatesOfCastleA(String coordinatesOfCastleA) {
        this.coordinatesOfCastleA = coordinatesOfCastleA;
    }

    public String getCoordinatesOfCastleB() {
        return coordinatesOfCastleB;
    }

    public void setCoordinatesOfCastleB(String coordinatesOfCastleB) {
        this.coordinatesOfCastleB = coordinatesOfCastleB;
    }

    public Character[][] getHalfmapA() {
        return halfmapA;
    }

    public void setHalfmapA(Character[][] halfmapA) {
        this.halfmapA = halfmapA;
    }

    public Character[][] getHalfmapB() {
        return halfmapB;
    }

    public void setHalfmapB(Character[][] halfmapB) {
        this.halfmapB = halfmapB;
    }

    public Boolean getEvalueated() {
        return evalueated;
    }

    public void setEvalueated(Boolean evalueated) {
        this.evalueated = evalueated;
    }

    public Boolean getHalfmapAvalid() {
        return isHalfmapAvalid;
    }

    public void setHalfmapAvalid(Boolean halfmapAvalid) {
        isHalfmapAvalid = halfmapAvalid;
    }

    public Boolean getHalfmapBvalid() {
        return isHalfmapBvalid;
    }

    public void setHalfmapBvalid(Boolean halfmapBvalid) {
        isHalfmapBvalid = halfmapBvalid;
    }
}


