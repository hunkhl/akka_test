package hl.akka.transformation;

import java.io.Serializable;

public class TransformationMessages {
	
	
	public static class TransformationJob implements Serializable {
		private static final long serialVersionUID = -3084126188279816733L;
		
		private final String text;
		
		public TransformationJob(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	
	public static class TransformationResult implements Serializable {
		private static final long serialVersionUID = 7399502281343584642L;
		private final String text;
		public TransformationResult(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
	}
	
	public static class JobFailed implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final String reason;
		private final TransformationJob job;
		public JobFailed(String reason, TransformationJob job) {
			this.reason = reason;
			this.job = job;
		}
		
		public String getReason() {
			return reason;
		}
		
		public TransformationJob getJob() {
			return job;
		}
	}
	
	
	public static final String BACK_REGISTRATION	= "BackendRegistration";
}
