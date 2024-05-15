package com.example.digitalline;

public class StatusUtils {
    public enum Status {
        WaitingForFixingToStart, DuringFix, WasFixed
    }

    public static String getTextFromStatus(StatusUtils.Status status) {
        switch (status) {
            case WaitingForFixingToStart:
                return "waiting for fixing to start";
            case DuringFix:
                return "during fix";
            case WasFixed:
                return "was fixed";
        }
        return null;
    }

    public static StatusUtils.Status getStatusFromText(String text) {
        switch (text) {
            case "waiting for fixing to start":
                return StatusUtils.Status.WaitingForFixingToStart;
            case "during fix":
                return StatusUtils.Status.DuringFix;
            case "was fixed":
                return StatusUtils.Status.WasFixed;
        }
        return null;
    }

    public static StatusUtils.Status getStatusFromCloudText(String text) {
        switch (text) {
            case "WaitingForFixingToStart":
                return StatusUtils.Status.WaitingForFixingToStart;
            case "DuringFix":
                return StatusUtils.Status.DuringFix;
            case "WasFixed":
                return StatusUtils.Status.WasFixed;
        }
        return null;
    }
}
