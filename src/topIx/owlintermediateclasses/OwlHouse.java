package topIx.owlintermediateclasses;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class OwlHouse {

    static Logger logger = Logger.getLogger(OwlHouse.class.getName());
    private static Map<String, Integer> houseInstancesPerSite = new HashMap<>();
    private String selectedHouseEntry;
    private int currentHouseHeight;
    
    public OwlHouse(String currentSiteName) {
        //this.currentHouseName=currentHouse;
    }

    public static Integer readHouseIndex(String siteName) {
        if (houseInstancesPerSite.containsKey(siteName))
            return new Integer(houseInstancesPerSite.get(siteName));
        else
            return new Integer(-1); 
   }

    public static Integer augmentHouseIndex(String siteName) {
        int counter = 1;

        if (readHouseIndex(siteName)>0) {
            counter = houseInstancesPerSite.get(siteName);
            logger.info(counter);
            houseInstancesPerSite.put(siteName, new Integer(++counter));
            logger.info(counter);
        } else {
            houseInstancesPerSite.put(siteName, new Integer(1));
        }
        return new Integer(counter);
    }

    public String returnHouseIndividualHash(String siteName, Integer index) {
        //returns hash value of a string formatted as: "siteName_house_xx"
        String returnString;
        returnString = String.format("%1$s_house_%2$02d", siteName, index.intValue());
        return (String.valueOf(returnString.hashCode()));
    }
    
    public String returnHouseIndividualHash(String siteName, String selectedHouseEntry) {
        String returnString;
        returnString=String.format("%1$s_%2$s", siteName, selectedHouseEntry);
        return String.valueOf(returnString.hashCode());
    }
    
    public String returnHouseIndividual(String siteName, Integer index) {
        //returns "siteName_house_xx"
        return String.format("%1$s_house_%3$02d", siteName, index.intValue());
    }

    public String returnHouseIndividualAnnotation(String siteName, Integer index) {
        //returns "house #xx of siteName";
        return String.format("house #%1$02d of %2$s .", index.intValue(), siteName);
    }

    public String returnHouseEntryInJTree(Integer index) {
        //returns string formatted as "house_xx"
        return String.format("house_%1$02d", index);
    }

    public String getSelectedHouseEntry() {
        return selectedHouseEntry;
    }

    public void setSelectedHouseEntry(String selectedHouseEntry) {
        this.selectedHouseEntry = selectedHouseEntry;
    }

    public int getCurrentHouseHeight() {
        return currentHouseHeight;
    }

    public void setCurrentHouseHeight(int currenHouseHeight) {
        this.currentHouseHeight = currenHouseHeight;
    }
    
}