package chocosolution;

import static choco.Choco.*;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.variables.integer.IntDomainVar;
import topIx.owlintermediateclasses.OwlSite;

import org.apache.log4j.Logger;

public class ChocoHouse {
    private String houseIndividualHash;
    private String houseIndividualLiteral;
    
    private IntegerVariable houseLengthVar;
    private IntegerVariable houseWidthVar;
    //private IntegerVariable houseHeightVar;

    private IntegerVariable houseXVar;
    private IntegerVariable houseYVar;
    
    private IntDomainVar houseLengthRes;
    private IntDomainVar houseWidthRes;
    //private IntDomainVar houseHeightRes;
    
    private IntDomainVar houseXRes;
    private IntDomainVar houseYRes;
        
    private Logger logger=Logger.getLogger(ChocoHouse.class.getName());
    
    public ChocoHouse(OwlSite currentSite, String houseIndividualHash, String houseIndividualLiteral) {
        this.houseIndividualHash=houseIndividualHash;
        this.houseIndividualLiteral=houseIndividualLiteral;
        
        houseLengthVar=makeIntVar(houseIndividualHash+"_l", 1, currentSite.getSiteLength());
        houseWidthVar=makeIntVar(houseIndividualHash+"_w", 1, currentSite.getSiteWidth());
        
        houseXVar=makeIntVar(houseIndividualHash+"_x", 0, currentSite.getSiteLength());
        houseYVar=makeIntVar(houseIndividualHash+"_y", 0, currentSite.getSiteWidth());
    }

    public IntDomainVar getHouseXRes() {
        return houseXRes;
    }

    public void setHouseXRes(IntDomainVar houseXRes) {
        this.houseXRes = houseXRes;
    }

    public IntegerVariable getHouseXVar() {
        return houseXVar;
    }

    public void setHouseXVar(IntegerVariable houseXVar) {
        this.houseXVar = houseXVar;
    }

    public IntDomainVar getHouseYRes() {
        return houseYRes;
    }

    public void setHouseYRes(IntDomainVar houseYRes) {
        this.houseYRes = houseYRes;
    }

    public IntegerVariable getHouseYVar() {
        return houseYVar;
    }

    public void setHouseYVar(IntegerVariable houseYVar) {
        this.houseYVar = houseYVar;
    }

    public String getHouseIndividualHash() {
        return houseIndividualHash;
    }

    public void setHouseIndividualHash(String houseIndividualHash) {
        this.houseIndividualHash = houseIndividualHash;
    }

    public String getHouseIndividualLiteral() {
        return houseIndividualLiteral;
    }

    public void setHouseIndividualLiteral(String houseIndividualLiteral) {
        this.houseIndividualLiteral = houseIndividualLiteral;
    }

    public IntDomainVar getHouseLengthRes() {
        return houseLengthRes;
    }

    public void setHouseLengthRes(IntDomainVar houseLengthRes) {
        this.houseLengthRes = houseLengthRes;
    }

    public IntegerVariable getHouseLengthVar() {
        return houseLengthVar;
    }

    public void setHouseLengthVar(IntegerVariable houseLengthVar) {
        this.houseLengthVar = houseLengthVar;
    }

    public IntDomainVar getHouseWidthRes() {
        return houseWidthRes;
    }

    public void setHouseWidthRes(IntDomainVar houseWidthRes) {
        this.houseWidthRes = houseWidthRes;
    }

    public IntegerVariable getHouseWidthVar() {
        return houseWidthVar;
    }

    public void setHouseWidthVar(IntegerVariable houseWidthVar) {
        this.houseWidthVar = houseWidthVar;
    }
}