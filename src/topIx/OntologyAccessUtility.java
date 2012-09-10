/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topIx;

/**
 *
 * @author Antiregulator
 */
//import t1.AnnotationStrings;
import java.io.BufferedReader;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;

import chocosolution.*;
import topIx.owlintermediateclasses.*;

import org.apache.log4j.Logger;


/*
 * this class to be reconstructed with methods: retrieveAnnotations() which will
 * provide a Set of all the asserted annotation axioms. then, they will be
 * filtered either in the same method, or discrete methods for each data
 * structure used to provide input for the gui elements (combo boxes). the
 * classification should be implemented based on criteria such as the entity
 * type each one annotation is bound to and/or their position in the hierarchy
 * (either class hier or property hier). this task should be accomplished by
 * retrieving also other types of axioms such as subclassofaxioms and
 * correlating the data provided by the annotation value/property etc to the
 * ones provided in the other type axioms.
 */
public class OntologyAccessUtility //implements Runnable
{

    File ontFile;
    OWLOntologyManager manager;
    OWLDataFactory OWLFactory;
    IRI toBeMappedIRI, ontologyIRI;
    OWLOntology topIxOnt;
    OWLOntologyFormat topIxFormat;
    PrefixManager topIxPrefixManager;
    Vector<String> roomsVec;
    Vector<String> objectPropsVec;
    Set<OWLSubClassOfAxiom> subclassOfSet;
    Set<OWLAxiom> allAxioms;
    HashSet<OWLClassExpression> hsStr;
    //the subObjProp map contains the leaf object properties as keys and each ones
    //respective parent as the corresponding value.
    //the structure referenced in retrieveRoomsMap method
    Map<String, String> roomToIRI = new HashMap<>(32);
    Map<String, String> propEntryNametoPropEntryIRI = new HashMap<>(64);
    Map<String, String> propEntryNameToPropCatName = new HashMap<>(64);
    Map<String, IRI> geometricPropertiesMap=new HashMap<>();
    //sets used in deciding the two level object properties hierarchy (leaves, leaves-1) in filterLeafObjProps
    //the data included are in the form of OWLPropertyExpression
    Set<OWLPropertyExpression> set1;    //ends up to contain the leaves
    Set<OWLPropertyExpression> set2;    //ends up to contain the leaves-1 level
    //Sets used in the GUI
    Set<String> roomSet = new HashSet<>(15);
    Set<String> objPropCategories = new HashSet<>(15);
    Set<String> objPropEntries = new HashSet<>(40);       //DONE!!
    
    private List<OwlSolution> solutionsList=new ArrayList<>();
    
    Logger logger;

////    public void run()
////    {
////        loadOntology();
////        retrieveSubObjectPropertyAxioms();
////        retrieveRoomsMap();
////    }
    public OntologyAccessUtility() {
        //edw na klhthoyn oi synarthseis ths sygkekrimenhs klashs me thn provlepomenh seira, oytwswste na mh xreiastei na ginei ayto sth main
        logger=Logger.getLogger(OntologyAccessUtility.class.getName());
        
        this.loadOntology();
        
        this.retrieveSubObjectPropertyAxioms();
        this.retrieveRoomsMap();
        


        /*
         * allAxioms=topIxOnt.getAxioms(); roomsVec=new Vector<String>();
         *
         * retrieveSuperClasses(); fillRoomsVector();
        retrieveAnnotations();
         */


        //filterLeafObjProps();


        //fillGuiSets();
    }

