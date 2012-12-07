package visualisation;

import com.sun.j3d.utils.geometry.Text2D;
import javax.media.j3d.*;
import javax.vecmath.*;
import org.apache.log4j.Logger;


public class Room3D extends Shape3D{
    public static final int VERTEX_COUNT=8;
    public static final int INDEX_COUNT=24;
    
    private IndexedLineArray roomLineArray;
    //unused object, to be removed promptly...
    private IndexedQuadArray roomPlaneArray;
    private Point3f[] roomPoints;
    private Color3f[] roomColors;
    private Vector3f[] roomNormals;
    
    private float roomLength;
    private float roomWidth;
    private float roomHeight;
    private float roomX;
    private float roomY;
    private float roomZ;
    
    Logger logger;
    
    public Room3D(int roomLength, int roomWidth, int roomHeight, int roomX, int roomY, int roomZ, boolean isSolid) {
        logger=Logger.getLogger(Site3D.class.getName());
        
        this.roomLength=(float)roomLength;
        this.roomWidth=(float)roomWidth;
        this.roomHeight=(float)roomHeight;
        this.roomX=(float)roomX;
        this.roomY=(float)roomY;
        this.roomZ=(float)roomZ;
        
        this.roomLength=this.roomLength;
        this.roomWidth=this.roomWidth;
        this.roomHeight=this.roomHeight;
        this.roomX=this.roomX;
        this.roomY=this.roomY;
        this.roomZ=this.roomZ;
        
        logger.info(this.roomLength);
        logger.info(this.roomWidth);
        
        
        if(isSolid) {
            roomLineArray=new IndexedLineArray(Room3D.VERTEX_COUNT, GeometryArray.COORDINATES|GeometryArray.COLOR_3|GeometryArray.NORMALS, Room3D.INDEX_COUNT);
        }
        else {
            roomLineArray=new IndexedLineArray(Room3D.VERTEX_COUNT, GeometryArray.COORDINATES|GeometryArray.COLOR_3, Room3D.INDEX_COUNT);
        }
        roomPoints=new Point3f[Room3D.VERTEX_COUNT];
        //siteColors=new Color3f[Site3D.VERTEX_COUNT];
        //---->roomPlaneArray=new IndexedQuadArray(roomZ, roomZ, roomZ);
        
        //creating the coordinate points for each vertex...
        roomPoints[0]=new Point3f(this.roomX, this.roomZ, this.roomY);
        roomPoints[1]=new Point3f(this.roomX+this.roomLength, this.roomZ, this.roomY);
        roomPoints[2]=new Point3f(this.roomX+this.roomLength, this.roomZ, this.roomY+this.roomWidth);
        roomPoints[3]=new Point3f(this.roomX, this.roomZ, this.roomY+this.roomWidth);
        roomPoints[4]=new Point3f(this.roomX, this.roomZ+this.roomHeight, this.roomY);
        roomPoints[5]=new Point3f(this.roomX+this.roomLength, this.roomZ+this.roomHeight, this.roomY);
        roomPoints[6]=new Point3f(this.roomX+this.roomLength, this.roomZ+this.roomHeight, this.roomY+this.roomWidth);
        roomPoints[7]=new Point3f(this.roomX, this.roomZ+this.roomHeight, this.roomY+this.roomWidth);
        
        //creating the corresponding color object for each vertex. no alpha blending for transparency.
        roomColors=new Color3f[Room3D.VERTEX_COUNT];
        for (int i=0; i<Room3D.VERTEX_COUNT; i++) {
            roomColors[i]=new Color3f(1f, 1f, 1f);
        }
        
        if (isSolid) {
            
            //creating the Vector3f array that describes the normals of the parallelogrammon
            roomNormals=new Vector3f[Room3D.VERTEX_COUNT];

            //and then initializing each one of them.
            for(int i=0; i<Room3D.VERTEX_COUNT; i++) {
                roomNormals[i]=new Vector3f();
            }
        

            //setting the values of the normal vectors to be the cross products of the corresponding normal edges
            roomNormals[0].cross(new Vector3f(0, 0, roomY), new Vector3f(0, roomZ, 0));
            roomNormals[1].cross(new Vector3f(0, 0, roomY), new Vector3f(-roomX, 0, 0));
            roomNormals[2].cross(new Vector3f(0, 0, -roomY), new Vector3f(0, roomZ, 0));
            roomNormals[3].cross(new Vector3f(roomX, 0, 0), new Vector3f(0, roomZ, 0));
            roomNormals[4].set(0, 0, 0);
            roomNormals[5].cross(new Vector3f(0, -roomZ, 0), new Vector3f(-roomX, 0, 0));
            roomNormals[6].set(0, 0, 0);
            roomNormals[7].cross(new Vector3f(roomX, 0, 0), new Vector3f(0, 0, -roomY));
            
            //normalize the fuckin vectors!!!
            for(int i=0; i<Room3D.VERTEX_COUNT; i++) {
                roomNormals[i].normalize();
            }
        }
        
        //setting the vertices to the array along with their corresponding colors, as cited in the IndexedLineArray constructor
        roomLineArray.setCoordinates(0, roomPoints);
        roomLineArray.setColors(0, roomColors);
        if (isSolid) roomLineArray.setNormals(0, roomNormals);
        
        
        //filling the coordinate index array. each pair of this array's entries define a line segment.
        //first the lower parallelogrammon
        roomLineArray.setCoordinateIndex(0, 0);
        roomLineArray.setCoordinateIndex(1, 1);
        roomLineArray.setCoordinateIndex(2, 1);
        roomLineArray.setCoordinateIndex(3, 2);
        roomLineArray.setCoordinateIndex(4, 2);
        roomLineArray.setCoordinateIndex(5, 3);
        roomLineArray.setCoordinateIndex(6, 3);
        roomLineArray.setCoordinateIndex(7, 0);
        //then the upper parallelogrammon
        roomLineArray.setCoordinateIndex(8, 4);
        roomLineArray.setCoordinateIndex(9, 5);
        roomLineArray.setCoordinateIndex(10, 5);
        roomLineArray.setCoordinateIndex(11, 6);
        roomLineArray.setCoordinateIndex(12, 6);
        roomLineArray.setCoordinateIndex(13, 7);
        roomLineArray.setCoordinateIndex(14, 7);
        roomLineArray.setCoordinateIndex(15, 4);
        //lastly the perpendicular pilars
        roomLineArray.setCoordinateIndex(16, 0);
        roomLineArray.setCoordinateIndex(17, 4);
        roomLineArray.setCoordinateIndex(18, 1);
        roomLineArray.setCoordinateIndex(19, 5);
        roomLineArray.setCoordinateIndex(20, 2);
        roomLineArray.setCoordinateIndex(21, 6);
        roomLineArray.setCoordinateIndex(22, 3);
        roomLineArray.setCoordinateIndex(23, 7);
        
        roomLineArray.setColorIndex(0, 0);
        roomLineArray.setColorIndex(1, 1);
        roomLineArray.setColorIndex(2, 1);
        roomLineArray.setColorIndex(3, 2);
        roomLineArray.setColorIndex(4, 2);
        roomLineArray.setColorIndex(5, 3);
        roomLineArray.setColorIndex(6, 3);
        roomLineArray.setColorIndex(7, 0);
        roomLineArray.setColorIndex(8, 4);
        roomLineArray.setColorIndex(9, 5);
        roomLineArray.setColorIndex(10, 5);
        roomLineArray.setColorIndex(11, 6);
        roomLineArray.setColorIndex(12, 6);
        roomLineArray.setColorIndex(13, 7);
        roomLineArray.setColorIndex(14, 7);
        roomLineArray.setColorIndex(15, 4);
        roomLineArray.setColorIndex(16, 0);
        roomLineArray.setColorIndex(17, 4);
        roomLineArray.setColorIndex(18, 1);
        roomLineArray.setColorIndex(19, 5);
        roomLineArray.setColorIndex(20, 2);
        roomLineArray.setColorIndex(21, 6);
        roomLineArray.setColorIndex(22, 3);
        roomLineArray.setColorIndex(23, 7);
        
        if (isSolid) {
            roomLineArray.setNormalIndex(0, 5);
            roomLineArray.setNormalIndex(1, 5);
            roomLineArray.setNormalIndex(2, 1);
            roomLineArray.setNormalIndex(3, 1);
            roomLineArray.setNormalIndex(4, 2);
            roomLineArray.setNormalIndex(5, 2);
            roomLineArray.setNormalIndex(6, 3);
            roomLineArray.setNormalIndex(7, 3);
            roomLineArray.setNormalIndex(8, 0);
            roomLineArray.setNormalIndex(9, 0);
            roomLineArray.setNormalIndex(10, 0);
            roomLineArray.setNormalIndex(11, 0);
            roomLineArray.setNormalIndex(12, 0);
            roomLineArray.setNormalIndex(13, 0);
            roomLineArray.setNormalIndex(14, 0);
            roomLineArray.setNormalIndex(15, 0);
            roomLineArray.setNormalIndex(16, 0);
            roomLineArray.setNormalIndex(17, 0);
            roomLineArray.setNormalIndex(18, 0);
            roomLineArray.setNormalIndex(19, 0);
            roomLineArray.setNormalIndex(20, 0);
            roomLineArray.setNormalIndex(21, 0);
            roomLineArray.setNormalIndex(22, 0);
            roomLineArray.setNormalIndex(23, 0);
        }
        
        this.setGeometry(roomLineArray);
    }
}