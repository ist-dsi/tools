package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoteStreamsHandlerImpl extends Thread {

	private volatile boolean stop = false;

	private List<RemoteHandledStream> remoteStreams = new ArrayList<RemoteHandledStream>();

	private long timeout = 1000L;

	private long checkInterval = 100L;

	public RemoteStreamsHandlerImpl(long timeout, long checkInterval) {
		super("RemoteStreamsHandlerThread");
		this.timeout = timeout;
		this.checkInterval = checkInterval;
	}

	@Override
	public void run() {
		while (!stop) {
			synchronized (remoteStreams) {
				try {
					remoteStreams.wait(checkInterval);
				} catch (InterruptedException ignored) {
				}

				if (stop) {
					break;
				}

				ArrayList<RemoteHandledStream> toRemove = new ArrayList<RemoteHandledStream>(remoteStreams.size());

				for (RemoteHandledStream ref : remoteStreams) {
					if (stop) {
						break;
					}

					if (ref == null) {
						toRemove.add(ref);
					} else {
						if (System.currentTimeMillis() - ref.getLastAccessTime() >= timeout && !ref.hasEnded()) {
							try {
								ref.destroy();
							} catch (IOException ignored) {
							}
							toRemove.add(ref);
						} else if (ref.hasEnded()) {
							toRemove.add(ref);
						}
					}
				}

				remoteStreams.removeAll(toRemove);
			}

		}
	}

	public void stopNow() {
		synchronized (remoteStreams) {
			stop = true;
			remoteStreams.notify();
		}
	}

	public void manageStream(RemoteHandledStream newStream) {
		synchronized (remoteStreams) {
			remoteStreams.add(newStream);
			remoteStreams.notify();
		}
	}

	public void unManageStream(RemoteHandledStream stream) {
		ArrayList<RemoteHandledStream> toRemove = new ArrayList<RemoteHandledStream>(remoteStreams.size());
		ArrayList<RemoteHandledStream> originalList = new ArrayList<RemoteHandledStream>(remoteStreams.size());
		synchronized (remoteStreams) {
			originalList.addAll(remoteStreams);
		}
		for (RemoteHandledStream ref : originalList) {
			if (ref == null) {
				toRemove.add(ref);
			} else if (ref == stream) {
				toRemove.add(ref);
			}
		}
		synchronized (remoteStreams) {
			remoteStreams.removeAll(toRemove);
			remoteStreams.notify();
		}

	}

}
