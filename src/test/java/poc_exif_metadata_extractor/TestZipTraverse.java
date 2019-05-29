package poc_exif_metadata_extractor;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

import org.junit.Test;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.opencsv.CSVWriter;

public class TestZipTraverse {
	private int counter;
	private int zeroFile;
	private int badFile;

	@Test
	public void test1() throws Throwable {
		Path root = Paths.get("C:\\Users\\lefto\\Downloads\\flickr\\ok");
		try (CSVWriter writer = new CSVWriter(new PrintWriter(System.out))) {
//		try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream("C:\\tmp\\Flickr.csv")))) {
			writer.writeNext(new String [] {
					"info.id",
					"info.dateTaken",
					"info.dateImported",
					"info.original",
					"info.geo.latitude",
					"info.geo.longitude",
					"info.exif.dateTimeOriginal",
					"info.exif.createDate",
					"info.exif.gPSLatitude",
					"info.exif.gPSLongitude",
					"info.albums.get(0).title",
					"info.albums.get(1).title",
					"info.albums.get(2).title",
					"info.albums.get(3).title",
					});
			counter = 0;
			Files.walk(root)
					.map(p -> p.toFile())
					.filter(f -> f.isFile())
					.filter(f -> f.getName().matches("^data-download-.+\\.zip$"))
					.forEach(f -> {
						System.out.println(f);
						try (ZipFile zipFile = new ZipFile(f)) {
							zipFile.stream()
									.filter(z -> !z.getName().matches("^.+\\.(mov|m4v)$"))
									.forEach(z -> {
										counter++;
										if (z.getSize() < 1) {
											zeroFile++;
											System.err.println("size zero: " + z);
											return;
										}
										System.out.println(z);
										Metadata metadata;
										try {
											metadata = ImageMetadataReader.readMetadata(zipFile.getInputStream(z));
											for (Directory directory : metadata.getDirectories()) {
												for (Tag tag : directory.getTags()) {
//													System.out.println(tag);
//													System.out.println(tag.getDirectoryName() + ", " + tag.getTagName() + ", " + tag.getDescription());
													if (tag.getTagType() == ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
														System.out.println(tag);
												}
											}
										} catch (ImageProcessingException | EOFException e) {
											badFile++;
											System.err.println("bad file: " + z);
										} catch (IOException e) {
											throw new RuntimeException(e);
										}
									});
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
			System.out.println("counter: " + counter);
			System.err.println("size zero: " + zeroFile);
			System.err.println("bad file: " + badFile);
		}
	}
}
