package chocosolution;

import static choco.Choco.*;
import choco.kernel.model.variables.integer.IntegerConstantVariable;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.variables.integer.IntDomainVar;

import topIx.owlintermediateclasses.*;

public class ChocoRoom {
    private String roomIndividualHash;
    private String roomIndividualLiteral;
    private String roomIsPartOfHouse;
    
    private IntegerVariable roomLengthVar;
    private IntegerVariable roomWidthVar;
    private IntegerVariable roomHeightVar; //actually a CONSTANT, equal to the FIXED HOUSE HEIGHT.
    
    private IntegerVariable roomXVar;
    private IntegerVariable roomYVar;
    private IntegerVariable roomZVar;
    
    private IntegerExpressionVariable roomAreaExpressionVariable;
    private IntegerExpressionVariable roomVolumeExpressionVariable;
    
    private IntDomainVar roomLengthRes;
    private IntDomainVar roomWidthRes;
    private IntDomainVar roomHeightRes;
    
    private IntDomainVar roomXRes;
    private IntDomainVar roomYRes;
    private IntDomainVar roomZRes;
    
    public ChocoRoom(String roomIndividualHash, String roomIndividualLiteral, OwlSite currentSite, OwlHouse currentHouse) {
        this.roomIndividualHash=roomIndividualHash;
        this.roomIndividualLiteral=roomIndividualLiteral;
        
        this.roomLengthVar=makeIntVar(roomIndividualHash+"_l", 1, currentSite.getSiteLength());
        this.roomWidthVar=makeIntVar(roomIndividualHash+"_w", 1, currentSite.getSiteWidth());
        this.roomHeightVar=makeIntVar(roomIndividualHash+"_h", currentHouse.getCurrentHouseHeight(), currentHouse.getCurrentHouseHeight());
        
        this.roomXVar=makeIntVar(roomIndividualHash+"_x", 0, currentSite.getSiteLength());
        this.roomYVar=makeIntVar(roomIndividualHash+"_y", 0, currentSite.getSiteWidth());
        this.roomZVar=makeIntVar(roomIndividualHash+"_z", 0, MAX_UPPER_BOUND, "cp:bound");
        
        this.roomAreaExpressionVariable=mult(this.roomLengthVar, this.roomWidthVar);
        this.roomVolumeExpressionVariable=mult(this.roomLengthVar, mult(this.roomWidthVar, this.roomHeightVar));
        this.roomIsPartOfHouse=currentHouse.returnHouseIndividualHash(currentSite.getSiteName(), currentHouse.getSelectedHouseEntry());
    }

    public IntDomainVar getRoomHeightRes() {
        return roomHeightRes;
    }

    public void setRoomHeightRes(IntDomainVar roomHeightRes) {
        this.roomHeightRes = roomHeightRes;
    }

    public IntegerVariable getRoomHeightVar() {
        return roomHeightVar;
    }

    public void setRoomHeightVar(IntegerConstantVariable roomHeightVar) {
        this.roomHeightVar = roomHeightVar;
    }

    public String getRoomIndividualHash() {
        return roomIndividualHash;
    }

    public void setRoomIndividualHash(String roomIndividualHash) {
        this.roomIndividualHash = roomIndividualHash;
    }

    public String getRoomIndividualLiteral() {
        return roomIndividualLiteral;
    }

    public void setRoomIndividualLiteral(String roomIndividualLiteral) {
        this.roomIndividualLiteral = roomIndividualLiteral;
    }

    public IntDomainVar getRoomLengthRes() {
        return roomLengthRes;
    }

    public void setRoomLengthRes(IntDomainVar roomLengthRes) {
        this.roomLengthRes = roomLengthRes;
    }

    public IntegerVariable getRoomLengthVar() {
        return roomLengthVar;
    }

    public void setRoomLengthVar(IntegerVariable roomLengthVar) {
        this.roomLengthVar = roomLengthVar;
    }

    public IntDomainVar getRoomWidthRes() {
        return roomWidthRes;
    }

    public void setRoomWidthRes(IntDomainVar roomWidthRes) {
        this.roomWidthRes = roomWidthRes;
    }

    public IntegerVariable getRoomWidthVar() {
        return roomWidthVar;
    }

    public void setRoomWidthVar(IntegerVariable roomWidthVar) {
        this.roomWidthVar = roomWidthVar;
    }

    public IntDomainVar getRoomXRes() {
        return roomXRes;
    }

    public void setRoomXRes(IntDomainVar roomXRes) {
        this.roomXRes = roomXRes;
    }

    public IntegerVariable getRoomXVar() {
        return roomXVar;
    }

    public void setRoomXVar(IntegerVariable roomXVar) {
        this.roomXVar = roomXVar;
    }

    public IntDomainVar getRoomYRes() {
        return roomYRes;
    }

    public void setRoomYRes(IntDomainVar roomYRes) {
        this.roomYRes = roomYRes;
    }

    public IntegerVariable getRoomYVar() {
        return roomYVar;
    }

    public void setRoomYVar(IntegerVariable roomYVar) {
        this.roomYVar = roomYVar;
    }

    public IntDomainVar getRoomZRes() {
        return roomZRes;
    }

    public void setRoomZRes(IntDomainVar roomZRes) {
        this.roomZRes = roomZRes;
    }

    public IntegerVariable getRoomZVar() {
        return roomZVar;
    }

    public void setRoomZVar(IntegerVariable roomZVar) {
        this.roomZVar = roomZVar;
    }

    public String getRoomIsPartOfHouse() {
        return roomIsPartOfHouse;
    }

    public void setRoomIsPartOfHouse(String roomIsPartOfHouse) {
        this.roomIsPartOfHouse = roomIsPartOfHouse;
    }

    public IntegerExpressionVariable getRoomAreaExpressionVariable() {
        return roomAreaExpressionVariable;
    }

    public void setRoomAreaExpressionVariable(IntegerExpressionVariable roomAreaExpressionVariable) {
        this.roomAreaExpressionVariable = roomAreaExpressionVariable;
    }

    public IntegerExpressionVariable getRoomVolumeExpressionVariable() {
        return roomVolumeExpressionVariable;
    }

    public void setRoomVolumeExpressionVariable(IntegerExpressionVariable roomVolumeExpressionVariable) {
        this.roomVolumeExpressionVariable = roomVolumeExpressionVariable;
    }
    
}
