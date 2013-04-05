import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventFilter;
import edu.rit.ds.RemoteEventGenerator;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * @author Punit
 * 
 */
public class GPSOffice implements GPSOfficeRef {

	private String name;
	private double x;
	private double y;
	private List<Neighbor> neighbors;
	private RegistryProxy registry;
	private static RemoteEventGenerator<GPSOfficeEvent> eventGenerator;
	private ScheduledExecutorService reaper;

	/**
	 * @param args
	 * @throws IOException
	 */
	public GPSOffice(String[] args) throws IOException {

		reaper = Executors.newSingleThreadScheduledExecutor();

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

		eventGenerator = new RemoteEventGenerator<GPSOfficeEvent>();

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

	}

	@Override
	public synchronized void generateNeighbors(long trackingNumber,
			final double x2, final double y2) throws RemoteException {

		List<String> offices = registry.list("GPSOffice");
		List<Neighbor> gpsNeighbors = new ArrayList<Neighbor>();
		List<GPSOfficeRef> allGPSOffices = new ArrayList<GPSOfficeRef>();

		for (String office : offices) {
			final GPSOfficeRef gpsOffice;
			try {

				gpsOffice = (GPSOfficeRef) registry.lookup(office);
				if (gpsOffice == null)
					continue;

				allGPSOffices.add(gpsOffice);

				try {
					if (gpsOffice.getGPSOfficeName().equals(name))
						continue;

				} catch (java.rmi.ConnectException e) {
					// When the GPSOffice is externally killed, the registry
					// takes some time to unbind it. If a look up is made
					// meanwhile, the unbound object is also return in the list
					// of the lookup
					continue;
				}

				double gpsOfficeX = gpsOffice.getGPSOfficeCoordinates()[0];
				double gpsOfficeY = gpsOffice.getGPSOfficeCoordinates()[1];
				final double dist = getDistance(x, y, gpsOfficeX, gpsOfficeY);

				Neighbor neighbor = new Neighbor(gpsOffice, dist);
				gpsNeighbors.add(neighbor);
			} catch (NotBoundException e) {
				e.printStackTrace();
				// eventGenerator.reportEvent(new GPSOfficeEvent(this,
				// trackingNumber, x2, y2, 3));
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
	public synchronized void forwardPackage(GPSOfficeRef office,
			final long trackingNumber, final double x2, final double y2,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException {

		final GPSOffice currentOffice = this;

		// reaper.schedule(new Runnable() {
		// public void run() {
		try {
			office = (GPSOfficeRef) registry.lookup(office.getGPSOfficeName());
			if (office != null) {
				eventGenerator.reportEvent(new GPSOfficeEvent(currentOffice,
						trackingNumber, x2, y2, 2));
				office.checkPackage(trackingNumber, x2, y2, officeListener);
			} else {
				eventGenerator.reportEvent(new GPSOfficeEvent(currentOffice,
						trackingNumber, x2, y2, 3));
			}

		} catch (RemoteException e) {
			e.printStackTrace();
			eventGenerator.reportEvent(new GPSOfficeEvent(currentOffice,
					trackingNumber, x2, y2, 3));
		} catch (Exception e) {
			e.printStackTrace();
			eventGenerator.reportEvent(new GPSOfficeEvent(currentOffice,
					trackingNumber, x2, y2, 3));
		}

		// }, 0, TimeUnit.SECONDS);

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

	@Override
	public Lease addListener(RemoteEventListener<GPSOfficeEvent> listener,
			RemoteEventFilter<GPSOfficeEvent> filter) throws RemoteException {
		return eventGenerator.addListener(listener, filter);
	}

	@Override
	public synchronized long checkPackage(long trackingNumber, final double x2,
			final double y2,
			final RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException, NotBoundException, InterruptedException {

		if (trackingNumber == 0l) {
			trackingNumber = System.currentTimeMillis();
		}

		GPSOfficeEventFilter filter = new GPSOfficeEventFilter(trackingNumber);
		addListener(officeListener, filter);

		final long tempTrack = trackingNumber;

		eventGenerator.reportEvent(new GPSOfficeEvent(this, trackingNumber, x2,
				y2, 1));

		reaper.schedule(new Runnable() {
			public void run() {
				try {
					examinePackage(tempTrack, x2, y2, officeListener);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}, 0, TimeUnit.SECONDS);

		return trackingNumber;
	}

	@Override
	public synchronized void examinePackage(long trackingNumber,
			final double x2, final double y2,
			RemoteEventListener<GPSOfficeEvent> officeListener)
			throws RemoteException {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			eventGenerator.reportEvent(new GPSOfficeEvent(this, trackingNumber,
					x2, y2, 3));
		}

		GPSOfficeRef office = null;
		try {

			generateNeighbors(trackingNumber, x2, y2);
			// printNeighbors(this);

			double destDist = getDistance(x, y, x2, y2);
			// System.out.println("dest dist: " + destDist);

			double[] neigh = new double[] { Double.MAX_VALUE, Double.MAX_VALUE,
					Double.MAX_VALUE };

			if (neighbors != null) {
				for (int i = 0; i < neighbors.size(); i++) {
					neigh[i] = getDistance(neighbors.get(i).getGpsOffice()
							.getGPSOfficeCoordinates()[0], neighbors.get(i)
							.getGpsOffice().getGPSOfficeCoordinates()[1], x2,
							y2);
				}
			}

			if (destDist <= neigh[0] && destDist <= neigh[1]
					&& destDist <= neigh[2]) {
				// System.out.println("direct");
				eventGenerator.reportEvent(new GPSOfficeEvent(this,
						trackingNumber, x2, y2, 4));
			} else {

				// System.out.println("neigh");
				if (neigh[0] < neigh[1]) {
					if (neigh[0] < neigh[2]) {
						// System.out.println("neigh 1.");
						office = (GPSOfficeRef) registry.lookup(neighbors
								.get(0).getGpsOffice().getGPSOfficeName());

					} else {
						// System.out.println("neigh 3.");
						office = (GPSOfficeRef) registry.lookup(neighbors
								.get(2).getGpsOffice().getGPSOfficeName());
					}
				} else {
					if (neigh[1] < neigh[2]) {
						// System.out.println("neigh 2");
						office = (GPSOfficeRef) registry.lookup(neighbors
								.get(1).getGpsOffice().getGPSOfficeName());
					} else {
						// System.out.println("neigh 3");
						office = (GPSOfficeRef) registry.lookup(neighbors
								.get(2).getGpsOffice().getGPSOfficeName());
					}
				}
				forwardPackage(office, trackingNumber, x2, y2, officeListener);

			}
		} catch (Exception e) {
			e.printStackTrace();
			eventGenerator.reportEvent(new GPSOfficeEvent(this, trackingNumber,
					x2, y2, 3));
		}

	}

	/**
	 * @param tempX1
	 * @param tempY1
	 * @param tempX2
	 * @param tempY2
	 * @return
	 */
	private double getDistance(double tempX1, double tempY1, double tempX2,
			double tempY2) {

		return Math.sqrt(Math.pow((tempX1 - tempX2), 2)
				+ Math.pow((tempY1 - tempY2), 2));
	}

}