    public void loadOntology() {
        try {
            /*
             * --apaitoymena vimata gia th dhmioyrgia mapped ontologias:
             * 1.dhmioyrgoyme ena toBeMappedIRI me skopo toy thn antistoixish me
             * to local File. 2.dhmioyrgoyme ena File me th dieythynsh ths
             * ontologias sto topiko apothikeytiko meso. 3.dhmioyrgoyme enan
             * SimpleIRIMapper kai ton prosthetoyme mesw toy manager. o
             * SimpleIRIMapper syndeei to toBeMappedIRI poy dwsame arxika, me
             * thn fysikh topothesia ths ontologias sto topiko apothikeytiko
             * meso. 4.dhmioyrgoyme ena ontologyIRI me akrivws thn idia arxiki
             * timh me to toBeMappedIRI to opoio tha einai to IRI ths ontologias
             * mas 5.dhmioyrgoyme thn ontologia mas xrhsimopoiwntas to
             * manager.loadOntology(ontologyIRI);
             */
            String sep = File.separator;

            manager = OWLManager.createOWLOntologyManager();
            toBeMappedIRI = IRI.create("http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl");
            //ontFile = new File("../src/ontologyresources/ptyxiaki_v0.8.owl");
            ontFile=new File("src/ontologyresources/ptyxiaki_v0.8.owl");


            //ontFile=new File("..\\ontologyData\\ptyxiaki_v0.72.owl");
            manager.addIRIMapper(new SimpleIRIMapper(toBeMappedIRI, IRI.create(ontFile)));
            ontologyIRI = IRI.create("http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl");
            topIxOnt = manager.loadOntology(ontologyIRI);
            OWLFactory = manager.getOWLDataFactory();
            topIxFormat = manager.getOntologyFormat(topIxOnt);
            topIxPrefixManager = new DefaultPrefixManager(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#');
            
            System.out.println("loaded ontology: "+this.topIxOnt);
            System.out.println("from: "+this.manager.getOntologyDocumentIRI(this.topIxOnt));
            
        } catch (OWLException oex) {logger.info(oex.getMessage());}
    }

    public void retrieveRoomsMap() {
        //this method retrieves the rooms to be insserted in the two rooms lists.
        //it deposits the retrieved data in a map where the key is the room name (String) and the value is the corresponding entity IRI.
        //the above data structure will be used in passing the entity IRIs in the jess working memory when constructing the facts
        //when the respective room name (String) is selected in the rooms list combo box.

        for (OWLSubClassOfAxiom scoAx : topIxOnt.getSubClassAxiomsForSuperClass(OWLFactory.getOWLClass(IRI.create("http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl#Room")))) {
            String tmpS = scoAx.getSubClass().toString();

            //System.out.println(tmpS.substring(tmpS.indexOf('<')+1, tmpS.indexOf('>')));
            Set<OWLAnnotationAssertionAxiom> tmpAnnSet = topIxOnt.getAnnotationAssertionAxioms(IRI.create(tmpS.substring(tmpS.indexOf('<') + 1, tmpS.indexOf('>'))));
            for (OWLAnnotationAssertionAxiom aaAx : tmpAnnSet) {
                //System.out.println(aaAx.getProperty().toString());
                if (aaAx.getProperty().toString().equals("<http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl#classID>")) {
                    roomToIRI.put(aaAx.getValue().toString().substring(1, aaAx.getValue().toString().indexOf('^') - 1), tmpS.substring(tmpS.indexOf('<') + 1, tmpS.indexOf('>')));
                }
            }
        }
        //System.out.println(roomToIRI);
    }

    public void retrieveSubObjectPropertyAxioms() {
        //this method is to perform a dual operation; it shall retrieve all the declarative object properties categories' annotations,
        //thus filling the objPropCategories Set, while parallelly will, for each category entry retrieve the object properties themselves
        //and adding their entries in the objPropEntries Set.
        Set<OWLSubObjectPropertyOfAxiom> tmpSet;
        OWLObjectPropertyExpression tempDeclarativePropertyClass=OWLFactory.getOWLObjectProperty(":DeclarativeProperty", topIxPrefixManager);
        tmpSet=topIxOnt.getObjectSubPropertyAxiomsForSuperProperty(tempDeclarativePropertyClass);//OWLFactory.getOWLObjectProperty(IRI.create("http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl#DeclarativeProperty")));
        Set<OWLSubObjectPropertyOfAxiom> tmpSet2;

        Set<OWLAnnotationAssertionAxiom> tmpAnnSet1;
        Set<OWLAnnotationAssertionAxiom> tmpAnnSet2;
        //to become class variables.

        for (OWLSubObjectPropertyOfAxiom sopAx : tmpSet) {
            String tmpS = sopAx.getSubProperty().toString();
            //categories...
            tmpAnnSet1 = topIxOnt.getAnnotationAssertionAxioms(IRI.create(tmpS.substring(1, tmpS.indexOf('>')))); //this set only contains one annotation per entry
            for (OWLAnnotationAssertionAxiom aaAx : tmpAnnSet1) {
                String currentObjPropCatName = aaAx.getValue().toString().substring(1, aaAx.getValue().toString().indexOf('^') - 1);
                //System.out.println(currentΟbjPropCatName);
                //System.out.println("blah!");

                tmpSet2 = topIxOnt.getObjectSubPropertyAxiomsForSuperProperty(OWLFactory.getOWLObjectProperty(IRI.create(tmpS.substring(1, tmpS.length() - 1))));
                for (OWLSubObjectPropertyOfAxiom sopAx2 : tmpSet2) {
                    String tmpS2 = sopAx2.getSubProperty().toString();
                    tmpAnnSet2 = topIxOnt.getAnnotationAssertionAxioms(IRI.create(tmpS2.substring(1, tmpS2.length() - 1)));

                    for (OWLAnnotationAssertionAxiom aaAx2 : tmpAnnSet2) {
                        String currentObjPropEntryName = aaAx2.getValue().toString().substring(1, aaAx2.getValue().toString().indexOf('^') - 1);
                        propEntryNameToPropCatName.put(currentObjPropEntryName, currentObjPropCatName);
                        propEntryNametoPropEntryIRI.put(currentObjPropEntryName, tmpS2.substring(1, tmpS2.length() - 1));
                    }
                }
            }
        }
//        System.out.println(propEntryNameToPropCatName);
//        System.out.println(propEntryNametoPropEntryIRI);
    }
    
    public void retrieveGeometricPropertiesMap(){
        OWLDataProperty geometricProperty=OWLFactory.getOWLDataProperty(":GeometricProperty", topIxPrefixManager);
        OWLAnnotationProperty propertyID=OWLFactory.getOWLAnnotationProperty(":propertyID", topIxPrefixManager);
        OWLAnnotationProperty houseSetable=OWLFactory.getOWLAnnotationProperty(":houseSetable", topIxPrefixManager);
        OWLAnnotationProperty roomSetable=OWLFactory.getOWLAnnotationProperty(":roomSetable", topIxPrefixManager);
        Set<OWLSubDataPropertyOfAxiom> tempGeometricDataPropertiesSet=topIxOnt.getDataSubPropertyAxiomsForSubProperty(geometricProperty);
        String geometricPropertyName;
        IRI geometricPropertyIRI;
        
        for(OWLSubDataPropertyOfAxiom tempGeomPropAxiom: tempGeometricDataPropertiesSet){
            tempGeomPropAxiom.get
        }
        
    }

    ////////////////////////////////
    //INDIVIDUAL ASSERTION METHODS//
    ////////////////////////////////
    public boolean assertSiteIndividual(String siteNameHash, String siteNameAnnotation) {
        //del-String tmpS=("Site Of "+siteNameHash);
        //del-String tmpHashedS=TopIxUtilityMethods.returnIndividualIdentifier(tmpS);
        OWLClassExpression tmpCE = OWLFactory.getOWLClass(":Site", topIxPrefixManager);
        OWLIndividual tmpInd = OWLFactory.getOWLNamedIndividual(':' + siteNameHash, topIxPrefixManager);

        manager.addAxiom(topIxOnt, OWLFactory.getOWLClassAssertionAxiom(tmpCE, tmpInd));
        manager.addAxiom(topIxOnt, OWLFactory.getOWLAnnotationAssertionAxiom(
                OWLFactory.getOWLAnnotationProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + "individualName")), IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + siteNameHash), OWLFactory.getOWLLiteral(siteNameAnnotation)));
        return true;
    }

    public boolean assertHouseIndividual(String houseEntryHash, String houseEntryAnnotation) {
        //del-String tmpS=TopIxUtilityMethods.returnIndividualIdentifier(s);

        OWLClassExpression tempClassExpression = OWLFactory.getOWLClass(":House", topIxPrefixManager);
        OWLIndividual tempIndividual = OWLFactory.getOWLNamedIndividual(':' + houseEntryHash, topIxPrefixManager);

        manager.addAxiom(topIxOnt, OWLFactory.getOWLClassAssertionAxiom(tempClassExpression, tempIndividual));
        manager.addAxiom(topIxOnt, OWLFactory.getOWLAnnotationAssertionAxiom(
                OWLFactory.getOWLAnnotationProperty(
                IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + "individualName")),
                IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + houseEntryHash),
                OWLFactory.getOWLLiteral(houseEntryAnnotation)));
        return true;
    }

    public boolean assertRoomIndividual(String roomName, String roomIndividualHash, String roomIndividualAnnotation) {
        //System.out.println(s);
        //List<String> tmpList=TopIxUtilityMethods.returnIndividualIdentifier(houseID, roomID);
        //if(!topIxOnt.containsEntityInSignature(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString()+'#'+tmpList.get(1))))
        //{
        OWLClassExpression tempClassExpression = OWLFactory.getOWLClass(IRI.create(roomToIRI.get(roomName))); //retrieves the Room Class IRI fron the roomToIRI map, using roomID as a key.
        OWLIndividual tempIndividual = OWLFactory.getOWLNamedIndividual(':' + roomIndividualHash, topIxPrefixManager);

        manager.addAxiom(topIxOnt, OWLFactory.getOWLClassAssertionAxiom(tempClassExpression, tempIndividual));
        manager.addAxiom(topIxOnt, OWLFactory.getOWLAnnotationAssertionAxiom(
                OWLFactory.getOWLAnnotationProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + "individualName")),
                IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + roomIndividualHash),
                OWLFactory.getOWLLiteral(String.format(roomIndividualAnnotation))));
        return true;

    }

