package visualisation;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Appearance;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.exp.swing.JCanvas3D;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Text2D;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import topIx.owlintermediateclasses.OwlSolution;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.vecmath.*;
import java.awt.Dimension;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import topIx.owlintermediateclasses.OwlSolvedHouse;
import topIx.owlintermediateclasses.OwlSolvedRoom;

public class TopIx3D extends JPanel{
    private static Dimension MY_SIZE;
    
    JCanvas3D canvas3D;
    GraphicsDevice device=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    SimpleUniverse simpleU;
    
    BranchGroup currentBranchGroup;
    //the following seems to have been rendered inutile.
    //to check the system dependency on it.
    BranchGroup rootBranchGroup;

    //mouse behaviors objects
    private MouseRotate mouseRotate;
    private MouseTranslate mouseTranslate;
    private MouseZoom mouseZoom;

    private BoundingSphere boundingSphere;
    
    private TransformGroup viewTG;
    
    //the following attributes are not used, to be removed promptly...
    private Site3D testSite;
    private Room3D testRoom;
    
    Appearance siteApp;
    LineAttributes siteLineAtt;
    ColoringAttributes siteColorAtt;
    PolygonAttributes sitePolygonAtt;
    TransparencyAttributes siteTransparencyAtt;
    
    
    Appearance houseApp;
    LineAttributes houseLineAtt;
    ColoringAttributes houseColorAtt;
    
    Appearance roomApp;
    LineAttributes roomLineAtt;
    ColoringAttributes roomColorAtt;
    PolygonAttributes roomPolygonAtt;
    
    Logger logger;
    
