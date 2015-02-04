package cn.peterchen.pets.xmpp.core;

import org.jivesoftware.smack.XMPPConnection;

public abstract class XmppConnectionChangeListener {
    
    public abstract void newConnection(XMPPConnection connection);

}
