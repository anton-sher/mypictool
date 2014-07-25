package ua.antonsher.mypictool.di;

import ua.antonsher.mypictool.actions.CreateTiledImage;
import ua.antonsher.mypictool.actions.CreateTiledImageImpl;
import ua.antonsher.mypictool.filewriter.ImageFileWriter;
import ua.antonsher.mypictool.filewriter.javaxiio.JavaxImageFileWriter;

import com.google.inject.AbstractModule;

public class TiledImageWithJavaxModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CreateTiledImage.class).to(CreateTiledImageImpl.class);
		bind(ImageFileWriter.class).to(JavaxImageFileWriter.class);
	}

}
