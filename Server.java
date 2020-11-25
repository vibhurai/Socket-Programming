// Server side script 
import java.net.*; 
import java.io.*; 
import java.awt.geom.AffineTransform;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;



//the class for drawing the graph 
class Sketch extends JFrame {
    int width;
    int height;

    ArrayList<Node> nodes; //arraylist of all the nodes 
    ArrayList<edge> edges; //arraylist of all the edges

	//Constructor
	public Sketch() { 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        width = 30;
        height = 30;
    }

	//Construct with label
	public Sketch(String name) { 
        this.setTitle(name);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<Node>();
        edges = new ArrayList<edge>();
        width = 30;
        height = 30;
    }

    class Node {
        int x_coord, y_coord;
        String name;

        public Node(String Name, int X, int Y) {
            x_coord = X;
            y_coord = Y;
            name = Name;
        }
    }

    class edge {
        int src,des;

        public edge(int f1, int f2) {
            src = f1;
            des = f2;
        }
    }

	//function to add nodes
	public void addNode(String name, int x, int y) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name,x,y));
        this.repaint();
	}
	
	//function to add edges
    public void addEdge(int i, int j) {
        //add an edge between nodes i and j
        edges.add(new edge(i,j));
        this.repaint();
	}
	
	
	private final int array_length = 5;
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double elevation = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx*dx + dy*dy);
		
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(elevation));
        g.transform(at);
		g.setColor(Color.GREEN);
		g.drawLine(0, 0, len, 0);
        g.setColor(Color.YELLOW);
        g.fillPolygon(new int[] {len-15, len-20,len-20}, new int[] {0, -array_length, array_length}, 3);
	}
	
    public void paint(Graphics g) {
        // draw the nodes and edges

        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());
        g.setColor(Color.DARK_GRAY);
        for (Sketch.edge e : edges) {
            drawArrow(g,nodes.get(e.src).x_coord,nodes.get(e.src).y_coord,nodes.get(e.des).x_coord, nodes.get(e.des).y_coord);
        }


        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
            g.setColor(Color.DARK_GRAY);
            g.fillOval(n.x_coord-nodeWidth/2, n.y_coord-nodeHeight/2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.white);
            g.drawOval(n.x_coord-nodeWidth/2, n.y_coord-nodeHeight/2,
                    nodeWidth, nodeHeight);

            g.drawString(n.name, n.x_coord-f.stringWidth(n.name)/2,
                    n.y_coord+f.getHeight()/3);
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
			
			// we accept the 25 elements of the adjacency matrix from the client 
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
			
			System.out.println("The data has been received.\n");
			count=-1;

			//creating the adjacency matrix
			int [][] adj_mat = new int[5][5];
            for(int i=0;i<5;i++)
                for(int j=0;j<5;j++)
					adj_mat[i][j]=arr[++count];
											
			int [][]res = new int[N][N]; 
			int  source, dest, len;			
			source=arr[++count];
			dest=arr[++count];
			len=arr[++count]; 

			System.out.println("Getting results...\n");
			//Sketch for path
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

			//making the graph
			Sketch frame = new Sketch("");
			frame.setSize(400,600);
			
			frame.addNode("A", 50,170);
			frame.addNode("B", 210,100);
			frame.addNode("C", 350,170);
			frame.addNode("D", 100,300);
			frame.addNode("E", 300,300);

			for(int i=0;i<5;i++)
            for(int j=0;j<5;j++)
                if(adj_mat[i][j]==1)
					frame.addEdge(i,j);
			capture_screenshot(frame, out);
			//close the socket connection
			System.out.println("Sending the results to the client....\n");
			System.out.println("Results sent successfully!\n");
            System.out.println("Closing connection"); 
			socket.close(); 
			in.close();
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}

	//Screencapture of the JFrame
	private void capture_screenshot(JFrame Component, OutputStream op) {
        try {
            BufferedImage img = new BufferedImage(550, 550, BufferedImage.TYPE_INT_RGB);
            Component.paint(img.getGraphics());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", byteArrayOutputStream);
            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            op.write(size);
            op.write(byteArrayOutputStream.toByteArray());
            op.flush();

        } catch (IOException e) {
            System.out.println(e);
        }}
	
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
		//connecting the server to port 6789
		Server server = new Server(6969);	
	}   
} 