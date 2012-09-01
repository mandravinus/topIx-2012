/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topIx;


import choco.cp.solver.propagation.ChocoEngine;
import java.awt.BorderLayout;
import java.awt.CardLayout;
//import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;
//import javax.swing.border.*;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeModelEvent;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreeModel;
//import javax.swing.tree.TreeSelectionModel;

import org.semanticweb.owlapi.model.OWLIndividual;

import chocosolution.TopIxChoco;
import chocosolution.ChocoRoom;
import chocosolution.ChocoUtility;

import choco.kernel.solver.variables.integer.IntDomainVar;
import chocosolution.*;
import java.awt.FlowLayout;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import topIx.owlintermediateclasses.*;
import visualisation.TopIx3D;
/**
 *
 * @author Antiregulator
 */
public class GUI extends JFrame implements ActionListener, ItemListener, TreeSelectionListener, FocusListener, KeyListener//, TreeModelListener, Runnable
{
    //GUI components
    JPanel mainPanel;
    //mainPanel components
    JPanel paneI;
    JPanel paneII;
    CardLayout layI_Crd;
    JTabbedPane tabs;
    //paneI components      - card layout holding the data entry panes
    JPanel paneIa;
    JPanel paneIb;
    GroupLayout layIa_Grp;
    GroupLayout layIb_Grp;
    //paneIa components     - primary data entry
    JLabel houseIdentifierLabel;
    JLabel xLabel;
    JLabel yLabel;
    JLabel floorsLabel;
    JLabel floorHeightLabel;
    JTextField houseIdentifierInput;
    JTextField xInput;
    JTextField yInput;
    JTextField floorsInput;
    JTextField floorHeightInput;
    JButton nextBtn;
    //paneIb components     - ontology data entry
    JPanel paneIb1;
    JPanel paneIb2;
    JPanel paneIb3;
    //paneIb1 components    - tree pane
        //will contemplate on the layout type
    //JScrollPane treeScroller;
    //JTree roomsTree;
    DynamicTree roomsTreePanel;
    
    
    
        //has default FlowLayout
    //paneIb2 components    - combo boxes
    JLabel rooms1Label;
    JLabel rooms2Label;
    JLabel catsCBoxLabel;
    JLabel propsCBoxLabel;
    JLabel compactnessChBoxLabel;
    JComboBox<String> rooms1;
    JComboBox<String> rooms2;
    JComboBox<String> catsCBox;
    JComboBox<String> propsCBox;
    JCheckBox compactnessChBox;
    GroupLayout layIb2_Grp;
    //paneIb3 components    - buttons
    JButton addHouseBtn;
    JButton addRoomBtn;
    JButton registerBtn;
    JButton calculateBtn;
    JButton resetBtn;
    JButton backBtn;
    JButton saveBtn;
    GroupLayout layIb3_Grp;
    //paneII components     - results presentation
    JPanel paneIIa;
    //paneIIa components    - result panel controls.
    JLabel availableSitesCBoxLabel;
    JLabel availableSolutionsCBoxLabel;
    JLabel renderSolidLabel;
    JComboBox<OWLIndividual> availableSitesCBox;
    JComboBox<OwlSolution> availableSolutionsCBox;
    JCheckBox renderSolidChBox;
    JButton printBtn;

    TopIx3D topIx3D;
    
    //JessMonitor pane components
    JPanel jessMonitorPane;
    //ConsolePanel jessPane;
    JTextField jessCommandField;
    JButton evalButton;
    
    //misc attributes
    //Map<String, Integer> roomName_roomHashCode;
    String houseIdentifier;
    Integer houseIdentifierHashCode;
    
    //private Map<DefaultMutableTreeNode, IRI> treeEntry_IndividualIRI;
    
    OntologyAccessUtility guiAccess;
    TopIxChoco guiChoco;
    //holds temporary OwlSite, OwlHouse, OwlRoom fields which are initialized when they are first needed (stoys event handlers toys dld)
    //- candidate to be moved to OntologyAccessUtility class or... God knows where!!!
    OwlSite currentSite;
    OwlHouse currentHouse;
    OwlRoom currentRoom;
    
    JOptionPane houseHeightPane;
    JDialog houseHeightDialog;
    
    
    //DeclarativeDescription decDes;
    
    Logger logger;
    
    public GUI(OntologyAccessUtility accessRef)
    {
        //deprecated constructor - the one with both OntologyAccessUtility and TopIxChoco references is used.
        super("topIx project - bromoiras-gioukakis");
        mainPanel=new JPanel();
        
        guiAccess=accessRef;
        
        //this.decDes=decDes;
        
        initializeComponents(guiAccess.propEntryNameToPropCatName, guiAccess.roomToIRI);
        addComponents();
        
        //setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 1100, 900);
        
        propsCBoxLabel.setVisible(true);
        propsCBox.setVisible(true);
        catsCBox.setSelectedIndex(1);
        rooms2Label.setVisible(false);
        rooms2.setVisible(false);
        //tabs.getTabComponentAt(tabs.indexOfTab("Results")).setVisible(false);
        //layI_Crd.show(paneI, "Model Input");
        
        logger= Logger.getLogger(GUI.class.toString());
        //BasicConfigurator.configure();
    }

