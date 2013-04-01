import java.io.IOException;
import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventGenerator;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

public class GPSOffice implements GPSOfficeRef {

	private String name;
	private double x;
	private double y;
	private List<Neighbor> neighbors;
	private RegistryProxy registry;
	private static RemoteEventGenerator<GPSOfficeEvent> eventGenerator;

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
		
		eventGenerator = new RemoteEventGenerator<GPSOfficeEvent>();

		generateNeighbors();
		// printing neighbors for debugging
		printNeighbors(this);

	}

	protected void resetNeighborNetwork(GPSOfficeRef gpsOffice, double dist) {

		printNeighbors(gpsOffice);
		try {
			System.out.println(dist + " vvvv "+gpsOffice.getGPSOfficeName());
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		try {
			List<Neighbor> neighbors = gpsOffice.getNeighbors();

			if (neighbors != null) {
				if (neighbors.size() < 3) {
					Neighbor n = new Neighbor(this, dist);
					neighbors.add(n);
					Collections.sort(neighbors, new NeighborComparator());
				} else {
					if (dist < neighbors.get(0).getDistance()) {
						Neighbor n = new Neighbor(this, dist);
						neighbors.add(0, n);
					} else if (dist < neighbors.get(1).getDistance()) {
						Neighbor n = new Neighbor(this, dist);
						neighbors.add(1, n);
					} else if (dist < neighbors.get(2).getDistance()) {
						Neighbor n = new Neighbor(this, dist);
						neighbors.add(2, n);
					}
				}
			} else {
				neighbors = new ArrayList<Neighbor>();
				Neighbor n = new Neighbor(this, dist);
				neighbors.add(n);
			}

			gpsOffice.setNeighbors(neighbors);
			registry.rebind(gpsOffice.getGPSOfficeName(), gpsOffice);

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		System.out.println("After reset");
		printNeighbors(gpsOffice);

	}

	private void printNeighbors(GPSOfficeRef gpsOffice) {

		List<Neighbor> neighbors;
		try {
			neighbors = gpsOffice.getNeighbors();

			if (neighbors != null)
				for (Neighbor n : neighbors) {
					try {
						System.out.println(n.getGpsOffice().getGPSOfficeName()
								+ " " + n.getDistance());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			System.out.println("***");
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void generateNeighbors() throws RemoteException {

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

				
				// resetting the network
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						resetNeighborNetwork(gpsOffice, dist);
					}

				});
				t.start();
				// resetting the network

				Neighbor neighbor = new Neighbor(gpsOffice, dist);
				gpsNeighbors.add(neighbor);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}

		Collections.sort(gpsNeighbors, new NeighborComparator());

		if (gpsNeighbors.size() == 1) {
			neighbors = new ArrayList<>(gpsNeighbors.subList(0, 1));
		} else if (gpsNeighbors.size() == 2) {
			neighbors = new ArrayList<>(gpsNeighbors.subList(0, 2));
		} else if (gpsNeighbors.size() >= 3) {
			neighbors = new ArrayList<>(gpsNeighbors.subList(0, 3));
		}

	}

	@Override
	public String checkPackage() throws RemoteException {
		return null;
	}

	@Override
	public String forwardPackage() throws RemoteException {
		return null;
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
	public void setNeighbors(List<Neighbor> offices) throws RemoteException {
		neighbors = offices;
	}
	
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener)
			throws RemoteException {
		return eventGenerator.addListener(listener);
	}

}
