package hl.akka.transformation;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import hl.akka.transformation.TransformationMessages.TransformationJob;
import hl.akka.transformation.TransformationMessages.JobFailed;

public class TransformationFrotend extends UntypedActor{
	List<ActorRef> backends = new ArrayList<ActorRef>();
	int jobCounter = 0;
	
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof TransformationJob) {
			TransformationJob job = (TransformationJob) message;
			if(backends.isEmpty()) {
				getSelf().tell(new JobFailed("Service unavaiable, try again later", job), getSender());
			} else {
				jobCounter ++;
				backends.get(jobCounter % backends.size()).forward(job, getContext());
			} 
		} else if(message.equals(TransformationMessages.BACK_REGISTRATION)) {
			getContext().watch(getSender());
			backends.add(getSender());
		} else if(message instanceof Terminated) {
			Terminated terminated = (Terminated) message;
			backends.remove(terminated.getActor());
		} else {
			unhandled(message);
		}
	}

}
