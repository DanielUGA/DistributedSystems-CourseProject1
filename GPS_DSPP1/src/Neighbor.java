import java.io.Serializable;


/**
 * @author Punit
 *
 */
public class Neighbor implements Serializable{
	
	private GPSOfficeRef gpsOffice;
	private double distance;
	
	/**
	 * @param office
	 * @param dist
	 */
	public Neighbor(GPSOfficeRef office, double dist){
		gpsOffice = office;
		distance = dist;
	}
	/**
	 * @return
	 */
	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}
	/**
	 * @param gpsOffice
	 */
	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}
	/**
	 * @return
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
