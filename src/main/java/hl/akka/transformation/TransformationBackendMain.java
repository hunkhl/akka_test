package hl.akka.transformation;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class TransformationBackendMain {
	public static void start(int port) {
		Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
				.withFallback(ConfigFactory.parseString("akka.cluster.roles=[backend]"))
				.withFallback(ConfigFactory.load());
		ActorSystem system = ActorSystem.create("ClusterSystem", config);
		system.actorOf(Props.create(TransformationBackend.class), "backend");
	}
	
	public static void main(String[] args) {
		final String port = args.length > 0 ? args[0] : "0";
		start(Integer.parseInt(port));
	}
}
