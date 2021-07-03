import java.net.*; 
import java.io.*; 

public class Echo {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7001);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			System.out.println("Message echoed from client: " + str);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(str.getBytes(), str.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}

}
