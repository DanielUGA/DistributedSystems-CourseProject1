import java.io.Serializable;


public class Neighbor implements Serializable{
	
	private GPSOfficeRef gpsOffice;
	private double distance;
	
	public Neighbor(GPSOfficeRef office, double dist){
		gpsOffice = office;
		distance = dist;
	}
	public GPSOfficeRef getGpsOffice() {
		return gpsOffice;
	}
	public void setGpsOffice(GPSOfficeRef gpsOffice) {
		this.gpsOffice = gpsOffice;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
