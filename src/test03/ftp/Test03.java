package test03.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Test03 {

	static final String SERVER_NAME = "192.168.11.8";
	static final int    PORT_NO     = 21;
	static final String USER_NAME   = "Akira";
	static final String PASSWORD     = "77august";

	public static void main(String[] args) {

		// Jakarta Commonsによるネットワークプログラミング
		// 4. FTP
		// http://www.visards.co.jp/java/net/net04.html

		// Apache CommonsのFTPClient
		// https://www.greptips.com/posts/264/

		// FTPにおけるアクティブモードとパッシブモードの違い
		// http://cos.linux-dvr.biz/archives/131

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		FTPClient ftpclient = new FTPClient();

		try {

			// サーバに接続
			ftpclient.connect(SERVER_NAME, PORT_NO);

			int reply = ftpclient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.err.println("connect fail");
				System.exit(1);
			}

			System.out.println("> 接続しました。");

			// ログイン
			if (ftpclient.login(USER_NAME, PASSWORD) == false) {
				System.err.println("login fail");
				System.exit(2);
			}

			System.out.println("> ログイン成功");

			// バイナリモードに設定
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE);

			ftpclient.enterLocalPassiveMode(); // PASVモードの設定
//			ftpclient.enterLocalActiveMode();  // 非PASVモードの設定

			// リモートファイルのファイル一覧の取得
			System.out.println("> リモートファイル一覧");
			String[] rfiles = ftpclient.listNames(".");
			for (String rfile : rfiles) {
				System.out.println(">   " + rfile);
			}

			// ファイル情報の取得
			System.out.println("> リモートファイル情報一覧");
			FTPFile[] ftpFiles = ftpclient.listFiles(".");
			for (FTPFile ftpFile : ftpFiles) {
//				ftpFile.getName();
//				ftpFile.getSize();
//				ftpFile.getTimestamp();
				System.out.format(">   [name] %s [size] %d [timestamp] %s\r\n", ftpFile.getName(), ftpFile.getSize(), sdf.format(ftpFile.getTimestamp().getTime()));
			}

			// ファイル受信
			ftp_get(ftpclient, "tsv.xlsx", "C:\\pleiades\\workspace\\JavaTest01\\work\\test03\\local.xlsx");
			ftp_get(ftpclient, "umu.txt", "C:\\pleiades\\workspace\\JavaTest01\\work\\test03\\local2.txt");

			System.out.println("> ファイル受信完了");

		} catch (SocketException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {

			if (ftpclient.isConnected()) {
				try {
					ftpclient.disconnect();
					System.out.println("> 切断しました。");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static void ftp_get(FTPClient ftpclient, String remoteFile, String localPath) throws FileNotFoundException, IOException {
		ftp_get(ftpclient, remoteFile, new File(localPath));
	}

	static void ftp_get(FTPClient ftpclient, String remoteFile, File localFile) throws FileNotFoundException, IOException {
		try (FileOutputStream os = new FileOutputStream(localFile)) {
			ftpclient.retrieveFile(remoteFile, os);
		}
	}

}
