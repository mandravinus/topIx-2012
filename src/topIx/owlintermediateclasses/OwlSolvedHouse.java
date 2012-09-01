package topIx.owlintermediateclasses;

public class OwlSolvedHouse {
    private String solvedHouseLiteral;
    private String solvedHouseHash;
    private int solvedHouseLength;
    private int solvedHouseWidth;
    private int solvedHouseX;
    private int solvedHouseY;
    
    public OwlSolvedHouse() {
        this.solvedHouseLiteral = null;
        this.solvedHouseHash = null;
        this.solvedHouseLength = 0;
        this.solvedHouseWidth = 0;
        this.solvedHouseX = 0;
        this.solvedHouseY = 0;}

    public OwlSolvedHouse(String solvedHouseHash, String solvedHouseLiteral, int solvedHouseLength, int solvedHouseWidth, int solvedHouseX, int solvedHouseY) {
        this.solvedHouseLiteral = solvedHouseLiteral;
        this.solvedHouseHash=solvedHouseHash;
        this.solvedHouseLength = solvedHouseLength;
        this.solvedHouseWidth = solvedHouseWidth;
        this.solvedHouseX = solvedHouseX;
        this.solvedHouseY = solvedHouseY;
    }

    public String getSolvedHouseHash() {
        return solvedHouseHash;
    }

    public void setSolvedHouseHash(String solvedHouseHash) {
        this.solvedHouseHash = solvedHouseHash;
    }

    public int getSolvedHouseLength() {
        return solvedHouseLength;
    }

    public void setSolvedHouseLength(int solvedHouseLength) {
        this.solvedHouseLength = solvedHouseLength;
    }

    public String getSolvedHouseLiteral() {
        return solvedHouseLiteral;
    }

    public void setSolvedHouseLiteral(String solvedHouseLiteral) {
        this.solvedHouseLiteral = solvedHouseLiteral;
    }

    public int getSolvedHouseWidth() {
        return solvedHouseWidth;
    }

    public void setSolvedHouseWidth(int solvedHouseWidth) {
        this.solvedHouseWidth = solvedHouseWidth;
    }

    public int getSolvedHouseX() {
        return solvedHouseX;
    }

    public void setSolvedHouseX(int solvedHouseX) {
        this.solvedHouseX = solvedHouseX;
    }

    public int getSolvedHouseY() {
        return solvedHouseY;
    }

    public void setSolvedHouseY(int solvedHouseY) {
        this.solvedHouseY = solvedHouseY;
    }
    
}