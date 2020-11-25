// A Java program for a Server 
import java.net.*; 
import java.io.*; 
import java.awt.geom.AffineTransform;
import java.util.*;
import java.awt.*;
import javax.swing.*;


 class check extends JFrame {
    int width;
    int height;

    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public check() { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        width = 30;
        height = 30;
    }

    public check(String name) { //Construct with label
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        width = 30;
        height = 30;
    }

    class Node {
        int x, y;
        String name;

        public Node(String myName, int myX, int myY) {
            x = myX;
            y = myY;
            name = myName;
        }
    }

    class edge {
        int i,j;

        public edge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    public void addNode(String name, int x, int y) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name,x,y));
        this.repaint();
    }
    public void addEdge(int i, int j) {
        //add an edge between nodes i and j
        edges.add(new edge(i,j));
        this.repaint();
    }
    private final int ARR_SIZE = 5;
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        int aX = (int) x2 + x1 / len * height;
        int aY = (int) y2 + y1 / len * height;

        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len-15, len-20,len-20}, new int[] {0, -ARR_SIZE, ARR_SIZE}, 3);
    }
    // {len, len-ARR_SIZE, len-ARR_SIZE, len},
    // new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    public void paint(Graphics g) {
        // draw the nodes and edges

        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());
        g.setColor(Color.black);
        for (check.edge e : edges) {
//            gr.drawLine(nodes.get(e.source).x, nodes.get(e.source).y,
//                    nodes.get(e.destination).x, nodes.get(e.destination).y);
            drawArrow(g,nodes.get(e.i).x,nodes.get(e.i).y,nodes.get(e.j).x, nodes.get(e.j).y);

        }


        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
            g.setColor(Color.white);
            g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.black);
            g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2,
                    nodeWidth, nodeHeight);

            g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
                    n.y+f.getHeight()/3);
        }
    }
}

public class Server 
{ 
	//initialize socket and input stream 
	private Socket		 socket = null; 
	private ServerSocket server = null; 
	private DataInputStream in	 = null;
	private DataOutputStream out	 = null; 
	static int N = 5;
	
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
			int [] arr = new int[28];
			
			// we accept the 9 elements of the adjacency matrix from the client 
			while (count<=28) 
			{ 
				if(count<=25)
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
			int [][] adj_mat = new int[5][5];
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
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

			out = new DataOutputStream(socket.getOutputStream());
			if(res[source][dest]!=0)
			{
				try
				{  
					out.writeUTF("\nThe path exists!");    
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
					out.writeUTF("\nThe path does not exist!");    
				}
				catch(IOException i) 
				{ 
					System.out.println(i); 
				}
			}
			check frame = new check("The Graph");
			frame.setSize(400,600);
			frame.setVisible(true);
			frame.addNode("0", 50,170);
			frame.addNode("1", 210,100);
			frame.addNode("2", 350,170);
			frame.addNode("3", 100,300);
			frame.addNode("4", 300,300);
			for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                if(adj_mat[i][j]==1)
                    frame.addEdge(i,j);
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

	//driver function
	public static void main(String args[]) 
	{ 
		Server server = new Server(6789);	
	}  
		 
} 
