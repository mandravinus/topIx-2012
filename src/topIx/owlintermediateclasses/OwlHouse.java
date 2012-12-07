package topIx.owlintermediateclasses;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class OwlHouse {

    static Logger logger = Logger.getLogger(OwlHouse.class.getName());
    //the following map contains the number of houses that each site 
    //(in the way the application is implemented only one site resides
    //in the map each time the application runs)
    //contains within its bounds.
    //RECOMMENDATION-30.11.12-THE FOLLOWING STRUCTURE SHOULD BE MOVED TO THE 
    //SITE CLASS AND BE CHANGED TO NON STATIC!!!
    private static Map<String, Integer> houseInstancesPerSite = new HashMap<>();
    //selectedHouseEntry is updated from the roomsTree valueChanged handler!!!
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

    //augments the house index each time a new house entity is inserted in the
    //current arrangement.
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
    
    public static void resetHouseIndex(String siteName){
        houseInstancesPerSite.put(siteName, 0);
    }

    //this is used when asserting a new house in the ontology.
    public String returnHouseIndividualHash(String siteName, Integer index) {
        //returns hash value of a string formatted as: "siteName_house_xx"
        String returnString;
        returnString = String.format("%1$s_house_%2$02d", siteName, index.intValue());
        return (String.valueOf(returnString.hashCode()));
    }
    
    //!!!!WHEN A HOUSE ENTITY IS SELECTED IN THE TREE, THE FOLLOWING METHOD RETURNS
    //A VALID HASHCODE FOR THE SELECTED HOUSE FOR USAGE IN THE CHOCOHOUSEMAP RETRIEVAL
    //METHOD!!!!!
    public String returnHouseIndividualHash(String siteName, String selectedHouseEntry) {
        String returnString;
        returnString=String.format("%1$s_%2$s", siteName, selectedHouseEntry);
        return String.valueOf(returnString.hashCode());
    }
    
    //NOT USED!!!
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