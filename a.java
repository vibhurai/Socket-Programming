/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kaustubh
 */import java.util.Scanner;
public class a {
    static int N = 3; 
    

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

// Driver code 
public static void main(String[] args) 
{ 
	// int G[][] = { { 0, 1, 0 }, 
    // { 1, 0, 1 }, 
    // { 0, 0, 0 } }; 
	int [][] data = new int[3][3];
		int x=1; 
		Scanner myObj = new Scanner(System.in);
		for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
                    data[i][j]=myObj.nextInt();
	int k; 
	int [][]res = new int[N][N]; 
	int  source, dest;
	System.out.println("Enter the source and destination vertices: ");
	source=myObj.nextInt();
	dest=myObj.nextInt();

	System.out.println("Enter the length of path: ");
	k=myObj.nextInt();


	power(data, res, k); 


	if(res[source][dest]!=0)
		System.out.print("\nThe path exists!");
	else
		System.out.print("\nThe path does not exist!");
		 
	} 
}
