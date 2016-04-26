package node.timer;

import java.util.Timer;

public class NodeTimer extends Timer {
	private Runnable task = null;
	
	private NodeTask node = null;
	
	public void schedule(Runnable runnable, long delay) {
        task = runnable;
        node = new NodeTask(runnable); 
        this.schedule(node, delay);
    }
	
	public void reschedule(long delay) {
        node.cancel();
        node = new NodeTask(task);
        this.schedule(node, delay);
    }	
}