        public GUI(OntologyAccessUtility accessRef, TopIxChoco chocoRef)
    {
        super("topIx project - bromoiras-gioukakis");
        logger= Logger.getLogger(GUI.class.toString());
        logger.info("creating GUI jpanel");
        mainPanel=new JPanel();
        
        guiAccess=accessRef;
        guiChoco=chocoRef;
        //this.decDes=decDes;
        
        initializeComponents(guiAccess.propEntryNameToPropCatName, guiAccess.roomToIRI);
        addComponents();
        
        //setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(50, 20, 1200, 700);
        
        populateResultsComponents();
        
        propsCBoxLabel.setVisible(true);
        propsCBox.setVisible(true);
        catsCBox.setSelectedIndex(1);
        rooms2Label.setVisible(false);
        rooms2.setVisible(false);
        //tabs.getTabComponentAt(tabs.indexOfTab("Results")).setVisible(false);
        //layI_Crd.show(paneI, "Model Input");
        //BasicConfigurator.configure();
    }

    private void initializeComponents(Map<String, String> objCatsCB, Map<String, String> roomsCB)     //initialize panels with their layouts as well as components
                                            
    {
        paneI=new JPanel();
        layI_Crd=new CardLayout();
        paneI.setLayout(layI_Crd);
        paneII=new JPanel();
            tabs=new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        //--------------------------------------------------------------------//
        paneIa=new JPanel();
        paneIb=new JPanel();
        layIa_Grp=new GroupLayout(paneIa);
        layIb_Grp=new GroupLayout(paneIb);
        paneIa.setLayout(layIa_Grp);
        paneIb.setLayout(layIb_Grp);
        //--------------------------------------------------------------------//
        houseIdentifierLabel=new JLabel("Name of Project:");
        xLabel=new JLabel("Site Length:");
        yLabel=new JLabel("Site Width:");
        floorsLabel=new JLabel("Number of Floors:");
        floorHeightLabel=new JLabel("Floor Height:");
        houseIdentifierInput=new JTextField();
        xInput=new JTextField();
        yInput=new JTextField();
        floorsInput=new JTextField();
        floorHeightInput=new JTextField();
        nextBtn=new JButton("Next");
            nextBtn.addActionListener(this);
        //--------------------------------------------------------------------//
        paneIb1=new JPanel();
            roomsTreePanel=new DynamicTree();
        paneIb2=new JPanel();
        layIb2_Grp=new GroupLayout(paneIb2);
        paneIb2.setLayout(layIb2_Grp);
            rooms1Label=new JLabel("Available Rooms");
            rooms2Label=new JLabel("Inserted Rooms");
            catsCBoxLabel=new JLabel("Relationships Categories");
            propsCBoxLabel=new JLabel("Relationships");
            compactnessChBoxLabel=new JLabel("Compact Site");
            rooms1=new JComboBox<>(roomsCB.keySet().toArray(new String[0]));
                rooms1.addActionListener(this);
                rooms1.addItemListener(this);
            rooms2=new JComboBox<>();
                rooms2.addActionListener(this);
            Set<String> hs1=new HashSet<>(objCatsCB.values());
            catsCBox=new JComboBox<>(hs1.toArray(new String[0]));
                catsCBox.addActionListener(this);
            propsCBox=new JComboBox<>();
            compactnessChBox=new JCheckBox();
                compactnessChBox.setSelected(true);
            
            
        paneIb3=new JPanel();
        layIb3_Grp=new GroupLayout(paneIb3);
        paneIb3.setLayout(layIb3_Grp);
            addHouseBtn=new JButton("Add House");
                addHouseBtn.addActionListener(this);
            addRoomBtn=new JButton("Add Room");
                addRoomBtn.addActionListener(this);
                addRoomBtn.setEnabled(false);
            registerBtn=new JButton("Register");
                registerBtn.addActionListener(this);
                registerBtn.setEnabled(false);
            calculateBtn=new JButton("Calculate");
                calculateBtn.addActionListener(this);
                calculateBtn.setEnabled(false);
            resetBtn=new JButton("Reset");
                resetBtn.addActionListener(this);
            backBtn=new JButton("Back");
                backBtn.addActionListener(this);
            saveBtn=new JButton("Save");
                saveBtn.addActionListener(this);
        //--------------------------------------------------------------------//
        paneIIa=new JPanel(new FlowLayout());
            availableSitesCBoxLabel=new JLabel("Available Sites in Ontology File");
            availableSolutionsCBoxLabel=new JLabel("Stored Arrangements");
            availableSitesCBox=new JComboBox<>();
                availableSitesCBox.addActionListener(this);
                availableSitesCBox.addItemListener(this);
            availableSolutionsCBox=new JComboBox<>();
                availableSolutionsCBox.addActionListener(this);
            renderSolidLabel=new JLabel("Render as Solid");
            renderSolidChBox=new JCheckBox();
                renderSolidChBox.setSelected(false);
                renderSolidChBox.addActionListener(this);
                
        topIx3D=new TopIx3D();
        
        
        //try
        //{
            jessMonitorPane=new JPanel(new BorderLayout());
            //jessPane=new ConsolePanel(decDes.getRt());
            jessCommandField=new JTextField(60);
                jessCommandField.setActionCommand("evalExpression");    
                jessCommandField.addFocusListener(this);
                jessCommandField.addActionListener(this);
                jessCommandField.addKeyListener(this);
            evalButton=new JButton("eval");
                evalButton.setActionCommand("evalExpression");
                evalButton.addActionListener(this);     
        //}catch (JessException je){System.out.println(je.getExecutionContext()+"\n"+je.getErrorCode()+" @line "+je.getLineNumber());}

               
        //--------------------------------------------------------------------//
    }
    
