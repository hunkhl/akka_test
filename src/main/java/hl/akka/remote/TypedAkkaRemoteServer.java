package hl.akka.remote;


import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.actor.TypedActor;
import akka.actor.TypedProps;
import akka.japi.Creator;
import akka.serialization.Serialization;
import akka.serialization.SerializationExtension;

/**
 * 测试akka 网络通讯服务器端
 * @author Administrator
 *
 */
public class TypedAkkaRemoteServer {
	
	
	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("CalcSystem", 
				ConfigFactory.load("calaculator-server"));
		
//		Serialization serialization = SerializationExtension.get(system);
		
//		System.out.println(serialization.findSerializerFor("xxx"));
		
		ICalculator c = TypedActor.get(system).typedActorOf(new TypedProps<ICalculator>(ICalculator.class, new Creator<ICalculator>() {
			private static final long serialVersionUID = 1L;
			@Override
			public ICalculator create() throws Exception {
				return new Calculator();
			}
		}), "calac");
		
//		System.out.println("ref=" + TypedActor.get(system).getActorRefFor(c));
		System.out.println("start calac server" +c );
//		system.actorOf(Props.create())
	}
}
