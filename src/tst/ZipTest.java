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
	 * 压缩整个文件夹中的所有文件，生成指定名称的zip压缩包---不论是否有空文件夹
	 * 
	 * @param filepath
	 *            文件所在目录
	 * @param zippath
	 *            压缩后zip文件名称
	 * @param dirFlag
	 *            zip文件中第一层是否包含一级目录，true包含；false没有 2015年6月9日
	 */
	/**
	 * 用于对指定源文件路径下的所有文件压缩到指定的文件中
	 * 
	 * @param des
	 *            String 需要压缩成的文件名
	 * @param src
	 *            String 压缩文件源路径,可以是文件或文件夹
	 * @return boolean 如果压缩成功返回true，否则表示失败
	 */
	public static boolean File2Zip(String des, String src) {

		boolean success = true; // 压缩成功标志
		File srcpath = new File(src);
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(new File(des))); // 创建压缩输出流
			out.setEncoding("gbk");
			compress(srcpath, out, ""); // 开始压缩
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
		if (src.isDirectory()) { // 如果是目录
			File[] files = src.listFiles(); // 列举出目录中所有的内容
			out.putNextEntry(new ZipEntry(base + "/")); // 放入一级目录
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < files.length; i++) {
				compress(files[i], out, base + files[i].getName());
			}

		} else { // 如果是文件
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
	 * 解压缩文件
	 * 
	 * @param src
	 *            String zip所在路径c:/test/kk.zip
	 * @param des
	 *            String 希望存放的目录
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
				if (entry.isDirectory()) {// 文件夹
					File directory = new File(des + "/" + entry.getName());
					if (!directory.exists())
						directory.mkdirs();
				} else {// 文件
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
	 * 解压缩zip文件
	 * 
	 * @param src
	 *            String
	 * @param des
	 *            String
	 * @return boolean 成功返回true;
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
		// compress("E:\\海南省%@%001005007", zipout, false);
		File2Zip("E:\\zipTest.zip", "E:\\文件名");
	}
}
