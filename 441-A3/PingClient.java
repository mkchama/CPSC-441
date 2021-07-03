import java.util.*;
import java.net.*;
import java.text.*;
import java.io.IOException;
import java.lang.reflect.Array;

public class PingClient{
    private static final String CRLF = "\r\n";
    private static final int TIMEOUT = 1000;
    private static final int NUM_REQUESTS = 10;
    private static final int DELAY = 1000;
    
    public static void main(String [] args) throws Exception {
        if(args.length != 2){
            System.out.println("Enter Host and Port");
        }
   
        InetAddress host = InetAddress.getByName(args[0]);
        int portNumber = Integer.parseInt(args[1]);
        DatagramSocket socket = new DatagramSocket();
        ArrayList<Long> times = new ArrayList();

        
        socket.setSoTimeout(TIMEOUT);
        for(int i = 0; i < NUM_REQUESTS; i++){
            String sendMsg = makePing(i);
            byte[] sendData = sendMsg.getBytes();
            DatagramPacket pingRequest = new DatagramPacket(sendData, sendData.length, host, portNumber);
            long recieveTime;
            long sendTime= System.currentTimeMillis();
            try{
                socket.send(pingRequest);
            }
            catch (IOException e){
                System.out.println("Could not send ping");
            }
 
            DatagramPacket pingResponse = new DatagramPacket(new byte[sendMsg.length()], sendMsg.length());
            
            try {
                socket.receive(pingResponse);
                recieveTime = System.currentTimeMillis()-sendTime;
                times.add(recieveTime);
                System.out.print("Delay " + recieveTime + " ms " + "Received from " + pingResponse.getAddress().getHostAddress() + " PING " + i + " " + sendTime + CRLF);
            }
            catch (SocketTimeoutException e){
                System.out.println("Lost packet: " + "PING " + i + " " +sendTime);
                recieveTime = 1000;
                times.add(recieveTime);
                //Thread.sleep(DELAY);

            }
            catch (IOException e){
                e.printStackTrace();
                return;
            }
            

        }
    
        Long min = times.get(0);
        Long max = times.get(0);
        long sum = 0;
        long avg = 0;
        for(long j : times){
            min = min < j ? min : j;
            max = max > j ? max : j;
            sum += j;
        }

        avg = sum/times.size();
        System.out.println("RTT: minDelay: " + min + "ms / maxDelay: " + max + "ms / averageDelay: " + avg + "ms ");

    }

    private static String makePing(int i){
        return "PING " + i + " " + System.currentTimeMillis();

    }

}