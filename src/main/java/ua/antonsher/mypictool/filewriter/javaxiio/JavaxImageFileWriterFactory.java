package ua.antonsher.mypictool.filewriter.javaxiio;

import ua.antonsher.mypictool.filewriter.ImageFileWriter;
import ua.antonsher.mypictool.filewriter.ImageWriterFactory;

public class JavaxImageFileWriterFactory implements ImageWriterFactory {
	@Override
	public ImageFileWriter createImageWriter() {
		return new JavaxImageFileWriter();
	}
}