/*
 * Melissa Ta, CPSC 501, Assignment 3, UCID# 10110850
 */

public class TempObject {

	private double aDouble = 98.7;
	private float aFloat = 10;
	private Random random = new Random();
	private int[] intArray = new int[4];
	private Random[] ranArr = {new Random(), new Random()};
	public TempObject(int n, int m, int k, int j)
	{
		intArray[0] = n;
		intArray[1] = m;
		intArray[2] = k;
		intArray[3] = j;
	}
	public TempObject(Random r, Random t)
	{
		ranArr[0] = r;
		ranArr[1] = t;
	}
	public TempObject()
	{
		
	}
	
	public TempObject(double d, float f)
	{
		aDouble = d;
		aFloat = f;
	}
	public TempObject(Random r)
	{
		random = r;
	}
}

