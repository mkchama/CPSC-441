import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Caesar {
	
	
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7005);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			StringBuilder caesar = new StringBuilder();
			for (char i  : str.toCharArray()) {
				if(Character.isLetter(i)) {
					if(Character.isUpperCase(i)) {
						caesar.append((char) ('A'+((i- 'A' + 2) % 26)));
					}
					else if(Character.isLowerCase(i)) {
						caesar.append((char) ('a' +((i- 'a' + 2) % 26 )));
					}
				}
				else {
					caesar.append(i);
				}
			}
			String ff = caesar.toString();

			
			System.out.println("Message ciphered from client: " + caesar);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(ff.getBytes(), ff.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}

}
