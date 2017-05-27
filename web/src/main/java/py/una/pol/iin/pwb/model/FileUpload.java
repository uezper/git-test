package py.una.pol.iin.pwb.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUpload {
	
	public static String TEMP_LOCATION="tempdir/";
	
	public static void writeToFile(InputStream inputStream,
		String fileName) {

		
		try {
			createDirectoryIfNotExists();
			File f = new File(TEMP_LOCATION + fileName);
			System.out.println(f.getAbsolutePath());
			OutputStream out = new FileOutputStream(f);
			
			
			int read = 0;
			byte[] bytes = new byte[1024];
		
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	private static void createDirectoryIfNotExists()
	{
		File file = new File(TEMP_LOCATION); 
		if (!file.exists() || !file.isDirectory())
		{
			file.mkdir();
		}
	}
}
