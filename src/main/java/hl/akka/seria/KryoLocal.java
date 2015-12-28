package hl.akka.seria;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import akka.actor.TypedActor.MethodCall;


public class KryoLocal {
	private static ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>(){
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			
			kryo.register(Object[].class);
			kryo.register(ArrayList.class);
			kryo.register(HashMap.class);
			kryo.register(Object.class);
			kryo.register(byte[].class);
			kryo.setRegistrationRequired(false);
			kryo.setCopyReferences(true);
			kryo.register(MethodCall.class);
			kryo.register(MethodCall.class, new Serializer<MethodCall>() {
				@Override
				public void write(Kryo kryo, Output output, MethodCall object) {
					
					Method method = object.method();
					kryo.writeClass(output, method.getDeclaringClass());
					kryo.writeClassAndObject(output, method.getName());
					kryo.writeClassAndObject(output, object.parameters());
					
				}

				@Override
				public MethodCall read(Kryo kryo, Input input, Class<MethodCall> type) {
//					Class clz = kryo.readClass(input);
					Class clz = kryo.readClass(input).getType();
					String name = kryo.readClassAndObject(input).toString();
					Object[] params = (Object[]) kryo.readClassAndObject(input);
					
					for(Method method :clz.getDeclaredMethods()) {
						if(method.getName().equals(name) && method.getParameterCount() == params.length) {
							Class<?>[] clzs = method.getParameterTypes();
							
							boolean fit = true;
							for(int i = 0; i< clzs.length; i++) {
//								if(params[i] == null) {
//									continue;
//								}
//								if(clz.isInstance(params) == false) {
//									fit = false;
//									break;
//								}
							}
							
							if(fit ) {
								return new MethodCall(method, params);
							}
							
							
						}
					}
					return null;
					
				}
			});
//			MethodCall call = new MethodCall(method, parameters)
			return kryo;
		};
	};
	public static  Kryo get(){
		return kryos.get();
	}
	
	
	public static void main(String[] args) {
		ByteArrayOutputStream os =new ByteArrayOutputStream(1024);
		Output output = new ByteBufferOutput(os);
		
		get().writeClassAndObject(output, "xxxxxxxxxx");
		byte[] data =os.toByteArray();
		
		
		System.out.println("size=" + data.length);
		
		
	}
}
