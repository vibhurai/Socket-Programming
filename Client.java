/*

	Name : Kaustubh Rai
	Roll number : 1810110102

*/

// Client side script

import java.net.*;
import java.io.*; 
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;

public class Client 
{ 
	// initialize socket and input output streams 
	private Socket socket		 = null; 
	private DataOutputStream out	 = null;
	private DataInputStream in	 = null; 
	

	// constructor to put ip address and port 
	public Client(String address, int port, int [] data) 
	{ 
		// establish a connection 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("\n----------------x-----CONNECTION DETAILS-----x----------------"); 
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
		System.out.println("Sending data to server.....");
		
		// we send the 25 elements of the adjacency matrix to the server
		while (count<=25) 
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
		
		//sending the source, destination and the length of the path seperately
		int fl=25;
		while (fl<=27) 
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
		
		System.out.println("Data sent successfully, waiting for results.....");

		//reading the inputs from the server
		try{
			in = new DataInputStream( 
				new BufferedInputStream(socket.getInputStream()));
			System.out.print(in.readUTF());
			
			//saving the image
			byte[] sizeAr = new byte[4];
            in.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            byte[] imageAr = new byte[size];
            in.read(imageAr);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
            ImageIO.write(image, "png", new File("Graph_image" + ".png"));	
		}
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
		System.out.println("\nYou can check the graph which has been saved in the current working directory by the name of 'Graph_image' in '.png' format.");

		System.out.println("Closing connection.....");
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
		System.out.println("\n----------------x-----CONNECTION TERMINATED-----x---------------- ");
	} 

	//driver function
	public static void main(String args[]) 
	{  
		int [] data = new int [28];
		int itr=-1; 
		Scanner myObj = new Scanner(System.in);

		//taking in the ajdacency matrix
		System.out.println("\nEnter the 5X5 adjacency matrix: ");
		for (int i = 0; i < 5; i++) {
			String line = myObj.nextLine();
			String[] arr = line.split(" ");
			for (String fl : arr) {
				data[++itr]=Integer.parseInt(fl);
			}
		}

		String source, dest;
		System.out.println("\nEnter the source (in upper case): ");
		source = myObj.next();
		char s = source.charAt(0);
		System.out.println("Enter the destination (in upper case): ");
		dest = myObj.next();
		char d = dest.charAt(0);
		int k;
		System.out.println("Enter the length of path: ");
		k=myObj.nextInt();

		data[25] =(int)s%65;
		data[26] =(int)d%65;
		data[27] =k;

		//initiating the connection
		Client client = new Client("127.0.0.1", 6969, data); 
	} 
} 