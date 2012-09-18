package chocosolution;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.variables.integer.IntegerConstantVariable;
import static choco.Choco.*;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;

import java.util.Collection;
import static java.util.Collections.*;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

import static chocosolution.ChocoUtility.*;
import choco.kernel.solver.variables.integer.IntDomainVar;

import org.apache.log4j.Logger;
import topIx.owlintermediateclasses.OwlSite;

public class TopIxChoco {
    private choco.cp.model.CPModel topIxModel;
    private choco.cp.solver.CPSolver topIxSolver;
    
    private Map<String, ChocoHouse> chocoHouseMap;
    private Map<String, ChocoRoom> chocoRoomMap; //this map contains K:roomNames in the same form these are entered in the ontology and V:IntegerVariables
    
    Logger logger=Logger.getLogger(TopIxChoco.class.getName());
    
    public TopIxChoco() { 
       topIxModel=new CPModel();
       topIxSolver=new CPSolver();
       
       chocoHouseMap=new HashMap<>();
       chocoRoomMap=new HashMap<>();
    }
    
    public void insertNonOverlappingHousesConstraints() {
        List<ChocoHouse> lhsChocoHouseCollection=new ArrayList<>(chocoHouseMap.values());
        List<ChocoHouse> rhsChocoHouseCollection=new ArrayList<>(lhsChocoHouseCollection);
        for(ChocoHouse lhsTempChocoHouse : lhsChocoHouseCollection) {
            rhsChocoHouseCollection.remove(lhsTempChocoHouse);
            for(ChocoHouse rhsTempChocoHouse : rhsChocoHouseCollection) {
                nonOverlappingHousesConstraint(lhsTempChocoHouse, rhsTempChocoHouse, this.topIxModel);
            }
        }
    }

    public void insertNonOverlappingRoomsConstraints() {
    List<ChocoRoom> lhsChocoRoomCollection=new ArrayList<>(chocoRoomMap.values());
    List<ChocoRoom> rhsChocoRoomCollection=new ArrayList<>(lhsChocoRoomCollection);
    for(ChocoRoom lhsTempChocoRoom : lhsChocoRoomCollection) {
        rhsChocoRoomCollection.remove(lhsTempChocoRoom);
        for(ChocoRoom rhsTempChocoRoom : rhsChocoRoomCollection) {
            nonOverlappingRoomsConstraint(lhsTempChocoRoom, rhsTempChocoRoom, this.topIxModel);
            }
        }
    }
    
    public void insertSiteCompactnessConstraints(OwlSite currentSite) {
        IntegerExpressionVariable sumHouseAreaExpressionVariable;
        IntegerExpressionVariable[] houseAreaExpressionVariables;
        
        //the pragmatic reason for using tempArray as an intermediate destination
        //is that using the collections for loop is easier and immediate array manipulation
        //requires a counter variable. so, i end up using ArrayList.toArray to fill my
        //IntegerVariable arrays.
        ArrayList<IntegerExpressionVariable> tempArrayList=new ArrayList<>();
        
        logger.info(" ");
        tempArrayList.clear();
        for(ChocoHouse tempChocoHouse:this.chocoHouseMap.values())  {
            IntegerExpressionVariable tempHouseAreaExpressionVariable=mult(tempChocoHouse.getHouseLengthVar(), tempChocoHouse.getHouseWidthVar());
            tempArrayList.add(tempHouseAreaExpressionVariable);
        }
        houseAreaExpressionVariables=(IntegerExpressionVariable[])tempArrayList.toArray(new IntegerExpressionVariable[0]);
        
        sumHouseAreaExpressionVariable=sum(houseAreaExpressionVariables);
        int siteArea=currentSite.getSiteLength()*currentSite.getSiteWidth();
        Constraint constToAdd=(eq(sumHouseAreaExpressionVariable, siteArea));
        this.topIxModel.addConstraint(constToAdd);
    }
    
