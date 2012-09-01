package topIx.owlintermediateclasses;

public class OwlSolvedRoom {
    private String solvedRoomLiteral;
    private String solvedRoomHash;
    private int solvedRoomLength;
    private int solvedRoomWidth;
    private int solvedRoomHeight;
    private int solvedRoomX;
    private int solvedRoomY;
    private int solvedRoomZ;
    
        public OwlSolvedRoom() {
        this.solvedRoomLiteral = null;
        this.solvedRoomHash = null;
        this.solvedRoomLength = 0;
        this.solvedRoomWidth = 0;
        this.solvedRoomHeight = 0;
        this.solvedRoomX = 0;
        this.solvedRoomY = 0;
        this.solvedRoomZ = 0;
    }

        
    public OwlSolvedRoom(String solvedRoomHash, String solvedRoomLiteral, int solvedRoomLength, int solvedRoomWidth, int solvedRoomHeight, int solvedRoomX, int solvedRoomY, int solvedRoomZ) {
        this.solvedRoomLiteral = solvedRoomLiteral;
        this.solvedRoomHash=solvedRoomHash;
        this.solvedRoomLength = solvedRoomLength;
        this.solvedRoomWidth = solvedRoomWidth;
        this.solvedRoomHeight = solvedRoomHeight;
        this.solvedRoomX = solvedRoomX;
        this.solvedRoomY = solvedRoomY;
        this.solvedRoomZ = solvedRoomZ;
    }

    public String getSolvedRoomHash() {
        return solvedRoomHash;
    }

    public void setSolvedRoomHash(String solvedRoomHash) {
        this.solvedRoomHash = solvedRoomHash;
    }

    public int getSolvedRoomHeight() {
        return solvedRoomHeight;
    }

    public void setSolvedRoomHeight(int solvedRoomHeight) {
        this.solvedRoomHeight = solvedRoomHeight;
    }

    public int getSolvedRoomLength() {
        return solvedRoomLength;
    }

    public void setSolvedRoomLength(int solvedRoomLength) {
        this.solvedRoomLength = solvedRoomLength;
    }

    public String getSolvedRoomLiteral() {
        return solvedRoomLiteral;
    }

    public void setSolvedRoomLiteral(String solvedRoomLiteral) {
        this.solvedRoomLiteral = solvedRoomLiteral;
    }

    public int getSolvedRoomWidth() {
        return solvedRoomWidth;
    }

    public void setSolvedRoomWidth(int solvedRoomWidth) {
        this.solvedRoomWidth = solvedRoomWidth;
    }

    public int getSolvedRoomX() {
        return solvedRoomX;
    }

    public void setSolvedRoomX(int solvedRoomX) {
        this.solvedRoomX = solvedRoomX;
    }

    public int getSolvedRoomY() {
        return solvedRoomY;
    }

    public void setSolvedRoomY(int solvedRoomY) {
        this.solvedRoomY = solvedRoomY;
    }

    public int getSolvedRoomZ() {
        return solvedRoomZ;
    }

    public void setSolvedRoomZ(int solvedRoomZ) {
        this.solvedRoomZ = solvedRoomZ;
    }
}