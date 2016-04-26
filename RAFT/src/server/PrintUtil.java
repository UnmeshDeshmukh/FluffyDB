/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package server;

import raft.proto.Work.WorkMessage;

public class PrintUtil {
	private static final String gap = "   ";

	public static void printWork(WorkMessage msg) {

		System.out.print("\nWork: ");
		if (msg.hasHeartBeatPacket()) {
			System.out.println("\n--------------HeartBeatPacket---------------\n");
			System.out.println(msg.getHeartBeatPacket().getUnixTimestamp());
			if (msg.getHeartBeatPacket().hasHeartbeat()) {
				System.out.println(msg.getHeartBeatPacket().getHeartbeat().getLeaderId());

			}
		}
	}

}
