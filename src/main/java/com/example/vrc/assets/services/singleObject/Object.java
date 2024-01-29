package com.example.vrc.assets.services.singleObject;

import java.io.IOException;

public interface Object {
    void fetch() throws IOException, InterruptedException;
    void parse(String responseBody);
    String getName();
    String getUrl();
    String getThumbnailUrl();
    String getId();
}
