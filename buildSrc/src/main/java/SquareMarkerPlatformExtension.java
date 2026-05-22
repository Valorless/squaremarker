import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

public abstract class SquareMarkerPlatformExtension {
    public abstract RegularFileProperty getProductionJar();

    public abstract Property<String> getModInfoFilePath();
}
