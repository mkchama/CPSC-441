import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Upper {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7003);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			String upper = str.toUpperCase();
			System.out.println("Message uppercased from client: " + upper);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(upper.getBytes(), upper.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}

}
