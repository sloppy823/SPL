package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.*;

/**
 * The {@link MessageBusImpl} class is the implementation of the MessageBus interface.
 * This class implements a thread-safe singleton message bus for handling events and broadcasts.
 */
public class MessageBusImpl implements MessageBus {

	// Maps for event and broadcast subscriptions
	private final ConcurrentHashMap<Class<? extends Event<?>>, Queue<MicroService>> eventSubscribers;
	private final ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> broadcastSubscribers;

	// Map for storing microservice message queues
	private final ConcurrentHashMap<MicroService, BlockingQueue<Message>> microServiceQueues;

	// Map to store futures associated with events
	private final ConcurrentHashMap<Event<?>, Future<?>> eventFutures;

	// Singleton instance
	private static class SingletonHolder {
		private static final MessageBusImpl INSTANCE = new MessageBusImpl();
	}

	// Private constructor for Singleton pattern
	private MessageBusImpl() {
		eventSubscribers = new ConcurrentHashMap<>();
		broadcastSubscribers = new ConcurrentHashMap<>();
		microServiceQueues = new ConcurrentHashMap<>();
		eventFutures = new ConcurrentHashMap<>();
	}

	// Singleton getInstance method
	public static MessageBusImpl getInstance() {
		return SingletonHolder.INSTANCE;
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
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
		@SuppressWarnings("unchecked")
		Future<T> future = (Future<T>) eventFutures.remove(e);
		if (future != null) {
			future.resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		List<MicroService> subscribers = broadcastSubscribers.getOrDefault(b.getClass(), new ArrayList<>());
		for (MicroService m : subscribers) {
			microServiceQueues.get(m).offer(b);
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Queue<MicroService> subscribers = eventSubscribers.get(e.getClass());
		if (subscribers == null || subscribers.isEmpty()) {
			return null;
		}

		MicroService target = null;
		synchronized (subscribers) {
			target = subscribers.poll(); // Round-robin selection
			if (target != null) {
				subscribers.offer(target);
			}
		}

		if (target != null) {
			Future<T> future = new Future<>();
			eventFutures.put(e, future);
			microServiceQueues.get(target).offer(e);
			return future;
		}
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
		microServiceQueues.remove(m);

		// Remove the microservice from event and broadcast subscribers
		for (Queue<MicroService> queue : eventSubscribers.values()) {
			queue.remove(m);
		}
		for (List<MicroService> list : broadcastSubscribers.values()) {
			list.remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		BlockingQueue<Message> queue = microServiceQueues.get(m);
		if (queue == null) {
			throw new IllegalStateException("MicroService is not registered.");
		}
		return queue.take();
	}
}
