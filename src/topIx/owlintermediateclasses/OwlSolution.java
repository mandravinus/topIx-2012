package topIx.owlintermediateclasses;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import chocosolution.ChocoHouse;
import chocosolution.ChocoRoom;

public class OwlSolution {
    private Integer solutionID;
    private Integer siteLength;
    private Integer siteWidth;
    private List<OwlSolvedHouse> solvedHouses;
    private List<OwlSolvedRoom> solvedRooms;
    
    
    //constructor used in RETRIEVE SOLUTION occasions.
    public OwlSolution() {
        this.solutionID=new Integer(0);
        this.solvedHouses=new ArrayList<>();
        this.solvedRooms=new ArrayList<>();
    }
    
    //constructor used in STORE SOLUTION occasions.
    public OwlSolution(Integer solutionID, Integer siteLength, Integer siteWidth, Map<String, ChocoHouse> chocoHouseMap, Map<String, ChocoRoom> chocoRoomMap) {
        this.solutionID=new Integer(solutionID.intValue());
        this.siteLength=siteLength;
        this.siteWidth=siteWidth;
        solvedHouses=new ArrayList<>();
        solvedRooms=new ArrayList<>();
        
        
        Collection<ChocoHouse> tempHouseCollection=chocoHouseMap.values();
        for(ChocoHouse tempChocoHouse:tempHouseCollection) {
            String tempHouseLiteral=tempChocoHouse.getHouseIndividualLiteral();
            String tempHouseHash=tempChocoHouse.getHouseIndividualHash();
            int tempL=tempChocoHouse.getHouseLengthRes().getVal();
            int tempW=tempChocoHouse.getHouseWidthRes().getVal();
            int tempX=tempChocoHouse.getHouseXRes().getVal();
            int tempY=tempChocoHouse.getHouseYRes().getVal();
            OwlSolvedHouse tempSolvedHouse=new OwlSolvedHouse(tempHouseHash, tempHouseLiteral, tempL, tempW, tempX, tempY);
            this.solvedHouses.add(tempSolvedHouse);
        }
        
        Collection<ChocoRoom> tempRoomCollection=chocoRoomMap.values();
        for(ChocoRoom tempChocoRoom:tempRoomCollection) {
            String tempRoomHash=tempChocoRoom.getRoomIndividualHash();
            String tempRoomLiteral=tempChocoRoom.getRoomIndividualLiteral();
            int tempL=tempChocoRoom.getRoomLengthRes().getVal();
            int tempW=tempChocoRoom.getRoomWidthRes().getVal();
            int tempH=tempChocoRoom.getRoomHeightRes().getVal();
            int tempX=tempChocoRoom.getRoomXRes().getVal();
            int tempY=tempChocoRoom.getRoomYRes().getVal();
            int tempZ=tempChocoRoom.getRoomZRes().getVal();
            OwlSolvedRoom tempSolvedRoom=new OwlSolvedRoom(tempRoomHash, tempRoomLiteral, tempL, tempW, tempH, tempX, tempY, tempZ);
            this.solvedRooms.add(tempSolvedRoom);
        }
    }

    public Integer getSolutionID() {
        return solutionID;
    }

    public void setSolutionID(Integer solutionID) {
        this.solutionID = solutionID;
    }

    public List<OwlSolvedHouse> getSolvedHouses() {
        return solvedHouses;
    }

    public void setSolvedHouses(List<OwlSolvedHouse> solvedHouses) {
        this.solvedHouses = solvedHouses;
    }

    public List<OwlSolvedRoom> getSolvedRooms() {
        return solvedRooms;
    }

    public void setSolvedRooms(List<OwlSolvedRoom> solvedRooms) {
        this.solvedRooms = solvedRooms;
    }

    public Integer getSiteLength() {
        return siteLength;
    }

    public void setSiteLength(Integer siteLength) {
        this.siteLength = siteLength;
    }

    public Integer getSiteWidth() {
        return siteWidth;
    }

    public void setSiteWidth(Integer siteWidth) {
        this.siteWidth = siteWidth;
    }
    
    @Override
    public String toString() {
        System.out.println(String.format("Solution#%1$02d", this.solutionID));
        System.out.println("SOLVED HOUSES OF THE SOLUTION\n"+this.solvedHouses.toString());
        System.out.println("SOLVED ROOMS OF THE SOLUTION\n"+this.solvedRooms.toString());
        System.out.println("END OF SOLUTION BLOCK");
        return(String.format("Solution#%1$02d", this.solutionID));
    }
}