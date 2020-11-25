// A Java program for a Client 
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

		try{
			in = new DataInputStream( 
				new BufferedInputStream(socket.getInputStream()));
			System.out.print(in.readUTF());
			byte[] sizeAr = new byte[4];
            in.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            byte[] imageAr = new byte[size];
            in.read(imageAr);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
            ImageIO.write(image, "png", new File("image" + System.currentTimeMillis() + ".png"));	
		}
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
		System.out.println("\nYou can check the graph which has been saved in the current working directory.");

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

	//driver function
	public static void main(String args[]) 
	{  
		int [] data = new int [28];//{0,1,0,0,0,0,0,1,1,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,0,0,3,1};//new int[28];
		int x=1; 
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter the 25 elements of the adjacency matrix:\n ");
		
		// taking in the adjacent matrix elements
		while (x<=25) 
		{ 
		             
            data[x-1] =myObj.nextInt();              
            x++;
		}
		String source, dest;
		//getting source, destination and length from user
		// int source, dest,k;
		System.out.println("Enter the source (in upper case): ");
		source = myObj.next();
		char s = source.charAt(0);
		System.out.println("Enter the destination (in upper case): ");
		dest = myObj.next();
		char d = dest.charAt(0);
		int k;
		// myObj.nextInt();
		System.out.println("Enter the length of path: ");
		k=myObj.nextInt();

		data[25] =(int)s%65;
		data[26] =(int)d%65;
		data[27] =k;

		Client client = new Client("127.0.0.1", 6789, data); 
	} 
} 
