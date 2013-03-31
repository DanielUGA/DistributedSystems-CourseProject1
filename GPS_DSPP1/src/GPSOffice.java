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
	private List<Neighbor> neighbors;
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

	protected void resetNeighborNetwork(GPSOfficeRef gpsOffice, double dist) {

		try {
			List<Neighbor> neighbors = gpsOffice.getNeighbors();
			
			if(dist < neighbors.get(0).getDistance()){
				Neighbor n = new Neighbor(this, dist);
				neighbors.add(0, n);
			}else if(dist < neighbors.get(1).getDistance()){
				Neighbor n = new Neighbor(this, dist);
				neighbors.add(1, n);
			}else if(dist < neighbors.get(2).getDistance()){
				Neighbor n = new Neighbor(this, dist);
				neighbors.add(2, n);
			}
			
			gpsOffice.setNeighgors(neighbors);
			registry.rebind(gpsOffice.getGPSOfficeName(), gpsOffice);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	private void resetNeighborNetwork(List<GPSOfficeRef> allGPSOffices) {

		for (GPSOfficeRef gpsOffice : allGPSOffices) {

			try {
				List<Neighbor> neighbors = new ArrayList<Neighbor>();
				double gpsOfficeX = gpsOffice.getGPSOfficeCoordinates()[0];
				double gpsOfficeY = gpsOffice.getGPSOfficeCoordinates()[1];
				double dist = Math.sqrt(Math.pow((x - gpsOfficeX), 2)
						+ Math.pow((y - gpsOfficeY), 2));

			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

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
		System.out.println();
	}

	@Override
	public void generateNeighborSet() throws RemoteException {

		List<String> offices = registry.list();
		List<Neighbor> gpsNeighbors = new ArrayList<Neighbor>();
		List<GPSOfficeRef> allGPSOffices = new ArrayList<GPSOfficeRef>();

		for (String office : offices) {
			final GPSOfficeRef gpsOffice;
			try {

				gpsOffice = (GPSOfficeRef) registry.lookup(office);
				allGPSOffices.add(gpsOffice);

				if (gpsOffice.getGPSOfficeName().equals(name)) {
					continue;
				}

				double gpsOfficeX = gpsOffice.getGPSOfficeCoordinates()[0];
				double gpsOfficeY = gpsOffice.getGPSOfficeCoordinates()[1];
				final double dist = Math.sqrt(Math.pow((x - gpsOfficeX), 2)
						+ Math.pow((y - gpsOfficeY), 2));

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						resetNeighborNetwork(gpsOffice, dist);
					}

				});
				t.start();

				Neighbor neighbor = new Neighbor(gpsOffice, dist);
				gpsNeighbors.add(neighbor);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}

		// for(Neighbor n:gpsNeighbors){
		// System.out.println(n.getGpsOffice().getGPSOfficeName());
		// }
		//
		Collections.sort(gpsNeighbors, new NeighborComparator());
		//
		// for(Neighbor n:gpsNeighbors){
		// System.out.println(n.getGpsOffice().getGPSOfficeName());
		// }

		if (gpsNeighbors.size() == 1) {
			neighbors = gpsNeighbors.subList(0, 1);
		} else if (gpsNeighbors.size() == 2) {
			neighbors = gpsNeighbors.subList(0, 2);
		} else if (gpsNeighbors.size() >= 3) {
			neighbors = gpsNeighbors.subList(0, 3);
		}

		// resetNeighborNetwork(allGPSOffices);
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
	public List<Neighbor> getNeighbors() throws RemoteException {
		return neighbors;
	}

	@Override
	public void setNeighgors(List<Neighbor> offices) throws RemoteException {
		neighbors = offices;
	}

}
