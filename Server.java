// A Java program for a Server 
import java.net.*; 
import java.io.*; 

public class Server 
{ 
	//initialize socket and input stream 
	private Socket		 socket = null; 
	private ServerSocket server = null; 
	private DataInputStream in	 = null; 
	static int N = 3;
	// constructor with port 
	public Server(int port) 
	{ 
		// starts server and waits for a connection 
		try
		{ 
			server = new ServerSocket(port); 
			System.out.println("Server started");
			System.out.println("Waiting for a client to connect..."); 
			socket = server.accept(); 
			System.out.println("Connection established successfully!!"); 

			// takes input from the client socket 
			in = new DataInputStream( 
				new BufferedInputStream(socket.getInputStream())); 

			int count=1;
			
			//array to store incoming data
			int [] arr = new int[12];
			
			// we accept the 9 elements of the adjacency matrix from the client 
			while (count<=12) 
			{ 
				if(count<=9)
				{
					try
					{ 
						arr[count-1] = in.readInt(); 
						count++;
					}
					catch(IOException i) 
					{ 
						System.out.println(i); 
					} 
				}
				else
				{
					try
					{ 
						arr[count-1] = in.readInt(); 
						count++;
					}
					catch(IOException i) 
					{ 
						System.out.println(i); 
					}
				}
				
			}
			
			
			
			count=-1;

			//creating the adjacency matrix
			int [][] adj_mat = new int[3][3];
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
					adj_mat[i][j]=arr[++count];
								
			// System.out.println("\nFollowing is the adjacency matrix: \n");
			// for(int i=0;i<3;i++)
            // {
            //     System.out.println("\n");
            //     for(int j=0;j<3;j++)
            //         System.out.print(adj_mat[i][j]+"-");        
			// }
			// System.out.print("\nSource: "+ arr[++count]);
			// System.out.print("\nDestination: "+ arr[++count]);
			// System.out.print("\nLength: "+ arr[++count]);
			
			int [][]res = new int[N][N]; 
			int  source, dest, len;			
			source=arr[++count];
			dest=arr[++count];
			len=arr[++count]; 

			power(adj_mat, res, len); 


			if(res[source][dest]!=0)
				System.out.print("\nThe path exists!\n");
			else
				System.out.print("\nThe path does not exist!\n");

			
			//close the socket connection
            System.out.println("Closing connection"); 
			socket.close(); 
			in.close();
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}

	// Function to multiply two matrices 
	static void multiply(int a[][], int b[][], int res[][]) 
	{ 
		int [][]mul = new int[N][N]; 
		for (int i = 0; i < N; i++) 
		{ 
			for (int j = 0; j < N; j++) 
			{ 
				mul[i][j] = 0; 
				for (int k = 0; k < N; k++) 
					mul[i][j] += a[i][k] * b[k][j]; 
			} 
		} 

		// Storing the multiplication result in res[][] 
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++) 
				res[i][j] = mul[i][j]; 
	}  

	// Function to compute G raised to the power n 
	static void power(int G[][], int res[][], int n) 
	{ 

		// Base condition 
		if (n == 1) { 
			for (int i = 0; i < N; i++) 
				for (int j = 0; j < N; j++) 
					res[i][j] = G[i][j]; 
			return; 
		} 

		// Recursion call for first half 
		power(G, res, n / 2); 

		// Multiply two halves 
		multiply(G, G, res); 

		// If n is odd 
		if (n % 2 != 0) 
			multiply(res, G, res); 
	} 
	public static void main(String args[]) 
	{ 
		Server server = new Server(5000);
		// int [][]res = new int[N][N]; 
		// int  source, dest;
		// System.out.println("Enter the source and destination vertices: ");
		// source=myObj.nextInt();
		// dest=myObj.nextInt();

		// System.out.println("Enter the length of path: ");
		// k=myObj.nextInt();


		// power(data, res, k); 


		// if(res[source][dest]!=0)
		// 	System.out.print("\nThe path exists!");
		// else
		// 	System.out.print("\nThe path does not exist!");
			
		}  
		 
} 
