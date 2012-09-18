/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topIx;


import choco.cp.solver.propagation.ChocoEngine;
import choco.kernel.model.variables.integer.IntegerConstantVariable;
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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
    GroupLayout mainLayout;
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
    JButton manualInputBtn;
    JButton calculateBtn;
    JButton resetBtn;
    JButton backBtn;
    JButton saveBtn;
    GroupLayout layIb3_Grp;
    //paneII components     - results presentation
    JPanel paneIIa;
    GroupLayout layII_Grp;
    //paneIIa components    - result panel controls.
    JLabel availableSitesCBoxLabel;
    JLabel availableSolutionsCBoxLabel;
    JLabel renderSolidLabel;
    JComboBox<String> availableSitesCBox;
    JComboBox<OwlSolution> availableSolutionsCBox;
    JCheckBox renderSolidChBox;
    GroupLayout layIIa_Grp;
    //navigation buttons
    JButton siteLeftBtn;
    JButton siteRightBtn;
    JButton solutionLeftBtn;
    JButton solutionRightBtn;
    //icons for the navigation buttons
    Icon left;
    Icon right;
    
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
    
    //manualInput dialog frame and its components
    JDialog manualInputDialog;
    JLabel manualPropertyListLabel;
    JLabel manualPropertyValueLabel;
    JButton manualInputRegisterButton;
    JButton manualInputCancelButton;
    JComboBox<String> manualPropertyComboBox;
    JTextField manualPropertyValueTextField;
    GroupLayout manualInputLay_Grp;
    
    //DeclarativeDescription decDes;
    
    Logger logger;
    
    //the following var is a flag that is set everytime a selection is made in
    //the JTree, and provides info as to whether the site, a house or a room is
    //currently selected.
    int selectedNodeDepth;
    
    //deprecated constructor - the one with both OntologyAccessUtility and TopIxChoco references is used.
    public GUI(OntologyAccessUtility accessRef)
    {
        super("topIx project - bromoiras-gioukakis");
        mainPanel=new JPanel();
        
        guiAccess=accessRef;
        
        selectedNodeDepth=0;
        
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
        mainLayout=new GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        guiAccess=accessRef;
        guiChoco=chocoRef;
        //this.decDes=decDes;
        
        initializeComponents(guiAccess.propEntryNameToPropCatName, guiAccess.roomToIRI);
        addComponents();
        
        this.setIconImage(new ImageIcon("src/ontologyresources/images/topix.png").getImage());
        //setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setMaximumSize(new Dimension(600, 600));
        setBounds(100, 100, 950, 500);
        this.setMaximumSize(new Dimension(950, 500));
        this.setMinimumSize(new Dimension(950, 500));
        setMaximizedBounds(this.getBounds());
        //populateResultsComponents();
        
        propsCBoxLabel.setVisible(true);
        propsCBox.setVisible(true);
        catsCBox.setSelectedIndex(1);
        rooms2Label.setVisible(false);
        rooms2.setVisible(false);
        //this.pack();
        //tabs.getTabComponentAt(tabs.indexOfTab("Results")).setVisible(false);
        //layI_Crd.show(paneI, "Model Input");
        //BasicConfigurator.configure();
        
        //add listeners to manage the mouse events on a panel level.
    }

    private void initializeComponents(Map<String, String> objCatsCB, Map<String, String> roomsCB)     //initialize panels with their layouts as well as components
                                            
    {
        paneI=new JPanel();
        layI_Crd=new CardLayout();
        paneI.setLayout(layI_Crd);
        paneII=new JPanel();
        layII_Grp=new GroupLayout(paneII);
        paneII.setLayout(layII_Grp);
            tabs=new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
            tabs.setSize(new Dimension(1000, 600));
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
            manualInputBtn=new JButton("Manual Input");
                manualInputBtn.addActionListener(this);
                manualInputBtn.setEnabled(false);
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
        paneIIa=new JPanel();
        layIIa_Grp=new GroupLayout(paneIIa);
        paneIIa.setLayout(layIIa_Grp);
            availableSitesCBoxLabel=new JLabel("Sites");
            availableSolutionsCBoxLabel=new JLabel("Arrangements");
            availableSitesCBox=new JComboBox<>();
                availableSitesCBox.addActionListener(this);
                availableSitesCBox.addItemListener(this);
            availableSolutionsCBox=new JComboBox<>();
                availableSolutionsCBox.addActionListener(this);
                availableSolutionsCBox.addItemListener(this);
            renderSolidLabel=new JLabel("Render as Solid");
            renderSolidChBox=new JCheckBox();
                renderSolidChBox.setSelected(false);
                renderSolidChBox.addActionListener(this);
            left=new ImageIcon("src/ontologyresources/images/left.png");
                //left.
            right=new ImageIcon("src/ontologyresources/images/right.png");
            siteLeftBtn=new JButton(left);
                siteLeftBtn.addActionListener(this);
            siteRightBtn=new JButton(right);
                siteRightBtn.addActionListener(this);
            solutionLeftBtn=new JButton(left);
                solutionLeftBtn.addActionListener(this);
            solutionRightBtn=new JButton(right);
                solutionRightBtn.addActionListener(this);
                
        topIx3D=new TopIx3D();
        
        
        //try
        //{
//            jessMonitorPane=new JPanel(new BorderLayout());
//            //jessPane=new ConsolePanel(decDes.getRt());
//            jessCommandField=new JTextField(60);
//                jessCommandField.setActionCommand("evalExpression");    
//                jessCommandField.addFocusListener(this);
//                jessCommandField.addActionListener(this);
//                jessCommandField.addKeyListener(this);
//            evalButton=new JButton("eval");
//                evalButton.setActionCommand("evalExpression");
//                evalButton.addActionListener(this);     
        //}catch (JessException je){System.out.println(je.getExecutionContext()+"\n"+je.getErrorCode()+" @line "+je.getLineNumber());}
        this.populateResultsComponents();
        availableSitesCBox.setSelectedItem(availableSitesCBox.getItemAt(0));
        availableSolutionsCBox.setSelectedItem(availableSolutionsCBox.getItemAt(0));
        siteLeftBtn.setEnabled(false);
        solutionLeftBtn.setEnabled(false);
        //--------------------------------------------------------------------//
        manualInputDialog=new JDialog(this, "Manual Data Input", true);
        manualInputDialog.setVisible(false);
        manualInputDialog.setIconImage(new ImageIcon("src/ontologyresources/images/topix.png").getImage());
        manualInputLay_Grp=new GroupLayout(manualInputDialog.getContentPane());
            manualInputDialog.getContentPane().setLayout(manualInputLay_Grp);
        manualPropertyListLabel=new JLabel("Property List");
        manualPropertyValueLabel=new JLabel("Value");
        manualInputRegisterButton=new JButton("Register");
            manualInputRegisterButton.addActionListener(this);
        manualInputCancelButton=new JButton("Cancel");
            manualInputCancelButton.addActionListener(this);
        manualPropertyComboBox=new JComboBox<>();
        manualPropertyValueTextField=new JTextField(4);
               
        //--------------------------------------------------------------------//
    }
    
    private void addComponents()            //add components to their respective containers
    {
        //fill paneIa
        layIa_Grp.setAutoCreateGaps(true);
        layIa_Grp.setHorizontalGroup(
                layIa_Grp.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(houseIdentifierLabel)
                        .addComponent(xLabel)
                        .addComponent(yLabel)
                        .addComponent(floorsLabel)
                        .addComponent(floorHeightLabel))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(houseIdentifierInput, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(xInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(yInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(floorsInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(floorHeightInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(nextBtn))
                    .addContainerGap());
        layIa_Grp.setVerticalGroup(
                layIa_Grp.createSequentialGroup()
                    .addContainerGap()
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
                        .addComponent(nextBtn))
                    .addContainerGap());
        //fill paneIb1
        //paneIb1.add(roomsTreePanel);
        //fill paneIb2
        layIb2_Grp.setHonorsVisibility(false);
        layIb2_Grp.setAutoCreateGaps(true);
        layIb2_Grp.setHorizontalGroup(
                layIb2_Grp.createSequentialGroup()
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(rooms1Label)
                        .addComponent(rooms1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layIb2_Grp.createSequentialGroup()
                            .addComponent(compactnessChBoxLabel)
                            .addComponent(compactnessChBox)))
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
        layIb3_Grp.setAutoCreateGaps(true);
        layIb3_Grp.setHorizontalGroup(
                layIb3_Grp.createSequentialGroup()
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addHouseBtn)
                        .addComponent(addRoomBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(registerBtn)
                        .addComponent(manualInputBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(calculateBtn)
                        .addComponent(resetBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(saveBtn)
                        .addComponent(backBtn)));
        layIb3_Grp.setVerticalGroup(
                layIb3_Grp.createSequentialGroup()
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addHouseBtn)
                        .addComponent(registerBtn)
                        .addComponent(calculateBtn)
                        .addComponent(saveBtn))
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addRoomBtn)
                        .addComponent(manualInputBtn)
                        .addComponent(resetBtn)
                        .addComponent(backBtn)));
        //fill paneIb
        layIb_Grp.setHorizontalGroup(
                layIb_Grp.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(roomsTreePanel, 100, roomsTreePanel.getSize().width, 250)
                    .addGap(15)
                    .addGroup(layIb_Grp.createParallelGroup()
                        .addComponent(paneIb2)
                        .addComponent(paneIb3))
                    .addContainerGap());
        layIb_Grp.setVerticalGroup(
                layIb_Grp.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layIb_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(roomsTreePanel)
                        .addComponent(paneIb2))
                    .addComponent(paneIb3)
                    .addContainerGap());
        //fill paneIIa
        //layIIa_Grp = new javax.swing.GroupLayout(getContentPane());
        //getContentPane().setLayout(layIIa_Grp);
        layIIa_Grp.setAutoCreateGaps(true);
        layIIa_Grp.setHorizontalGroup(
            layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layIIa_Grp.createSequentialGroup()
                .addContainerGap()
                .addGroup(layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layIIa_Grp.createSequentialGroup()
                        .addComponent(availableSitesCBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(siteLeftBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(siteRightBtn))
                    .addComponent(availableSitesCBox, 0, 200, Short.MAX_VALUE)
                    .addGroup(layIIa_Grp.createSequentialGroup()
                        .addComponent(availableSolutionsCBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(solutionLeftBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(solutionRightBtn))
                    .addComponent(availableSolutionsCBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, layIIa_Grp.createSequentialGroup()
                        .addComponent(renderSolidLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(renderSolidChBox))
                .addGap(0, 0, Short.MAX_VALUE)))
        );
        layIIa_Grp.setVerticalGroup(
            layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layIIa_Grp.createSequentialGroup()
                .addContainerGap()
                .addGroup(layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(siteRightBtn)
                    .addGroup(layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(siteLeftBtn)
                        .addComponent(availableSitesCBoxLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availableSitesCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layIIa_Grp.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableSolutionsCBoxLabel)
                    .addComponent(solutionLeftBtn)
                    .addComponent(solutionRightBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availableSolutionsCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layIIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(renderSolidLabel)
                    .addComponent(renderSolidChBox))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        paneI.add(paneIa, "Basic Input");
        paneI.add(paneIb, "Model Input");
        
        //setting the layout for paneII
        layII_Grp.setHorizontalGroup(
                layII_Grp.createSequentialGroup()
                .addComponent(paneIIa, 250, 250, 300)
                //.addContainerGap()
                .addGap(15)
                .addComponent(topIx3D)
        );
        layII_Grp.setVerticalGroup(
                layII_Grp.createSequentialGroup()
                .addContainerGap()
                .addGroup(layII_Grp.createParallelGroup()
                    .addComponent(paneIIa)
                    .addComponent(topIx3D))
                .addContainerGap()
        );
        //paneII.add(topIx3D, BorderLayout.CENTER);
        
        
        //jessMonitorPane.add(BorderLayout.CENTER, jessPane);
        //jessMonitorPane.add(BorderLayout.SOUTH, jessCommandField);
        //jessMonitorPane.add(BorderLayout.EAST, evalButton);
        //tabs.setSize(new Dimension(1100, 700));
        //paneI.setSize(1300, 600);
        //paneII.setSize(1300, 600);
        //paneII.validate();
        tabs.addTab("Input", paneI);
        tabs.addTab("Results", paneII);
        //tabs.addTab("Jess Monitor", jessMonitorPane);
        //mainPanel.setSize(new Dimension(1100, 700));
        
        //setting the layout for mainPanel
        GroupLayout mainPanelLay_Grp=(GroupLayout)mainPanel.getLayout();
        mainPanelLay_Grp.setHorizontalGroup(
                mainPanelLay_Grp.createSequentialGroup()
                //.addContainerGap()
                .addComponent(tabs)
                //.addContainerGap()
        );
        mainPanelLay_Grp.setVerticalGroup(
                mainPanelLay_Grp.createSequentialGroup()
                //.addContainerGap()
                .addComponent(tabs)
                //.addContainerGap()
        );
        
        //setting the layout for manualInputDialog
        manualInputLay_Grp.setAutoCreateContainerGaps(true);
        manualInputLay_Grp.setAutoCreateGaps(true);
        manualInputLay_Grp.setHorizontalGroup(
                manualInputLay_Grp.createSequentialGroup()
                .addGroup(manualInputLay_Grp.createParallelGroup()
                    .addComponent(manualPropertyListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualPropertyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(manualInputLay_Grp.createSequentialGroup()
                        .addComponent(manualInputCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(manualInputRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(manualInputLay_Grp.createParallelGroup()
                    .addComponent(manualPropertyValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualPropertyValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        manualInputLay_Grp.setVerticalGroup(
                manualInputLay_Grp.createSequentialGroup()
                .addGroup(manualInputLay_Grp.createParallelGroup()
                    .addComponent(manualPropertyListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualPropertyValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(manualInputLay_Grp.createParallelGroup()
                    .addComponent(manualPropertyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualPropertyValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(manualInputLay_Grp.createParallelGroup()
                    .addComponent(manualInputCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manualInputRegisterButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        manualInputDialog.setLocationRelativeTo(manualInputDialog.getOwner());
        manualInputDialog.setResizable(false);
        manualInputDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        manualInputDialog.pack();
        
        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.pack();
    }
    
    public void populateResultsComponents () {
        if (!guiAccess.returnSitesInOntology().isEmpty()) {
            //THE NEXT IS A VERY IMPORTANT LINE TO KNOW, VERY USEFUL AS A CASTING METHOD!!!!
            String[] tempModelArray=(String[])guiAccess.returnSitesInOntology().keySet().toArray(new String[0]);
            ComboBoxModel<String> tempModel=new DefaultComboBoxModel<>(tempModelArray);
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
                                                                            //isws apla arxikopoiw to currentRoom gia thn akolouthi xrhsh toy...?

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
            
            logger.info(guiChoco.getChocoHouseMap().toString());
            logger.info(currentHouse.returnHouseIndividualHash(
                        currentSite.getSiteName(),
                        currentHouse.getSelectedHouseEntry()));
            
            //inserting the new house's variables into the choco model.
            ChocoUtility.insertHouseVariables(tempChocoHouse, guiChoco.getTopIxModel());
            ChocoUtility.houseIsPartOfSiteConstraint(currentSite, tempChocoHouse, guiChoco.getTopIxModel());
            ChocoUtility.houseHeightTimesFloorHeightConstraint(tempChocoHouse, tempHouseHeightInteger.intValue(), guiChoco.getTopIxModel());
            logger.info("tempchocohouse1");
            logger.info(tempChocoHouse);
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
            logger.info("tempchocohouse2");
            logger.info(tempChocoHouse);
            
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
                case "Long":
                    ChocoUtility.isLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Short":
                    ChocoUtility.isShortConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Wide":
                    ChocoUtility.isWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    break;
                case "Narrow":
                    ChocoUtility.isNarrowConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
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
            guiChoco.insertHouseVolumeCompactnessConstraints();
            logger.info("calculate-e: -> "+guiChoco.getTopIxModel().getNbConstraints());
            
            boolean lastSolutionFeasible;
            OwlSolution tempSolution;
            guiAccess.getSolutionsList().clear();
            
            //initialise the solver and recall call the solution
            logger.info("the variables of the model in string");
            logger.info(guiChoco.getTopIxModel().varsToString());
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
                //this.populateResultsComponents();
                tempSolution.toString();
            }
            else {
                logger.info("not possible the first solution");
            }
            
            while(lastSolutionFeasible && solutionCounter<=100) {
                lastSolutionFeasible=guiChoco.getTopIxSolver().nextSolution();
                if (lastSolutionFeasible) {
                    //solutionCounter++;
                    guiChoco.initialiseChocoHouseResVars();
                    guiChoco.initialiseChocoRoomResVars();
                    tempSolution=new OwlSolution(
                            ++solutionCounter,
                            new Integer(currentSite.getSiteLength()),
                            new Integer(currentSite.getSiteWidth()),
                            guiChoco.getChocoHouseMap(),
                            guiChoco.getChocoRoomMap());
                    guiAccess.getSolutionsList().add(tempSolution);
                //guiAccess.registerSolution(guiChoco.getChocoHouseMap(), guiChoco.getChocoRoomMap());
                }
            }
            
            logger.info("--------------------------------------------------------------------------1070");
            logger.info(guiAccess.getSolutionsList().toString());
            logger.info("-------------------------------------------------------------------END OF 1070");
            
            if(!(guiAccess.getSolutionsList().isEmpty()))
            {
                
                guiAccess.storeSolutions(currentSite.returnSiteNameHash());
            }
            else
                logger.info("MANTARA, DEN EXEI LYSEIS TO SENARIO!!!!");
            
            
            
            logger.info("THE SOLVER RETURNED!");
//            guiAccess.storeSolutions(currentSite.returnSiteNameHash());
//            int totalSolutionsCounter=1;
//            while(lastSolutionFeasible && totalSolutionsCounter<=30) {
//                lastSolutionFeasible=guiChoco.getTopIxSolver().nextSolution();
//                totalSolutionsCounter++;
//            }
            
//            logger.info(totalSolutionsCounter);
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
            OWLIndividual tempOwlInd=guiAccess.returnSitesInOntology().get(availableSitesCBox.getSelectedItem());
            //isolating the site hash from its IRI in order to...
            String tempSiteToBeLoaded=tempOwlInd.toString().substring(tempOwlInd.toString().indexOf('#')+1, tempOwlInd.toString().length()-1);
            //...pass it as a parameter to the method retrieveSolutions which in turn...
            guiAccess.retrieveSolutions(tempSiteToBeLoaded);
            //...populates the solutionList attribute that will accommodate the set of the solutions
            //each time the selected site refreshes.
            Vector<OwlSolution> tempSolutionVector=new Vector<>(guiAccess.getSolutionsList());
            logger.info(tempSolutionVector.size());
            ComboBoxModel<OwlSolution> solutionsModel=new DefaultComboBoxModel<>(tempSolutionVector);
            availableSolutionsCBox.setModel(solutionsModel);
            availableSolutionsCBox.setSelectedIndex(0);
        }
        
        //AVAILABLESOLUTIONS COMBO BOX----------------------------------------//
        if (actionEvent.getSource()==availableSolutionsCBox) {
            logger.info("avail sol cbox evt");
            OwlSolution tempSolution=(OwlSolution)availableSolutionsCBox.getSelectedItem();
            if(tempSolution!=null)
                this.topIx3D.renderSolution(tempSolution, renderSolidChBox.isSelected());
            //this.addMouseListener(topIx3D.getMouseTranslate());
            //this.addMouseListener(topIx3D.getMouseRotate());
            //this.addMouseMotionListener(topIx3D.getMouseTranslate());
            //this.addMouseMotionListener(topIx3D.getMouseRotate());
            //this.addMouseWheelListener(topIx3D.getMouseZoom());
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
        
        //SITELEFT CHECKBOX---------------------------------------------------//
        if(actionEvent.getSource()==siteLeftBtn && availableSitesCBox.getItemCount()!=0) {
            if (availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()-1)!=null)
                availableSitesCBox.setSelectedItem(availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()-1));
        }
        
        //SITERIGHT CHECKBOX--------------------------------------------------//
        if(actionEvent.getSource()==siteRightBtn && availableSitesCBox.getItemCount()!=0) {
            if(availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()+1)!=null)
                availableSitesCBox.setSelectedItem(availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()+1));
        }
        
        //SOLUTIONLEFT CHECKBOX-----------------------------------------------//
        if(actionEvent.getSource()==solutionLeftBtn && availableSitesCBox.getItemCount()!=0) {
            if(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()-1)!=null)
                availableSolutionsCBox.setSelectedItem(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()-1));
        }
        
        //SOLUTIONRIGHT CHECKBOX----------------------------------------------//
        if(actionEvent.getSource()==solutionRightBtn && availableSolutionsCBox.getItemCount()!=0) {
            if(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()+1)!=null)
                availableSolutionsCBox.setSelectedItem(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()+1));
        }
        
        //MANUAL INPUT BUTTON-------------------------------------------------//
        if(actionEvent.getSource()==manualInputBtn){
            logger.info("stats for geometric property maps");
            logger.info(guiAccess.geometricPropertiesMap.size());
            logger.info(guiAccess.houseSetableGeometricPropertiesMap.size());
            logger.info(guiAccess.roomSetableGeometricPropertiesMap.size());
            
            this.manualPropertyValueTextField.setText("");
            
            if(selectedNodeDepth==1){
                String[] tempModelArray=(String[])guiAccess.houseSetableGeometricPropertiesMap.keySet().toArray(new String[0]);
                ComboBoxModel<String> tempModel=new DefaultComboBoxModel<>(tempModelArray);
                this.manualPropertyComboBox.setModel(tempModel);
            }
            else if(selectedNodeDepth==2){
                String[] tempModelArray=(String[])guiAccess.roomSetableGeometricPropertiesMap.keySet().toArray(new String[0]);
                ComboBoxModel tempModel=new DefaultComboBoxModel(tempModelArray);
                this.manualPropertyComboBox.setModel(tempModel);
            }
            this.manualPropertyComboBox.setSelectedItem(manualPropertyComboBox.getItemAt(0));
            this.manualInputDialog.setVisible(true);
        }
        
        //MANUAL INPUT CANCEL BUTTON------------------------------------------//
        if(actionEvent.getSource()==manualInputCancelButton){
            manualInputDialog.setVisible(false);
        }
        
        //MANUAL INPUT REGISTER BUTTON----------------------------------------//
        if(actionEvent.getSource()==manualInputRegisterButton){            
            String selectedProperty=(String)manualPropertyComboBox.getSelectedItem();
            int tempValue=validateIntegerInput2(this.manualPropertyValueTextField.getText());
            if(tempValue==-1){
                JOptionPane.showMessageDialog(manualInputDialog,
                        "Please, enter an integer value, greater or equal to zero.",
                        "Invalid entry...",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                if(selectedNodeDepth==1){       //this is for a house entity
                    String houseHash=currentHouse.returnHouseIndividualHash(
                            currentSite.getSiteName(),
                            currentHouse.getSelectedHouseEntry());
                    ChocoHouse tempHouse=guiChoco.getChocoHouseMap().get(houseHash);
                    if("Has X".equals(selectedProperty) && tempValue<=currentSite.getSiteLength()){
                        ChocoUtility.positionXisConstraint(tempHouse, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Y".equals(selectedProperty) && tempValue<=currentSite.getSiteWidth()){
                        ChocoUtility.positionYisConstraint(tempHouse, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Length".equals(selectedProperty) && tempValue>0 && tempValue<=currentSite.getSiteLength()-1){
                        ChocoUtility.lengthIsConstraint(tempHouse, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Width".equals(selectedProperty) && tempValue>0 && tempValue<=currentSite.getSiteWidth()-1){
                        ChocoUtility.widthIsConstraint(tempHouse, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if(("Has X".equals(selectedProperty) && tempValue>currentSite.getSiteLength()) ||
                            ("Has Y".equals(selectedProperty) && tempValue>currentSite.getSiteWidth()) ||
                            ("Has Length".equals(selectedProperty) && tempValue>0 && tempValue>currentSite.getSiteLength()-1) ||
                            ("Has Width".equals(selectedProperty) && tempValue>0 && tempValue>currentSite.getSiteWidth()-1)){
                        JOptionPane.showMessageDialog(
                                this.manualInputDialog,
                                String.format(
                                    "Please, enter appopriate values! for X <=%d, for Y<=%d, for Length <=%d, for Width <=%d",
                                    currentSite.getSiteLength(),
                                    currentSite.getSiteWidth(),
                                    currentSite.getSiteLength()-1,
                                    currentSite.getSiteWidth()-1),
                                "Invalid Entry",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                manualInputDialog,
                                "Please, enter a positive number for either length or width variables.",
                                "Invalid Entry",
                                JOptionPane.WARNING_MESSAGE); 
                        manualPropertyValueTextField.setText("");
                    }
                }
                else if(selectedNodeDepth==2){  //this is for a room entity
                    String roomHash=currentRoom.returnRoomIndividualHash(
                            currentSite.getSiteName(),
                            currentHouse.getSelectedHouseEntry(),
                            currentRoom.getSelectedRoomEntry());
                    ChocoRoom tempRoom=guiChoco.getChocoRoomMap().get(roomHash);
                    if("Has X".equals(selectedProperty) && tempValue<=currentSite.getSiteLength()){
                        ChocoUtility.positionXisConstraint(tempRoom, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Y".equals(selectedProperty) && tempValue<=currentSite.getSiteWidth()){
                        ChocoUtility.positionYisConstraint(tempRoom, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Z".equals(selectedProperty) && tempValue%currentHouse.getCurrentHouseHeight()==0){
                        ChocoUtility.positionZisConstraint(tempRoom, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Z".equals(selectedProperty) && tempValue%currentHouse.getCurrentHouseHeight()!=0){
                        JOptionPane.showMessageDialog(this.manualInputDialog, "Please, set the room Z to be exact multiple of the floor height, which is "+currentHouse.getCurrentHouseHeight()+".", "Invalid Entry", JOptionPane.WARNING_MESSAGE);
                    }
                    else if("Has Length".equals(selectedProperty) && tempValue>0 && tempValue<=currentSite.getSiteLength()-1){
                        ChocoUtility.lengthIsConstraint(tempRoom, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if("Has Width".equals(selectedProperty) && tempValue>0 && tempValue<=currentSite.getSiteWidth()-1){
                        ChocoUtility.widthIsConstraint(tempRoom, tempValue, guiChoco.getTopIxModel());
                        this.manualInputDialog.setVisible(false);
                    }
                    else if(("Has X".equals(selectedProperty) && tempValue>currentSite.getSiteLength()) ||
                            ("Has Y".equals(selectedProperty) && tempValue>currentSite.getSiteWidth()) ||
                            ("Has Length".equals(selectedProperty) && tempValue>0 && tempValue>currentSite.getSiteLength()-1) ||
                            ("Has Width".equals(selectedProperty) && tempValue>0 && tempValue>currentSite.getSiteWidth()-1)){
                        JOptionPane.showMessageDialog(
                                this.manualInputDialog,
                                String.format(
                                    "Please, enter appopriate values! for X <=%d, for Y<=%d, for Length <=%d, for Width <=%d",
                                    currentSite.getSiteLength(),
                                    currentSite.getSiteWidth(),
                                    currentSite.getSiteLength()-1,
                                    currentSite.getSiteWidth()-1),
                                "Invalid Entry",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(
                                manualInputDialog,
                                "Please, enter a positive number for either length or width variables.",
                                "Invalid Entry",
                                JOptionPane.WARNING_MESSAGE); 
                        manualPropertyValueTextField.setText("");
                    }
                }
            }
        }
        
        //RESET BUTTON--------------------------------------------------------//
        //will clear all the structures and return the application in its load state.
        if(actionEvent.getSource()==resetBtn){
            //clears the tree and the whole tree model except for the root node!
            this.roomsTreePanel.clearTree();
            //will clear the whole roomInstanceCounters map, since the siteName
            //does not play a particular role there.
            OwlRoom.resetRoomInstanceCounters();
            //will clear the current site entry of the static map houseInstancesPerSite
            //which resides in the OwlHouse class
            OwlHouse.resetHouseIndex(currentSite.getSiteName());
            //reloading the ontology, thus doign away with any addition made so far.
            this.guiAccess.loadOntology();
            this.guiChoco.reinitializeModel();
            logger.info("variables in the model after the reset");
            logger.info(guiChoco.getTopIxModel().getNbTotVars());
            this.guiChoco.getTopIxSolver().clear();
        }
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
//        logger.info("blah BLAH BLAH BLAH");
//        logger.info(roomsTreePanel.getTree().getLastSelectedPathComponent());
//        logger.info(selectedNode);
//        this.selectedNodeDepth=selectedNode.getLevel();
        logger.info("root child count");
        logger.info(roomsTreePanel.getRootChildCount());
        
        this.selectedNodeDepth=e.getPath().getPathCount()-1;
//        
//        if(true) {
//            //to gather all posibly uninitialised instances necessary for the actions performed when selecting a node
//            //and initialise them here, as soon as a node is inserted in the tree.
//            //if (instance==null) {
//            //  instance=new Instance();
//            //}
//        }
        
        //in case selectedNode is ROOT (SITENAME)
        if (selectedNodeDepth==0) {
            DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)roomsTreePanel.getTree().getLastSelectedPathComponent();
            this.addHouseBtn.setEnabled(true);
            this.addRoomBtn.setEnabled(false);
            this.registerBtn.setEnabled(false);
//            if (!(selectedNode.isLeaf()))
//                this.calculateBtn.setEnabled(true);
//            else
            if(roomsTreePanel.getRootChildCount()>0)
                this.calculateBtn.setEnabled(true);
            this.manualInputBtn.setEnabled(false);this.selectedNodeDepth=0;
            //this.currentHouse=null; //TO RECONSIDER THESE TWO LINES!!! (maybe my comment out of the node selection if's will compensate for that).
            //this.currentRoom=null;
            
            rooms2.removeAllItems();
        }
        //in case selectedNode is LEVEL_1 (HOUSE)
        else if (selectedNodeDepth==1) {
            DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)roomsTreePanel.getTree().getLastSelectedPathComponent();
            this.addRoomBtn.setEnabled(true);
            this.addHouseBtn.setEnabled(false);
            this.registerBtn.setEnabled(false);
            this.manualInputBtn.setEnabled(true);
            this.calculateBtn.setEnabled(false);
            
            
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
            DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)roomsTreePanel.getTree().getLastSelectedPathComponent();
            this.addHouseBtn.setEnabled(false);
            this.addRoomBtn.setEnabled(false);
            this.registerBtn.setEnabled(true);
            this.calculateBtn.setEnabled(false);
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
            this.manualInputBtn.setEnabled(true);
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
        
        if (itemEvent.getSource()==availableSitesCBox){
            if(availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()-1)==null)
                siteLeftBtn.setEnabled(false);
            else if(availableSitesCBox.getItemAt(availableSitesCBox.getSelectedIndex()+1)==null)
                siteRightBtn.setEnabled(false);
            else{
                siteLeftBtn.setEnabled(true);
                siteRightBtn.setEnabled(true);
            }
        }
        
        if (itemEvent.getSource()==availableSolutionsCBox){
            if(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()-1)==null)
                solutionLeftBtn.setEnabled(false);
            else if(availableSolutionsCBox.getItemAt(availableSolutionsCBox.getSelectedIndex()+1)==null)
                solutionRightBtn.setEnabled(false);
            else{
                solutionLeftBtn.setEnabled(true);
                solutionRightBtn.setEnabled(true);
            }
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
    
    int validateIntegerInput2(String integerInput) {
        try {
            int returnInt=Integer.parseInt(integerInput);
            if (returnInt<0)
                throw(new NumberFormatException());
            return returnInt;
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }
}