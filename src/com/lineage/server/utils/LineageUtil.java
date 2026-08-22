package com.lineage.server.utils;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;
import com.lineage.config.Config;
import com.lineage.server.datatables.CraftListTable;
import com.lineage.server.templates.L1Craft;

import l1j.server.server.datas.protobuf.PBMessageALL3;

public class LineageUtil {

	private static Logger _log = Logger.getLogger(LineageUtil.class.getName());

	public static void write(final String fileName, final String s) {
		try {
			final File file = new File(fileName);
			final BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

			bw.write(s);
			bw.newLine();

			bw.close();
		} catch (final IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public static void write(final String fileName, final byte[] b, final boolean isAppend) {
		try {
			final FileOutputStream fos = new FileOutputStream(fileName, isAppend); // isAppend...是否接下去寫入
			fos.write(b);
			fos.close();
		} catch (final IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	// 字串轉bytes array

	public static ByteString getByteString(final String s) {
		ByteString result = ByteString.EMPTY;
		try {
			result = ByteString.copyFrom(s.getBytes(Config.CLIENT_LANGUAGE_CODE));
		} catch (final UnsupportedEncodingException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		return result;
	}

	public static byte[] getByte(final String s) {
		byte[] result = new byte[0];
		try {
			result = s.getBytes(Config.CLIENT_LANGUAGE_CODE);
		} catch (final UnsupportedEncodingException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		return result;
	}

	// 得到檔案的副檔名
	public static String getFileExtension(final File file) {
		final String fileName = file.getName();
		final int index = fileName.lastIndexOf('.');
		if (index != -1) {
			return fileName.substring(index + 1, fileName.length());
		}
		return "";
	}

	// 得到檔案的名字(不包含副檔名)
	public static String getFileNameWithoutExtension(final File file) {
		final String fileName = file.getName();
		final int index = fileName.lastIndexOf('.');
		if (index != -1) {
			return fileName.substring(0, index);
		}
		return "";
	}

	// 得到目前使用的記憶體(MB)
	public static long getUsedMemoryMB() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L;
	}

	// 關閉串流 (?
	public static void closeStream(final Closeable... closeables) {
		for (final Closeable c : closeables) {
			try {
				if (c != null) {
					c.close();
				}
			} catch (final IOException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	public static String printData(final byte abyte0[]) {
		final StringBuffer stringbuffer = new StringBuffer();
		int j = 0;
		for (int k = 0; k < abyte0.length; k++) {
			if (j % 16 == 0) {
				stringbuffer.append((new StringBuilder()).append(fillHex(k, 4)).append(": ").toString());
			}
			stringbuffer.append((new StringBuilder()).append(fillHex(abyte0[k] & 0xff, 2)).append(" ").toString());
			if (++j != 16) {
				continue;
			}
			stringbuffer.append("   ");
			int i1 = k - 15;
			for (int l1 = 0; l1 < 16; l1++) {
				final byte byte0 = abyte0[i1++];
				if (byte0 > 31 && byte0 < 128) {
					stringbuffer.append((char) byte0);
				} else {
					stringbuffer.append('.');
				}
			}
			stringbuffer.append("\r\n");
			j = 0;
		}
		final int l = abyte0.length % 16;
		if (l > 0) {
			for (int j1 = 0; j1 < 17 - l; j1++) {
				stringbuffer.append("   ");
			}

			int k1 = abyte0.length - l;
			for (int i2 = 0; i2 < l; i2++) {
				final byte byte1 = abyte0[k1++];
				if (byte1 > 31 && byte1 < 128) {
					stringbuffer.append((char) byte1);
				} else {
					stringbuffer.append('.');
				}
			}
			stringbuffer.append("\r\n");
		}
		return stringbuffer.toString();
	}

	public static String fillHex(final int i, final int j) {
		String s = Integer.toHexString(i);
		for (int k = s.length(); k < j; k++) {
			s = (new StringBuilder()).append("0").append(s).toString();
		}
		return s;
	}

	public static void makeCraftSha1Code() {
		try {

			final String path = "./data/craftinfo.dat";

			// 生成一份craftinfo.dat
			write(path, new byte[] { 0x08, 0x02 }, false);

			for (final L1Craft craft : CraftListTable.getInstance().getList()) {
				final PBMessageALL3.type10.Builder builder = PBMessageALL3.type10.newBuilder();
				builder.addArray2(craft.getByteString());
				final byte[] array = builder.build().toByteArray();

				write(path, array, true);
			}

			// sha-1 取得驗證碼
			final byte[] data = Files.readAllBytes(Paths.get(path));
			final MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(data);

			System.out.println("[自動生成道具清單驗證碼]");
			String s = "0a 14 ";
			for (final byte b : sha.digest()) {
				s += fillHex((b & 0xff), 2) + " ";
			}

			Config.CraftinfoCode = s.trim();
			System.out.println(Config.CraftinfoCode);

		} catch (final Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
