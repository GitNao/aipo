package com.aimluck.eip.cayenne.access;

import org.apache.cayenne.DataChannel;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.access.DataContextFactory;
import org.apache.cayenne.access.ObjectStore;

public class IsolatedDataContextFactory implements DataContextFactory {

  public DataContext createDataContext(DataChannel parent,
      ObjectStore objectStore) {

    // stop listening for peer events
    parent.getEventManager().removeListener(objectStore);
    return new DataContext(parent, objectStore);
  }

}
