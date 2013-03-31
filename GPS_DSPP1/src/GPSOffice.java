import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import edu.rit.ds.registry.AlreadyBoundException;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

public class GPSOffice implements GPSOfficeRef {

	private String name;
	private double x;
	private double y;
	private List<GPSOffice> neighbors;
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

		registry = new RegistryProxy(host,port);
		UnicastRemoteObject.exportObject (this, 0);
		
		try {
			registry.bind(name, this);
		} catch (AlreadyBoundException e) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException e2) {
				e2.printStackTrace();
			}
			throw new IllegalArgumentException("GPSOffice(): <name> = \"" + name
					+ "\" already exists");
		} catch (RemoteException e) {
			try {
				UnicastRemoteObject.unexportObject(this, true);
			} catch (NoSuchObjectException e2) {
				e2.printStackTrace();
			}
			throw e;
		}

		generateNeighborSet();

	}

	@Override
	public void generateNeighborSet() throws RemoteException {

		List<String> offices = registry.list();
		
		for(String office:offices){
			GPSOfficeRef gpsOffice;
			try {
				gpsOffice = (GPSOffice) registry.lookup(office);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void checkPackage() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void forwardPackage() throws RemoteException {
		// TODO Auto-generated method stub

	}

}
