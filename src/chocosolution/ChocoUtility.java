/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chocosolution;

import static choco.Choco.*;
import choco.cp.model.CPModel;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.model.constraints.Constraint;
import topIx.owlintermediateclasses.OwlSite;

/**
 *
 * @author Mandravinos
 */
public class ChocoUtility {

//    public ChocoUtility() {
//        
//    }
    public static void insertHouseVariables(ChocoHouse chocoHouse, CPModel model) {
        model.addVariable(chocoHouse.getHouseLengthVar());
        model.addVariable(chocoHouse.getHouseWidthVar());
        
        model.addVariable(chocoHouse.getHouseXVar());
        model.addVariable(chocoHouse.getHouseYVar());
    }

    public static void insertRoomVariables(ChocoRoom room, CPModel model) {
        model.addVariable(room.getRoomLengthVar());
        model.addVariable(room.getRoomWidthVar());
        model.addVariable(room.getRoomHeightVar());
        model.addVariable(room.getRoomXVar());
        model.addVariable(room.getRoomYVar());
        model.addVariable(room.getRoomZVar());
    }

    //the event handler in the gui class will access the roomNameToChocoRoomInstance map and it shall retrieve
    //the corresponding ChocoRoom object(s) that will be passed as argument(s) in all the constraints.
    public static void adjacentNorthConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room1.getRoomXVar(), minus(room2.getRoomXVar(), room1.getRoomLengthVar())));
        model.addConstraint(leq(room1.getRoomXVar(), plus(room2.getRoomXVar(), room2.getRoomLengthVar())));

        model.addConstraint(geq(room1.getRoomZVar(), minus(room2.getRoomZVar(), room1.getRoomHeightVar())));
        model.addConstraint(leq(room1.getRoomZVar(), plus(room2.getRoomZVar(), room2.getRoomHeightVar())));

        model.addConstraint(eq(room1.getRoomYVar(), plus(room2.getRoomYVar(), room2.getRoomWidthVar())));
    }

    public static void adjacentSouthConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room2.getRoomXVar(), minus(room1.getRoomXVar(), room2.getRoomLengthVar())));
        model.addConstraint(leq(room2.getRoomXVar(), plus(room1.getRoomXVar(), room1.getRoomLengthVar())));

        model.addConstraint(geq(room2.getRoomZVar(), minus(room1.getRoomZVar(), room2.getRoomHeightVar())));
        model.addConstraint(leq(room2.getRoomZVar(), plus(room1.getRoomZVar(), room1.getRoomHeightVar())));

        model.addConstraint(eq(room1.getRoomYVar(), plus(room1.getRoomYVar(), room1.getRoomWidthVar())));
    }

    public static void adjacentWestConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room1.getRoomYVar(), minus(room2.getRoomYVar(), room1.getRoomWidthVar())));
        model.addConstraint(leq(room1.getRoomYVar(), plus(room2.getRoomYVar(), room2.getRoomWidthVar())));

        model.addConstraint(geq(room1.getRoomZVar(), minus(room2.getRoomZVar(), room1.getRoomHeightVar())));
        model.addConstraint(leq(room1.getRoomZVar(), plus(room2.getRoomZVar(), room2.getRoomHeightVar())));

        model.addConstraint(eq(room1.getRoomXVar(), minus(room2.getRoomXVar(), room1.getRoomLengthVar())));
    }

    public static void adjacentEastConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room2.getRoomYVar(), minus(room1.getRoomYVar(), room2.getRoomWidthVar())));
        model.addConstraint(leq(room2.getRoomYVar(), plus(room1.getRoomYVar(), room1.getRoomWidthVar())));

        model.addConstraint(geq(room2.getRoomZVar(), minus(room1.getRoomZVar(), room2.getRoomHeightVar())));
        model.addConstraint(leq(room2.getRoomZVar(), plus(room1.getRoomZVar(), room1.getRoomHeightVar())));

        model.addConstraint(eq(room2.getRoomXVar(), minus(room1.getRoomXVar(), room2.getRoomLengthVar())));
    }

    public static void adjacentOverConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room1.getRoomXVar(), minus(room2.getRoomXVar(), room1.getRoomLengthVar())));
        model.addConstraint(leq(room1.getRoomXVar(), plus(room2.getRoomXVar(), room2.getRoomLengthVar())));

        model.addConstraint(geq(room1.getRoomYVar(), minus(room2.getRoomYVar(), room1.getRoomWidthVar())));
        model.addConstraint(leq(room1.getRoomYVar(), plus(room2.getRoomYVar(), room2.getRoomWidthVar())));

        model.addConstraint(eq(room1.getRoomZVar(), plus(room2.getRoomZVar(), room2.getRoomHeightVar())));
    }

    public static void adjacentUnderConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(geq(room2.getRoomXVar(), minus(room1.getRoomXVar(), room2.getRoomLengthVar())));
        model.addConstraint(leq(room2.getRoomXVar(), plus(room1.getRoomXVar(), room1.getRoomLengthVar())));

        model.addConstraint(geq(room2.getRoomYVar(), minus(room1.getRoomYVar(), room2.getRoomWidthVar())));
        model.addConstraint(leq(room2.getRoomYVar(), plus(room1.getRoomYVar(), room1.getRoomWidthVar())));

        model.addConstraint(eq(room2.getRoomZVar(), plus(room1.getRoomZVar(), room1.getRoomHeightVar())));
    }

    public static void equalLengthConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(eq(room1.getRoomLengthVar(), room2.getRoomLengthVar()));
    }

    public static void equalWidthConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(eq(room1.getRoomWidthVar(), room2.getRoomWidthVar()));
    }

    public static void equalHeightConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(eq(room1.getRoomHeightVar(), room2.getRoomHeightVar()));
    }

    //the following can be used also as shorter-than with inversed argument order
    public static void longerThanConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(gt(room1.getRoomLengthVar(), room2.getRoomLengthVar()));
    }

    //the following can be used also as narrower-than with inversed argument order
    public static void widerThanConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(gt(room1.getRoomWidthVar(), room2.getRoomWidthVar()));
    }

    //the following can be used also as lower-than with inversed argument order
    public static void higherThanConstraint(ChocoRoom room1, ChocoRoom room2, CPModel model) {
        model.addConstraint(gt(room1.getRoomHeightVar(), room2.getRoomHeightVar()));
    }

    public static void higherThanLongConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomHeightVar(), room.getRoomLengthVar()));
    }

    public static void higherThanWideConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomHeightVar(), room.getRoomWidthVar()));
    }

    public static void longerThanHighConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomLengthVar(), room.getRoomHeightVar()));
    }

    public static void longerThanWideConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomLengthVar(), room.getRoomWidthVar()));
    }

    public static void widerThanHighConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomWidthVar(), room.getRoomHeightVar()));
    }

    public static void widerThanLongConstraint(ChocoRoom room, CPModel model) {
        model.addConstraint(gt(room.getRoomWidthVar(), room.getRoomLengthVar()));
    }

    public static void positionXisConstraint(ChocoRoom room, int x, CPModel model) {
        room.getRoomXVar().setLowB(x);
        room.getRoomXVar().setUppB(x);
    }

    public static void positionYisConstraint(ChocoRoom room, int y, CPModel model) {
        room.getRoomYVar().setLowB(y);
        room.getRoomYVar().setUppB(y);
    }

    public static void positionZisConstraint(ChocoRoom room, int z, CPModel model) {
        room.getRoomZVar().setLowB(z);
        room.getRoomZVar().setUppB(z);
    }

    public static void lengthIsConstraint(ChocoRoom room, int l, CPModel model) {
        room.getRoomLengthVar().setLowB(l);
        room.getRoomLengthVar().setUppB(l);
    }

    public static void widthIsConstraint(ChocoRoom room, int w, CPModel model) {
        room.getRoomWidthVar().setLowB(w);
        room.getRoomWidthVar().setUppB(w);
    }

    public static void heightIsConstraint(ChocoRoom room, int h, CPModel model) {
        room.getRoomHeightVar().setLowB(h);
        room.getRoomHeightVar().setUppB(h);
    }

    public static void houseIsPartOfSiteConstraint(OwlSite currentSite, ChocoHouse chocoHouse, CPModel model) {
        model.addConstraint(geq(chocoHouse.getHouseXVar(), 0));
        model.addConstraint(geq(chocoHouse.getHouseYVar(), 0));
        
        model.addConstraint(leq(sum(chocoHouse.getHouseXVar(), chocoHouse.getHouseLengthVar()), currentSite.getSiteLength()));
        model.addConstraint(leq(sum(chocoHouse.getHouseYVar(), chocoHouse.getHouseWidthVar()), currentSite.getSiteWidth()));
    }
    
    public static void roomIsPartOfHouseConstraint(ChocoHouse chocoHouse, ChocoRoom chocoRoom, CPModel model) {
        model.addConstraint(geq(chocoRoom.getRoomXVar(), chocoHouse.getHouseXVar()));
        model.addConstraint(geq(chocoRoom.getRoomYVar(), chocoHouse.getHouseYVar()));
        
        model.addConstraint(leq(sum(chocoRoom.getRoomXVar(), chocoRoom.getRoomLengthVar()), (sum(chocoHouse.getHouseXVar(), chocoHouse.getHouseLengthVar()))));
        model.addConstraint(leq(sum(chocoRoom.getRoomYVar(), chocoRoom.getRoomWidthVar()), (sum(chocoHouse.getHouseYVar(), chocoHouse.getHouseWidthVar()))));
    }
    
    public static void nonOverlappingHousesConstraint(ChocoHouse lhsHouse, ChocoHouse rhsHouse, CPModel model) {
        model.addConstraint(or(
                geq(lhsHouse.getHouseXVar(), (sum(rhsHouse.getHouseXVar(), rhsHouse.getHouseLengthVar()))),
                geq(rhsHouse.getHouseXVar(), (sum(lhsHouse.getHouseXVar(), lhsHouse.getHouseLengthVar()))),
                geq(lhsHouse.getHouseYVar(), (sum(rhsHouse.getHouseYVar(), rhsHouse.getHouseWidthVar()))),
                geq(rhsHouse.getHouseYVar(), (sum(lhsHouse.getHouseYVar(), lhsHouse.getHouseWidthVar())))));
    }
    
    public static void nonOverlappingRoomsConstraint(ChocoRoom lhsRoom, ChocoRoom rhsRoom, CPModel model) {
        model.addConstraint(or(
                geq(lhsRoom.getRoomXVar(), (sum(rhsRoom.getRoomXVar(), rhsRoom.getRoomLengthVar()))),
                geq(rhsRoom.getRoomXVar(), (sum(lhsRoom.getRoomXVar(), lhsRoom.getRoomLengthVar()))),
                geq(lhsRoom.getRoomYVar(), (sum(rhsRoom.getRoomYVar(), rhsRoom.getRoomWidthVar()))),
                geq(rhsRoom.getRoomYVar(), (sum(lhsRoom.getRoomYVar(), lhsRoom.getRoomWidthVar()))),
                geq(lhsRoom.getRoomZVar(), (sum(rhsRoom.getRoomZVar(), rhsRoom.getRoomHeightVar()))),
                geq(rhsRoom.getRoomZVar(), (sum(lhsRoom.getRoomZVar(), lhsRoom.getRoomHeightVar())))
                ));
    }
    
//    public static void compactingRoomsConstraint
}