    public TopIx3D() {
        logger=Logger.getLogger(TopIx3D.class.getName());        
        
        MY_SIZE=new Dimension(600, 600);
        
        canvas3D=new JCanvas3D(device);
        //this.setPreferredSize(new Dimension(600, 600));
        this.setBounds(0, 0, 600, 600);
        this.setSize(MY_SIZE);
        this.setLayout(null);
        canvas3D.setLocation(0, 0);
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
        canvas3D.setSize(this.getSize());
        
        logger.info("created canvas3D.");

        
        simpleU=new SimpleUniverse(canvas3D.getOffscreenCanvas3D());
        double scale=simpleU.getViewingPlatform().getViewers()[0].getView().getScreenScale();
        logger.info(simpleU.getViewingPlatform().getViewers()[0].getView().getScreenScalePolicy());
        simpleU.getViewingPlatform().getViewers()[0].getView().setScreenScalePolicy(View.SCALE_EXPLICIT);
        logger.info(simpleU.getViewingPlatform().getViewers()[0].getView().getScreenScalePolicy());
        simpleU.getViewingPlatform().getViewers()[0].getView().setScreenScale(.2d);
        scale=simpleU.getViewingPlatform().getViewers()[0].getView().getScreenScale();
        logger.info("the view scale");
        logger.info(scale);
        
        logger.info("number of trasfromgroups");
        logger.info(simpleU.getViewingPlatform().getMultiTransformGroup().getNumTransforms());
        
        simpleU.getViewer().getJFrame(0).setVisible(false);
        JPanel tempCanvas3D=simpleU.getViewer().getJPanel(0);
        tempCanvas3D.setSize(MY_SIZE);
        this.add(tempCanvas3D);
        
        //appearance and attributes that concern the site rendering
        siteApp=new Appearance();
        siteLineAtt=new LineAttributes(2.0f, LineAttributes.PATTERN_DASH_DOT, true);
        siteColorAtt=new ColoringAttributes(.8f, .8f, .8f, ColoringAttributes.SHADE_GOURAUD);
        sitePolygonAtt=new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0);
        siteTransparencyAtt=new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.7f);
        siteApp.setLineAttributes(siteLineAtt);
        siteApp.setColoringAttributes(siteColorAtt);
        siteApp.setPolygonAttributes(sitePolygonAtt);
        siteApp.setTransparencyAttributes(siteTransparencyAtt);
        
        //appearance and attributes that concern the house rendering
        //the rest happens in the render house loop
        houseLineAtt=new LineAttributes(1.5f, LineAttributes.PATTERN_DASH, true);


        
        //appearance and attributes that concern the rooms rendering
        roomApp=new Appearance();
        roomLineAtt=new LineAttributes(1.0f, LineAttributes.PATTERN_SOLID, true);
        roomColorAtt=new ColoringAttributes(.7f, .7f, .3f, ColoringAttributes.SHADE_GOURAUD);
        roomPolygonAtt=new PolygonAttributes();
            roomPolygonAtt.setPolygonMode(PolygonAttributes.POLYGON_FILL);
            roomPolygonAtt.setCullFace(PolygonAttributes.CULL_NONE);
        roomApp.setLineAttributes(roomLineAtt);
        roomApp.setColoringAttributes(roomColorAtt);
        roomApp.setPolygonAttributes(roomPolygonAtt);

        logger.info("reached the end of topix3d constructor");
    }

    public MouseRotate getMouseRotate() {
        return mouseRotate;
    }

    public MouseTranslate getMouseTranslate() {
        return mouseTranslate;
    }

    public MouseZoom getMouseZoom() {
        return mouseZoom;
    }
    
    //doesn't need to return a value, since this method is never assigned to any
    //BranchGroup attribute or local variable.
    public void createSceneGraph(/*BranchGroup branchGroupToRender*/) {
        //create the root of the BranchGraph
        rootBranchGroup=new BranchGroup();
        //return rootBranchGroup;
    }
    
    public void renderSolution(OwlSolution renderedSolution, boolean renderSolid) {
        Transform3D moveBackTransform3D=new Transform3D();
        moveBackTransform3D.setTranslation(new Vector3d(0, 0, renderedSolution.getSiteLength()*2));
        simpleU.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0).setTransform(moveBackTransform3D);
        
        viewTG=new TransformGroup();
        viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        viewTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        //the following lines mean to translate the whole site so that the
        //center of the ground plane lies in the begining of the axes (0, 0, 0)
        //which for some particular reason will not occur. maby because
        float xTrans=(float)-renderedSolution.getSiteLength()/2;
        float zTrans=(float)-renderedSolution.getSiteWidth()/2;
        Transform3D centerTranslate=new Transform3D();
        centerTranslate.setTranslation(new Vector3f(xTrans, 0, zTrans));
        
        viewTG.setTransform(centerTranslate);
        
        mouseRotate=new MouseRotate(viewTG);
        mouseRotate.setFactor(mouseRotate.getXFactor()/4f);
        mouseTranslate=new MouseTranslate(viewTG);
        mouseZoom=new MouseZoom(viewTG);
        
        boundingSphere=new BoundingSphere(new Point3d(), 1000d);
        
        mouseRotate.setSchedulingBounds(boundingSphere);
        mouseTranslate.setSchedulingBounds(boundingSphere);
        mouseZoom.setSchedulingBounds(boundingSphere);

        logger.info("04");
        logger.info("05");
        BranchGroup newBG=new BranchGroup();
        newBG.setCapability(BranchGroup.ALLOW_DETACH);
        newBG.addChild(viewTG);

        //create and attach the site rectangle to the BranchGroup
        logger.info(renderedSolution.getSiteLength());
        logger.info(renderedSolution.getSiteWidth());
        Site3D renderedSite3D=new Site3D(renderedSolution.getSiteLength(), renderedSolution.getSiteWidth());
        renderedSite3D.setAppearance(siteApp);
        viewTG.addChild(renderedSite3D);
        
        //loop to create and attach all house shape3d's to the BranchGroup
        for(OwlSolvedHouse renderedSolvedHouse:renderedSolution.getSolvedHouses()) {
            House3D renderedHouse3D=new House3D(
                    renderedSolvedHouse.getSolvedHouseLength(),
                    renderedSolvedHouse.getSolvedHouseWidth(),
                    renderedSolvedHouse.getSolvedHouseX(),
                    renderedSolvedHouse.getSolvedHouseY());
            //here to add the text2d for the house nomenclature system...
            //...
            
            houseApp=new Appearance();
            houseApp.setLineAttributes(houseLineAtt);
            float redf=(float)Math.random();
            float greenf=(float)Math.random();
            float bluef=(float)Math.random();
            houseApp.setColoringAttributes(new ColoringAttributes(redf*0.8f+0.2f, greenf*0.8f+0.2f, bluef*0.8f+0.2f, ColoringAttributes.SHADE_GOURAUD));
            //using the same settings for rendering the site normals for transparency and culling
            houseApp.setPolygonAttributes(sitePolygonAtt);
            houseApp.setTransparencyAttributes(siteTransparencyAtt);
            renderedHouse3D.setAppearance(houseApp);
            viewTG.addChild(renderedHouse3D);
        }
        logger.info("07");
        
        for (OwlSolvedRoom renderedSolvedRoom:renderedSolution.getSolvedRooms()) {
            Room3D renderedRoom3D=new Room3D(
                    renderedSolvedRoom.getSolvedRoomLength(),
                    renderedSolvedRoom.getSolvedRoomWidth(),
                    renderedSolvedRoom.getSolvedRoomHeight(),
                    renderedSolvedRoom.getSolvedRoomX(),
                    renderedSolvedRoom.getSolvedRoomY(),
                    renderedSolvedRoom.getSolvedRoomZ(),
                    renderSolid);
            renderedRoom3D.setAppearance(roomApp);
            viewTG.addChild(renderedRoom3D);
            
            //for each room we create a transform group where the roomName is
            //to be mounted and translated, and then each roomName TG is to be 
            //mounted on viewTG
            TransformGroup tempRoomNameTG=new TransformGroup();
            Vector3d tempRoomNameVector=new Vector3d(renderedSolvedRoom.getSolvedRoomX(), renderedSolvedRoom.getSolvedRoomZ(), (renderedSolvedRoom.getSolvedRoomY()+renderedSolvedRoom.getSolvedRoomWidth()-0.5f));
            Transform3D tempRoomNameTransform3D=new Transform3D();
            tempRoomNameTransform3D.setTranslation(tempRoomNameVector);
            tempRoomNameTG.setTransform(tempRoomNameTransform3D);
            Text2D tempRoomNameText2D=new Text2D(renderedSolvedRoom.getSolvedRoomLiteral(), new Color3f(1f, 1f, 1f), "Arial", 100, 0);
            tempRoomNameTG.addChild(tempRoomNameText2D);
            viewTG.addChild(tempRoomNameTG);
        }
        logger.info("this is the count of the children of the viewTG");
        Enumeration en=viewTG.getAllChildren();
        while(en.hasMoreElements()){
            logger.info(en.nextElement());
        }
        
        newBG.addChild(mouseRotate);
        newBG.addChild(mouseTranslate);
        newBG.addChild(mouseZoom);
        logger.info("edw kanw print to rotate behaviour");
        logger.info(mouseRotate);

        newBG.compile();
        logger.info("09");
        Locale tempLocale=this.simpleU.getLocale();
        logger.info("10");
        logger.info(tempLocale.numBranchGraphs());
        if(tempLocale.numBranchGraphs()>1) {
            logger.info("11");
            logger.info(tempLocale.numBranchGraphs());
            tempLocale.removeBranchGraph(currentBranchGroup);
            logger.info("12");
            logger.info(tempLocale.numBranchGraphs());
            currentBranchGroup=newBG;
            this.simpleU.addBranchGraph(currentBranchGroup);
            logger.info("13");
            logger.info(tempLocale.numBranchGraphs());
        }
        else {
            logger.info("14");
            this.simpleU.addBranchGraph(newBG);
            currentBranchGroup=newBG;
            logger.info("15");
        }
    }
    
    public void repaintCanvas() {
        this.canvas3D.repaint();
    }
}