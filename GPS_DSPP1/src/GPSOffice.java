import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

public class GPSOffice implements GPSOfficeRef {

	private String name;
	private double x;
	private double y;
	private Neighbor[] neighbors;
	private RegistryProxy registry;

	public GPSOffice(String[] args) throws IOException {

		if (args.length != 5) {
			throw new IllegalArgumentException(
					"Usage: java Start GPSOffice <host> <port> <name> <X> <Y>");
		}

		String host = args[0];
		name = args[2];
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Port has to be an Integer value");
		}

		try {
			x = Double.parseDouble(args[3]);
			y = Double.parseDouble(args[4]);
		} catch (NumberFormatException e) {
			throw new NumberFormatException(
					"X and Y co-ordinates should be Double value");
		}

		registry = new RegistryProxy(host, port);
		UnicastRemoteObject.exportObject(this, 0);

		
		generateNeighborSet();
		
		try {
			registry.bind(name, this);
		} catch (AlreadyBoundException e) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException e2) {
				e2.printStackTrace();
			}
			throw new IllegalArgumentException("GPSOffice(): <name> = \""
					+ name + "\" already exists");
		} catch (RemoteException e) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException e2) {
				e2.printStackTrace();
			}
			throw e;
		}

		
		// printing neighbors for debugging
		printNeighbors();

	}

	private void resetNeighborNetwork() {
		
	}

	private void printNeighbors() {

		if (neighbors != null)
			for (Neighbor n : neighbors) {
				try {
					System.out.println(n.getGpsOffice().getGPSOfficeName()
							+ " " + n.getDistance());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
	}

	@Override
	public void generateNeighborSet() throws RemoteException {

		List<String> offices = registry.list();
		List<Neighbor> gpsNeighbors = new ArrayList<Neighbor>();

		for (String office : offices) {
			GPSOfficeRef gpsOffice;
			try {
				
				gpsOffice = (GPSOfficeRef) registry.lookup(office);
				
				if (gpsOffice.getGPSOfficeName().equals(name)) {
					continue;
				}
				
				double gpsOfficeX = gpsOffice.getGPSOfficeCoordinates()[0];
				double gpsOfficeY = gpsOffice.getGPSOfficeCoordinates()[1];
				double dist = Math.sqrt(Math.pow((x - gpsOfficeX), 2)
						+ Math.pow((y - gpsOfficeY), 2));
				
				Neighbor neighbor = new Neighbor(gpsOffice, dist);
				gpsNeighbors.add(neighbor);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}

		for(Neighbor n:gpsNeighbors){
			System.out.println(n.getGpsOffice().getGPSOfficeName());
		}
		
		Collections.sort(gpsNeighbors,new NeighborComparator());
		
		for(Neighbor n:gpsNeighbors){
			System.out.println(n.getGpsOffice().getGPSOfficeName());
		}
		
		if (gpsNeighbors.size() == 1) {
			neighbors = new Neighbor[] { gpsNeighbors.get(0) };
		} else if (gpsNeighbors.size() == 2) {
			neighbors = new Neighbor[] { gpsNeighbors.get(0),
					gpsNeighbors.get(1) };
		} else if (gpsNeighbors.size() >= 3) {
			neighbors = new Neighbor[] { gpsNeighbors.get(0),
					gpsNeighbors.get(1), gpsNeighbors.get(2) };
		}
		
		
		
		resetNeighborNetwork();
	}

	@Override
	public void checkPackage() throws RemoteException {

	}

	@Override
	public void forwardPackage() throws RemoteException {

	}

	@Override
	public String getGPSOfficeName() throws RemoteException {
		return name;
	}

	@Override
	public double[] getGPSOfficeCoordinates() throws RemoteException {
		return new double[] { x, y };
	}

	@Override
	public void setNeighgors(Neighbor[] offices) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Neighbor[] getNeighbors() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
