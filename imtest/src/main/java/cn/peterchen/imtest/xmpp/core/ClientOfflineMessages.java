package cn.peterchen.imtest.xmpp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;

import cn.peterchen.imtest.xmpp.files.ClientOfflineMessagesDateFile;
import cn.peterchen.imtest.xmpp.files.DateFile;


/**
 * Store messages sent when offline Only keep 7 days fo messages
 *
 * @author peter
 *
 */
public class ClientOfflineMessages {
	private static final String DIRECTORY = "clientOfflineMessagesData";
	private static File sDirFile;
	private static XMPPConnection sXMPPConnection;

	private static ClientOfflineMessages sClientOfflineMessages;

	private ClientOfflineMessages(Context ctx) {
		sDirFile = new File(ctx.getFilesDir(), DIRECTORY);
		if (!sDirFile.exists()) {
			sDirFile.mkdir();
		}
		cleanUp();
	}

	public static ClientOfflineMessages getInstance(Context ctx) {
		if (sClientOfflineMessages == null) {
			sClientOfflineMessages = new ClientOfflineMessages(ctx);
		}
		return sClientOfflineMessages;
	}

	public void registerListener(XmppManager xmppMgr) {
		XmppConnectionChangeListener listener = new XmppConnectionChangeListener() {
			public void newConnection(XMPPConnection connection) {
				sXMPPConnection = connection;
				sendOfflineMessages();
			}
		};
		xmppMgr.registerConnectionChangeListener(listener);
	}

	private static void sendOfflineMessages() {
		List<ClientOfflineMessagesDateFile> files = getDateFiles();
		for (ClientOfflineMessagesDateFile f : files) {
			try {
				Message msg = f.getMessage();
				sXMPPConnection.sendPacket(msg);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} catch (Exception e) {
				// don't delete the file here
				continue;
			}
			f.delete();
		}
	}

	public boolean addOfflineMessage(Message msg) {
		ClientOfflineMessagesDateFile file;
		try {
			file = ClientOfflineMessagesDateFile.construct(sDirFile);
			file.setMessage(msg);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private static void cleanUp() {
		List<ClientOfflineMessagesDateFile> datefiles = getDateFiles();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Date date = cal.getTime();

		DateFile.deleteDatefilesOlderThan(datefiles, date);
	}

	private static List<ClientOfflineMessagesDateFile> getDateFiles() {
		File[] files = sDirFile.listFiles();
		List<ClientOfflineMessagesDateFile> dateFiles = new ArrayList<ClientOfflineMessagesDateFile>();
		for (File f : files) {
			try {
				ClientOfflineMessagesDateFile df = ClientOfflineMessagesDateFile
						.reconstruct(f);
				dateFiles.add(df);
			} catch (NumberFormatException e) {
			}
		}
		return dateFiles;
	}
}
