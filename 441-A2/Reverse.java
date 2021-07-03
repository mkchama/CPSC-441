import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Reverse {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7002);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			String reverse = new StringBuilder(str).reverse().toString();
			System.out.println("Message reveresed from client: " + reverse);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(reverse.getBytes(), reverse.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}


}
