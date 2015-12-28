package hl.akka.typed;

import scala.concurrent.Future;

public interface ITransformationBackend {
	Future<String> getResult(String data2);
	
}
