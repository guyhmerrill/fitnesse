package com.agrium.customer.mdm.fitnesse;

import java.util.Random;

public class TestRandom {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random generator = new Random(1112);
		
		
		double random = generator.nextDouble();
		
		
		System.out.println(" random "  + generator.nextDouble());
		System.out.println(" random "  + generator.nextDouble());
		System.out.println(" random "  + generator.nextDouble());
		
		double random3Digit = (Math.random() );
		
		System.out.println(" random3Digit "  + random3Digit);

	}

}
