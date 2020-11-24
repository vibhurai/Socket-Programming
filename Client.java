// A Java program for a Client 
import java.net.*;

// import jdk.jshell.resources.l10n_ja;

import java.io.*; 
import java.util.Scanner;

public class Client 
{ 
	// initialize socket and input output streams 
	private Socket socket		 = null; 
	private DataOutputStream out	 = null; 
	

	// constructor to put ip address and port 
	public Client(String address, int port, int [] data) 
	{ 
		// establish a connection 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Successfully connected to server!"); 
			
			// sends output to the socket 
			out = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(UnknownHostException u) 
		{ 
			System.out.println(u); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		}
		
		int count=1;
		System.out.println("Sending data to server!");
		
		// we send the 9 elements of the adjacency matrix to the server
		while (count<=9) 
		{ 
			try
			{  
				out.writeInt(data[count-1]);    
				count++;           
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
            } 
            
		} 
		
		int fl=9;
		while (fl<=11) 
		{ 
			try
			{  
				out.writeInt(data[fl]);    
				fl++;           
			} 
			catch(IOException i) 
			{ 
				System.out.println(i); 
            } 
            
		} 
		
		System.out.println("Data sent successfully!");

		// close the connection 
		try
		{ 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	} 

	public static void main(String args[]) 
	{  
		int [] data = new int[12];
		int x=1; 
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter the 9 elements of the adjacency matrix:\n ");
		while (x<=9) 
		{ 
		             
            data[x-1] =myObj.nextInt();              
            x++;
		}
		
		int source, dest,k;
		System.out.println("Enter the source and destination vertices: ");
		source=myObj.nextInt();
		dest=myObj.nextInt();
		System.out.println("Enter the length of path: ");
		k=myObj.nextInt();

		data[9] =source;
		data[10] =dest;
		data[11] =k;
		Client client = new Client("127.0.0.1", 5000, data); 
	} 
} 
