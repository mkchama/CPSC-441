import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Lower {
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7004);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			String lower = str.toLowerCase();
			System.out.println("Message lowercased from client: " + lower);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(lower.getBytes(), lower.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}

}
