package com.doof.pages;

import java.io.File;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TapUpload {

    @Property
    private UploadedFileItem file;
    

    private Logger logger = LoggerFactory.getLogger(TapUpload.class);

    public void onSuccess()
    {
        logger.info(file.getFileName());
        file.cleanup();
        
//        file.write(copied);
    }
    
}
