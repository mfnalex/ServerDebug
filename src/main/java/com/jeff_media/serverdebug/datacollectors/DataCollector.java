package com.jeff_media.serverdebug.datacollectors;

import java.io.File;
import java.io.IOException;

public interface DataCollector {

    File getFile();

    void save() throws IOException;
}
