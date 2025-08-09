/**
 * Subject/Observable Interface - Sabhi channels ko implement karna hoga
 * Yeh interface define karta hai ki kaise subscribers manage karne hain
 */
public interface IChannel {

    /**
     * Naya subscriber add karne ke liye
     * @param subscriber - Jo subscriber add karna hai
     */
    void subscribe(ISubscriber subscriber);

    /**
     * Subscriber remove karne ke liye
     * @param subscriber - Jo subscriber remove karna hai
     */
    void unsubscribe(ISubscriber subscriber);

    /**
     * Sabhi subscribers ko notification bhejna
     */
    void notifySubscribers();

    /**
     * Channel ka naam return karta hai
     * @return channel name
     */
    String getName();

    /**
     * Latest video ka title return karta hai
     * @return latest video title
     */
    String getLatestVideo();
}