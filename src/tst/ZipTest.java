package tst;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipTest {

	/**
	 * @param args
	 */
	/**
	 * ѹ�������ļ����е������ļ�������ָ�����Ƶ�zipѹ����---�����Ƿ��п��ļ���
	 * 
	 * @param filepath
	 *            �ļ�����Ŀ¼
	 * @param zippath
	 *            ѹ����zip�ļ�����
	 * @param dirFlag
	 *            zip�ļ��е�һ���Ƿ����һ��Ŀ¼��true������falseû�� 2015��6��9��
	 */
	/**
	 * ���ڶ�ָ��Դ�ļ�·���µ������ļ�ѹ����ָ�����ļ���
	 * 
	 * @param des
	 *            String ��Ҫѹ���ɵ��ļ���
	 * @param src
	 *            String ѹ���ļ�Դ·��,�������ļ����ļ���
	 * @return boolean ���ѹ���ɹ�����true�������ʾʧ��
	 */
	public static boolean File2Zip(String des, String src) {

		boolean success = true; // ѹ���ɹ���־
		File srcpath = new File(src);
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(new File(des))); // ����ѹ�������
			out.setEncoding("gbk");
			compress(srcpath, out, ""); // ��ʼѹ��
		} catch (FileNotFoundException ex) {
			success = false;
		} catch (IOException ex1) {
			success = false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex2) {
				}
			}
		}
		return success;
	}

	/**
	 * 
	 * @param src
	 *            File
	 * @param out
	 *            ZipOutputStream
	 * @param base
	 *            String
	 * @throws IOException
	 */
	public static void compress(File src, ZipOutputStream out, String base)
			throws IOException {
		if (src.isDirectory()) { // �����Ŀ¼
			File[] files = src.listFiles(); // �оٳ�Ŀ¼�����е�����
			out.putNextEntry(new ZipEntry(base + "/")); // ����һ��Ŀ¼
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < files.length; i++) {
				compress(files[i], out, base + files[i].getName());
			}

		} else { // ������ļ�
			if ("".equals(base)) {
				out.putNextEntry(new ZipEntry(base + "/"));
				base = src.getName();
			}
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(src);
			byte[] data = new byte[4096];
			int b;
			while ((b = in.read(data)) != -1) {
				out.write(data, 0, b);
			}
			in.close();

		}
	}

	/**
	 * ��ѹ���ļ�
	 * 
	 * @param src
	 *            String zip����·��c:/test/kk.zip
	 * @param des
	 *            String ϣ����ŵ�Ŀ¼
	 * @throws IOException
	 * @throws IOException
	 */
	public static void decompress(String src, String des) throws IOException {
		ZipFile file = null;
		FileOutputStream fout = null;
		DataOutputStream dout = null;
		try {
			file = new ZipFile(src);
			InputStream in = null;
			des = des.replace('/', '/');
			if (des.startsWith("//")) {
				des = des.replaceFirst("//", "//");
			}
			Enumeration en = file.getEntries();
			while (en.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) en.nextElement();
				if (entry.isDirectory()) {// �ļ���
					File directory = new File(des + "/" + entry.getName());
					if (!directory.exists())
						directory.mkdirs();
				} else {// �ļ�
					String path = entry.getName();
					path = path.replace('/', '/');
					if (path.startsWith("//")) {
						path = path.replaceFirst("//", "//");
					}
					int pos = path.lastIndexOf('/');
					if (pos != -1) {
						path = path.substring(0, pos + 1);
						File d = new File(des + "/" + path);
						if (!d.exists())
							d.mkdirs();
					}
					try {
						File files = new File(entry.getName());
						File f = new File(des + "/" + files.getPath());

						fout = new FileOutputStream(f);
						dout = new DataOutputStream(fout);
						in = file.getInputStream(entry);

						byte[] b = new byte[4096];
						int len = 0;
						while ((len = in.read(b)) != -1) {
							dout.write(b, 0, len);
						}

					} catch (IOException e) {
						throw e;
					} finally {
						if (fout != null)
							fout.close();
						if (dout != null)
							dout.close();
						if (in != null)
							in.close();
					}
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (file != null)
				file.close();
			if (fout != null)
				fout.close();
			if (dout != null)
				dout.close();
		}
	}

	/**
	 * ��ѹ��zip�ļ�
	 * 
	 * @param src
	 *            String
	 * @param des
	 *            String
	 * @return boolean �ɹ�����true;
	 */
	public static boolean zipDecompress(String src, String des) {
		boolean success = true;
		try {
			decompress(src, des);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			success = false;
		} catch (IOException ex) {
			ex.printStackTrace();
			success = false;
		}

		return success;
	}

	public static void main(String[] args) {
		// ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(
		// "E:\\zipTest.zip"));
		// compress("E:\\����ʡ%@%001005007", zipout, false);
		File2Zip("E:\\zipTest.zip", "E:\\�ļ���");
	}
}
