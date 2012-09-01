package topIx.owlintermediateclasses;

import java.util.HashMap;
import java.util.Map;
//import topIx.intermediateclasses.*;

public class OwlSite /*extends Site*/ {
    private String siteName;
    private int siteLength;
    private int siteWidth;
    private Map<String, Integer> houseHeightMap;
    
    public OwlSite(String siteName, int siteLength, int siteWidth) {
        //super(siteName, siteLength, siteWidth, siteHeight);
        this.siteName=siteName;
        this.siteLength=siteLength;
        this.siteWidth=siteWidth;
        houseHeightMap=new HashMap<>();
    }

    public int getSiteLength() {
        return siteLength;
    }

    public void setSiteLength(int siteLength) {
        this.siteLength = siteLength;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public int getSiteWidth() {
        return siteWidth;
    }

    public void setSiteWidth(int siteWidth) {
        this.siteWidth = siteWidth;
    }
    
    //method poy tha kalei ton siteID getter
    //public String getSiteName() {
    //    return this.siteName;
    //}
    
    //method poy epistrefei orisma katallhlo gia Site entity assertion sthn ontologia.
    public String returnSiteNameHash() {
        return String.valueOf(this.siteName.hashCode());
    }
    
    //method poy epistrefei orisma katallhlo gia Root entity sto JTree.
    public String returnSiteNameCompact() {
        if (siteName.length()>20)
            return (siteName.substring(0, 10)+"..."+siteName.substring((siteName.length()-6), siteName.length()));
        else
            return (siteName);
    }
    
    //method poy epistrefei orisma katallhlo gia site entity annotation assertion
    public String returnSiteNameAnnotation() {
        return siteName;
    }

    public Map<String, Integer> getHouseHeightMap() {
        return houseHeightMap;
    }

    public void setHouseHeightMap(Map<String, Integer> houseHeightMap) {
        this.houseHeightMap = houseHeightMap;
    }
}