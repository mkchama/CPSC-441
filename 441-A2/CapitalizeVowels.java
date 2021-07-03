import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CapitalizeVowels {
	public static void main(String[] args) throws Exception {
		DatagramSocket ds = new DatagramSocket(7006);
		while(true) {
			
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, 1024);
			ds.receive(dp);
			String str = new String(dp.getData(), 0, dp.getLength());
			StringBuilder vowel = new StringBuilder();
			for (char ch  : str.toCharArray()) {
				char i = Character.toUpperCase(ch);
				if(i == 'A' || i == 'E' || i == 'I' || i == 'O' || i == 'U') {
						vowel.append(i);
				}
					
				else {
					vowel.append(ch);
					}
				}
			
			String ff = vowel.toString();
			
			System.out.println("Message capitalized vowels from client: " + ff);
			
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramPacket dpb = new DatagramPacket(ff.getBytes(), ff.length(), ip, 7700);
			ds.send(dpb);
		}
		
		
	}

}
