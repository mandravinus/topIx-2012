/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package topIx;

import chocosolution.TopIxChoco;
import java.lang.reflect.Field;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
/**
 *
 * @author Antiregulator
 */
public class TopIxMain
{
    static Logger logger;
    
    
    public static void main(String[] args)
    {
        logger=Logger.getLogger(TopIxMain.class.getName());
        BasicConfigurator.configure();
        
        System.setProperty("java.library.path", "C:\\Program Files\\Java\\Java3D\\1.5.1\\bin;"+System.getProperty("java.library.path"));
        try {
            Field fieldSysPath=ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        }catch(Exception e) {}
        
        
        logger.info("test");
        //logger.info(System.getProperty("java.library.path"));
        
        OntologyAccessUtility access=new OntologyAccessUtility();
        logger.info(access.propEntryNameToPropCatName);
        TopIxChoco chocoModule=new TopIxChoco();
        GUI aGui=new GUI(access, chocoModule);
        
        System.out.println("loaded ontology: "+access.topIxOnt);
        System.out.println("from: "+access.manager.getOntologyDocumentIRI(access.topIxOnt));
    }
}