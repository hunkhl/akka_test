package hl.akka.typed;

import akka.actor.ActorRef;
import akka.actor.TypedActor;
import akka.actor.TypedActor.PostStop;
import akka.actor.TypedActor.PreStart;
import akka.actor.TypedActor.Receiver;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.dispatch.Futures;
import scala.concurrent.Future;

public class TransformationBackend implements ITransformationBackend, PreStart, PostStop, Receiver{
	Cluster cluster = Cluster.get(TypedActor.context().system());
	
	@Override
	public void onReceive(Object message, ActorRef ref) {
		if(message instanceof CurrentClusterState) {
			
		}
	}

	@Override
	public void postStop() {
		cluster.unsubscribe(TypedActor.get(TypedActor.context().system()).getActorRefFor(this));
	}

	@Override
	public void preStart() {
		cluster.subscribe(TypedActor.get(TypedActor.context().system()).getActorRefFor(this), MemberUp.class);
	}

	@Override
	public Future<String> getResult(String data2) {
		return Futures.successful(data2.toUpperCase());
	}

}
