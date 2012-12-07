/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chocosolution;

/**
 *
 * @author Antiregulator
 */


import choco.kernel.model.variables.integer.IntegerVariable;
import java.util.*;

public class RoomVariableStructures {
    private Map<String, RoomVariableStructures> roomVarsMappedToHouses;
    
    private List<IntegerVariable> xVars;
    private List<IntegerVariable> yVars;
    private List<IntegerVariable> zVars;
    
    private List<IntegerVariable> lVars;
    private List<IntegerVariable> wVars;
    private List<IntegerVariable> hVars;
    
    private IntegerVariable houseXVar;
    private IntegerVariable houseYVar;
    private IntegerVariable houseZVar;
    
    private IntegerVariable houseLVar;
    private IntegerVariable houseWVar;
    private IntegerVariable houseHVar;
    
    public RoomVariableStructures() {
        xVars=new ArrayList<>();
        yVars=new ArrayList<>();
        zVars=new ArrayList<>();
        
        lVars=new ArrayList<>();
        wVars=new ArrayList<>();
        hVars=new ArrayList<>();
        
        roomVarsMappedToHouses=new HashMap<>();
    }
    
    //this method initializes the roomVarsMappedToHouses with the househashes 
    //and a new RoomVariableStructures (an object of this containing class) as
    //a map entry.
    public void initialiseRoomVarsMappedToHouses(Map<String, ChocoHouse> chocoHouseMap) {
        for(String tempString:chocoHouseMap.keySet()) {
            this.roomVarsMappedToHouses.put(tempString, new RoomVariableStructures());
        }
    }
    
    public void fillVariableStructures(Map<String, ChocoHouse> chocoHouseMap, Map<String, ChocoRoom> chocoRoomMap) {
        Set<String> tempHouseHashKeySet=chocoHouseMap.keySet();
        
        for(ChocoHouse tempChocoHouse:chocoHouseMap.values()) {
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseXVar(tempChocoHouse.getHouseXVar());
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseYVar(tempChocoHouse.getHouseYVar());
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseZVar(tempChocoHouse.getHouseZVar());
            
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseLVar(tempChocoHouse.getHouseLengthVar());
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseWVar(tempChocoHouse.getHouseWidthVar());
            this.roomVarsMappedToHouses.get(tempChocoHouse.getHouseIndividualHash()).setHouseHVar(tempChocoHouse.getHouseHeightVar());
        }
        
        for(ChocoRoom tempChocoRoom:chocoRoomMap.values()) {
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).getlVars().add(tempChocoRoom.getRoomLengthVar());
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).getwVars().add(tempChocoRoom.getRoomWidthVar());
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).gethVars().add(tempChocoRoom.getRoomHeightVar());
            
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).getxVars().add(tempChocoRoom.getRoomXVar());
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).getyVars().add(tempChocoRoom.getRoomYVar());
            this.roomVarsMappedToHouses.get(tempChocoRoom.getRoomIsPartOfHouse()).getzVars().add(tempChocoRoom.getRoomZVar());
        }
    }

    public List<IntegerVariable> gethVars() {
        return hVars;
    }

    public void sethVars(List<IntegerVariable> hVars) {
        this.hVars = hVars;
    }

    public List<IntegerVariable> getlVars() {
        return lVars;
    }

    public void setlVars(List<IntegerVariable> lVars) {
        this.lVars = lVars;
    }

    public Map<String, RoomVariableStructures> getRoomVarsMappedToHouses() {
        return roomVarsMappedToHouses;
    }

    public void setRoomVarsMappedToHouses(Map<String, RoomVariableStructures> roomVarsMappedToHouses) {
        this.roomVarsMappedToHouses = roomVarsMappedToHouses;
    }

    public List<IntegerVariable> getwVars() {
        return wVars;
    }

    public void setwVars(List<IntegerVariable> wVars) {
        this.wVars = wVars;
    }

    public List<IntegerVariable> getxVars() {
        return xVars;
    }

    public void setxVars(List<IntegerVariable> xVars) {
        this.xVars = xVars;
    }

    public List<IntegerVariable> getyVars() {
        return yVars;
    }

    public void setyVars(List<IntegerVariable> yVars) {
        this.yVars = yVars;
    }

    public List<IntegerVariable> getzVars() {
        return zVars;
    }

    public void setzVars(List<IntegerVariable> zVars) {
        this.zVars = zVars;
    }

    public IntegerVariable getHouseLVar() {
        return houseLVar;
    }

    public void setHouseLVar(IntegerVariable houseLVar) {
        this.houseLVar = houseLVar;
    }

    public IntegerVariable getHouseWVar() {
        return houseWVar;
    }

    public void setHouseWVar(IntegerVariable houseWVar) {
        this.houseWVar = houseWVar;
    }

    public IntegerVariable getHouseXVar() {
        return houseXVar;
    }

    public void setHouseXVar(IntegerVariable houseXVar) {
        this.houseXVar = houseXVar;
    }

    public IntegerVariable getHouseYVar() {
        return houseYVar;
    }

    public void setHouseYVar(IntegerVariable houseYVar) {
        this.houseYVar = houseYVar;
    }

    public IntegerVariable getHouseHVar() {
        return houseHVar;
    }

    public void setHouseHVar(IntegerVariable houseHVar) {
        this.houseHVar = houseHVar;
    }

    public IntegerVariable getHouseZVar() {
        return houseZVar;
    }

    public void setHouseZVar(IntegerVariable houseZVar) {
        this.houseZVar = houseZVar;
    }
    
}
