import com.cdd.utils.JplagRunner;

import java.util.Objects;

public class JplagRunnerTest {
    public static void main(String[] args) {
        String jplagPath = Objects.requireNonNull(JplagRunnerTest.class.getClassLoader().getResource("jplag.jar")).getPath();
        String submissionsDirectory = Objects.requireNonNull(JplagRunnerTest.class.getClassLoader().getResource("download")).getPath();
        String outputDirectory = Objects.requireNonNull(JplagRunnerTest.class.getClassLoader().getResource("submissions")).getPath()+"/result";
        JplagRunner jplagRunner = new JplagRunner();
        jplagRunner.runJPlag(submissionsDirectory, outputDirectory, "", "");
    }
}