    public void insertHouseAreaCompactnessConstraints() {
        //the following map will accommodate arraylists of integerExpressionVars,
        //listing the area expression variables that are members
        //of each room of each house.
        Map<String, List<IntegerExpressionVariable>> housesToBelongingRoomAreaExpVars=new HashMap<>();
        for(String tempHouseHash:this.chocoHouseMap.keySet()) {
            housesToBelongingRoomAreaExpVars.put(tempHouseHash, new ArrayList<IntegerExpressionVariable>());
            for(ChocoRoom tempChocoRoom:chocoRoomMap.values()) {
                if(tempHouseHash.equals(tempChocoRoom.getRoomIsPartOfHouse())) {
                    housesToBelongingRoomAreaExpVars.get(tempHouseHash).add(tempChocoRoom.getRoomAreaExpressionVariable());
                }
            }
        }
        
        for(ChocoHouse tempChocoHouse:chocoHouseMap.values()) {
            //for area compactness...
            IntegerExpressionVariable tempChocoHouseAreaExpVar=mult(tempChocoHouse.getHouseLengthVar(), tempChocoHouse.getHouseWidthVar());
            IntegerExpressionVariable[] roomAreaExpVars;
            IntegerExpressionVariable sumRoomAreaExpVar;
            
            roomAreaExpVars=(IntegerExpressionVariable[])housesToBelongingRoomAreaExpVars
                .get(tempChocoHouse.getHouseIndividualHash())
                    .toArray(new IntegerExpressionVariable[0]);
            
            sumRoomAreaExpVar=sum(roomAreaExpVars);
            Constraint houseCompactnessConstraint=eq(sumRoomAreaExpVar, tempChocoHouseAreaExpVar);
            this.topIxModel.addConstraint(houseCompactnessConstraint);
        }
//        RoomVariableStructures roomVariableStructures=new RoomVariableStructures();
//        roomVariableStructures.initialiseRoomVarsMappedToHouses(chocoHouseMap);
//        roomVariableStructures.fillVariableStructures(chocoHouseMap, chocoRoomMap);
//        
//        for(RoomVariableStructures tempRoomVariableStructures:roomVariableStructures.getRoomVarsMappedToHouses().values()) {
//            IntegerVariable[] roomLengthVars;
//            IntegerVariable[] roomWidthVars;
//            
//            IntegerExpressionVariable sumRoomLength;
//            IntegerExpressionVariable sumRoomWidth;
//            
//            IntegerExpressionVariable sumRoomAreaExpressionVariable;
//            IntegerExpressionVariable[] roomAreaExpressionVariables;
//            
//            roomLengthVars=(IntegerVariable[])tempRoomVariableStructures.getlVars().toArray(new IntegerVariable[0]);
//            roomWidthVars=(IntegerVariable[])tempRoomVariableStructures.getwVars().toArray(new IntegerVariable[0]);
//            
//            roomAreaExpressionVariables=new IntegerExpressionVariable[roomLengthVars.length];
//            
//            for(int i=0; i<roomLengthVars.length; i++) {
//                IntegerExpressionVariable tempRoomAreaExpressionVariable=
//            }
//            IntegerExpressionVariable sumRoomL=sum(roomLengthVars);
//            //roomWidthVars=(IntegerVariable[])tempRoomVariableStructures.getwVars().toArray(new IntegerVariable[0]);
//            //IntegerExpressionVariable sumRoomW=sum(roomWidthVars);
//            IntegerExpressionVariable sumRoomW=sum(tempRoomVariableStructures.getwVars().toArray(new IntegerVariable[0]));
//            
//            IntegerExpressionVariable tempRoomArea=mult(sumRoomL, sumRoomW);
//            IntegerExpressionVariable tempHouseArea=mult(tempRoomVariableStructures.getHouseLVar(), tempRoomVariableStructures.getHouseWVar());
//            Constraint houseCompactnessConstraint=(eq(tempRoomArea, tempHouseArea));
            
            //this.topIxModel.addConstraint(houseCompactnessConstraint);
        //}
    }
    
    //this method matches the houses to the roomAreaExpressionVariables that 
    //reside in each ChocoRoom entity that conceptually belongs to the house.
    //that way, we can later create the sum of the volumes that make up each house!
    //the previous method does that for AREA COMPACTNESS ONLY!
    public void insertHouseVolumeCompactnessConstraints(){
        Map<String, List<IntegerExpressionVariable>> housesToBelongingRoomVolumeExpVars=new HashMap<>();
        for(String tempHouseHash:this.chocoHouseMap.keySet()){
            housesToBelongingRoomVolumeExpVars.put(tempHouseHash, new ArrayList<IntegerExpressionVariable>());
            for(ChocoRoom tempChocoRoom:chocoRoomMap.values()){
                if(tempHouseHash.equals(tempChocoRoom.getRoomIsPartOfHouse())){
                    housesToBelongingRoomVolumeExpVars.get(tempHouseHash).add(tempChocoRoom.getRoomVolumeExpressionVariable());
                }
            }
            logger.info("........................................................................163");
            logger.info(housesToBelongingRoomVolumeExpVars.values().toString());
        }
        
        for(ChocoHouse tempChocoHouse:chocoHouseMap.values()){
            //for volume compactness...
            logger.info("........................................................................167");
            logger.info(chocoHouseMap.values().toString());
            IntegerExpressionVariable tempChocoHouseVolumeExpVar=mult(tempChocoHouse.getHouseLengthVar(), (mult(tempChocoHouse.getHouseWidthVar(), tempChocoHouse.getHouseHeightVar())));
            IntegerExpressionVariable[] roomVolumeExpVars;
            IntegerExpressionVariable sumRoomVolumeExpVar;
            
            //temp structure that holds 
            roomVolumeExpVars=(IntegerExpressionVariable[])housesToBelongingRoomVolumeExpVars
                    .get(tempChocoHouse.getHouseIndividualHash())
                        .toArray(new IntegerExpressionVariable[0]);
            logger.info("........................................................................177");
            logger.info(roomVolumeExpVars.length);
            
            //the sum method can accept as an orisma an array of appropriate elements
            //so we feed it with an array of roomVolumeExpVars, which sum adds one by one.
            sumRoomVolumeExpVar=sum(roomVolumeExpVars);
            //creating the volume equality constraint between the house and its rooms
            Constraint houseVolumeCompactnessConstraint=eq(sumRoomVolumeExpVar, tempChocoHouseVolumeExpVar);
            //adding the miraculous constraint to our model!!
            this.topIxModel.addConstraint(houseVolumeCompactnessConstraint);
        }
    }
    
