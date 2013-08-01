package pl.stock.commons.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * Utility class for remote file operations 
 * @author Piotr Mińkowski
 *
 */
public final class DownloadFileUtils {

	
	/**
	 * Download URL stream, save on disc and return handle
	 * @param fileURL - URL of file
	 * @return - file handle
	 * @throws IOException - file opening error
	 */
	public static final File downloadAndSaveURL(String fileURL) throws IOException {
		
		// create download URL
		final URL url = new URL(fileURL);
		
		// create temporary file with stream from URL
		final File temp = File.createTempFile("_daily", ".temp");
		FileUtils.copyURLToFile(url, temp);
		
		// return temporary file handle
		return temp;
	}
	
}
