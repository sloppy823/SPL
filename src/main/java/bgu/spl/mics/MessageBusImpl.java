package bgu.spl.mics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;


/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private final ConcurrentHashMap<Class<? extends Message>, Queue<MicroService>> eventSubscribers;
	private final Map<Class<? extends Broadcast>, List<MicroService>> broadcastSubscribers;
	private final Map<MicroService, BlockingQueue<Message>> microServiceQueues;


	private MessageBusImpl() {
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		microServiceQueues = new ConcurrentHashMap<>();
	}

	@Override
	public synchronized  <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventSubscribers.putIfAbsent(type, new LinkedList<>());
		Queue<MicroService> subscribersQueue = eventSubscribers.get(type);
		if (!subscribersQueue.contains(m)) {
			subscribersQueue.offer(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcastSubscribers.putIfAbsent(type, new LinkedList<>());
		List<MicroService> subscribersList = broadcastSubscribers.get(type);
		if (!subscribersList.contains(m)) {
			subscribersList.add(m);
		}


	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		//take the microservice that is subscribed to the event type from the queue
		//eventSubscriber.get(e).handleEvent(e);
		return null;
	}

	@Override
	public void register(MicroService m) {
		if (microServiceQueues.containsKey(m)) {
			throw new IllegalStateException("MicroService is already registered.");
		}
		microServiceQueues.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}



}