//    public List<String> assertRoomIndividual(String houseID, String roomID)
//    {
//        //System.out.println(s);
//        List<String> tmpList=TopIxUtilityMethods.returnIndividualIdentifier(houseID, roomID);
//        //if(!topIxOnt.containsEntityInSignature(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString()+'#'+tmpList.get(1))))
//        //{
//            OWLClassExpression tmpCE=OWLFactory.getOWLClass(IRI.create(roomToIRI.get(roomID))); //retrieves the Room Class IRI fron the roomToIRI map, using roomID as a key.
//            OWLIndividual tmpInd=OWLFactory.getOWLNamedIndividual(':'+tmpList.get(1), topIxPrefixManager);
//
//            manager.addAxiom(topIxOnt, OWLFactory.getOWLClassAssertionAxiom(tmpCE, tmpInd));
//            manager.addAxiom(topIxOnt, OWLFactory.getOWLAnnotationAssertionAxiom(
//                    OWLFactory.getOWLAnnotationProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString()+'#'+"individualName"))
//                    , IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString()+'#'+tmpList.get(1))
//                    , OWLFactory.getOWLLiteral(String.format("%1$s_%2$s", houseID, tmpList.get(0)))));
//            System.out.println(roomID+'\n'+houseID+'\n'+tmpList.get(1));
//            return(tmpList);
//        //}
//        //else
//        //{
//        //    System.out.println("thn pathses me to hashcode, malaka!");
//        //    
//            //return null;
//        //}
//    }
    //////////////////////////////
    //PROPERTY ASSERTION METHODS//
    //////////////////////////////
    public boolean assertPropertyInstance(String comboBoxObjectPropertyEntry, String room1Hash, String room2Hash) {
        OWLObjectProperty objProp = OWLFactory.getOWLObjectProperty(IRI.create(propEntryNametoPropEntryIRI.get(comboBoxObjectPropertyEntry)));
        OWLIndividual room1Individual = OWLFactory.getOWLNamedIndividual(':' + room1Hash, topIxPrefixManager);
        OWLIndividual room2Individual = OWLFactory.getOWLNamedIndividual(':' + room2Hash, topIxPrefixManager);
        manager.addAxiom(topIxOnt, OWLFactory.getOWLObjectPropertyAssertionAxiom(objProp, room1Individual, room2Individual));
        return true;
    }

    public boolean assertHasHousePropertyInstance(String houseEntryHash, String siteNameHash) {
        OWLObjectProperty objProp = OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI() + "#hasHouse"));
        OWLIndividual houseIndividual = OWLFactory.getOWLNamedIndividual(':' + houseEntryHash, topIxPrefixManager);
        OWLIndividual siteIndividual = OWLFactory.getOWLNamedIndividual(':' + siteNameHash, topIxPrefixManager);
        manager.addAxiom(topIxOnt, OWLFactory.getOWLObjectPropertyAssertionAxiom(objProp, siteIndividual, houseIndividual));
        return true;
    }

