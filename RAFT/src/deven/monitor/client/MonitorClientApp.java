/*
 * copyright 2016, gash
 * 
 * Gash licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package deven.monitor.client;

import raft.NodeState;
import raft.proto.Monitor.ClusterMonitor;
import raft.proto.Work.WorkMessage;

public class MonitorClientApp implements MonitorListener {
	private MonitorClient mc;
	private static int ticker = 0;

	public MonitorClientApp(MonitorClient mc) {
		init(mc);
	}

	private void init(MonitorClient mc) {
		this.mc = mc;
		this.mc.addListener(this);
	}

	public ClusterMonitor sendDummyMessage(int noOfNodes, int addProcessedId) {
		/*
		 * This message should be created and sent by only one node inside the
		 * cluster.
		 * 
		 */
		try {

			// Build the message to be sent to monitor server
			ClusterMonitor.Builder cm = ClusterMonitor.newBuilder();
			// your cluster ID
			cm.setClusterId(989);
			// No of nodes in your cluster
			cm.setNumNodes(noOfNodes);
			// Node Id = Process Id
			cm.addProcessId(0);
			cm.addProcessId(1);
			// Set processId,No of EnquedTask for that processId
			cm.addEnqueued(5);
			// cm.addEnqueued(5);
			// Set processId,No of ProcessedTask for that processId
			cm.addProcessed(addProcessedId);
			// cm.addProcessed(3);
			// Set processId,No of StolenTask for that processId
			cm.addStolen(2);
			cm.addStolen(2);
			// Increment tick every time you send the message, or else it would
			// be ignored
			// Tick starts from 0
			cm.setTick(ticker++);

			return cm.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getListenerID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMessage(ClusterMonitor msg) {
		// TODO Auto-generated method stub
	}
	/*
	 * public static void main(String[] args) {
	 * 
	 * String host = "127.0.0.1"; int port = 5000;
	 * 
	 * try { MonitorClient mc = new MonitorClient(host, port); MonitorClientApp
	 * ma = new MonitorClientApp(mc);
	 * 
	 * 
	 * 
	 * // do stuff w/ the connection System.out.println("Creating message");
	 * ClusterMonitor msg = ma.sendDummyMessage(); System.out.println(
	 * "Sending generated message"); mc.write(msg);
	 * 
	 * System.out.println("\n** exiting in 10 seconds. **"); System.out.flush();
	 * Thread.sleep(10 * 1000); } catch (Exception e) { e.printStackTrace(); }
	 * finally {
	 * 
	 * } }
	 * 
	 * 
	 */
}
