/*
 * Authors: Evan Friday, Cameron Johnston, Kevin Petersen
 * Date: 2014-03-02
 * EECE 411 Project Phase 2 Server:
 * 
 * 
 */



package clientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

<<<<<<< HEAD

//changes

=======
>>>>>>> 7b163c5fd5c14d7be1cbf02dd42311d8c92c8424

public class Server implements Remote {
	
	final static int WAITING_FOR_CONNECTION = 0;
	final static int ACCEPTING_DATA = 1;
	final static int PROPAGATING_DATA = 2;
	
	
	public static int i;
	public static int STATUS = WAITING_FOR_CONNECTION;
	public static boolean matchingKeyFound = false;
	public static boolean isPutOperation = false;
	public static byte[] command = new byte[1];
	public static byte[] key = new byte[32];
	public static byte[] value = new byte[1024];
	public static byte[] error_code = new byte[1];
	public static byte[] return_value = new byte[1024];

	
	public static ArrayList<KeyValuePair> KVStore;
	
	public static void main(String argv[]) throws IOException, OutOfMemoryError{
		//TODO: wait command for accept
		
		while(true){
				switch(STATUS){
				case WAITING_FOR_CONNECTION:
					//wait for connection
					//connection ready ->
					STATUS=ACCEPTING_DATA;
					
				case ACCEPTING_DATA:
					//read in new data
					acceptUpdate();
					STATUS=PROPAGATING_DATA;
					
				case PROPAGATING_DATA:
					//Connect to other nodes, and send data.
					propagateUpdate();
					STATUS=WAITING_FOR_CONNECTION;
					
				default:
					System.out.println("somehow we are no in the state machine...\n");
				}
		}
		
		
	}
	public static void propagateUpdate() throws IOException, OutOfMemoryError{
		//TODO: Implement Pushing features
	}
	
	public  static void acceptUpdate() throws IOException, OutOfMemoryError{
		//TODO: properly read in commands from propagate
		try {
			
			Socket serversocket = new Socket("localhost", 12345);
			InputStream is = serversocket.getInputStream();
			OutputStream os = serversocket.getOutputStream();
			
			while(true) {
				
				is.read(command, 1, 0);
				
				if(command[0] == 0x01) // Put operation - includes value
				{
					isPutOperation = true;
					is.read(key, 32, 1);
					is.read(value, 1024, 33);
					for(i=0; i<KVStore.size(); i++) // Search for a KV pair with matching key
					{
						KVStore.get(i);
						if(KeyValuePair.getKey() == key) // Match found
						{
							KeyValuePair.setValue(value);
							matchingKeyFound = true;
							break;
						}
					}
					if(matchingKeyFound)
						matchingKeyFound = false;
					else // Only add a new entry if there was none already with matching key
					{
						if(KVStore.size() < 40000)
						{
							KVStore.add(new KeyValuePair(key, value));
							error_code[0] = 0x00;
						}
						else // Out of space
						{
							error_code[0] = 0x02;
						}
					}
						
					error_code[0] = 0x00;
				}
				
				else if(command[0] == 0x02) // Get operation
				{
					is.read(key, 32, 1);
					for(i=0; i<KVStore.size(); i++) // Search for a KV pair with matching key
					{
						KVStore.get(i);
						if(KeyValuePair.getKey() == key) // Match found
						{
							return_value = KeyValuePair.getValue();
							error_code[0] = 0x00;
							matchingKeyFound = true;
							break;
						}
					}
					if(matchingKeyFound)
						matchingKeyFound = false;
					else
						error_code[0] = 0x01;
				}
				
				else if(command[0] == 0x03) // Remove operation
				{
					is.read(key, 32, 1);
					for(i=0; i<KVStore.size(); i++) // Search for a KV pair with matching key
					{
						KVStore.get(i);
						if(KeyValuePair.getKey() == key) // Match found
						{
							KVStore.remove(i);
							error_code[0] = 0x00;
							matchingKeyFound = true;
							break;
						}
					}
					if(matchingKeyFound)
						matchingKeyFound = false;
					else
						error_code[0] = 0x01;
				}
				
				else // Invalid command
					error_code[0] = 0x05;
				
				// Send result
				if(isPutOperation)
				{
					os.write(error_code);
					os.write(return_value);
					isPutOperation = false;
				}
				else
					os.write(error_code);
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
			error_code[0] = 0x04; // Internal KV Store failure?
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error_code[0] = 0x04; // Internal KV Store failure?
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			error_code[0] = 0x02; // Out of space
		}
	}
	
}