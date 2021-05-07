namespace java com.coder.lee.drift.server
namespace py com.coder.lee.drift.server

enum DriftResultCode {
  OK = 0;
  TRY_LATER = 1;
}

struct DriftLogEntry {
  1: string category;
  2: string message;
}

service DriftAsyncScribe {
  DriftResultCode asyncLog(1: list<DriftLogEntry> arg0);
}

service scribe {
  DriftResultCode Log(1: list<DriftLogEntry> arg0);
}
