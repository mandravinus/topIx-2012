package visualisation;

import javax.media.j3d.*;
import javax.vecmath.*;
import org.apache.log4j.Logger;

public class House3D extends Shape3D{
    public static final int VERTEX_COUNT=4;
    public static final int INDEX_COUNT=8;
    
    private IndexedLineArray houseLineArray;
    private Point3f[] housePoints;
    private Color3f[] houseColors;
    
    float houseLength;
    float houseWidth;
    float houseX;
    float houseY;
    
    Logger logger;
    
    public House3D(int houseLength, int houseWidth, int houseX, int houseY) {
        logger=Logger.getLogger(Site3D.class.getName());
        
        this.houseLength=(float)houseLength;
        this.houseWidth=(float)houseWidth;
        this.houseX=(float)houseX;
        this.houseY=(float)houseY;
        
        this.houseLength=this.houseLength;
        this.houseWidth=this.houseWidth;
        this.houseX=this.houseX;
        this.houseY=this.houseY;
        
        logger.info(this.houseLength);
        logger.info(this.houseWidth);
        
        houseLineArray=new IndexedLineArray(House3D.VERTEX_COUNT, GeometryArray.COORDINATES|GeometryArray.COLOR_3, House3D.INDEX_COUNT);
        housePoints=new Point3f[Site3D.VERTEX_COUNT];
        //siteColors=new Color3f[Site3D.VERTEX_COUNT];
        
        //creating the coordinate points for each vertex...
        housePoints[0]=new Point3f(this.houseX, 0f, this.houseY);
        housePoints[1]=new Point3f(this.houseX+this.houseLength, 0f, this.houseY);
        housePoints[2]=new Point3f(this.houseX+this.houseLength, 0f, this.houseY+this.houseWidth);
        housePoints[3]=new Point3f(this.houseX, 0f, this.houseY+this.houseWidth);
        
        //creating the corresponding color object for each vertex. no alpha blending for transparency.
        houseColors=new Color3f[House3D.VERTEX_COUNT];
        for (int i=0; i<House3D.VERTEX_COUNT; i++) {
            houseColors[i]=new Color3f(1f, 1f, 1f);
        }
        
        //setting the vertices to the array along with their corresponding colors, as cited in the IndexedLineArray constructor
        houseLineArray.setCoordinates(0, housePoints);
//        siteLineArray.setCoordinate(0, sitePoints[0]);
//        siteLineArray.setCoordinate(1, sitePoints[1]);
//        siteLineArray.setCoordinate(2, sitePoints[2]);
//        siteLineArray.setCoordinate(3, sitePoints[3]);
        
        houseLineArray.setColors(0, houseColors);
        
        houseLineArray.setCoordinateIndex(0, 0);
        houseLineArray.setCoordinateIndex(1, 1);
        houseLineArray.setCoordinateIndex(2, 1);
        houseLineArray.setCoordinateIndex(3, 2);
        houseLineArray.setCoordinateIndex(4, 2);
        houseLineArray.setCoordinateIndex(5, 3);
        houseLineArray.setCoordinateIndex(6, 3);
        houseLineArray.setCoordinateIndex(7, 0);
        
        houseLineArray.setColorIndex(0, 0);
        houseLineArray.setColorIndex(1, 1);
        houseLineArray.setColorIndex(2, 1);
        houseLineArray.setColorIndex(3, 2);
        houseLineArray.setColorIndex(4, 2);
        houseLineArray.setColorIndex(5, 3);
        houseLineArray.setColorIndex(6, 3);
        houseLineArray.setColorIndex(7, 0);
        
        this.setGeometry(houseLineArray);
    }
}