package poc_exif_metadata_extractor;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

public class ExifTest {
	@Test
	public void test2() {
		ExifSubIFDDirectory dsid = new ExifSubIFDDirectory();
//		dsid.setDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, new Date());
		dsid.setString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, new Date().toString());
		Tag tag = new Tag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, dsid);
		System.out.println(tag);
		// [Exif SubIFD] Date/Time Original - 2017:08:14 15:05:11
	}

	@Test
	public void test1() {
//		File jpegFile = new File("C:\\Users\\lefto\\Downloads\\flickr\\test1\\46443242052_9be6dbbe2f_o.jpg");
//		File jpegFile = new File("C:\\Users\\lefto\\Downloads\\flickr\\test2\\20170814-150511-6_36561788895_o.jpg");
		File jpegFile = new File("C:\\Users\\lefto\\Downloads\\flickr\\test2\\20170814-150511-6_36561788895_o_EDIT.jpg");
		Metadata metadata;
		try {
			metadata = ImageMetadataReader.readMetadata(jpegFile);
			for (Directory directory : metadata.getDirectories()) {
				for (Tag tag : directory.getTags()) {
					System.out.println(tag);
//					System.out.println(tag.getDirectoryName() + ", " + tag.getTagName() + ", " + tag.getDescription());
//					if (tag.getTagType() == ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
//						System.out.println(tag);
				}
			}
		} catch (ImageProcessingException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
