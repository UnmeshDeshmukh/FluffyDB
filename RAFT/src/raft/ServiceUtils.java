package raft;

import com.google.protobuf.ByteString;

import raft.proto.AppendEntriesRPC;
import raft.proto.AppendEntriesRPC.AppendEntries;
import raft.proto.AppendEntriesRPC.AppendEntries.RequestType;
import raft.proto.AppendEntriesRPC.AppendEntriesPacket;
import raft.proto.AppendEntriesRPC.AppendEntriesResponse;
import raft.proto.AppendEntriesRPC.AppendEntriesResponse.IsUpdated;
import raft.proto.HeartBeatRPC.HeartBeat;
import raft.proto.HeartBeatRPC.HeartBeatPacket;
import raft.proto.HeartBeatRPC.HeartBeatResponse;
import raft.proto.VoteRPC.RequestVoteRPC;
import raft.proto.VoteRPC.ResponseVoteRPC;
import raft.proto.VoteRPC.ResponseVoteRPC.IsVoteGranted;
import raft.proto.VoteRPC.VoteRPCPacket;
import raft.proto.Work.WorkMessage;
import server.ServerUtils;
import server.db.DatabaseService;

public class ServiceUtils {

	public static WorkMessage prepareRequestVoteRPC() {
		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		RequestVoteRPC.Builder requestVoteRPC = RequestVoteRPC.newBuilder();
		requestVoteRPC.setTerm(NodeState.getInstance().getServerState().getConf().getNodeId());
		requestVoteRPC.setCandidateId("" + NodeState.getInstance().getServerState().getConf().getNodeId());
		requestVoteRPC.setTerm(NodeState.currentTerm);
		requestVoteRPC.setTimeStampOnLatestUpdate(NodeState.getTimeStampOnLatestUpdate());
		// requestVoteRPC.setTimeStampOnLatestUpdate(DatabaseService.getInstance().getDb().getCurrentTimeStamp());

		VoteRPCPacket.Builder voteRPCPacket = VoteRPCPacket.newBuilder();
		voteRPCPacket.setUnixTimestamp(ServerUtils.getCurrentUnixTimeStamp());
		voteRPCPacket.setRequestVoteRPC(requestVoteRPC);
		
		work.setVoteRPCPacket(voteRPCPacket);

		return work.build();
	}

	public static WorkMessage prepareAppendEntriesResponse() {
		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		AppendEntriesPacket.Builder appendEntriesPacket = AppendEntriesPacket.newBuilder();
		appendEntriesPacket.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		AppendEntriesResponse.Builder appendEntriesResponse = AppendEntriesResponse.newBuilder();

		appendEntriesResponse.setIsUpdated(IsUpdated.YES);

		appendEntriesPacket.setAppendEntriesResponse(appendEntriesResponse);

		work.setAppendEntriesPacket(appendEntriesPacket);

		return work.build();

	}

	public static WorkMessage prepareHeartBeatResponse() {
		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		HeartBeatResponse.Builder heartbeatResponse = HeartBeatResponse.newBuilder();
		heartbeatResponse.setNodeId(NodeState.getInstance().getServerState().getConf().getNodeId());
		heartbeatResponse.setTerm(NodeState.currentTerm);
		heartbeatResponse.setTimeStampOnLatestUpdate(NodeState.getTimeStampOnLatestUpdate());
		// heartbeatResponse.setTimeStampOnLatestUpdate(DatabaseService.getInstance().getDb().getCurrentTimeStamp());
		HeartBeatPacket.Builder heartBeatPacket = HeartBeatPacket.newBuilder();
		heartBeatPacket.setUnixTimestamp(ServerUtils.getCurrentUnixTimeStamp());
		heartBeatPacket.setHeartBeatResponse(heartbeatResponse);
		
		work.setHeartBeatPacket(heartBeatPacket);

		return work.build();

	}

	public static WorkMessage prepareHeartBeat() {
		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		HeartBeat.Builder heartbeat = HeartBeat.newBuilder();
		heartbeat.setLeaderId(NodeState.getInstance().getServerState().getConf().getNodeId());
		heartbeat.setTerm(NodeState.currentTerm);
		// Optional

		heartbeat.setTimeStampOnLatestUpdate(NodeState.getTimeStampOnLatestUpdate());

		// heartbeat.setTimeStampOnLatestUpdate(DatabaseService.getInstance().getDb().getCurrentTimeStamp());
		HeartBeatPacket.Builder heartBeatPacket = HeartBeatPacket.newBuilder();
		heartBeatPacket.setUnixTimestamp(ServerUtils.getCurrentUnixTimeStamp());
		heartBeatPacket.setHeartbeat(heartbeat);

		work.setHeartBeatPacket(heartBeatPacket);

		return work.build();
	}

	public static WorkMessage prepareAppendEntriesPacket(String key, byte[] imageData, long timestamp,
			RequestType type) {

		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		AppendEntriesPacket.Builder appendEntriesPacket = AppendEntriesPacket.newBuilder();
		appendEntriesPacket.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		AppendEntriesRPC.ImageMsg.Builder imageMsg = AppendEntriesRPC.ImageMsg.newBuilder();
		imageMsg.setKey(key);

		ByteString byteString = null;
		if (imageData == null) {
			byteString = ByteString.copyFrom(new byte[1]);
		} else {
			byteString = ByteString.copyFrom(imageData);
		}
		imageMsg.setImageData(byteString);

		AppendEntries.Builder appendEntries = AppendEntries.newBuilder();
		appendEntries.setTimeStampOnLatestUpdate(timestamp);
		appendEntries.setImageMsg(imageMsg);
		appendEntries.setLeaderId(NodeState.getInstance().getServerState().getConf().getNodeId());

		appendEntries.setRequestType(type);
		appendEntriesPacket.setAppendEntries(appendEntries);

		work.setAppendEntriesPacket(appendEntriesPacket);

		return work.build();

	}

	public static WorkMessage prepareResponseVoteRPC(IsVoteGranted decision) {
		WorkMessage.Builder work = WorkMessage.newBuilder();
		work.setUnixTimeStamp(ServerUtils.getCurrentUnixTimeStamp());

		VoteRPCPacket.Builder voteRPCPacket = VoteRPCPacket.newBuilder();
		voteRPCPacket.setUnixTimestamp(ServerUtils.getCurrentUnixTimeStamp());

		ResponseVoteRPC.Builder responseVoteRPC = ResponseVoteRPC.newBuilder();
		responseVoteRPC.setTerm(NodeState.getInstance().getServerState().getConf().getNodeId());
		responseVoteRPC.setIsVoteGranted(decision);

		voteRPCPacket.setResponseVoteRPC(responseVoteRPC);

		work.setVoteRPCPacket(voteRPCPacket);

		return work.build();
	}
}
