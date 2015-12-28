package hl.akka.seria;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import akka.serialization.JSerializer;

public class KryoSerializer extends JSerializer {

	@Override
	public int identifier() {
		return 192334;
	}

	@Override
	public boolean includeManifest() {
		return false;
	}

	@Override
	public byte[] toBinary(Object obj) {
		ByteArrayOutputStream os =new ByteArrayOutputStream(1024);
		Output output = new ByteBufferOutput(os);
		KryoLocal.get().writeClassAndObject(output, obj);
		output.flush();
		return os.toByteArray();
	}

	@Override
	public Object fromBinaryJava(byte[] data, Class<?> clz) {
		Input input = new Input(data);
		Object obj = KryoLocal.get().readClassAndObject(input);
		return obj;
	}

}
