package com.example.digitalline;

import java.util.HashMap;
import java.util.LinkedList;

public interface LabsDownloadListener {
    void onLabsDownloaded(HashMap<String, LinkedList<LabDetails>> labs);
}
