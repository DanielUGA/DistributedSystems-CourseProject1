import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;


public class Customer {
	
	private static RemoteEventListener<GPSOfficeEvent> officeListener;
	public static void main(String[] args){
		
		if(args.length != 5){
			showUsage();
		}
		
		String host = args[0];
		String name = args[2];
		int port;
		double x;
		double y;
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

		try {
			RegistryProxy registry = new RegistryProxy(host, port);
			GPSOfficeRef gpsOffice = (GPSOfficeRef) registry.lookup(name);
			String track = gpsOffice.checkPackage();
			
			officeListener = new RemoteEventListener<GPSOfficeEvent>() {
				public void report(long seqnum, GPSOfficeEvent event) {
					// Print log report on the console.
					try {
						System.out.println("office name: "+ event.getGpsOffice().getGPSOfficeName());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			};
			UnicastRemoteObject.exportObject(officeListener, 0);
			
			Lease lease = gpsOffice.addListener(officeListener);
			
			System.out.println(track);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private static void showUsage() {
		
		System.err.println("Usage: java Customer <host> <port> <name> <X> <Y>");
		System.err.println("<host> = Registry Server's host");
		System.err.println("<port> = Registry Server's port");
		System.err.println("<name> = name of the city in which GPS office is located");
		System.err.println("<X> = X co-ordinate of the destinataion");
		System.err.println("<Y> = Y co-ordinate of the destinataion");
		System.exit(1);
	}

}
