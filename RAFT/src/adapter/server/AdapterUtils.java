package adapter.server;

import com.google.protobuf.ByteString;

import adapter.server.proto.Common;
import adapter.server.proto.Common.Header;
import adapter.server.proto.Global;
import adapter.server.proto.Global.GlobalCommandMessage;
import adapter.server.proto.Storage;
import adapter.server.proto.Storage.Action;
import server.ServerUtils;

public class AdapterUtils {

	public static Global.GlobalCommandMessage prepareClusterRouteRequestForGET(int destination, String key) {

		Global.GlobalCommandMessage.Builder globalCommandMessage = Global.GlobalCommandMessage.newBuilder();

		Common.Header.Builder header = Common.Header.newBuilder();
		header.setNodeId(1);
		header.setTime(ServerUtils.getCurrentUnixTimeStamp());

		header.setDestination(destination);

		globalCommandMessage.setHeader(header);

		Storage.Query.Builder query = Storage.Query.newBuilder();
		query.setAction(Storage.Action.GET);
		query.setKey(key);

		globalCommandMessage.setQuery(query);

		return globalCommandMessage.build();

	}
	
	public static Global.GlobalCommandMessage prepareClusterRouteRequestForPOST(int destination, byte[] imageArray) {

		Global.GlobalCommandMessage.Builder globalCommandMessage = Global.GlobalCommandMessage.newBuilder();

		Common.Header.Builder header = Common.Header.newBuilder();
	

		header.setDestination(destination);
		header.setNodeId(1);
		header.setTime(ServerUtils.getCurrentUnixTimeStamp());

		globalCommandMessage.setHeader(header);
		
		Storage.Query.Builder query = Storage.Query.newBuilder();
		query.setAction(Storage.Action.STORE);
		query.setKey("123");
		query.setData(ByteString.copyFrom(imageArray));

		globalCommandMessage.setQuery(query);

		return globalCommandMessage.build();

	}
	
	
	public static GlobalCommandMessage ReponseBuilderForGET(byte[] imageArray){
		Global.GlobalCommandMessage.Builder globalmsg = Global.GlobalCommandMessage.newBuilder();
		Header.Builder header = Header.newBuilder();
		header.setNodeId(AdapterServer.conf.getNodeId());
		header.setTime(ServerUtils.getCurrentUnixTimeStamp());
		
		Storage.Response.Builder response = Storage.Response.newBuilder();
		response.setAction(Storage.Action.GET);
		response.setData(ByteString.copyFrom(imageArray));
		
		globalmsg.setHeader(header);
		globalmsg.setResponse(response);
		
		
		//globalmsg.setHeader(
		return globalmsg.build();
		
	}

		
	public static GlobalCommandMessage ReponseBuilderForPOST(String key){
		Global.GlobalCommandMessage.Builder globalmsg = Global.GlobalCommandMessage.newBuilder();
		Header.Builder header = Header.newBuilder();
		header.setNodeId(AdapterServer.conf.getNodeId());
		header.setTime(ServerUtils.getCurrentUnixTimeStamp());
		
		Storage.Response.Builder response = Storage.Response.newBuilder();
		response.setAction(Storage.Action.STORE);
		response.setKey(key);
		
		
		globalmsg.setHeader(header);
		globalmsg.setResponse(response);
		
		
		//globalmsg.setHeader(
		return globalmsg.build();
	}

	

}
