package visualisation;
import javax.media.j3d.*;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.IndexedQuadArray;
import javax.vecmath.*;
import org.apache.log4j.Logger;

public class Site3D extends Shape3D {
    public static final int LINE_VERTEX_COUNT=4;
    public static final int LINE_INDEX_COUNT=8;
    public static final int QUAD_VERTEX_COUNT=8;
    public static final int QUAD_INDEX_COUNT=16;
    
    //the following attribute's usage has been replaced by siteQuadArray
    private IndexedLineArray siteLineArray;
    private IndexedQuadArray siteQuadArray;
    private Point3f[] sitePoints;
    private Color3f[] siteColors;
    private Point3f[] quadPoints;
    private Color4f[] quadColors;
    
    float siteLength;
    float siteWidth;
    
    Logger logger;
    
    public Site3D(int siteLength, int siteWidth) {
        logger=Logger.getLogger(Site3D.class.getName());
        
        this.siteLength=(float)siteLength;
        this.siteWidth=(float)siteWidth;
        
        this.siteLength=this.siteLength;
        this.siteWidth=this.siteWidth;
        
        logger.info(this.siteLength);
        logger.info(this.siteWidth);
        
        siteLineArray=new IndexedLineArray(Site3D.LINE_VERTEX_COUNT, GeometryArray.COORDINATES|GeometryArray.COLOR_3, Site3D.LINE_INDEX_COUNT);
        sitePoints=new Point3f[Site3D.LINE_VERTEX_COUNT];
        quadPoints=new Point3f[Site3D.QUAD_VERTEX_COUNT];
        //siteColors=new Color3f[Site3D.VERTEX_COUNT];
        
        //designing the impression of the earth level normal as a green stripe
        //of 1m width arround the bounds of the site.
        siteQuadArray=new IndexedQuadArray(Site3D.QUAD_VERTEX_COUNT, GeometryArray.COORDINATES, Site3D.QUAD_INDEX_COUNT);
        
        //creating the coordinate points for each vertex...
        //as in each of the plotting cases, the current design of the application
        //requires that the y-coordinates are plotted on z-axis and vice versa
        sitePoints[0]=new Point3f(-1f, 0f, -1f);
        sitePoints[1]=new Point3f(this.siteLength+2, 0f, -1f);
        sitePoints[2]=new Point3f(this.siteLength+2, 0f, this.siteWidth+2);
        sitePoints[3]=new Point3f(-1f, 0f, this.siteWidth+2);
        
        quadPoints[0]=new Point3f(0f, 0f, 0f);
        quadPoints[1]=new Point3f(this.siteLength, 0f, 0f);
        quadPoints[2]=new Point3f(this.siteLength, 0f, this.siteWidth);
        quadPoints[3]=new Point3f(0f, 0f, this.siteWidth);
        quadPoints[4]=new Point3f(-1f, 0f, -1f);
        quadPoints[5]=new Point3f(this.siteLength+1, 0f, -1f);
        quadPoints[6]=new Point3f(this.siteLength+1, 0f, this.siteWidth+1f);
        quadPoints[7]=new Point3f(-1f, 0f, this.siteWidth+1f);
        
        //creating the corresponding color object for each vertex. no alpha blending for transparency.
        siteColors=new Color3f[Site3D.LINE_VERTEX_COUNT];
        for (int i=0; i<Site3D.LINE_VERTEX_COUNT; i++) {
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
        
        //arranging the data for the quad array
        siteQuadArray.setCoordinates(0, quadPoints);
        
        siteQuadArray.setCoordinateIndex(0, 0);
        siteQuadArray.setCoordinateIndex(1, 4);
        siteQuadArray.setCoordinateIndex(2, 7);
        siteQuadArray.setCoordinateIndex(3, 3);
        siteQuadArray.setCoordinateIndex(4, 1);
        siteQuadArray.setCoordinateIndex(5, 5);
        siteQuadArray.setCoordinateIndex(6, 4);
        siteQuadArray.setCoordinateIndex(7, 0);
        siteQuadArray.setCoordinateIndex(8, 2);
        siteQuadArray.setCoordinateIndex(9, 6);
        siteQuadArray.setCoordinateIndex(10, 5);
        siteQuadArray.setCoordinateIndex(11, 1);
        siteQuadArray.setCoordinateIndex(12, 3);
        siteQuadArray.setCoordinateIndex(13, 7);
        siteQuadArray.setCoordinateIndex(14, 6);
        siteQuadArray.setCoordinateIndex(15, 2);
        
        
        this.setGeometry(siteQuadArray);
    }
}