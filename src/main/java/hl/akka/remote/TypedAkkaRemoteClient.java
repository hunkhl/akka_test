package hl.akka.remote;

import java.util.concurrent.atomic.AtomicInteger;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.dispatch.OnComplete;
import scala.concurrent.Future;

/**
 * 测试akka 网络通讯客户端
 * @author Administrator
 *
 */
public class TypedAkkaRemoteClient {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void start() {
		ActorSystem system = ActorSystem.create("LookupSystem", ConfigFactory.load("calaculator-client"));
		
		final String path = "akka.tcp://CalcSystem@127.0.0.1:2252/user/calac";
		
		
		
//		TypedActor.get(system).typedActor().
//		ActorSelection remoteActor = system.actorSelection(path);
//		System.out.println(remoteActor.);
//		TypedActor.get(system).actorFactory(
		ActorRef  ref = system.actorFor(path);
		System.out.println(ref);
		ICalculator remote = TypedActor.get(system).typedActorOf(new TypedProps(ICalculator.class), ref);
		
		remote.sum(1, 2);
//		System.out.println(1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		final long start = System.currentTimeMillis();
		final AtomicInteger cnt = new AtomicInteger(0);
		for(int i = 0; i< 30000; i++) {
			Future<Integer> future = remote.sum(1, 2);
			future.onComplete(new OnComplete(){
				@Override
				public void onComplete(Throwable arg0, Object arg1) throws Throwable {
					if(arg0 != null) {
						arg0.printStackTrace();
					}
					if(cnt.incrementAndGet() == 30000) {
						System.out.println("return spendTIme=" + (System.currentTimeMillis() - start) +"ms");
					} 
				}}, system.dispatcher());
			
		}
		long end = System.currentTimeMillis();
		
		System.out.println("send spendTime=" + (end - start) +"ms");
	}
	
	public static void main(String[] args) {
		
		start();
	}
}

