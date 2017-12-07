package map;

public class MapHilfe {

    private boolean isHalfmapA;
    private String[] waterCoordinates;
    private String[] mountainCoordinates;
    private String[] grassCoordinates;
    private String castleCoordinates;
    private String treasureCoordinates;


    public MapHilfe(){};

    public boolean isHalfmapA() {
        return isHalfmapA;
    }

    public void setHalfmapA(boolean halfmapA) {
        isHalfmapA = halfmapA;
    }

    public String[] getWaterCoordinates() {
        return waterCoordinates;
    }

    public void setWaterCoordinates(String[] waterCoordinates) {
        this.waterCoordinates = waterCoordinates;
    }

    public String[] getMountainCoordinates() {
        return mountainCoordinates;
    }

    public void setMountainCoordinates(String[] mountainCoordinates) {
        this.mountainCoordinates = mountainCoordinates;
    }

    public String[] getGrassCoordinates() {
        return grassCoordinates;
    }

    public void setGrassCoordinates(String[] grassCoordinates) {
        this.grassCoordinates = grassCoordinates;
    }

    public String getCastleCoordinates() {
        return castleCoordinates;
    }

    public void setCastleCoordinates(String castleCoordinates) {
        this.castleCoordinates = castleCoordinates;
    }

    public String getTreasureCoordinates() {
        return treasureCoordinates;
    }

    public void setTreasureCoordinates(String treasureCoordinates) {
        this.treasureCoordinates = treasureCoordinates;
    }
}

