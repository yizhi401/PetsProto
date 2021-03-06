package cn.peterchen.imtest.xmpp.core;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;

import cn.peterchen.imtest.xmpp.tool.Log;
import cn.peterchen.imtest.xmpp.tool.Tools;


/**
 * listens to chat packet(messages)
 * 
 * @author peter
 * 
 */
public class ChatPacketListener implements PacketListener {
	private final SettingsManager mSettings;
	private final Context mCtx;

	public ChatPacketListener(Context ctx) {
		this.mCtx = ctx;
		this.mSettings = SettingsManager.getSettingsManager(ctx);
	}

	public void processPacket(Packet packet) {
		Message message = (Message) packet;
		String from = message.getFrom();

		if (message.getBody() != null) {
			Log.d("XMPP packet received - sending Intent: "
                    + MainService.ACTION_XMPP_MESSAGE_RECEIVED);
			// Acquire a WakeLock just before we are about to send the intent
			MainService.maybeAcquireWakeLock();
			Tools.startSvcXMPPMsg(mCtx, message.getBody(), from);
		} else {
			if (!mSettings.cameFromNotifiedAddress(from)) {
				Log.i("XMPP packet received - but from address \""
						+ from.toLowerCase()
						+ "\" does not match notification address \""
						+ mSettings.getNotifiedAddresses().get() + "\"");
			} else if (message.getBody() == null) {
				Log.i("XMPP Packet received - but without body (body == null)");
			}
		}
	}
}
