package com.doof.pages;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;

@Import(library={"context:js/main.js",
				"context:js/locale.js",
				"context:js/jquery.fileupload-fp.js",
				"context:js/jquery.fileupload-ui.js",
				"context:js/jquery.fileupload.js",
				"context:js/jquery.iframe-transport.js",
				"context:js/vendor/jquery.ui.widget.js",
				"context:css/style.css",
				"context:css/jquery.fileupload-ui.css",
				"context:css/bootstrap-image-gallery.min.css",
				"context:css/bootstrap-responsive.min.css",
				"context:css/bootstrap.min.css"
		})

public class About
{
	
	@Persist(PersistenceConstants.FLASH)
	private String message;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

}
