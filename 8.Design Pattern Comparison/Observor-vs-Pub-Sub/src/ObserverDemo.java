import java.util.ArrayList;
import java.util.List;

interface IObserver{
    void update(String status);
}

class SMSService implements IObserver {

    @Override
    public void update(String status) {
        System.out.println("SMS send: Status is:" + status);
    }
}

class EmailService implements IObserver {

    @Override
    public void update(String status) {
        System.out.println("Email send: Status is:" + status);
    }
}

interface Subject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void notifyObservers();
}

class Order implements Subject {

    private List<IObserver> observers = new ArrayList<>();
    private String status;

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    public void attach(IObserver observer) {
        observers.add(observer);
    }

    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(status);
        }
    }
}

class ObserverDemo{
    public static void main(String[] args) {
        Order order = new Order();

        order.attach(new EmailService());
        order.attach(new SMSService());

        order.setStatus("SHIPPED");
        order.setStatus("DELIVERED");
    }
}

