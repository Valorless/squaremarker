import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

public abstract class CopyFile extends DefaultTask {

    @InputFile
    public abstract RegularFileProperty getFileToCopy();

    @OutputFile
    public abstract RegularFileProperty getDestination();

    @TaskAction
    public void copyFile() {
        getDestination().get().getAsFile().getParentFile().mkdirs();
        try {
            java.nio.file.Files.copy(
                getFileToCopy().get().getAsFile().toPath(),
                getDestination().get().getAsFile().toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to copy file", e);
        }
    }
}
