package tests;

import java.util.LinkedList;

import peers.Host;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting tracker");
		Host tracker = new Host();
		LinkedList<Host> hosts = new LinkedList<Host>();
		for(int i=1;i<2;i++){
			System.out.println("Starting host "+i);
			hosts.add(new Host(true));
			
		}
	}

}
