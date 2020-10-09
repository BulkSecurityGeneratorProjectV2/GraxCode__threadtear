package me.nov.threadtear.io;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import me.nov.threadtear.CoreUtils;

public final class Manifest {
  private Manifest() {
  }

  private static String lineSeparator = "\r\n";

  public static byte[] patchManifest(byte[] manifestBytes) throws UnsupportedEncodingException {
    String manifest = new String(manifestBytes, StandardCharsets.UTF_8);
    StringBuilder patchedManifest = new StringBuilder();
    for (String line : manifest.split(lineSeparator)) {
      if (line.length() > 1 && !line.startsWith("JAR-Signature:") && !line.startsWith("Name:") &&
              !line.matches(".+-Digest: .+")) {
        patchedManifest.append(line);
        patchedManifest.append(lineSeparator);
      }
    }
    patchedManifest.append(lineSeparator); // need a new line
    // because without it won't recognize it
    return patchedManifest.toString().getBytes(StandardCharsets.UTF_8);
  }

  public static byte[] watermark(byte[] manifestBytes) throws UnsupportedEncodingException {
    String manifest = new String(manifestBytes, StandardCharsets.UTF_8);
    if (!manifest.contains("Deobfuscated-By: ")) {
      manifest = manifest.substring(0, manifest.length() - lineSeparator.length()); // remove new line
      manifest += "Deobfuscated-By: Threadtear " + CoreUtils.getVersion() + lineSeparator + lineSeparator;
    }
    return manifest.getBytes(StandardCharsets.UTF_8);
  }
}
