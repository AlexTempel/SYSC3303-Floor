import java.time.LocalTime;
public class TimedRequest {
    private LocalTime time;
    private final Request request;
    TimedRequest(LocalTime time, Request request){
        this.time = time;
        this.request = request;
    }

    public LocalTime getTime(){
        return time;
    }

    public Request getRequest(){
        return request;
    }
}