//    public boolean assertHasSitePropertyInstance(String houseName, String siteName)
//    {
//        OWLObjectProperty objProp=OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI()+"#hasSite"));
//        OWLIndividual houseInd=OWLFactory.getOWLNamedIndividual(':'+houseName, topIxPrefixManager);
//        OWLIndividual siteInd=OWLFactory.getOWLNamedIndividual(':'+siteName, topIxPrefixManager);
//        manager.addAxiom(topIxOnt, OWLFactory.getOWLObjectPropertyAssertionAxiom(objProp, houseInd, siteInd));
//        return true;
//    }
    public boolean assertHasRoomPropertyInstance(String houseIndividualHash, String roomIndividualHash) {
        OWLObjectProperty tempOWLbjectProperty = OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI() + "#hasRoom"));
        OWLIndividual houseIndividual = OWLFactory.getOWLNamedIndividual(':' + houseIndividualHash, topIxPrefixManager);
        OWLIndividual roomIndividual = OWLFactory.getOWLNamedIndividual(':' + roomIndividualHash, topIxPrefixManager);
        manager.addAxiom(topIxOnt, OWLFactory.getOWLObjectPropertyAssertionAxiom(tempOWLbjectProperty, houseIndividual, roomIndividual));
        return true;
    }

    public boolean assertDataPropertyInstance(String ind, String prop, int value) {
        OWLDataProperty dataProp = OWLFactory.getOWLDataProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + prop));
        OWLDataPropertyAssertionAxiom tmpAx = OWLFactory.getOWLDataPropertyAssertionAxiom(
                dataProp, OWLFactory.getOWLNamedIndividual(IRI.create(topIxOnt.getOntologyID().getOntologyIRI().toString() + '#' + ind)), OWLFactory.getOWLLiteral(value));
        manager.addAxiom(topIxOnt, tmpAx);
        return true;
    }

    public boolean assertDataPropertyInstance(String ind, String prop, String value) {
        return true;
    }
    
    public boolean storeSolutions(String siteNameHash) {
        for(OwlSolution tempSolution:solutionsList) {
            //assert Solution individual and assert Site->hasSolution
            OWLClassExpression solutionClassExpression=OWLFactory.getOWLClass(":Solution", topIxPrefixManager);
            String tempSolutionID=String.format(siteNameHash+"_S_%1$02d", tempSolution.getSolutionID().intValue());
            OWLIndividual tempSolutionIndividual=OWLFactory.getOWLNamedIndividual(":"+tempSolutionID, topIxPrefixManager);
            OWLClassAssertionAxiom tempClassAssertionAxiom=OWLFactory.getOWLClassAssertionAxiom(solutionClassExpression, tempSolutionIndividual);
            manager.addAxiom(topIxOnt, tempClassAssertionAxiom);
            
            OWLObjectProperty tempSolutionObjectProperty=OWLFactory.getOWLObjectProperty(":hasSolution", topIxPrefixManager);
            OWLIndividual tempSiteIndividual=OWLFactory.getOWLNamedIndividual(":"+siteNameHash, topIxPrefixManager);
            OWLObjectPropertyAssertionAxiom tempObjPropAssAxiom=
                    OWLFactory.getOWLObjectPropertyAssertionAxiom(tempSolutionObjectProperty, tempSiteIndividual, tempSolutionIndividual);
            manager.addAxiom(topIxOnt, tempObjPropAssAxiom);
            
            //assert SolvedHouse individuals and assert Solution->hasSolvedHouse objProperty. also assert the DATA PROPERTIES for each solvedHouse indvidual
            for(OwlSolvedHouse tempSolvedHouse:tempSolution.getSolvedHouses()) {
                OWLClassExpression solvedHouseClassExpression=OWLFactory.getOWLClass(":SolvedHouse", topIxPrefixManager);
                OWLIndividual tempSolvedHouseIndividual=OWLFactory.getOWLNamedIndividual(":"+tempSolutionID+"_SH_"+tempSolvedHouse.getSolvedHouseHash(), topIxPrefixManager);
                OWLClassAssertionAxiom tempClassAssAx=OWLFactory.getOWLClassAssertionAxiom(solvedHouseClassExpression, tempSolvedHouseIndividual);
                manager.addAxiom(topIxOnt, tempClassAssAx);
                
                OWLObjectProperty tempSolvedHouseObjectProperty=OWLFactory.getOWLObjectProperty(":hasSolvedHouse", topIxPrefixManager);
                OWLObjectPropertyAssertionAxiom tempHouseObjPropAssAx=OWLFactory.getOWLObjectPropertyAssertionAxiom(tempSolvedHouseObjectProperty, tempSolutionIndividual, tempSolvedHouseIndividual);
                manager.addAxiom(topIxOnt, tempHouseObjPropAssAx);
                
                OWLDataProperty tempDataProperty;
                OWLDataPropertyAssertionAxiom tempDataPropertyAssertionAxiom;
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasLiteral", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedHouseIndividual, tempSolvedHouse.getSolvedHouseLiteral());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasL", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedHouseIndividual, tempSolvedHouse.getSolvedHouseLength());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasW", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedHouseIndividual, tempSolvedHouse.getSolvedHouseWidth());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasX", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedHouseIndividual, tempSolvedHouse.getSolvedHouseX());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasY", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedHouseIndividual, tempSolvedHouse.getSolvedHouseY());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
            }    

            //assert SolvedRoom individuals and assert Solution->hasSolvedRoom objProperty. also assert the DATA PROPERTIES for each solvedRoom indvidual
            for(OwlSolvedRoom tempSolvedRoom:tempSolution.getSolvedRooms()) {
                OWLClassExpression solvedRoomClassExpression=OWLFactory.getOWLClass(":SolvedRoom", topIxPrefixManager);
                OWLIndividual tempSolvedRoomIndividual=OWLFactory.getOWLNamedIndividual(":"+tempSolutionID+"_SR_"+tempSolvedRoom.getSolvedRoomHash(), topIxPrefixManager);
                OWLClassAssertionAxiom tempClassAssAx=OWLFactory.getOWLClassAssertionAxiom(solvedRoomClassExpression, tempSolvedRoomIndividual);
                manager.addAxiom(topIxOnt, tempClassAssAx);
                
                OWLObjectProperty tempSolvedRoomObjectProperty=OWLFactory.getOWLObjectProperty(":hasSolvedRoom", topIxPrefixManager);
                OWLObjectPropertyAssertionAxiom tempRoomObjPropAssAx=OWLFactory.getOWLObjectPropertyAssertionAxiom(tempSolvedRoomObjectProperty, tempSolutionIndividual, tempSolvedRoomIndividual);
                manager.addAxiom(topIxOnt, tempRoomObjPropAssAx);
                
                OWLDataProperty tempDataProperty;
                OWLDataPropertyAssertionAxiom tempDataPropertyAssertionAxiom;
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasLiteral", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomLiteral());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasL", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomLength());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasW", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomWidth());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasH", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomHeight());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasX", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomX());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasY", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomY());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
                
                tempDataProperty=OWLFactory.getOWLDataProperty(":hasZ", topIxPrefixManager);
                tempDataPropertyAssertionAxiom=OWLFactory.getOWLDataPropertyAssertionAxiom(tempDataProperty, tempSolvedRoomIndividual, tempSolvedRoom.getSolvedRoomZ());
                manager.addAxiom(topIxOnt, tempDataPropertyAssertionAxiom);
            }    
        }
        return true;
    }
    
    public boolean retrieveSolutions(String siteNameHash) {
        //use the siteNameHash to access a certain Site individual in the ontology
        //retrieve each Solution individual of this site and use it to re-initialise the Solutions List
        OwlSolution tempSolution;
        OwlSolvedHouse tempSolvedHouse;
        OwlSolvedRoom tempSolvedRoom;
        
        //first clear the Solution List contents...
        this.solutionsList.clear();
        
        
        OWLIndividual tempSiteIndividual=OWLFactory.getOWLNamedIndividual(":"+siteNameHash, topIxPrefixManager);
        OWLClassExpression tempSolutionClassExpression=OWLFactory.getOWLClass(":Solution", topIxPrefixManager);
        Set<OWLObjectPropertyAssertionAxiom> tempSitePropertyAxioms=topIxOnt.getObjectPropertyAssertionAxioms(tempSiteIndividual);
        
        Set<OWLIndividual> tempSolutionIndividuals=new HashSet<>(); //Solution individuals for this particular site. will be filled below...
        Set<OWLIndividual> tempSolvedHouseIndividuals=new HashSet<>();
        Set<OWLIndividual> tempSolvedRoomIndividuals=new HashSet<>();
        
        //populate the tempSolutionIndividuals Set<OWLIndividual> with the Solution individuals that are solutions of the current Site
        //this block runs through the set of all the object property axioms that have this site as their subject
        //and filters out the ones that are of type "hasSolution"
        for (OWLObjectPropertyAssertionAxiom tempObjPropAssAx:tempSitePropertyAxioms) {
            OWLObjectProperty tempHasSolutionObjectProperty=OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI()+"#hasSolution"));
            if(tempObjPropAssAx.getProperty()==tempHasSolutionObjectProperty) {
                tempSolutionIndividuals.add(tempObjPropAssAx.getObject());
            }
        
        }
        //retrieving the site dimensions for this solution set (the site remains the same throughout the plethos of solutions).
        //
        int tempSiteLength=0;
        int tempSiteWidth=0;
        OWLDataProperty siteHasLengthProp=OWLFactory.getOWLDataProperty(":hasX", topIxPrefixManager);
        OWLDataProperty siteHasWidthProp=OWLFactory.getOWLDataProperty(":hasY", topIxPrefixManager);
        Set<OWLDataPropertyAssertionAxiom> tempSiteDataProperties=new HashSet<>();
        
        tempSiteDataProperties=topIxOnt.getDataPropertyAssertionAxioms(tempSiteIndividual);
        
        for(OWLDataPropertyAssertionAxiom tempSiteDataProperty:tempSiteDataProperties) {
            if(tempSiteDataProperty.getProperty()==siteHasLengthProp) {
                tempSiteLength=tempSiteDataProperty.getObject().parseInteger();
            }
            if(tempSiteDataProperty.getProperty()==siteHasWidthProp) {
                tempSiteWidth=tempSiteDataProperty.getObject().parseInteger();
            }
        }
        
        logger.info(tempSolutionIndividuals.size());
        
        //running every solution individual for/of that particular site in order to extract the solvedHouse's and the solvedRoom's
        //via the hasSolvedHouse and hasSolvedRoom properties.
        int solutionCounter=0;
        for(OWLIndividual tempSolutionIndividual:tempSolutionIndividuals) {
            tempSolution=new OwlSolution();
            tempSolution.setSolutionID(new Integer(++solutionCounter));
            
            tempSolution.setSiteLength(new Integer(tempSiteLength));
            tempSolution.setSiteWidth(new Integer(tempSiteWidth));
            
            //clearing the contents of these two structuresin order to accommodate the solved entities of the next solution.
            tempSolvedHouseIndividuals.clear();
            tempSolvedRoomIndividuals.clear();
            
            for(OWLObjectPropertyAssertionAxiom tempObjPropAssAx:topIxOnt.getObjectPropertyAssertionAxioms(tempSolutionIndividual)) {
                OWLObjectProperty tempHasSolvedHouseObjectProperty=OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI()+"#hasSolvedHouse"));
                OWLObjectProperty tempHasSolvedRoomObjectProperty=OWLFactory.getOWLObjectProperty(IRI.create(topIxOnt.getOntologyID().getOntologyIRI()+"#hasSolvedRoom"));
                if(tempObjPropAssAx.getProperty()==tempHasSolvedHouseObjectProperty) {
                    tempSolvedHouseIndividuals.add(tempObjPropAssAx.getObject());
                }
                else if(tempObjPropAssAx.getProperty()==tempHasSolvedRoomObjectProperty) {
                    tempSolvedRoomIndividuals.add(tempObjPropAssAx.getObject());
                }
            }

            for(OWLIndividual tempSolvedHouseIndividual:tempSolvedHouseIndividuals) {
                tempSolvedHouse=new OwlSolvedHouse();
                for(OWLDataPropertyAssertionAxiom tempDatPropAssAx:topIxOnt.getDataPropertyAssertionAxioms(tempSolvedHouseIndividual)) {
                    if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasL", topIxPrefixManager))) {
                        //logger.info(tempDatPropAssAx.getObject().parseInteger());
                        tempSolvedHouse.setSolvedHouseLength(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasW", topIxPrefixManager))) {
                        tempSolvedHouse.setSolvedHouseWidth(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasX", topIxPrefixManager))) {
                        tempSolvedHouse.setSolvedHouseX(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasY", topIxPrefixManager))) {
                        tempSolvedHouse.setSolvedHouseY(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasLiteral", topIxPrefixManager))) {
                        tempSolvedHouse.setSolvedHouseLiteral(tempDatPropAssAx.getObject().getLiteral());
                    }
                }
                //logger.info(tempSolvedHouse.getSolvedHouseX()+" "+tempSolvedHouse.getSolvedHouseY());
                tempSolution.getSolvedHouses().add(tempSolvedHouse);
            }
            
            for(OWLIndividual tempSolvedRoomIndividual:tempSolvedRoomIndividuals) {
                tempSolvedRoom=new OwlSolvedRoom();
                for(OWLDataPropertyAssertionAxiom tempDatPropAssAx:topIxOnt.getDataPropertyAssertionAxioms(tempSolvedRoomIndividual)) {
                    if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasL", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomLength(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasW", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomWidth(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasH", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomHeight(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasX", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomX(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasY", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomY(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasZ", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomZ(tempDatPropAssAx.getObject().parseInteger());
                    }
                    else if(tempDatPropAssAx.getProperty()==(OWLFactory.getOWLDataProperty(":hasLiteral", topIxPrefixManager))) {
                        tempSolvedRoom.setSolvedRoomLiteral(tempDatPropAssAx.getObject().getLiteral());
                    }
                }
                tempSolution.getSolvedRooms().add(tempSolvedRoom);
            }
            this.solutionsList.add(tempSolution);
        }
        
        return true;
    }
    
    public Map<String, OWLIndividual> returnSitesInOntology () {
        Map<String, OWLIndividual> returnMap=new HashMap<>();
        List<OWLIndividual> returnList=new ArrayList<>();
        OWLAnnotationProperty hasNameAnnotationProperty=OWLFactory.getOWLAnnotationProperty(":individualName", topIxPrefixManager);
        
        
        OWLClassExpression tempSiteClassExpression=OWLFactory.getOWLClass(":Site", topIxPrefixManager);
        for(OWLClassAssertionAxiom tempClassAssAx:topIxOnt.getClassAssertionAxioms(tempSiteClassExpression)) {
            returnList.add(tempClassAssAx.getIndividual());
            
            Set<OWLAnnotationAssertionAxiom> tempSiteAnnotationsSet=topIxOnt.getAnnotationAssertionAxioms(tempClassAssAx.getIndividual().asOWLNamedIndividual().getIRI());
            for (OWLAnnotationAssertionAxiom tempAnnotationAssertionAxiom:tempSiteAnnotationsSet) {
                if(tempAnnotationAssertionAxiom.getProperty().equals(hasNameAnnotationProperty))
                {
                    String tempString=tempAnnotationAssertionAxiom.getValue().toString();
                    logger.info(tempString);
                    tempString=tempString.substring(tempString.indexOf('"')+1, tempString.indexOf('^')-1);
                    logger.info(tempString);
                    logger.info(tempClassAssAx.getIndividual().toString());
                    returnMap.put(tempString, tempClassAssAx.getIndividual());
                }
            }
        }
        return returnMap;
    }

    public void saveOntology() {
        try {
            manager.saveOntology(topIxOnt);
        } catch (OWLOntologyStorageException ose) {
            System.out.println(ose.getMessage());
        }
    }

    public List<OwlSolution> getSolutionsList() {
        return solutionsList;
    }

    public void setSolutionsList(List<OwlSolution> solutionsList) {
        this.solutionsList = solutionsList;
    }
    
    

////    public void filterLeafObjProps()
////    {
////        set1=new HashSet(aMap.keySet());
////        set2=new HashSet(set1);
////        Set<OWLObjectPropertyExpression> tmpSet=new HashSet(aMap.values());
////        
////        set1.removeAll(tmpSet);
////        System.out.println(set1.size());
////        set2.retainAll(tmpSet);
////
////        Set<IRI> tmpValues=new HashSet(this.subObjProp.values());
////        for(IRI pex:tmpValues)
////        {
////            this.subObjProp.remove(pex);
////        }
////    }
//    public void retrieveAnnotations()
    //   {
////        AnnotationUseful utility=null;
////        for(OWLAnnotationAssertionAxiom ax:topIxOnt.getAxioms(AxiomType.ANNOTATION_ASSERTION))
////        {
////            utility.updateAttrs(ax);
////            AnnotationStuff.put(ax.getSubject(), utility);
////        }
    // }
    /*
     * public void fillGuiSets() { //to become class attribute. Set<IRI>
     * leafObjectProperties = new HashSet<>(30);
     *
     * for(OWLPropertyExpression pex:this.subObjProp.values()) {
     * leafObjectProperties.add(IRI.create(pex.toString().substring(1,
     * pex.toString().length()-1))); }
     *
     * for(IRI iri:leafObjectProperties) { for(OWLAnnotationAssertionAxiom
     * aaAx:topIxOnt.getAnnotationAssertionAxioms(iri)) {
     *
     * //System.out.println(aaAx.getProperty().toString()); switch
     * (aaAx.getProperty().toString()) { case
     * "<htp://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl#propertyID>":
     * objPropEntries.add(aaAx.getValue().toString().substring(1,
     * aaAx.getValue().toString().indexOf('^')-1)); break; } }
     *
     * }
     * }
     */
    public void retrieveAnnotationsDeprecated() {
        Set<OWLAnnotationAssertionAxiom> annotationsSet = topIxOnt.getAxioms(AxiomType.ANNOTATION_ASSERTION);
        Iterator<OWLAnnotationAssertionAxiom> it1 = annotationsSet.iterator();
        while (it1.hasNext()) {
            OWLAnnotationAssertionAxiom aaaAx = it1.next();

            //AnnotationStrings tmpStrs=new AnnotationStrings(aaaAx.toString());
            //String tmpSaa=aaaAx.toString();
            //System.out.println(tmpStrs.getTmpSaa() + '\n' + tmpStrs.getTmpSap() + '\n');
        }
    }

    public void retrieveSuperClasses() {
        subclassOfSet = topIxOnt.getAxioms(AxiomType.SUBCLASS_OF);
        hsStr = new HashSet<>();
        for (OWLSubClassOfAxiom scoAx : subclassOfSet) {
            hsStr.add(scoAx.getSuperClass());
        }
        //for(OWLClassExpression ce:hsStr)
        //    System.out.println(ce.toString());
    }

    public void fillRoomsVector() {
//        String tmpS;
//        OWLSubClassOfAxiom aaAx;

        for (OWLAxiom ax : allAxioms) {
            //System.out.println(ax./*getAxiomType().*/toString());
            if (ax.getAxiomType().toString().equals("AnnotationAssertion")) {
                OWLAnnotationAssertionAxiom aaAx2 = (OWLAnnotationAssertionAxiom) ax;
                String oasS = aaAx2.getSubject().toString();  //ayto to string tha sygkrithei me to string poy tha prokypsei
                //ap to getsubclass toy subclassOfAxiom

                //aaAx=(OWLSubClassOfAxiom)ax;
                //if(aaAx.getSubject().equals()
                //System.out.println(oas.toString());

                for (OWLAxiom ax2 : allAxioms) {
                    if (ax2.getAxiomType().toString().equals("SubClassOf")) {
                        OWLSubClassOfAxiom scoAx = (OWLSubClassOfAxiom) ax2;
                        String tmpS2 = scoAx.getSubClass().toString();
                        tmpS2 = tmpS2.substring(tmpS2.indexOf('<') + 1, tmpS2.indexOf('>'));

                        if (oasS.equals(tmpS2)) {
                            OWLClassExpression cexS = scoAx.getSuperClass();
                            //for(OWLClassExpression cexVec:hsStr)
                            {
                                if (cexS.toString().equals("<http://www.semanticweb.org/ontologies/ptyxiaki_v0.6/2011/5/Ontology1308067064597.owl#Room>")) {
                                    //String - substring to katallhlo meros toy string in order for it to
                                    String finalVec = aaAx2.getValue().toString();
                                    finalVec = finalVec.substring(finalVec.indexOf('"') + 1, finalVec.indexOf('^') - 1);
                                    roomsVec.add(finalVec);
                                }
                            }
                        }
                    }
                }
            }


            /*
             * if(ax.getAxiomType().toString().equals("SubClassOf")) {
             * OWLSubClassOfAxiom scoAx2=(OWLSubClassOfAxiom)ax;
             * OWLClassExpression cex=scoAx2.getSuperClass();
             *
             * //System.out.println('\n');
            }
             */
            //System.out.println(roomsVec.size());
        }
    }

    public void fillObjectPropsVec() {
    }

    /*
     * public void test1() { OWLSubClassOfAxiom scoAx;
     * OWLAnnotationAssertionAxiom aaAx;
     *
     * String s1=new String(); String s2=new String();
     *
     * for(OWLAxiom ax:allAxioms) {
     * if(ax.getAxiomType().toString().equals("SubClassOf")) {
     * scoAx=(OWLSubClassOfAxiom)ax; String tmpS=scoAx.getSubClass().toString();
     * System.out.println("SubclassOf axiom sub:
     * "+tmpS.substring(tmpS.indexOf('<')+1, tmpS.indexOf('>')));
     * if(tmpS.substring(tmpS.indexOf('#')+1,
     * tmpS.indexOf('>')).equals("Office"))
     * s1=tmpS.substring(tmpS.indexOf('<')+1, tmpS.indexOf('>')); }
     * if(ax.getAxiomType().toString().equals("AnnotationAssertion")) {
     * aaAx=(OWLAnnotationAssertionAxiom)ax; String
     * tmpS=aaAx.getSubject().toString();
     * System.out.println("AnnotationAssertion axiom:
     * "+aaAx.getSubject().toString());
     * if(tmpS.substring(tmpS.indexOf('#')+1).equals("Office")) s2=tmpS; } }
     *
     * System.out.println("test start..."); System.out.println(s1);
     * System.out.println(s2); System.out.println("test end...");
     * if(s1.equals(s2)) System.out.println("yeah!"); } public void stringTest()
     * { String s1, s2; s1=new String("abcdef");
     * s2=s1.substring(s1.indexOf('a'), s1.indexOf('f'));
     *
     * System.out.println("\n\n"+s2);
    }
     */
}