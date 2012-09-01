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
    
    public void insertHouseCompactnessConstraints() {
        //the following map will accommodate arraylists of integerExpressionVars, listing the area expression variables that are members
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
            IntegerExpressionVariable tempChocoHouseAreaExpVar=mult(tempChocoHouse.getHouseLengthVar(), tempChocoHouse.getHouseWidthVar());
            IntegerExpressionVariable[] RoomAreaExpVars;
            IntegerExpressionVariable sumRoomAreaExpVar;
            
            RoomAreaExpVars=(IntegerExpressionVariable[])housesToBelongingRoomAreaExpVars
                .get(tempChocoHouse.getHouseIndividualHash())
                    .toArray(new IntegerExpressionVariable[0]);
            
            sumRoomAreaExpVar=sum(RoomAreaExpVars);
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
    
    public void initialiseChocoHouseResVars() {
        for (ChocoHouse tempChocoHouse:chocoHouseMap.values()) {
            tempChocoHouse.setHouseLengthRes(topIxSolver.getVar(tempChocoHouse.getHouseLengthVar()));
            tempChocoHouse.setHouseWidthRes(topIxSolver.getVar(tempChocoHouse.getHouseWidthVar()));
            logger.info(tempChocoHouse.getHouseLengthRes().getVal());
            logger.info(tempChocoHouse.getHouseWidthRes().getVal());
            
            tempChocoHouse.setHouseXRes(topIxSolver.getVar(tempChocoHouse.getHouseXVar()));
            tempChocoHouse.setHouseYRes(topIxSolver.getVar(tempChocoHouse.getHouseYVar()));
            logger.info("next are the getvals directly from inside the solver");
            IntDomainVar tempHouseXvar=topIxSolver.getVar(tempChocoHouse.getHouseXVar());
            if (tempHouseXvar.isInstantiated()) logger.info("einai instantiated to X res");
            logger.info(topIxSolver.getVar(tempChocoHouse.getHouseXVar()).getVal());
            logger.info("next are the getVal's after the supposed initialisation");
            logger.info(tempChocoHouse.getHouseXRes().getVal());
            logger.info(tempChocoHouse.getHouseYRes().getVal());
        }
    }
    
    public void initialiseChocoRoomResVars() {
        for (ChocoRoom tempChocoRoom:chocoRoomMap.values()) {
            tempChocoRoom.setRoomLengthRes(topIxSolver.getVar(tempChocoRoom.getRoomLengthVar()));
            tempChocoRoom.setRoomWidthRes(topIxSolver.getVar(tempChocoRoom.getRoomWidthVar()));
            tempChocoRoom.setRoomHeightRes(topIxSolver.getVar(tempChocoRoom.getRoomHeightVar()));
            
            tempChocoRoom.setRoomXRes(topIxSolver.getVar(tempChocoRoom.getRoomXVar()));
            tempChocoRoom.setRoomYRes(topIxSolver.getVar(tempChocoRoom.getRoomYVar()));
            tempChocoRoom.setRoomZRes(topIxSolver.getVar(tempChocoRoom.getRoomZVar()));
        }
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