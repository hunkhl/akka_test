package hl.akka.transformation;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class TransformationFrontendMain {
	
	public static void start(int port) {
		final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
				.withFallback(ConfigFactory.parseString("akka.cluster.roles=[frontend]"))
				.withFallback(ConfigFactory.load());
		
		
		ActorSystem system = ActorSystem.create("ClusterSystem", config);
		
		final ActorRef frontend = system.actorOf(Props.create(TransformationFrotend.class), "frontend");
		final FiniteDuration interval = Duration.create(20, TimeUnit.SECONDS);
		
		
		final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
		
		final ExecutionContext ec = system.dispatcher();
		
		final AtomicInteger counter = new AtomicInteger(0);
		system.scheduler().schedule(interval, interval, new Runnable() {
			public void run() {

				Future<Object> future = Patterns.ask(frontend, new TransformationMessages.TransformationJob("hello_" + counter.incrementAndGet()), timeout);
				future.onSuccess(new OnSuccess<Object>(){
					@Override
					public void onSuccess(Object result) throws Throwable {
						System.out.println(" ack result=" + result);
					}}, ec);
				
				future.onComplete(new OnComplete<Object>(){
					@Override
					public void onComplete(Throwable t, Object obj) throws Throwable {
						System.out.println(" ack resul========t=" + obj +", t=" + t +" self=" + Thread.currentThread().getName());
					}}, ec);
				
			}
		}, ec);
		
	}
	public static void main(String[] args) {
		final String port = args.length > 0 ? args[0] : "0";
		start(Integer.parseInt(port));
	}
}
