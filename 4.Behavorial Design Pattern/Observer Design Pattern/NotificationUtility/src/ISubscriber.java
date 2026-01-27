/**
 * Observer Interface - Sabhi subscribers ko implement karna hoga
 * Yeh interface define karta hai ki kaise notification receive karni hai
 */
public interface ISubscriber {

    /**
     * Update method - jab channel mein koi naya video upload hota hai
     * @param channelName - Konsa channel hai
     * @param videoTitle - Nayi video ka title
     */
    void update(String channelName, String videoTitle);

    /**
     * Subscriber ka naam return karta hai
     * @return subscriber name
     */
    String getName();
}