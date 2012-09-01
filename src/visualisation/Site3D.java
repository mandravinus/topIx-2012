package visualisation;

import javax.media.j3d.Shape3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedLineArray;
import javax.vecmath.*;
import org.apache.log4j.Logger;

public class Site3D extends Shape3D {
    public static final int VERTEX_COUNT=4;
    public static final int INDEX_COUNT=8;
    
    private IndexedLineArray siteLineArray;
    private Point3f[] sitePoints;
    private Color3f[] siteColors;
    
    float siteLength;
    float siteWidth;
    
    Logger logger;
    
    public Site3D(int siteLength, int siteWidth) {
        logger=Logger.getLogger(Site3D.class.getName());
        
        this.siteLength=(float)siteLength;
        this.siteWidth=(float)siteWidth;
        
        this.siteLength=this.siteLength/10;
        this.siteWidth=this.siteWidth/10;
        
        logger.info(this.siteLength);
        logger.info(this.siteWidth);
        
        siteLineArray=new IndexedLineArray(Site3D.VERTEX_COUNT, GeometryArray.COORDINATES|GeometryArray.COLOR_3, Site3D.INDEX_COUNT);
        sitePoints=new Point3f[Site3D.VERTEX_COUNT];
        //siteColors=new Color3f[Site3D.VERTEX_COUNT];
        
        //creating the coordinate points for each vertex...
        sitePoints[0]=new Point3f(0f, 0f, 0f);
        sitePoints[1]=new Point3f(this.siteLength, 0f, 0f);
        sitePoints[2]=new Point3f(this.siteLength, 0f, this.siteWidth);
        sitePoints[3]=new Point3f(0f, 0f, this.siteWidth);
        
        //creating the corresponding color object for each vertex. no alpha blending for transparency.
        siteColors=new Color3f[Site3D.VERTEX_COUNT];
        for (int i=0; i<Site3D.VERTEX_COUNT; i++) {
            siteColors[i]=new Color3f(1f, 1f, 1f);
        }
        
        //setting the vertices to the array along with their corresponding colors, as cited in the IndexedLineArray constructor
        siteLineArray.setCoordinates(0, sitePoints);
//        siteLineArray.setCoordinate(0, sitePoints[0]);
//        siteLineArray.setCoordinate(1, sitePoints[1]);
//        siteLineArray.setCoordinate(2, sitePoints[2]);
//        siteLineArray.setCoordinate(3, sitePoints[3]);
        
        siteLineArray.setColors(0, siteColors);
        
        siteLineArray.setCoordinateIndex(0, 0);
        siteLineArray.setCoordinateIndex(1, 1);
        siteLineArray.setCoordinateIndex(2, 1);
        siteLineArray.setCoordinateIndex(3, 2);
        siteLineArray.setCoordinateIndex(4, 2);
        siteLineArray.setCoordinateIndex(5, 3);
        siteLineArray.setCoordinateIndex(6, 3);
        siteLineArray.setCoordinateIndex(7, 0);
        
        siteLineArray.setColorIndex(0, 0);
        siteLineArray.setColorIndex(1, 1);
        siteLineArray.setColorIndex(2, 1);
        siteLineArray.setColorIndex(3, 2);
        siteLineArray.setColorIndex(4, 2);
        siteLineArray.setColorIndex(5, 3);
        siteLineArray.setColorIndex(6, 3);
        siteLineArray.setColorIndex(7, 0);
        
        this.setGeometry(siteLineArray);
    }
}