    private void addComponents()            //add components to their respective containers
    {
        //fill paneIa
        layIa_Grp.setHorizontalGroup(
                layIa_Grp.createSequentialGroup()
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(houseIdentifierLabel)
                        .addComponent(xLabel)
                        .addComponent(yLabel)
                        .addComponent(floorsLabel)
                        .addComponent(floorHeightLabel))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(houseIdentifierInput, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(xInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(yInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(floorsInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(floorHeightInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(nextBtn)));
        layIa_Grp.setVerticalGroup(
                layIa_Grp.createSequentialGroup()
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(houseIdentifierLabel)
                        .addComponent(houseIdentifierInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(xLabel)
                        .addComponent(xInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(yLabel)
                        .addComponent(yInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(floorsLabel)
                        .addComponent(floorsInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(floorHeightLabel)
                        .addComponent(floorHeightInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nextBtn)));
        //fill paneIb1
        paneIb1.add(roomsTreePanel);
        //fill paneIb2
        layIb2_Grp.setHonorsVisibility(false);
        layIb2_Grp.setHorizontalGroup(
                layIb2_Grp.createSequentialGroup()
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(rooms1Label)
                        .addComponent(rooms1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(compactnessChBoxLabel))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(catsCBoxLabel)
                        .addComponent(catsCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(compactnessChBox))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(propsCBoxLabel)
                        .addComponent(propsCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(rooms2Label)
                        .addComponent(rooms2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
        layIb2_Grp.setVerticalGroup(
                layIb2_Grp.createSequentialGroup()
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(rooms1Label)
                        .addComponent(catsCBoxLabel)
                        .addComponent(propsCBoxLabel)
                        .addComponent(rooms2Label))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(rooms1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(catsCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(propsCBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(rooms2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(compactnessChBoxLabel)
                        .addComponent(compactnessChBox)));
        //visibility settings for optional components
        propsCBoxLabel.setVisible(false);
        propsCBox.setVisible(false);
        rooms2Label.setVisible(false);
        rooms2.setVisible(false);
        //fill paneIb3
        layIb3_Grp.setHorizontalGroup(
                layIb3_Grp.createSequentialGroup()
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addHouseBtn)
                        .addComponent(addRoomBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(registerBtn)
                        .addComponent(calculateBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(resetBtn)
                        .addComponent(backBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(saveBtn)));
        layIb3_Grp.setVerticalGroup(
                layIb3_Grp.createSequentialGroup()
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addHouseBtn)
                        .addComponent(registerBtn)
                        .addComponent(resetBtn)
                        .addComponent(saveBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addRoomBtn)
                        .addComponent(calculateBtn)
                        .addComponent(backBtn)));
        //fill paneIb
        layIb_Grp.setHorizontalGroup(
                layIb_Grp.createSequentialGroup()
                    .addComponent(paneIb1)
                    .addGroup(layIb_Grp.createParallelGroup()
                        .addComponent(paneIb2)
                        .addComponent(paneIb3)));
        layIb_Grp.setVerticalGroup(
                layIb_Grp.createSequentialGroup()
                    .addGroup(layIb_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(paneIb1)
                        .addComponent(paneIb2))
                    .addComponent(paneIb3));
        //fill paneIIa
        paneIIa.add(availableSitesCBoxLabel);
        paneIIa.add(availableSitesCBox);
        paneIIa.add(availableSolutionsCBoxLabel);
        paneIIa.add(availableSolutionsCBox);
        paneIIa.add(renderSolidLabel);
        paneIIa.add(renderSolidChBox);
        
        paneI.add(paneIa, "Basic Input");
        paneI.add(paneIb, "Model Input");

        paneII.add(paneIIa);
        paneII.add(topIx3D);
        
        
        //jessMonitorPane.add(BorderLayout.CENTER, jessPane);
        //jessMonitorPane.add(BorderLayout.SOUTH, jessCommandField);
        //jessMonitorPane.add(BorderLayout.EAST, evalButton);
        
        tabs.addTab("Input", paneI);
        tabs.addTab("Results", paneII);
        //tabs.addTab("Jess Monitor", jessMonitorPane);
        mainPanel.add(tabs);
        this.add(mainPanel);
        this.setVisible(true);
        this.pack();
    }
    
    public void populateResultsComponents () {
        if (!guiAccess.returnSitesInOntology().isEmpty()) {
            OWLIndividual[] tempModelArray=(OWLIndividual[])guiAccess.returnSitesInOntology().toArray(new OWLIndividual[0]);
            ComboBoxModel<OWLIndividual> tempModel=new DefaultComboBoxModel<>(tempModelArray);
            this.availableSitesCBox.setModel(tempModel);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        //--------------------------------------------------------------------//
        if(actionEvent.getSource()==catsCBox)
        {
            Set<String> tmpSet=new HashSet<>(16);
            String tmpS=catsCBox.getSelectedItem().toString();
            propsCBox.removeAllItems();
            for(Map.Entry<String, String> anEntry:guiAccess.propEntryNameToPropCatName.entrySet())
            {
                if(anEntry.getValue().equals(tmpS))
                {
                    propsCBox.addItem(anEntry.getKey());
                }
            }
            if((catsCBox.getSelectedItem().equals("Dimension Self Comparison")|catsCBox.getSelectedItem().equals("Relative Dimension")))
            {
                rooms2.setVisible(false);
                rooms2Label.setVisible(false);
            }
            else
            {
                rooms2.setVisible(true);
                rooms2Label.setVisible(true);
            }
        }
        
        //NEXT BUTTON---------------------------------------------------------//
        if (actionEvent.getSource()==nextBtn && ((validateIntegerInput(xInput.getText())<=-1)||(validateIntegerInput(yInput.getText())<=-1))) {
            JOptionPane.showMessageDialog(this, "Please provide a positive integer for site length and width!");
        }
        
        //if(actionEvent.getSource()==nextBtn)
        else if (actionEvent.getSource()==nextBtn)
        {
            //int tmpL, tmpW;

            currentSite=new OwlSite(houseIdentifierInput.getText(), Integer.parseInt(xInput.getText()),Integer.parseInt(yInput.getText()));
            
            guiAccess.assertSiteIndividual(currentSite.returnSiteNameHash(), currentSite.returnSiteNameAnnotation());
            roomsTreePanel.addRootNodeToTree(currentSite.returnSiteNameCompact());
            roomsTreePanel.getTree().addTreeSelectionListener(this);
            //roomsTreePanel.getTree().getModel().addTreeModelListener(this);
            roomsTreePanel.setVisible(true);
            //the following commented block shows sort of what oughts to be done in the addHouse actionPerformed part.
            //
            //del-initializing the houseIdentifier attribute which will be used in naming each project's rooms.
            //del-houseIdentifier=houseIdentifierInput.getText();
            //del-guiAccess.assertHouseIndividual(houseIdentifier);
            //del-String houseHashed=TopIxUtilityMethods.returnIndividualIdentifier(houseIdentifier);
            //del-String siteHashed=guiAccess.assertSiteIndividual(houseIdentifier); //returns the hashed string of "Site Of_<houseIdentifier>
            //del-guiAccess.assertHasSitePropertyInstance(houseHashed, siteHashed);
            
            layI_Crd.show(paneI, "Model Input");
            
            //del-tmpL=Integer.parseInt(xInput.getText());
            //del-tmpW=Integer.parseInt(yInput.getText());
            //del-tmpH=Integer.parseInt(floorHeightInput.getText());
            //del-tmpF=Integer.parseInt(floorsInput.getText());
            
            guiAccess.assertDataPropertyInstance(currentSite.returnSiteNameHash(), "hasX", currentSite.getSiteLength());
            guiAccess.assertDataPropertyInstance(currentSite.returnSiteNameHash(), "hasY", currentSite.getSiteWidth());
            
            //height is a per-house attribute
            //del-guiAccess.assertDataPropertyInstance(currentSite.returnSiteNameHash(), "hasZ", currentSite.getSiteHeight());
            //number of floors to be automatically inferenced by the csp engine.
            //del-guiAccess.assertDataPropertyInstance(siteHashed, "hasFloors", tmpF);
            
           
            //decDes.assertSiteFact(siteHashed, tmpL, tmpW, tmpH);
        }
        
        //ADD HOUSE BUTTON----------------------------------------------------//
        if (actionEvent.getSource()==addHouseBtn) {
            //String hereWeStoreTheHouse_xxStringLiteral = houseStringReturningMethod();
            currentHouse=new OwlHouse(currentSite.getSiteName());
            int tempIndex=OwlHouse.augmentHouseIndex(currentSite.getSiteName()).intValue();
            //int tempIndex=OwlHouse.readHouseIndex(currentSite.getSiteName());
            //del-DefaultMutableTreeNode currentHouseNode=new DefaultMutableTreeNode(currentHouse.returnHouseEntryInJTree(tempIndex));
            
            guiAccess.assertHouseIndividual(
                    currentHouse.returnHouseIndividualHash(currentSite.getSiteName(), tempIndex),
                    currentHouse.returnHouseIndividualAnnotation(currentSite.getSiteName(), tempIndex));
            guiAccess.assertHasHousePropertyInstance(currentHouse.returnHouseIndividualHash(currentSite.getSiteName(),tempIndex),
                    currentSite.returnSiteNameHash());
            roomsTreePanel.addHouseNodeToTree(currentHouse.returnHouseEntryInJTree(tempIndex));
            currentRoom=new OwlRoom(rooms1.getSelectedItem().toString());   //GIATI TO KANW AYTO EDW??

            Integer tempHouseHeightInteger;
            for (;;)
            {
                String tempHeightString=(String)JOptionPane.showInputDialog(this, "Enter a floor height", "house height input", JOptionPane.DEFAULT_OPTION, null, null, "enter an integer number");
                int height=validateIntegerInput(tempHeightString);
                if(height!=-1) {
                    tempHouseHeightInteger=new Integer(height);
                    System.out.println(tempHouseHeightInteger);
                    break;
                }
            }
            
            
            //creating a temporary ChocoHouseInstance in order to add it in the chocoHouseMap, but also to insert its variables in the choco model.
            
            //keeping the currentHouse instance up to date concerning the height, height will also be altered by the valueChanged method, responding to Jtree selection
            currentHouse.setCurrentHouseHeight(tempHouseHeightInteger);
            //inserting a pair <houseIndividualHash, houseHeight> in the Map residing in the currentSite instance.
            currentSite.getHouseHeightMap().put(currentHouse.returnHouseIndividualHash(currentSite.getSiteName(), tempIndex), new Integer(tempHouseHeightInteger));
            
            //creating a ChocoHouse instance to accommodate both the model and solver variables that will be fed to chocosolver.
            ChocoHouse tempChocoHouse=new ChocoHouse(
                    currentSite,
                    currentHouse.returnHouseIndividualHash(
                        currentSite.getSiteName(),
                        OwlHouse.readHouseIndex(currentSite.getSiteName())),
                    currentHouse.returnHouseEntryInJTree(tempIndex));
            
            //inserting the above instance to a Map in order to be able to recover the variables by their names.
            guiChoco.getChocoHouseMap().put(
                    currentHouse.returnHouseIndividualHash(
                        currentSite.getSiteName(),
                        OwlHouse.readHouseIndex(
                            currentSite.getSiteName())),
                    tempChocoHouse);
            
            //inserting the new house's variables into the choco model.
            ChocoUtility.insertHouseVariables(tempChocoHouse, guiChoco.getTopIxModel());
            ChocoUtility.houseIsPartOfSiteConstraint(currentSite, tempChocoHouse, guiChoco.getTopIxModel());
        }
        
        //ADD ROOM BUTTON-----------------------------------------------------//
        if(actionEvent.getSource()==addRoomBtn)
        {
            //del-String ontologyRoomName=guiAccess.roomToIRI.get(rooms1.getSelectedItem().toString());
            //del-ontologyRoomName=ontologyRoomName.substring(ontologyRoomName.indexOf('#')+1, ontologyRoomName.indexOf('>'));
            /*int tempIndex=*/
            OwlRoom.augmentRoomIndex(currentSite.getSiteName(),
                currentHouse.getSelectedHouseEntry(),
                currentRoom.getComboBoxRoomEntry());
            
            //currentRoom=new OwlRoom(currentSite.getSiteName(), roomsTreePanel.returnSelectedNodeString(), rooms1.getSelectedItem().toString());
            guiAccess.assertRoomIndividual(
                currentRoom.getComboBoxRoomEntry(),
                currentRoom.returnRoomIndividualHash(
                    currentSite.getSiteName(),
                    currentHouse.getSelectedHouseEntry()),
                currentRoom.returnRoomIndividualAnnotation(
                    currentSite.getSiteName(),
                    currentHouse.getSelectedHouseEntry()));
            
            guiAccess.assertHasRoomPropertyInstance(
                    currentHouse.returnHouseIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()),
                    currentRoom.returnRoomIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()));
            //del-String roomIndividualHashTmp;
            
            //creating a temporary ChocoRoom instance to accommodate both the model and solver variables that will be fed to chocosolver.
            ChocoRoom tempChocoRoom=new ChocoRoom(
                    currentRoom.returnRoomIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()),
                    currentRoom.returnRoomEntryInGui(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()),
                    currentSite,
                    currentHouse);
            
            //inserting the above instance to a Map in order to be able to recover the variables by their names.
            guiChoco.getChocoRoomMap().put(
                    currentRoom.returnRoomIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()),
                    tempChocoRoom);
            
            //inserting the new room's variables into the choco model.
            ChocoUtility.insertRoomVariables(tempChocoRoom, guiChoco.getTopIxModel());
            
            ChocoHouse tempChocoHouse=guiChoco
                .getChocoHouseMap()
                .get(
                    currentHouse.returnHouseIndividualHash(
                        currentSite.getSiteName(), 
                        currentHouse.getSelectedHouseEntry()));
            ChocoUtility.roomIsPartOfHouseConstraint(tempChocoHouse, tempChocoRoom, guiChoco.getTopIxModel());
            
            logger.info("room stats");
            logger.info(tempChocoRoom.getRoomLengthVar().getUppB());
            logger.info(tempChocoRoom.getRoomWidthVar().getUppB());
            logger.info(tempChocoRoom.getRoomHeightVar().getUppB());
            logger.info("");
            
            //del-guiAccess.assertHasRoomPropertyInstance(houseHash, roomHash);
            roomsTreePanel.addRoomNodeToTree(currentRoom.returnRoomEntryInGui(currentSite.getSiteName(), currentHouse.getSelectedHouseEntry()));
            roomsTreePanel.roomsTree.expandPath(roomsTreePanel.roomsTree.getPathForRow(0));
            
            logger.info(guiChoco.getTopIxModel().getNbTotVars());
            
            //decDes.assertRoomFact(roomHash);
            //jess.Rete tmpRt=decDes.getRt();
            //try{
            //tmpRt.run();
            //}catch (JessException je){System.out.println(je.getCause().toString());}
            
            //access.saveOntology();
        }
        
        //BACK BUTTON---------------------------------------------------------//
        if(actionEvent.getSource()==backBtn)
        {
            layI_Crd.show(paneI, "Basic Input");
            //System.out.println(guiChoco.getRmNmsToChRmInstncs().toString());
        }
        
        //REGISTER BUTTON-----------------------------------------------------//
        if(actionEvent.getSource()==registerBtn)
        {
            String room1Hash=currentRoom.returnRoomIndividualHash(
                    currentSite.getSiteName(),
                    currentHouse.getSelectedHouseEntry(),
                    currentRoom.getSelectedRoomEntry());
            String room2Hash;
            if(rooms2.isVisible())
            {
                room2Hash=currentRoom.returnRoomIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry(),
                        currentRoom.getComboBox2RoomEntry());
            }
            else
            {
                room2Hash=OwlRoom.DRONEHASH;
            }
            String declarativeProperty=(String)propsCBox.getSelectedItem();
            guiAccess.assertPropertyInstance(declarativeProperty, room1Hash, room2Hash);
            //decDes.assertJessFact(IRI.create(GuiAccess.propEntryNametoPropEntryIRI.get(declarativeProperty)).toString(), room1Hash, room2Hash);

            switch (declarativeProperty) {
                case "Eastwards Adjacent To":
                    ChocoUtility.adjacentEastConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Northwards Adjacent To":
                    ChocoUtility.adjacentNorthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Top-wards Adjacent To":
                    ChocoUtility.adjacentOverConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Southwards Adjacent To":
                    ChocoUtility.adjacentSouthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Bottom-wards Adjacent To":
                    ChocoUtility.adjacentUnderConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Westwards Adjacent To":
                    ChocoUtility.adjacentWestConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Higher Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Longer Than":
                    ChocoUtility.longerThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Lower Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Narrower Than":
                    ChocoUtility.widerThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Shorter Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Wider Than":
                    ChocoUtility.widerThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Higher Than Long":
                    ChocoUtility.higherThanLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Higher Than Wide":
                    ChocoUtility.higherThanWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Longer Than High":
                    ChocoUtility.longerThanHighConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Longer Than Wide":
                    ChocoUtility.longerThanWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Wider Than High":
                    ChocoUtility.widerThanHighConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Wider Than Long":
                    ChocoUtility.widerThanLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Equal Height To":
                    ChocoUtility.equalHeightConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Equal Length To":
                    ChocoUtility.equalLengthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                case "Equal Width To":
                    ChocoUtility.equalWidthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    break;
                default:
                    break;
            }
            logger.info("registered: -> "+guiChoco.getTopIxModel().getNbConstraints());
        }
        
        //CALCULATE BUTTON----------------------------------------------------//
        if (actionEvent.getSource()==calculateBtn) {
            logger.info("calculate-a: -> "+guiChoco.getTopIxModel().getNbConstraints());
            
            //first insert all the extra (non-overlapping & is-part-of constraitns)
            guiChoco.insertNonOverlappingHousesConstraints();
            logger.info("calculate-b: -> "+guiChoco.getTopIxModel().getNbConstraints());
            guiChoco.insertNonOverlappingRoomsConstraints();
            
            logger.info("calculate-c: -> "+guiChoco.getTopIxModel().getNbConstraints());
            if(compactnessChBox.isSelected()) {
                guiChoco.insertSiteCompactnessConstraints(currentSite);
            }
            logger.info("calculate-d: -> "+guiChoco.getTopIxModel().getNbConstraints());
            guiChoco.insertHouseCompactnessConstraints();
            logger.info("calculate-e: -> "+guiChoco.getTopIxModel().getNbConstraints());
            
            boolean lastSolutionFeasible;
            OwlSolution tempSolution;
            
            //initialise the solver and recall call the solution
            guiChoco.getTopIxSolver().read(guiChoco.getTopIxModel());
            lastSolutionFeasible=guiChoco.getTopIxSolver().solve();
            
            int solutionCounter=1;
            if(lastSolutionFeasible) {
                logger.info("solved the first.");
                
                guiChoco.initialiseChocoHouseResVars();
                guiChoco.initialiseChocoRoomResVars();
                
                tempSolution=new OwlSolution(
                        new Integer(solutionCounter),
                        new Integer(currentSite.getSiteLength()),
                        new Integer(currentSite.getSiteWidth()),
                        guiChoco.getChocoHouseMap(),
                        guiChoco.getChocoRoomMap());
                guiAccess.getSolutionsList().add(tempSolution);
            }
            else {
                logger.info("not possible the first solution");
            }
            
//            while(lastSolutionFeasible && solutionCounter<=30) {
//                lastSolutionFeasible=guiChoco.getTopIxSolver().nextSolution();
//                if (lastSolutionFeasible) {
//                    //solutionCounter++;
//                    guiChoco.initialiseChocoHouseResVars();
//                    guiChoco.initialiseChocoRoomResVars();
//                    tempSolution=new OwlSolution(
//                            ++solutionCounter,
//                            new Integer(currentSite.getSiteLength()),
//                            new Integer(currentSite.getSiteWidth()),
//                            guiChoco.getChocoHouseMap(),
//                            guiChoco.getChocoRoomMap());
//                    guiAccess.getSolutionsList().add(tempSolution);
//                //guiAccess.registerSolution(guiChoco.getChocoHouseMap(), guiChoco.getChocoRoomMap());
//                }
//            }
//            if(!(guiAccess.getSolutionsList().isEmpty()))
//                guiAccess.storeSolutions(currentSite.returnSiteNameHash());
//            else
//                logger.info("MANTARA, DEN EXEI LYSEIS TO SENARIO!!!!");
            
            
            
            logger.info("THE SOLVER RETURNED!");
//            guiAccess.storeSolutions(currentSite.returnSiteNameHash());
            int totalSolutionsCounter=1;
            while(lastSolutionFeasible) {
                lastSolutionFeasible=guiChoco.getTopIxSolver().nextSolution();
                totalSolutionsCounter++;
            }
            
            logger.info(totalSolutionsCounter);
//            IntDomainVar tmpVar;
//            for (String s:guiChoco.getChocoRoomMap().keySet()) {
//                System.out.println(guiChoco.getChocoRoomMap().get(s).getRoomIndividualHash());
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomLengthVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomWidthVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomHeightVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomXVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomYVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                tmpVar=guiChoco.getTopIxSolver().getVar(guiChoco.getChocoRoomMap().get(s).getRoomZVar());
//                System.out.println(String.valueOf(tmpVar.getVal()));
//                System.out.println();
//            }
        }
        
        //SAVE BUTTON---------------------------------------------------------//
        if(actionEvent.getSource()==saveBtn)
        {
            guiAccess.saveOntology();
            this.availableSitesCBox.removeAllItems();
            this.populateResultsComponents();
        }
        
        //EVAL BUTTON---------------------------------------------------------//
        //if(evt.getActionCommand().equals("evalExpression"))
        //{
        //   try
        //    {
        //        decDes.getRt().eval(jessCommandField.getText());
        //    }catch(JessException je){}
        //}
        
        
        //ROOMS2 COMBO BOX----------------------------------------------------//
        if ((actionEvent.getSource()==rooms2)&&(rooms2.getItemCount()>1)) {
            currentRoom.setComboBox2RoomEntry(rooms2.getSelectedItem().toString());
        }
        
        //AVAILABLESITES COMBO BOX--------------------------------------------//
        if (actionEvent.getSource()==availableSitesCBox&&availableSitesCBox.getItemCount()>0) {
            OWLIndividual tempOwlInd=(OWLIndividual)availableSitesCBox.getSelectedItem();
            String tempSiteToBeLoaded=tempOwlInd.toString().substring(tempOwlInd.toString().indexOf('#')+1, tempOwlInd.toString().length()-1);
            guiAccess.retrieveSolutions(tempSiteToBeLoaded);
            Vector<OwlSolution> tempSolutionVector=new Vector<>(guiAccess.getSolutionsList());
            logger.info(tempSolutionVector.size());
            ComboBoxModel<OwlSolution> solutionsModel=new DefaultComboBoxModel<>(tempSolutionVector);
            availableSolutionsCBox.setModel(solutionsModel);
        }
        
        //AVAILABLESOLUTIONS COMBO BOX----------------------------------------//
        if (actionEvent.getSource()==availableSolutionsCBox) {
            logger.info("avail sol cbox evt");
            OwlSolution tempSolution=(OwlSolution)availableSolutionsCBox.getSelectedItem();
            this.topIx3D.renderSolution(tempSolution, renderSolidChBox.isSelected());
            //this.topIx3D.repaintCanvas();
//            for (OwlSolvedHouse tempHouse:testSol.getSolvedHouses()) {
//                logger.info(tempHouse.getSolvedHouseLength());
//            }
            logger.info("");
//            for (OwlSolvedRoom tempRoom:testSol.getSolvedRooms()) {
//                logger.info(tempRoom.getSolvedRoomWidth());
//            }
        }
        
        //RENDERSOLIDCHECKBOX CHECKBOX----------------------------------------//
        if(actionEvent.getSource()==renderSolidChBox && availableSolutionsCBox.getSelectedItem()!=null) {
            OwlSolution tempSolution=(OwlSolution)availableSolutionsCBox.getSelectedItem();
            topIx3D.renderSolution(tempSolution, renderSolidChBox.isSelected());
        }
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode) roomsTreePanel.getTree().getLastSelectedPathComponent();
        //del-DefaultMutableTreeNode tempRootNode=(DefaultMutableTreeNode) roomsTreePanel.getTree().getModel().getRoot();
        //del-int treeDepth=tempRootNode.getDepth();
        int selectedNodeDepth=selectedNode.getLevel();
        
        if(true) {
            //to gather all posibly uninitialised instances necessary for the actions performed when selecting a node
            //and initialise them here, as soon as a node is inserted in the tree.
            //if (instance==null) {
            //  instance=new Instance();
            //}
        }
        
        //in case selectedNode is ROOT (SITENAME)
        if (selectedNodeDepth==0) {
            this.addHouseBtn.setEnabled(true);
            this.addRoomBtn.setEnabled(false);
            this.registerBtn.setEnabled(false);
            if (!(selectedNode.isLeaf()))
                this.calculateBtn.setEnabled(true);
            else
                this.calculateBtn.setEnabled(false);
            //this.currentHouse=null; //TO RECONSIDER THESE TWO LINES!!! (maybe my comment out of the node selection if's will compensate for that).
            //this.currentRoom=null;
            
            rooms2.removeAllItems();
        }
        //in case selectedNode is LEVEL_1 (HOUSE)
        else if (selectedNodeDepth==1) {
            this.addRoomBtn.setEnabled(true);
            this.addHouseBtn.setEnabled(false);
            this.registerBtn.setEnabled(false);
            
            
            this.currentHouse.setSelectedHouseEntry(selectedNode.toString());
            //keeping the currentHouseHeight up to date.
            this.currentHouse.setCurrentHouseHeight(currentSite.getHouseHeightMap().get(currentHouse.returnHouseIndividualHash(currentSite.getSiteName(), currentHouse.getSelectedHouseEntry())));
            
            rooms2.removeAllItems();
            for (int i=0; i<selectedNode.getChildCount(); i++) {
                rooms2.addItem(selectedNode.getChildAt(i).toString());
            }
            rooms2.removeItem(currentRoom.getSelectedRoomEntry());
        }
        //in case selectedNode is LEAF (ROOM)
        else if (selectedNodeDepth==2) {
            this.addHouseBtn.setEnabled(false);
            this.addRoomBtn.setEnabled(false);
            this.registerBtn.setEnabled(true);
            this.currentHouse.setSelectedHouseEntry(selectedNode.getParent().toString());
            this.currentRoom.setSelectedRoomEntry(selectedNode.toString());
            //keeping the currentHouseHeight up to date.
            this.currentHouse.setCurrentHouseHeight(currentSite.getHouseHeightMap().get(currentHouse.returnHouseIndividualHash(currentSite.getSiteName(), currentHouse.getSelectedHouseEntry())));
            
            DefaultMutableTreeNode tempHouseNode=(DefaultMutableTreeNode)selectedNode.getParent();
            rooms2.removeAllItems();
            for (int i=0; i<tempHouseNode.getChildCount(); i++) {
                rooms2.addItem(tempHouseNode.getChildAt(i).toString());
            }
            rooms2.removeItem(currentRoom.getSelectedRoomEntry());
        }
    }
    
    
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource()==rooms1) {
            if (currentRoom==null) {
                currentRoom=new OwlRoom();
                currentRoom.setComboBoxRoomEntry(rooms1.getSelectedItem().toString());
            }
            currentRoom.setComboBoxRoomEntry(rooms1.getSelectedItem().toString());
        }
        
        if (itemEvent.getSource()==rooms2) {
            currentRoom.setComboBox2RoomEntry(rooms2.getSelectedItem().toString());
        }
    }
    
    @Override
    public void focusGained(FocusEvent evt)
    {
        if(evt.getSource()==jessCommandField)
        {
            jessCommandField.selectAll();
        }
    }
    
    @Override
    public void focusLost(FocusEvent evt){}
    
    @Override
    public void keyPressed(KeyEvent evt){keyTyped(evt);}
    
    @Override
    public void keyReleased(KeyEvent evt){}
    
    @Override
    public void keyTyped(KeyEvent evt)
    {
        if((evt.getSource()==jessCommandField)&&(evt.getKeyCode()==13))
        {
            jessCommandField.setText("");
        }
    }
    
    int validateIntegerInput(String integerInput) {
        try {
            int returnInt=Integer.parseInt(integerInput);
            if (returnInt<=0)
                throw(new NumberFormatException());
            return returnInt;
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}