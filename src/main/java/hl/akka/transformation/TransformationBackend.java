package hl.akka.transformation;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import hl.akka.transformation.TransformationMessages.TransformationJob;

public class TransformationBackend extends UntypedActor{
	Cluster cluster = Cluster.get(getContext().system());
	
	@Override
	public void preStart() throws Exception {
		cluster.subscribe(getSelf(), MemberUp.class);
	}
	
	@Override
	public void postStop() throws Exception {
		cluster.unsubscribe(getSelf());
	}
	
	
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg instanceof TransformationJob) {
			TransformationJob job = (TransformationJob)msg;
			getSender().tell(new TransformationMessages.TransformationResult(job.getText().toUpperCase() ), getSelf());
			
		} else if(msg instanceof CurrentClusterState) {
			CurrentClusterState state = (CurrentClusterState) msg;
			for(Member member: state.getMembers()) {
				if(member.status().equals(MemberStatus.up())) {
					register(member);
				}
			}
		} else if(msg instanceof MemberUp) {
			MemberUp up = (MemberUp) msg;
			register(up.member());
		} else{
			unhandled(msg);
		}
	}
	
	
	public void register(Member member) {
		if(member.hasRole("frontend")) {
			System.out.println("address=" + member.address());
			getContext().actorSelection(member.address() +"/user/frontend").tell(TransformationMessages.BACK_REGISTRATION,  getSelf());
		}
	}
	
	

}
