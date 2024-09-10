package online.mayak.energy.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://www.baeldung.com/java-acquire-lock-by-key
 * https://stackoverflow.com/questions/12450402/java-synchronizing-based-on-a-parameter-named-mutex-lock/55405291#55405291
 */
public class LockByKey<K> {

	private static class LockWrapper {
		private final Lock lock = new ReentrantLock();
		private final AtomicInteger numberOfThreadsInQueue = new AtomicInteger(1);

		private LockWrapper addThreadInQueue() {
			numberOfThreadsInQueue.incrementAndGet();
			return this;
		}

		private int removeThreadFromQueue() {
			return numberOfThreadsInQueue.decrementAndGet();
		}

	}

	private final Map<K, LockWrapper> locks = new ConcurrentHashMap<>();

	public void lock(K key) {
		LockWrapper lockWrapper = locks.compute(key, (k, v) -> v == null ? new LockWrapper() : v.addThreadInQueue());
		lockWrapper.lock.lock();
	}

	public void unlock(K key) {
		LockWrapper lockWrapper = locks.get(key);
		lockWrapper.lock.unlock();
		if (lockWrapper.removeThreadFromQueue() == 0) {
			// We pass in the specific value to remove to handle the case where another
			// thread would queue right before the removal
			locks.remove(key, lockWrapper);
		}
	}
}
