package raft;

import io.netty.channel.ChannelFuture;
import logger.Logger;
import node.timer.NodeTimer;
import raft.proto.AppendEntriesRPC.AppendEntries.RequestType;
import raft.proto.VoteRPC.ResponseVoteRPC;
import raft.proto.Work.WorkMessage;
import server.ServerUtils;
import server.db.DatabaseService;
import server.edges.EdgeInfo;
import server.queue.ServerQueueService;

public class FollowerService extends Service implements Runnable {

	public static Boolean isHeartBeatRecieved = Boolean.FALSE;
	NodeTimer timer;

	private static FollowerService INSTANCE = null;
	Thread fThread = null;
	private FollowerService() {
		// TODO Auto-generated constructor stub
	}

	public static FollowerService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FollowerService();

		}
		return INSTANCE;
	}

	@Override
	public void run() {
		Logger.DEBUG("-----------------------FOLLOWER SERVICE STARTED ----------------------------");
		initFollower();

		fThread = new Thread(){
		    public void run(){
				while (running) {
					while (NodeState.getInstance().getState() == NodeState.FOLLOWER) {
					}
				}

		    }
		 };

		fThread.start();
		ServerQueueService.getInstance().createGetQueue();
	}

	private void initFollower() {
		// TODO Auto-generated method stub

		timer = new NodeTimer();

		timer.schedule(new Runnable() {
			@Override
			public void run() {
				NodeState.getInstance().setState(NodeState.CANDIDATE);
			}
		}, ServerUtils.getElectionTimeout());

	}

	public void onReceivingHeartBeatPacket() {
		timer.reschedule(ServerUtils.getElectionTimeout());
	}

	@Override
	public WorkMessage handleRequestVoteRPC(WorkMessage workMessage) {

		if (workMessage.getVoteRPCPacket().getRequestVoteRPC().getTimeStampOnLatestUpdate() < NodeState.getTimeStampOnLatestUpdate()) {
			Logger.DEBUG(NodeState.getInstance().getServerState().getConf().getNodeId() + " has replied NO");
			return ServiceUtils.prepareResponseVoteRPC(ResponseVoteRPC.IsVoteGranted.NO);

		}
		Logger.DEBUG(NodeState.getInstance().getServerState().getConf().getNodeId() + " has replied YES");
		return ServiceUtils.prepareResponseVoteRPC(ResponseVoteRPC.IsVoteGranted.YES);

	}

	public void handleHeartBeat(WorkMessage wm) {
		Logger.DEBUG("HeartbeatPacket received from leader :" + wm.getHeartBeatPacket().getHeartbeat().getLeaderId());
		NodeState.currentTerm = wm.getHeartBeatPacket().getHeartbeat().getTerm();
		onReceivingHeartBeatPacket();
		WorkMessage heartBeatResponse = ServiceUtils.prepareHeartBeatResponse();

		for (EdgeInfo ei : NodeState.getInstance().getServerState().getEmon().getOutboundEdges().getMap().values()) {

			if (ei.isActive() && ei.getChannel() != null
					&& ei.getRef() == wm.getHeartBeatPacket().getHeartbeat().getLeaderId()) {

				Logger.DEBUG("Sent HeartBeatResponse to " + ei.getRef());
				ChannelFuture cf = ei.getChannel().writeAndFlush(heartBeatResponse);
				if (cf.isDone() && !cf.isSuccess()) {
					Logger.DEBUG("failed to send message (HeartBeatResponse) to server");
				}
			}
		}

	}

	@Override
	public void handleAppendEntries(WorkMessage wm) {
		String key = wm.getAppendEntriesPacket().getAppendEntries().getImageMsg().getKey();
		byte[] image = wm.getAppendEntriesPacket().getAppendEntries().getImageMsg().getImageData().toByteArray();
		long unixTimeStamp = wm.getAppendEntriesPacket().getAppendEntries().getTimeStampOnLatestUpdate();
		RequestType type = wm.getAppendEntriesPacket().getAppendEntries().getRequestType();
		
		if (type == RequestType.GET) {
			DatabaseService.getInstance().getDb().get(key);
		} else if (type == RequestType.POST) {
			NodeState.setTimeStampOnLatestUpdate(unixTimeStamp);
			DatabaseService.getInstance().getDb().post(key, image, unixTimeStamp);
		} else if (type == RequestType.PUT) {
			NodeState.setTimeStampOnLatestUpdate(unixTimeStamp);
			DatabaseService.getInstance().getDb().put(key, image, unixTimeStamp);
		} else if (type == RequestType.DELETE) {
			NodeState.setTimeStampOnLatestUpdate(System.currentTimeMillis());
			DatabaseService.getInstance().getDb().delete(key);
		}
		
		Logger.DEBUG("Inserted entry with key " + key + " received from "
				+ wm.getAppendEntriesPacket().getAppendEntries().getLeaderId());
	}
	
	@Override
	public byte[] handleGetMessage(String key) {
		System.out.println("GET Request Processed by Node: " + NodeState.getInstance().getServerState().getConf().getNodeId());
		NodeState.updateTaskCount();
		return DatabaseService.getInstance().getDb().get(key);
	}


	@Override
	public void startService(Service service) {

		running = Boolean.TRUE;
		cthread = new Thread((FollowerService) service);
		cthread.start();

	}

	@Override
	public void stopService() {
		running = Boolean.FALSE;
	}

}
