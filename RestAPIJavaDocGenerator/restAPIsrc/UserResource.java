package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/UserResource.java 1.9 2013/03/06 13:29:08EST tresea Exp  $ */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;

import com.sdl.cd.livecontent.dao.exist.UserDao;
import com.sdl.cd.livecontent.exist.util.MimeTypeUtil;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.UserModel;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.ui.SkinResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/user")
public class UserResource extends RestServiceBase {
	protected static final Logger logger = Logger.getLogger(UserResource.class);

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public UserModel getProfile(@PathParam("username") String username) {
		// permission check
		this.assertPermission(request, "Use application");
		return user;
	}
	
	@GET
	@Path("{username}/profile")
	public Response getProfileImage(@PathParam("username") String username,
			@DefaultValue("false") @QueryParam("working") boolean working) {
		// permission check
		this.assertPermission(request, "Use application");

		UserDao udao = new UserDao(request.getSession(true));
		// does the user profile image exist in the repository?
		boolean exists = udao.checkProfileImage(username, working);
		if(exists) {
			return sendConditionalCachedContent("userprofile-" + username, udao);
		} else {
			// the image was not found, so get the profile image from the skin:
			// Use the RESTful SkinResource to fetch the image with all the right HTTP headers
			SkinResource sr = new SkinResource();
			sr.setRequest(request);
			return sr.getResource("img", "icon_user32.png", null); 
		}
	}

	@GET
	@Path("profile")
	public Response getProfileImage() {
		// permission check
		this.assertPermission(request, "Use application");

		UserDao udao = new UserDao(request.getSession(true));
		// does the user profile image exist in the repository?
		boolean exists = udao.checkProfileImage(user.getUsername(), false);
		if(exists) {
			return sendConditionalCachedContent("userprofile-" + user.getUsername(), udao);
		} else {
			// the image was not found, so get the profile image from the skin:
			// Use the RESTful SkinResource to fetch the image with all the right HTTP headers
			SkinResource sr = new SkinResource();
			sr.setRequest(request);
			return sr.getResource("img", "icon_user32.png", null); 
		}
	}	
	
	@POST
	@Path("{username}/profile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadProfileImage(@PathParam("username") String username,
			@DefaultValue("true") @QueryParam("working") boolean working,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition disposition) { 				
		// permission check
		this.assertPermission(request, "Use application");
		// build the user model:
		UserDao udao = new UserDao(request.getSession(true));
		// assert that this is the current user
		this.assertCurrentUser(user, username);

		// prepare our result:
		ApiResultModel ar = new ApiResultModel();

		// TODO - proper error handling here
		String fileName = disposition.getFileName();
		String mime = MimeTypeUtil.getMimeType("file:///" + fileName);
		logger.debug("Determined upload was mime-type: " + mime);
		if (uploadedInputStream == null) {
			logger.error("[Upload Profile Image] Uploaded file ("+fileName+") is 'null'");
			ar.init(200, "msg.missingparams", false, user);
		}
		
		// convert JPG to PNG:
		boolean check = false;
		if(mime.equals("image/jpeg")) {
			logger.debug("[Upload Profile Image] Coverting a JPG to a PNG");
			// read a jpeg from a inputFile
			BufferedImage bufferedImage;
			try {
				bufferedImage = ImageIO.read(uploadedInputStream);
				// this writes the bufferedImage into a byte array called resultingBytes
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", byteArrayOut);
				byte[] bytes = byteArrayOut.toByteArray();
				// actually save the image to the database
				check = udao.saveProfileImage(username, bytes, working);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// actually save the image to the database
			check = udao.saveProfileImage(username, uploadedInputStream, working);
		}
		if (check) {
			// return a success:
			ar.init(0, "Uploaded profile image", true, user);
		} else {
			// TODO return a valid failure
			ar.init(103, "FAILED to upload profile image", true, user);
			logger.error("[Upload Profile Image] FAILED to upload profile image: " + ar);
		}
		// return Response.ok(ar).build();
		ResponseBuilder response = Response.ok("{\"status\":\"SUCCESS\", \"statusCode\": \"0\"}");
		// IE will only accept text/plain for JSON, so send it over
		response.header("Content-Type", "text/plain");
		return response.build();
	}

	@POST
	@Path("{username}/profile/crop")
	@Produces(MediaType.APPLICATION_XML)
	// note that this function treats everything as a PNG, but it does work for JPGs
	public ApiResultModel cropProfileImage(
			@PathParam("username") String username, @FormParam("x") float x,
			@FormParam("y") float y, @FormParam("height") float height,
			@FormParam("width") float width) {
		this.assertPermission(request, "Use application");
		
		// build the user model:
		UserDao udao = new UserDao(request.getSession(true));
		// assert that this is the current user
		this.assertCurrentUser(user, username);
		
		// the ApiResultModel to use throughout the function:
		ApiResultModel ar = new ApiResultModel();

		// get the profile-temp.png from the database:
		byte[] imageBytes = udao.loadProfileImage(username, true);

		// if the image is non-existent
		if (imageBytes == null) {
			// TODO return a valid failure
			ar.init(103, "Image for cropping does not exist", true, user);
			logger.error("[Crop Profile Image] Failed to upload image; image was null: " + ar);
			return ar;
		}

		// build the image:
		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(
					imageBytes));
			// chop the image down to the selected rectangle:
			BufferedImage dest = image.getSubimage(Math.round(x),
					Math.round(y), Math.round(width), Math.round(height));
			// then scale the image to the right size:
			BufferedImage scaled = resizeImage(dest, 32, 32);

			// get the final image as an input stream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(scaled, "png", baos);
			InputStream is = new ByteArrayInputStream(baos.toByteArray());
			// logger.error("CROPPING: " + dest + " to IS = " + is);

			// store the final image into the database:
			udao.saveProfileImage(username, (InputStream) is, false);
		} catch (IOException e) {
			// in the case of an IOException, drop the message and a failure via the API
			e.printStackTrace();
			ar.init(103, "Image failed to crop / resize: " + e.getMessage(), true, user);
			return ar;
		} catch (Exception e) {
			// this could be literally anything -- including the "negative or zero width" exception
			e.printStackTrace();
			ar.init(103, "Image failed to crop / resize: " + e.getMessage(), true, user);
			return ar;
		}
		ar.init(0, "Cropped and resized image", true, user);
		return ar;
	}

	// ================================================
	// 	take an image and resize it to a W/H
	// ================================================	
	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
}
