/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chocosolution;

/**
 *
 * @author Mandravinos
 */
public class ChocoSite {
    
    private int siteLengthConst;
    private int siteWidthConst;
    
    public ChocoSite() {
        this.siteLengthConst=0;
        this.siteWidthConst=0;
    }
    
    public ChocoSite(int siteLength, int siteWidth) {
        this.siteLengthConst=siteLength;
        this.siteWidthConst=siteWidth;
    }

    public int getSiteLengthConst() {
        return siteLengthConst;
    }

    public void setSiteLengthConst(int siteLengthConst) {
        this.siteLengthConst = siteLengthConst;
    }

    public int getSiteWidthConst() {
        return siteWidthConst;
    }

    public void setSiteWidthConst(int siteWidthConst) {
        this.siteWidthConst = siteWidthConst;
    }
}