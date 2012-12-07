/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topIx;

import java.awt.CardLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import org.semanticweb.owlapi.model.OWLIndividual;

import chocosolution.TopIxChoco;
import chocosolution.ChocoRoom;
import chocosolution.ChocoUtility;

import chocosolution.*;
import java.awt.Dimension;


import org.apache.log4j.Logger;

import topIx.owlintermediateclasses.*;
import visualisation.TopIx3D;
/**
 *
 * @author Antiregulator
 */
public class GUI extends JFrame implements ActionListener, ItemListener, TreeSelectionListener
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
    JLabel siteIdentifierLabel;
    JLabel xLabel;
    JLabel yLabel;
    JTextField siteIdentifierInput;
    JTextField xInput;
    JTextField yInput;
    JButton nextBtn;
    //paneIb components     - ontology data entry
    JPanel paneIb1;
    JPanel paneIb2;
    JPanel paneIb3;
    //paneIb1 components    - tree pane
        //will contemplate on the layout type
    //JScrollPane treeScroller;
    //JTree roomsTree;
    DynamicTree roomsTreePanel; //--
        //has default FlowLayout
    JTextArea relationsTextArea;
    JScrollPane relationsScrollPane;
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
    
    //misc attributes
    String houseIdentifier;
    Integer houseIdentifierHashCode;
    //the following var is a flag that is set everytime a selection is made in
    //the JTree, and provides info as to whether the site, a house or a room is
    //currently selected.
    int selectedNodeDepth;
    
    //components of other classes that have an association relation with the GUI
    //instance
    OntologyAccessUtility guiAccess;    //--
    TopIxChoco guiChoco;    //--
    TopIx3D topIx3D;    //--
    
    //holds temporary OwlSite, OwlHouse, OwlRoom fields which are initialized
    //when they are first needed (stoys event handlers toys dld)
    // -candidate to be moved to OntologyAccessUtility class or...
    //...God knows where!!!
    OwlSite currentSite;    //--
    OwlHouse currentHouse;  //--
    OwlRoom currentRoom;    //--
    
    Map<String, List<String>> roomHashToPropertiesList;
    
    //manualInput dialog frame and its components
    JDialog manualInputDialog;  //--
    JLabel manualPropertyListLabel;
    JLabel manualPropertyValueLabel;
    JButton manualInputRegisterButton;
    JButton manualInputCancelButton;
    JComboBox<String> manualPropertyComboBox;
    JTextField manualPropertyValueTextField;
    GroupLayout manualInputLay_Grp;
    
    //select percentage of the solutions dialog frame and its components
    JDialog selectSolutionsDialog;  //--
    JLabel selectSolutionsLbl;
    JComboBox<Integer> selectSolutionCBox;
    GroupLayout selectSolutionsLay_Grp;
    JButton selectSolutionOkBtn;
    
    Logger logger;

        public GUI(OntologyAccessUtility accessRef, TopIxChoco chocoRef)
    {
        super("TopIx project -- Bromoiras David - TEI of Athens");
        logger= Logger.getLogger(GUI.class.toString());
        logger.info("creating GUI jpanel");
        mainPanel=new JPanel();
        mainLayout=new GroupLayout(mainPanel);
        mainPanel.setLayout(mainLayout);
        guiAccess=accessRef;
        guiChoco=chocoRef;       
        initializeComponents(guiAccess.propEntryNameToPropCatName, guiAccess.roomToIRI);
        addComponents();
        
        this.setIconImage(new ImageIcon("src/ontologyresources/images/topix.png").getImage());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 500);
        this.setMaximumSize(new Dimension(950, 500));
        this.setMinimumSize(new Dimension(950, 500));
        setMaximizedBounds(this.getBounds());
        
        propsCBoxLabel.setVisible(true);
        propsCBox.setVisible(true);
        catsCBox.setSelectedIndex(1);
        rooms2Label.setVisible(false);
        rooms2.setVisible(false);
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
        siteIdentifierLabel=new JLabel("Name of Project:");
        xLabel=new JLabel("Site Length:");
        yLabel=new JLabel("Site Width:");
        siteIdentifierInput=new JTextField();
        xInput=new JTextField();
        yInput=new JTextField();
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
            relationsTextArea=new JTextArea();
                relationsTextArea.setEditable(false);
                relationsTextArea.setSize(300, 150);
            relationsScrollPane=new JScrollPane(
                    relationsTextArea,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            
            
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
                backBtn.setVisible(false);
                backBtn.addActionListener(this);
            saveBtn=new JButton("Save");
                saveBtn.addActionListener(this);
                saveBtn.setVisible(false);
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
                renderSolidLabel.setVisible(false);
            renderSolidChBox=new JCheckBox();
                renderSolidChBox.setSelected(false);
                renderSolidChBox.setVisible(false);
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
        selectSolutionsDialog=new JDialog(this, "Select solutions", true);
        selectSolutionsDialog.setIconImage(new ImageIcon("src/ontologyresources/images/topix.png").getImage());
        selectSolutionsDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        selectSolutionsDialog.setVisible(false);
        selectSolutionsLay_Grp=new GroupLayout(selectSolutionsDialog.getContentPane());
            selectSolutionsDialog.getContentPane().setLayout(selectSolutionsLay_Grp);
        selectSolutionsLbl=new JLabel("testlabel");
        selectSolutionOkBtn=new JButton("Ok");
            selectSolutionOkBtn.addActionListener(this);
        //building the structure to provide the combo box model
        {
            Integer[] tempIntegerArray=new Integer[10];
            for(int i=0, j=10; i<10; i++, j+=10){
                tempIntegerArray[i]=new Integer(j);
            }
            selectSolutionCBox=new JComboBox<>(tempIntegerArray);
        }
        
        roomHashToPropertiesList=new HashMap<>();
    }
    
    private void addComponents()            //add components to their respective containers
    {
        //fill paneIa
        layIa_Grp.setAutoCreateGaps(true);
        layIa_Grp.setHorizontalGroup(
                layIa_Grp.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(siteIdentifierLabel)
                        .addComponent(xLabel)
                        .addComponent(yLabel))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(siteIdentifierInput, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                        .addComponent(xInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                        .addComponent(yInput, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(nextBtn))
                    .addContainerGap());
        layIa_Grp.setVerticalGroup(
                layIa_Grp.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(siteIdentifierLabel)
                        .addComponent(siteIdentifierInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(xLabel)
                        .addComponent(xInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(layIa_Grp.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(yLabel)
                        .addComponent(yInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(compactnessChBox))
                        .addGroup(layIb2_Grp.createSequentialGroup()
                            .addComponent(relationsScrollPane)))
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
                        .addComponent(compactnessChBox))
                    .addGroup(layIb2_Grp.createParallelGroup()
                        .addComponent(relationsScrollPane)));
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
                        .addComponent(backBtn)));
        layIb3_Grp.setVerticalGroup(
                layIb3_Grp.createSequentialGroup()
                    .addGroup(layIb3_Grp.createParallelGroup()
                        .addComponent(addHouseBtn)
                        .addComponent(registerBtn)
                        .addComponent(calculateBtn))
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
        
        tabs.addTab("Input", paneI);
        tabs.addTab("Results", paneII);
        
        //setting the layout for mainPanel
        GroupLayout mainPanelLay_Grp=(GroupLayout)mainPanel.getLayout();
        mainPanelLay_Grp.setHorizontalGroup(
                mainPanelLay_Grp.createSequentialGroup()
                .addComponent(tabs)
        );
        mainPanelLay_Grp.setVerticalGroup(
                mainPanelLay_Grp.createSequentialGroup()
                .addComponent(tabs)
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
        
        //setting the layout for selectSolutionsDialog
        selectSolutionsLay_Grp.setAutoCreateContainerGaps(true);
        selectSolutionsLay_Grp.setAutoCreateGaps(true);
        selectSolutionsLay_Grp.setHorizontalGroup(
                selectSolutionsLay_Grp.createSequentialGroup()
                .addComponent(selectSolutionsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(selectSolutionsLay_Grp.createParallelGroup()
                    .addComponent(selectSolutionCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectSolutionOkBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        selectSolutionsLay_Grp.setVerticalGroup(selectSolutionsLay_Grp.createSequentialGroup()
                .addGroup(selectSolutionsLay_Grp.createParallelGroup()
                    .addComponent(selectSolutionsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectSolutionCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(selectSolutionOkBtn));
        selectSolutionsDialog.setLocationRelativeTo(selectSolutionsDialog.getOwner());
        selectSolutionsDialog.setResizable(false);
        selectSolutionsDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        selectSolutionsDialog.pack();
        
        this.setContentPane(mainPanel);
        this.setVisible(true);
        this.pack();
    }
    
    public void populateResultsComponents() {
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
        if (actionEvent.getSource()==nextBtn && ((validateIntegerInput(xInput.getText())<=-1)&&(validateIntegerInput(yInput.getText())<=-1))) {
            JOptionPane.showMessageDialog(this, "Please provide a positive integer for site length and width!");
        }
        else if(actionEvent.getSource()==nextBtn && (siteIdentifierInput.getText().equals(""))){
            JOptionPane.showMessageDialog(this, "Please, provide a valid name for the Site!");
        }
        else if (actionEvent.getSource()==nextBtn)
        {
            currentSite=new OwlSite(siteIdentifierInput.getText(), Integer.parseInt(xInput.getText()),Integer.parseInt(yInput.getText()));
            guiAccess.assertSiteIndividual(currentSite.returnSiteNameHash(), currentSite.returnSiteNameAnnotation());
            roomsTreePanel.addRootNodeToTree(currentSite.returnSiteNameCompact());
            roomsTreePanel.getTree().addTreeSelectionListener(this);
            roomsTreePanel.setVisible(true);
            layI_Crd.show(paneI, "Model Input");
            
            guiAccess.assertDataPropertyInstance(currentSite.returnSiteNameHash(), "hasX", currentSite.getSiteLength());
            guiAccess.assertDataPropertyInstance(currentSite.returnSiteNameHash(), "hasY", currentSite.getSiteWidth());
            
            //disables editing to the sitename textfield
            this.siteIdentifierInput.setEditable(false);
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
            //the following line initializes the currentRoom object for future use.
            currentRoom=new OwlRoom(rooms1.getSelectedItem().toString());   

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
        }
        
        //BACK BUTTON---------------------------------------------------------//
        if(actionEvent.getSource()==backBtn)
        {
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
            this.guiChoco.reinitializeChocoSystem();
            logger.info("variables in the model after the reset");
            logger.info(guiChoco.getTopIxModel().getNbTotVars());
            this.guiChoco.getTopIxSolver().clear();
            //returns the view to the site input stuff
            layI_Crd.show(paneI, "Basic Input");
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
            switch (declarativeProperty) {
                case "Eastwards Adjacent To":
                    ChocoUtility.adjacentEastConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Eastwards Adjacent To");
                    break;
                case "Northwards Adjacent To":
                    ChocoUtility.adjacentNorthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Northwards Adjacent To");
                    break;
                case "Top-wards Adjacent To":
                    ChocoUtility.adjacentOverConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Top-wards Adjacent To");
                    break;
                case "Southwards Adjacent To":
                    ChocoUtility.adjacentSouthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Southwards Adjacent To");
                    break;
                case "Bottom-wards Adjacent To":
                    ChocoUtility.adjacentUnderConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Bottom-wards Adjacent To");
                    break;
                case "Westwards Adjacent To":
                    ChocoUtility.adjacentWestConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Westwards Adjacent To");
                    break;
                case "Higher Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Higher Than");
                    break;
                case "Longer Than":
                    ChocoUtility.longerThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Longer Than");
                    break;
                case "Lower Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Lower Than");
                    break;
                case "Narrower Than":
                    ChocoUtility.widerThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Narrower Than");
                    break;
                case "Shorter Than":
                    ChocoUtility.higherThanConstraint(guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Shorter Than");
                    break;
                case "Wider Than":
                    ChocoUtility.widerThanConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Wider Than");
                    break;
                case "Higher Than Long":
                    ChocoUtility.higherThanLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Higher Than Long");
                    break;
                case "Higher Than Wide":
                    ChocoUtility.higherThanWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Higher Than Wide");
                    break;
                case "Longer Than High":
                    ChocoUtility.longerThanHighConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Longer Than High");
                    break;
                case "Longer Than Wide":
                    ChocoUtility.longerThanWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Longer Than Wide");
                    break;
                case "Wider Than High":
                    ChocoUtility.widerThanHighConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Wider Than High");
                    break;
                case "Wider Than Long":
                    ChocoUtility.widerThanLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Wider Than Long");
                    break;
                case "Equal Height To":
                    ChocoUtility.equalHeightConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Equal Height To");
                    break;
                case "Equal Length To":
                    ChocoUtility.equalLengthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Equal Length To");
                    break;
                case "Equal Width To":
                    ChocoUtility.equalWidthConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getChocoRoomMap().get(room2Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Equal Width To");
                    break;
                case "Long":
                    ChocoUtility.isLongConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Long");
                    break;
                case "Short":
                    ChocoUtility.isShortConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Short");
                    break;
                case "Wide":
                    ChocoUtility.isWideConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Wide");
                    break;
                case "Narrow":
                    ChocoUtility.isNarrowConstraint(guiChoco.getChocoRoomMap().get(room1Hash), guiChoco.getTopIxModel());
                    insertLiteralRelation("Narrow");
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
                tempSolution.toString();
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "The solver cannot reach any solutions for the particular scenario. Please RESET and provide a new declarative description.",
                        "Warning: No solutions!",
                        JOptionPane.WARNING_MESSAGE);
            }
            
            while(lastSolutionFeasible) {
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
                }
            }
            
//            logger.info("--------------------------------------------------------------------------1070");
//            logger.info(guiAccess.getSolutionsList().toString());
//            logger.info("-------------------------------------------------------------------END OF 1070");
            
            if(!(guiAccess.getSolutionsList().isEmpty())&&guiAccess.getSolutionsList().size()>=10)
            {
                String tempSolutionSelection=String.format(
                    "The solver has returned %d solutions. Choose a percentage of which you would like to keep",
                    guiAccess.getSolutionsList().size());
                this.selectSolutionsLbl.setText(tempSolutionSelection);
                selectSolutionsDialog.pack();
                selectSolutionsDialog.setVisible(true);
            //after this, the action is transfered to the selectSolutionOkBtn event handler...
            }
            else if(!(guiAccess.getSolutionsList().isEmpty())&&guiAccess.getSolutionsList().size()<10){
                guiAccess.storeSolutions(currentSite.returnSiteNameHash());
                //saving the ontology
                guiAccess.saveOntology();
                //disabling the calcilate button, so that no scenario is overwritten in the ontology.
                this.calculateBtn.setVisible(false);
                //refreshing the list of the solved sites
                this.availableSitesCBox.removeAllItems();
                this.populateResultsComponents();
                JOptionPane.showMessageDialog(
                        this.rootPane,
                        String.format(
                            "The solver returned %d solutions. They are all stored to the ontology.",
                            guiAccess.getSolutionsList().size()),
                        "Less than ten solutions.",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else
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
        //SELECT SOLUTION OK BUTTON-------------------------------------------//
        if(actionEvent.getSource()==this.selectSolutionOkBtn)
        {
            List<OwlSolution> tempSolutionsToDiscard=new ArrayList<>(64);
            Integer percentage=(Integer)this.selectSolutionCBox.getSelectedItem()/10;
            selectSolutionsDialog.setVisible(false);
            int counter=1;
            for(OwlSolution tempOwlSolution:guiAccess.getSolutionsList()){
                if ((counter>percentage)&&(counter<=10)){
                    tempSolutionsToDiscard.add(tempOwlSolution);
                }
                else if(counter>10)
                    counter=1;
                counter++;
            }
            //removing the contents of the discard list from the solutions list...
            guiAccess.getSolutionsList().removeAll(tempSolutionsToDiscard);
            //storing the *selected percentage* of the solutions that have been recovered--NOT READY YET!!!!
            guiAccess.storeSolutions(currentSite.returnSiteNameHash());
            //saving the ontology
            guiAccess.saveOntology();
            //disabling the calcilate button, so that no scenario is overwritten in the ontology.
            this.calculateBtn.setVisible(false);
            //refreshing the list of the solved sites
            this.availableSitesCBox.removeAllItems();
            this.populateResultsComponents();
            
        }
        
        //SAVE BUTTON---------------------------------------------------------//
        if(actionEvent.getSource()==saveBtn)
        {
            guiAccess.saveOntology();
            this.availableSitesCBox.removeAllItems();
            this.populateResultsComponents();
        }
        
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
            logger.info("");
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
            this.guiChoco.reinitializeChocoSystem();
            logger.info("variables in the model after the reset");
            logger.info(guiChoco.getTopIxModel().getNbTotVars());
            this.guiChoco.getTopIxSolver().clear();
            //returns the view to the site input stuff
            layI_Crd.show(paneI, "Basic Input");
        }
    }
    
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        logger.info("root child count");
        logger.info(roomsTreePanel.getRootChildCount());
        
        this.selectedNodeDepth=e.getPath().getPathCount()-1;

        
        //in case selectedNode is ROOT (SITENAME)
        if (selectedNodeDepth==0) {
            this.addHouseBtn.setEnabled(true);
            this.addRoomBtn.setEnabled(false);
            this.registerBtn.setEnabled(false);
            if(roomsTreePanel.getRootChildCount()>0)
                this.calculateBtn.setEnabled(true);
            this.manualInputBtn.setEnabled(false);
            this.selectedNodeDepth=0;
            this.relationsTextArea.setText("");
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
            this.relationsTextArea.setText("");
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
            
            //the following block feeds the text area of the already inserted 
            //object properties per room
            String tempRoomHash=currentRoom.returnRoomIndividualHash(
                    currentSite.getSiteName(),
                    currentHouse.getSelectedHouseEntry(),
                    currentRoom.getSelectedRoomEntry());
            logger.info("from inside the valuechanged method");
            logger.info(tempRoomHash);
            relationsTextArea.setText("");
            if(roomHashToPropertiesList.get(tempRoomHash)!=null){
                for (String tempRelationString:roomHashToPropertiesList.get(tempRoomHash))
                {
                    if(relationsTextArea.getText()==null||relationsTextArea.getText().equals("")){
                        relationsTextArea.setText(tempRelationString+"\n");
                    }
                    else
                        relationsTextArea.append(tempRelationString+"\n");
                }
            }
            else
                relationsTextArea.setText("");
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
    
    //inserting an entry to the properties list map, which will feed the text
    //area with the object relation each room is entagnled in.
    void insertLiteralRelation(String relation){
        //temporarily stores the hash value of the selected room in the tree
        String tempRoom1Hash=currentRoom.returnRoomIndividualHash(
                    currentSite.getSiteName(),
                    currentHouse.getSelectedHouseEntry(),
                    currentRoom.getSelectedRoomEntry());
        //temporarily stores the unhashed literar of the relationship a subject
        //participates in. in a way "is longer than kithcen_03"
        String tempRelationObjectString;
        if(rooms2.isVisible())
            tempRelationObjectString=new String(relation+" "+currentRoom.getComboBox2RoomEntry());
        else
            tempRelationObjectString=new String(relation);
            
        logger.info("from inside insertLiteralRelation");
        logger.info(tempRoom1Hash);
        if(roomHashToPropertiesList.containsKey(tempRoom1Hash))
            roomHashToPropertiesList.get(tempRoom1Hash).add(tempRelationObjectString);
        else{
            roomHashToPropertiesList.put(tempRoom1Hash, new ArrayList<String>());
            roomHashToPropertiesList.get(tempRoom1Hash).add(tempRelationObjectString);
            logger.info(roomHashToPropertiesList.get(tempRoom1Hash).toString());
        }
    }
}