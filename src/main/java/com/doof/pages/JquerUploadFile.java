package com.doof.pages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageDetached;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.got5.tapestry5.jquery.JQueryEventConstants;
import org.got5.tapestry5.jquery.components.AjaxUpload;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doof.entities.PersistableDocument;


public class JquerUploadFile {

	Logger logger = LoggerFactory.getLogger(JquerUploadFile.class);

	private String tempDir = System.getProperty("java.io.tmpdir");

	@Property
	private PersistableDocument document;

	@Inject
	private Session session;

	public List<PersistableDocument> getFiles() {
		return session.createCriteria(PersistableDocument.class).list();
	}

	@Persist(PersistenceConstants.FLASH)
	private String message;

	@Property
	private List<UploadedFileItem> uploadedFiles;
	
	@Property
	private UploadedFileItem uploadedFile;

	@InjectComponent
	private Zone uploadResult;

	@Inject
	private ComponentResources resources;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	void onActivate() {

		if (uploadedFiles == null)
			uploadedFiles = new ArrayList<UploadedFileItem>();
		logger.info("############### Temp directory is set to : " + tempDir);

	}
	
	@PageDetached	
	void cleanUp(){

//		logger.info("############### RUNNING CLEANUP");
//		for (UploadedFileItem item : uploadedFiles){
//			item.cleanup();
//		}
		
		
		
	}

	@OnEvent(component = "uploadFile", value = JQueryEventConstants.AJAX_UPLOAD)
	void onUploadFile(UploadedFileItem uploadedFile) throws IOException {

		if (uploadedFile != null) {
			this.uploadedFiles.add(uploadedFile);
		}
		

		message = "This upload was: AJAX_UPLOAD";

		logger.info("Temp directory is set to : " + tempDir);
		saveFile(uploadedFile);
		ajaxResponseRenderer.addRender("uploadResult", uploadResult);
		uploadedFile.cleanup();

	}
	
//	@OnEvent(component = "uploadFile", value = JQueryEventConstants.AJAX_UPLOAD)
//	Object onUploadFile(UploadedFileItem uploadedFile) throws IOException {
//
//		if (uploadedFile != null) {
//			this.uploadedFiles.add(uploadedFile);
//		}
//		message = "This upload was: AJAX_UPLOAD";
//
//		logger.info("Temp directory is set to : " + tempDir);
//		
//		final JSONObject result = new JSONObject();
//		final JSONObject params = new JSONObject().put(
//				"url",
//				resources.createEventLink("myCustomEvent", "AJAX_UPLOAD")
//						.toURI()).put("zoneId", "uploadResult");
//
//		result.put(AjaxUpload.UPDATE_ZONE_CALLBACK, params);
//		return result;
//
//	}

	@OnEvent(component = "uploadFile", value = JQueryEventConstants.NON_XHR_UPLOAD)
	Object onNonXHRUploadFile(UploadedFileItem uploadedFile) {

		logger.info(" >>> onNonXHRUploadFile");

		if (uploadedFile != null) {
			this.uploadedFiles.add(uploadedFile);
		}

		final JSONObject result = new JSONObject();
		final JSONObject params = new JSONObject().put(
				"url",
				resources.createEventLink("myCustomEvent", "NON_XHR_UPLOAD")
						.toURI()).put("zoneId", "uploadResult");

		result.put(AjaxUpload.UPDATE_ZONE_CALLBACK, params);
		return result;
	}

	@OnEvent(value = "myCustomEvent")
	void onMyCustomEvent(final String someParam) {

		message = "This upload was: " + someParam;

		ajaxResponseRenderer.addRender("uploadResult", uploadResult);
	}

	void onUploadException(FileUploadException ex) {

		message = "Upload exception: " + ex.getMessage();

		ajaxResponseRenderer.addRender("uploadResult", uploadResult);
	}

	public String getMessage() {

		return message;
	}

	@CommitAfter
	private void saveFile(UploadedFileItem uploadedFile) throws IOException {

		// byte[] bytes = generateBytesFromUploadedFile(uploadedFile);

		if (document == null) {
			this.document = new PersistableDocument();
		}

		File file = new File(uploadedFile.getFileName());
		uploadedFile.write(file);

		logger.info("Path is : " + file.getPath());
		document.setPath(file.getPath());
		logger.info(file.getAbsolutePath());
		session.persist(document);

	}

	/**
	 * @param uploadedFile
	 * @return
	 * @throws IOException
	 */
	private byte[] generateBytesFromUploadedFile(UploadedFile uploadedFile)
			throws IOException {
		InputStream is = uploadedFile.getStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = is.read(b)) != -1) {
			bos.write(b, 0, bytesRead);
		}
		byte[] bytes = bos.toByteArray();
		logger.info("Bytes : " + bytes.length);
		return bytes;
	}
	


}
