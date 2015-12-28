package hl.akka.transformation;

public class Main {
	public static void main(String[] args) {
		TransformationBackendMain.main(new String[]{"2551"});
		TransformationBackendMain.main(new String[]{"2552"});
		TransformationBackendMain.main(new String[]{});
		
		TransformationFrontendMain.main(new String[]{});
	}
}
