import java.net.InetAddress;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        int floorPort = 19555;
        int schedulerPort = 20000;
        InetAddress schedulerIP = InetAddress.getLoopbackAddress();

        try {
            FloorSubsystem floorSubsystem = new FloorSubsystem(floorPort, schedulerPort, 22, schedulerIP);

            Thread floorThread = new Thread(floorSubsystem);
            floorThread.start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}