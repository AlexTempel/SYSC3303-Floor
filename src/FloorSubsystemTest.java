import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class FloorSubsystemTest {
    @Test
    void readCSV() {
        try{
            InetAddress ipAddress = InetAddress.getLocalHost();
            int testFloorPort = 16000;
            int testSchedPort = 17000;

            ArrayList<TimedRequest> testRequests = new ArrayList<TimedRequest>();;
            String file = "TestInput.csv";

            FloorSubsystem testFloor = new FloorSubsystem(testFloorPort, testSchedPort, 5, ipAddress);
            testRequests = testFloor.readCSV(file);

            System.out.println(testRequests.get(0).getTime().truncatedTo(ChronoUnit.MINUTES));
            assertTrue(testRequests.get(0).getTime().truncatedTo(ChronoUnit.MINUTES).compareTo(LocalTime.of(17,30).truncatedTo(ChronoUnit.MINUTES)) == 0);
            assertEquals(1, testRequests.get(0).getRequest().getRequestID());
            assertEquals(18, testRequests.get(0).getRequest().getStartingFloor());
            assertEquals(19, testRequests.get(0).getRequest().getDestinationFloor());

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getCurrentRequest() {
        try{
            InetAddress ipAddress = InetAddress.getLoopbackAddress();
            int testFloorPort = 16001;
            int testSchedPort = 17001;
            ArrayList<TimedRequest> testRequests = new ArrayList<TimedRequest>();
            String file = "TestInput.csv";

            FloorSubsystem testFloor = new FloorSubsystem(testFloorPort, testSchedPort, 5, ipAddress);

            testRequests = testFloor.readCSV(file);
            Request testRequest = testFloor.getCurrentRequest(testRequests);

            assertEquals(2, testRequest.getRequestID());
            assertEquals(16, testRequest.getStartingFloor());
            assertEquals(8, testRequest.getDestinationFloor());


        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void sendToScheduler() {
        try{
            InetAddress ipAddress = InetAddress.getLoopbackAddress();
            int testFloorPort = 16002;
            int testSchedPort = 17002;
            FloorSubsystem testFloor = new FloorSubsystem(testFloorPort, testSchedPort, 5, ipAddress);

            //Get request list from input file
            ArrayList<TimedRequest> testRequests = new ArrayList<TimedRequest>();
            String file = "TestInput.csv";
            testRequests = testFloor.readCSV(file);

            //take the request occurring right now and form it into a packet message
            Request testRequest = testFloor.getCurrentRequest(testRequests);
            String message = testRequest.convertToPacketMessage();

            // Build dummy socket to receive
            InetAddress schedulerAddress = InetAddress.getByName("127.0.0.1");
            DatagramPacket sendPacket = new DatagramPacket(message.getBytes(StandardCharsets.UTF_8), message.getBytes().length, schedulerAddress,testSchedPort);
            DatagramSocket receiveSocket = new DatagramSocket(testSchedPort);

            // Send to Scheduler socket
            testFloor.getFloorSocket().send(sendPacket);


            DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
            receiveSocket.receive(receivedPacket);

            // Receive Request from floor
            assertEquals(message, Request.parsePacket(receivedPacket).convertToPacketMessage());


        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
