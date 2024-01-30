package com.example.vrc.assets.services.objects;

import java.io.IOException;
import java.util.List;

public interface ObjectsList {
    public List<String> getNames() throws IOException, InterruptedException;
}
