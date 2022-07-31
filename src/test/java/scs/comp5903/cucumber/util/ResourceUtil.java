package scs.comp5903.cucumber.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-22
 */
public class ResourceUtil {

  public static Path getResourcePath(String resourcePathName) throws URISyntaxException {
    return Paths.get(Objects.requireNonNull(ResourceUtil.class.getClassLoader().getResource(resourcePathName)).toURI());
  }
}
