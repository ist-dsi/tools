package pt.utl.ist.fenix.tools.file.rmi;


public class RemoteStreamHandlerFactory {
	
	
	/**
	 * By default a remote stream will be considered idle and it will be closed if not being used for 10 seconds
	 */
	public static final long DEFAULT_REMOTE_STREAMS_IDLE_TIMEOUT = 10000L;
	/**
	 * By default the thread will check for idle streams in 1 second intervals
	 */
	public static final long DEFAULT_REMOTE_STREAMS_IDLE_CHECK_INTERVAL = 1000L;

	public static final String REMOTE_STREAM_HANDLER_CLEANING_INTERVAL_KEY="remote.streams.handler.cleaning.interval";
	public static final String REMOTE_STREAM_HANDLER_MAX_IDLE_INTERVAL_KEY="remote.streams.handler.max.idle.interval";
	
	private static RemoteStreamHandlerFactory instance=new RemoteStreamHandlerFactory();
	private RemoteStreamsHandlerImpl remoteStreamHandler=null;
	
	public static RemoteStreamHandlerFactory getInstance()
	{
		return instance;
	}
	
	public synchronized void initRemoteStreamsHandler(long remoteStreamsHandlerMaxIdleInterval,long remoteStreamsHandlerCleaningInterval)
	{
		if(remoteStreamHandler==null)
		{
			if(remoteStreamsHandlerMaxIdleInterval<=0)
				remoteStreamsHandlerMaxIdleInterval=DEFAULT_REMOTE_STREAMS_IDLE_TIMEOUT;
			if(remoteStreamsHandlerCleaningInterval<=0)
				remoteStreamsHandlerCleaningInterval=DEFAULT_REMOTE_STREAMS_IDLE_CHECK_INTERVAL;
			
			remoteStreamHandler=new RemoteStreamsHandlerImpl(remoteStreamsHandlerMaxIdleInterval,remoteStreamsHandlerCleaningInterval);
			remoteStreamHandler.start();
		}
		
	}

	private synchronized RemoteStreamsHandlerImpl getRemoteStreamsHandler()
	{
		if(remoteStreamHandler==null)
		{
			remoteStreamHandler=new RemoteStreamsHandlerImpl(DEFAULT_REMOTE_STREAMS_IDLE_TIMEOUT,DEFAULT_REMOTE_STREAMS_IDLE_CHECK_INTERVAL);;
			remoteStreamHandler.start();
		}
		
		return remoteStreamHandler;
	}

	public static void manageStream(RemoteHandledStream stream)
	{
		getInstance().getRemoteStreamsHandler().manageStream(stream);
	}
	public static void unManageStream(RemoteHandledStream stream)
	{
		getInstance().getRemoteStreamsHandler().unManageStream(stream);
	}
	
	public static void shutdown()
	{
		if(getInstance().remoteStreamHandler!=null)
			getInstance().remoteStreamHandler.stopNow();
		
		getInstance().remoteStreamHandler=null;
	}
	
}
