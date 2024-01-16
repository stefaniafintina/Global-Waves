package app;

/**
 * Represents the subject in the Observer design pattern.
 * This interface declares methods for registering, removing, and notifying observers.
 * Classes that implement this interface act as subjects that observers can subscribe to.
 */
public interface Subject {
    /**
     * Registers an observer to receive updates.
     *
     * @param o The observer to be registered.
     */
    void registerObserver(Observer o);

    /**
     * Removes an observer from the subscription list.
     *
     * @param o The observer to be removed.
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers with a specific notification.
     *
     * @param notification The notification object to be sent to all observers.
     */

    void notifyObservers(Notifications notification);
}
