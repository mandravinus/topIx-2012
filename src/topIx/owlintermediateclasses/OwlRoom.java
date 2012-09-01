package topIx.owlintermediateclasses;

import java.util.*;
import java.util.Map.Entry;

public class OwlRoom {
    
    private static Map<String, Map<String, Integer>> roomInstanceCounters = new HashMap<>();
    public static String DRONEHASH=String.valueOf(new String("droneHash").hashCode());
    private String selectedRoomEntry;   //currently selected room entry in the JTree in order to assert a relationship with another room entity"roomName_xx"
    private String comboBoxRoomEntry;   //roomName selected at this moment in the rooms1 list in order to feed the assert room individual "roomName"
private String comboBox2RoomEntry;  //roomName selected at this moment in the rooms2 list in order to assert an object property instance that involves two room individuals.


    public OwlRoom() {
        this.selectedRoomEntry=null;
        this.comboBoxRoomEntry=null;
        this.comboBox2RoomEntry=null;
    }
    
    public OwlRoom(String comboBoxEntry) {
        this.selectedRoomEntry=null;
        this.comboBoxRoomEntry=comboBoxEntry;
        this.comboBox2RoomEntry=null;
    }

    public static Integer readRoomIndex(String siteName_House_xx, String selectedRoomEntry) {
        if (roomInstanceCounters.containsKey(siteName_House_xx)) {
            if (roomInstanceCounters.get(siteName_House_xx).containsKey(selectedRoomEntry)) {
                return roomInstanceCounters.get(siteName_House_xx).get(selectedRoomEntry);
            }
            else {
                return new Integer(-2);
            }
        }
        else {
            return new Integer(-1);
        }
    }

    public static Integer augmentRoomIndex(String siteName, String selectedHouseEntry, String comboBoxRoomEntry) {
        int counter=1;
        String siteName_House_xx=String.format("%1$s_%2$s", siteName, selectedHouseEntry);
        if (readRoomIndex(siteName_House_xx, comboBoxRoomEntry).intValue()==-1) {
            roomInstanceCounters.put(siteName_House_xx, new HashMap<String, Integer>());
            roomInstanceCounters.get(siteName_House_xx).put(comboBoxRoomEntry, new Integer(1));
            return new Integer(1);
        }
        else if (readRoomIndex(siteName_House_xx, comboBoxRoomEntry).intValue()==-2) {
            roomInstanceCounters.get(siteName_House_xx).put(comboBoxRoomEntry, new Integer(1));
            return new Integer(1);
        }
        else {
            counter=roomInstanceCounters.get(siteName_House_xx).get(comboBoxRoomEntry).intValue();
            roomInstanceCounters.get(siteName_House_xx).put(comboBoxRoomEntry, new Integer(++counter));
            return counter;
        }
    }
    
    public String returnRoomIndividualHash(String siteName, String selectedHouseEntry) {
        //returns hash code of "siteName_house_xx_selectedRoomName_xx"
        String returnString;
        returnString=String.format("%1$s_%2$s_%3$s_%4$02d",
                siteName,
                selectedHouseEntry,
                this.comboBoxRoomEntry,
                readRoomIndex(String.format("%1$s_%2$s", siteName, selectedHouseEntry),
                    this.comboBoxRoomEntry));
        return String.valueOf(returnString.hashCode());
    }
    
    public String returnRoomIndividualHash(String siteName, String selectedHouseEntry, String indexedRoomEntry) {   //indexed room entry either
                                                                                                                    //refers to tree selected room
                                                                                                                    //or rooms2 combo selected item.
        String returnString=String.format("%1$s_%2$s_%3$s", siteName, selectedHouseEntry, indexedRoomEntry);
        return String.valueOf(returnString.hashCode());
    }
    
    public String returnRoomIndividualAnnotation(String siteName, String selectedHouseEntry) {
        //returns "room #xx of house #xx of siteName"
        String siteName_House_xx=String.format("%1$s_%2$s", siteName, selectedHouseEntry);
        return String.format("%1$s #%2$02d of house #%3$02d of %4$s",
                this.comboBoxRoomEntry,
                readRoomIndex(siteName_House_xx,
                    this.comboBoxRoomEntry),
                Integer.parseInt(selectedHouseEntry.substring(selectedHouseEntry.length()-2)),
                siteName);
    }
    
    //returns the expression of the room entry that is used in the jtree and the rooms2 combo. it represents NOT room classes but room individuals.
    public String returnRoomEntryInGui(String siteName, String selectedHouseEntry) {
        //returns "room_xx"
        return String.format("%1$s_%2$02d",
            this.comboBoxRoomEntry,
            readRoomIndex(String.format("%1$s_%2$s", siteName,selectedHouseEntry),
                this.comboBoxRoomEntry));
    }

    public String getComboBoxRoomEntry() {
        return comboBoxRoomEntry;
    }

    public void setComboBoxRoomEntry(String comboBoxRoomEntry) {
        this.comboBoxRoomEntry = comboBoxRoomEntry;
    }

    public String getSelectedRoomEntry() {
        return selectedRoomEntry;
    }

    public void setSelectedRoomEntry(String selectedRoomEntry) {
        this.selectedRoomEntry = selectedRoomEntry;
    }

    public String getComboBox2RoomEntry() {
        return comboBox2RoomEntry;
    }

    public void setComboBox2RoomEntry(String comboBox2RoomEntry) {
        this.comboBox2RoomEntry = comboBox2RoomEntry;
    }
    
}