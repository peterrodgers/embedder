package euler.maxrectangle;

public class GeomEdge{
    
    int xmin, xmax; /* horiz, +x is right */
    int ymin, ymax; /* vertical, +y is down */
    double m,b; /* y = mx + b */
    boolean isTop, isRight; /* position of edge w.r.t. hull */
    
    public GeomEdge(GeomPoint p, GeomPoint q){
        this.xmin = p.min(p.x, q.x);
        this.xmax = p.max(p.x, q.x);
        this.ymin = p.min(p.y, q.y);
        this.ymax = p.max(p.y, q.y);
        this.m = ((double)(q.y-p.y))/((double)(q.x-p.x));
        this.b = p.y - m*(p.x);
        this.isTop = p.x > q.x; //edge from right to left (ccw)
        this.isRight = p.y > q.y; //edge from bottom to top (ccw)
    }
}