    public void initialiseChocoHouseResVars() {
        for (ChocoHouse tempChocoHouse:chocoHouseMap.values()) {
            tempChocoHouse.setHouseLengthRes(topIxSolver.getVar(tempChocoHouse.getHouseLengthVar()));
            logger.info(".......................................189");
            logger.info(topIxSolver.getVar(tempChocoHouse.getHouseLengthVar()).getVal());
            tempChocoHouse.setHouseWidthRes(topIxSolver.getVar(tempChocoHouse.getHouseWidthVar()));
            logger.info(topIxSolver.getVar(tempChocoHouse.getHouseWidthVar()).getVal());
            tempChocoHouse.setHouseHeightRes(topIxSolver.getVar(tempChocoHouse.getHouseHeightVar()));
            logger.info(tempChocoHouse.getHouseLengthRes().getVal());
            logger.info(tempChocoHouse.getHouseWidthRes().getVal());
            
            tempChocoHouse.setHouseXRes(topIxSolver.getVar(tempChocoHouse.getHouseXVar()));
            logger.info(topIxSolver.getVar(tempChocoHouse.getHouseXVar()).getVal());
            tempChocoHouse.setHouseYRes(topIxSolver.getVar(tempChocoHouse.getHouseYVar()));
            logger.info(topIxSolver.getVar(tempChocoHouse.getHouseYVar()).getVal());
            logger.info(".................................END OF 189");
            tempChocoHouse.setHouseZRes(topIxSolver.getVar(tempChocoHouse.getHouseZVar()));
            IntDomainVar tempHouseXvar=topIxSolver.getVar(tempChocoHouse.getHouseXVar());
            if (tempHouseXvar.isInstantiated()) logger.info("einai instantiated to X res");
        }
    }
   
    
    
    public void initialiseChocoRoomResVars() {
        for (ChocoRoom tempChocoRoom:chocoRoomMap.values()) {
            logger.info("------------------......................------------------215");
            tempChocoRoom.setRoomLengthRes(topIxSolver.getVar(tempChocoRoom.getRoomLengthVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomLengthVar()).getVal());
            tempChocoRoom.setRoomWidthRes(topIxSolver.getVar(tempChocoRoom.getRoomWidthVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomWidthVar()).getVal());
            tempChocoRoom.setRoomHeightRes(topIxSolver.getVar(tempChocoRoom.getRoomHeightVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomHeightVar()).getVal());
            
            tempChocoRoom.setRoomXRes(topIxSolver.getVar(tempChocoRoom.getRoomXVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomXVar()).getVal());
            tempChocoRoom.setRoomYRes(topIxSolver.getVar(tempChocoRoom.getRoomYVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomYVar()).getVal());
            tempChocoRoom.setRoomZRes(topIxSolver.getVar(tempChocoRoom.getRoomZVar()));
            logger.info(topIxSolver.getVar(tempChocoRoom.getRoomZVar()).getVal());
            logger.info("------------------......................--------END OF----215");
        }
    }
    
    public void clearAllModelVariables(){
        int varPointer=this.topIxModel.getNbTotVars();
        for(int i=0; i<varPointer; i++) {
            this.topIxModel.getIntVar(i);
        }
    }
    
    public void reinitializeModel(){
        this.topIxModel=new CPModel();
    }
    
    public Map<String, ChocoHouse> getChocoHouseMap() {
        return chocoHouseMap;
    }

    public void setChocoHouseMap(Map<String, ChocoHouse> chocoHouseMap) {
        this.chocoHouseMap = chocoHouseMap;
    }

    public Map<String, ChocoRoom> getChocoRoomMap() {
        return chocoRoomMap;
    }

    public void setChocoRoomMap(Map<String, ChocoRoom> chocoRoomMap) {
        this.chocoRoomMap = chocoRoomMap;
    }
    
    public CPSolver getTopIxSolver() {
        return topIxSolver;
        
    }
    public CPModel getTopIxModel() {
        return topIxModel;
    }
    
    public void setTopIxModel(CPModel topIxModel) {
        this.topIxModel = topIxModel;
    }
}