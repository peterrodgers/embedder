package euler;
/**
 * Utility class for the convex polygon tester
 * */
public class Coord
{
	public double lon, lat;
	public Coord(double lon, double lat)
	{
		this.lon = lon;
		this.lat = lat;
	}	
	public String toString()
	{
		return "(lon: "+ lon + ", lat: " +lat +")";
	}
}
