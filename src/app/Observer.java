package app;

public interface Observer {
    /**
     *
     * @param notification
     */
    void receiveUpdate(Notifications notification);
}
