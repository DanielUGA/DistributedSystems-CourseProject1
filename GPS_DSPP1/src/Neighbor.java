import java.io.Serializable;

/**
 * class Neighbor represents the neighbor of the GPS Office
 * 
 * @author Punit
 * 
 */
public class Neighbor implements Serializable {

	private GPSOfficeRef gpsOffice;
	private double distance;

	/**
	 * Constructor takes GPS Office and distance from the source GPS office
	 * @param office the neighbor office
	 * @param dist distance between the offices
	 */
	public Neighbor(GPSOfficeRef office, double dist) {
		gpsOffice = office;
		distance = dist;
	}

	/**
	 *  Get the neighbor office
	 * @return neighbor GPS office 
	 */
	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}

	/**
	 * Set the neighbor GPS office
	 * @param gpsOffice GPS office to be set
	 */
	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}

	/**
	 * Get the distance between the Offices
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Set the distance between the Offices
	 * @param distance distance to be set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
