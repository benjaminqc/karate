package com.intuit.karate.netty;

import java.io.File;
import com.intuit.karate.netty.FeatureServer;

public class FileChangedWatcher {

  private File file;
  private FeatureServer server;
  private Integer port;
  private boolean ssl;
  private File cert;
  private File key;

  public FileChangedWatcher(File mock, FeatureServer server, Integer port, boolean sll, File cert, File key) {
    this.file = mock;
    this.server = server;
    this.port = port;
    this.ssl = sll;
    this.cert = cert;
    this.key = key;
  }

  public void watch() throws InterruptedException {

    long currentModifiedDate = file.lastModified();

    while (true) {

      long newModifiedDate = file.lastModified();

      if (newModifiedDate != currentModifiedDate) {
        currentModifiedDate = newModifiedDate;
        onModified();
      }
      Thread.sleep(500);
    }
  }

  public void onModified() {
    if (server != null) {
      server.stop();
      if (ssl) {
        server = FeatureServer.start(file, port, cert, key, null);
      } else {
        server = FeatureServer.start(file, port, false, null);
      }
    }
  }
}
