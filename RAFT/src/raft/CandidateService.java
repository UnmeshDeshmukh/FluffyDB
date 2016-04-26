	package raft;
	
	import io.netty.channel.ChannelFuture;
	import logger.Logger;
	import node.timer.NodeTimer;
	import raft.proto.Work;
	import raft.proto.AppendEntriesRPC.AppendEntriesPacket;
	import raft.proto.AppendEntriesRPC.AppendEntriesResponse;
	import raft.proto.AppendEntriesRPC.AppendEntriesResponse.IsUpdated;
	import raft.proto.HeartBeatRPC.HeartBeatPacket;
	import raft.proto.HeartBeatRPC.HeartBeatResponse;
	import raft.proto.VoteRPC.ResponseVoteRPC;
	import raft.proto.VoteRPC.VoteRPCPacket;
	import raft.proto.Work.WorkMessage;
	import server.ServerUtils;
	import server.db.DatabaseService;
	import server.edges.EdgeInfo;
	
	public class CandidateService extends Service implements Runnable {
	
		private static CandidateService INSTANCE = null;
		private int numberOfYESResponses;
		private int TotalResponses;
		NodeTimer timer = new NodeTimer();
	
		private CandidateService() {
			// TODO Auto-generated constructor stub
		}
	
		public static CandidateService getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CandidateService();
			}
			return INSTANCE;
		}
	
		@Override
		public void run() {
			Logger.DEBUG("-----------------------CANDIDATE SERVICE STARTED ----------------------------");
			startElection();
			while (running) {
	
			}
		}
	
		private void startElection() {
			numberOfYESResponses = 0;
			TotalResponses = 0;
			NodeState.currentTerm++;
			
			for (EdgeInfo ei : NodeState.getInstance().getServerState().getEmon().getOutboundEdges().getMap().values()) {
	
				if (ei.isActive() && ei.getChannel() != null) {
					WorkMessage workMessage = ServiceUtils.prepareRequestVoteRPC();
					Logger.DEBUG("Sent VoteRequestRPC to " + ei.getRef());
					ChannelFuture cf = ei.getChannel().writeAndFlush(workMessage);
					if (cf.isDone() && !cf.isSuccess()) {
						Logger.DEBUG("failed to send message (VoteRequestRPC) to server");
					}
				}
			}
			timer = new NodeTimer();
			timer.schedule(new Runnable() {
				@Override
				public void run() {
	
					if (isWinner()) {
						Logger.DEBUG(NodeState.getInstance().getServerState().getConf().getNodeId() + " has won the election.");
						NodeState.getInstance().setState(NodeState.LEADER);
					} else {
						Logger.DEBUG(NodeState.getInstance().getServerState().getConf().getNodeId() + " has lost the election.");
						NodeState.getInstance().setState(NodeState.FOLLOWER);
					}
				}
	
				private Boolean isWinner() {
	
					Logger.DEBUG("Total number of responses = "+TotalResponses);
					Logger.DEBUG("Total number of YES responses = "+ numberOfYESResponses);
					
					if ((numberOfYESResponses + 1) > (TotalResponses + 1) / 2) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
	
				}
			}, ServerUtils.getFixedTimeout());
	
		}
	
		@Override
		public void handleResponseVoteRPCs(WorkMessage workMessage) {
			TotalResponses++;
			
			if (workMessage.getVoteRPCPacket().getResponseVoteRPC()
					.getIsVoteGranted() == ResponseVoteRPC.IsVoteGranted.YES) {
				
				Logger.DEBUG("Vote 'YES' is granted from Node Id " + workMessage.getVoteRPCPacket().getResponseVoteRPC().getTerm());
				numberOfYESResponses++;
				
			}else{
				Logger.DEBUG("Vote 'NO' is granted from Node Id " + workMessage.getVoteRPCPacket().getResponseVoteRPC().getTerm());
			}
			
	
		}
	
		@Override
		public WorkMessage handleRequestVoteRPC(WorkMessage workMessage) {
			if (workMessage.getVoteRPCPacket().getRequestVoteRPC().getTimeStampOnLatestUpdate() < NodeState.getTimeStampOnLatestUpdate()) {
				return ServiceUtils.prepareResponseVoteRPC(ResponseVoteRPC.IsVoteGranted.NO);
	
			}
			return ServiceUtils.prepareResponseVoteRPC(ResponseVoteRPC.IsVoteGranted.YES);
		}
	
		@Override
		public void handleHeartBeat(WorkMessage wm) {
			Logger.DEBUG("HeartbeatPacket received from leader :" + wm.getHeartBeatPacket().getHeartbeat().getLeaderId());
	
			NodeState.getInstance().setState(NodeState.FOLLOWER);
	
		}
	
		public void startService(Service service) {
			running = Boolean.TRUE;
			cthread = new Thread((CandidateService) service);
			cthread.start();
		}
	
		public void stopService() {
			timer.cancel();
			running = Boolean.FALSE;
	
		}
	
	}
