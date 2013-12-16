package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/SiteMapResource.java 1.25 2013/08/26 18:52:33EDT bbathen Exp  $ */
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType; 

import com.sdl.cd.livecontent.dao.exist.SiteMapDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.SiteMapListModelA;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
 
 
/** 
** 
** @CLASSGUID: GUID-96237A49-0000-0140-1296-F0B7FFFF8000
** 
** @ClassTitle: Sitemap Management  V2
** 
** @ShortDesc: Fetch, Create, Update, or Delete Sitemaps of publications within @include_AppName
** 
**/
 
@Path("/sitemap") 
public class SiteMapResource extends RestServiceBase{ 

	
	/**
	** 
	** @APIGUID: GUID-96237A49-0000-0140-1296-F0B7FFFF8001
	** 
	** @APITitle: Fetch a Sitemap
	** 
	** @ShortDesc: Fetches the sitemap describing the publications within @include_AppName.
	** 
	** @URI: /v2/sitemap
	** 
	** @HTTPMethod: GET
	** 
	** @ContentType: [application/xml]
	** 
	** @RespSuccessXML: {@code <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9&#x9;http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"><url><loc>http://localhost:4444/LiveContent/content/en/LiveContentDoc/GUID-00C07F29-027C-4A54-A645-DFFF0BFE189E</loc><lastmod>2013-05-28T10:59:23.832-04:00</lastmod><changefreq>monthly</changefreq><priority>1.0</priority></url></urlset>}
	** 
	** @RespFailedXML: {@code <result message="Sitemap resource is not found!" messageKey="Sitemap resource is not found!" status="FAIL" statusCode="404" /> }
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/sitemap</codeph></p>
	** 
	** @ExplResponse:  {@code <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9&#x9;http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"><url><loc>http://localhost:4444/LiveContent/content/en/LiveContentDoc/GUID-00C07F29-027C-4A54-A645-DFFF0BFE189E</loc><lastmod>2013-05-28T10:59:23.832-04:00</lastmod><changefreq>monthly</changefreq><priority>1.0</priority></url></urlset>}
	** 
	**/	
	@GET 
	@Produces({MediaType.APPLICATION_XML})
	public SiteMapListModelA getSiteMapList() {
		logger.debug("[SDL LCR] GET /v2/sitemap. \nPath params: N/A. \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Use application"); 
		SiteMapDao dao = new SiteMapDao(request.getSession(true)); 
		SiteMapListModelA sitemaplistmod = dao.getSiteMapList(); 
		logger.debug("[SDL LCR] API succeed: GET /v2/sitemap. SiteMapListModelA returned to be marshaled.");
		return sitemaplistmod;  
	}

	
	
	/** 
	**  
	** @APIGUID: GUID-96237A49-0000-0140-1296-F0B7FFFF8002
	** 
	** @APITitle: Create or Update Sitemap 
	** 
	** @ShortDesc: Creates a new sitemap or updates the current sitemap with specified pub or language sitemaps.
	** 
	** @Desc:
	** 
	** @URI: /v2/sitemap/{pub}/{lang}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b> Update the sitemap with the specified publication. If this parameter is not provided, regenerate a new sitemap with all existing publications and their language variants. Any publication configured as "hidden" will be ignored and not added to the sitemap, nor will any of its language variants be added, even if they are individually configured as "visible." 
	** 
	** @PathParam: <b>lang: </b> Update the sitemap with the specified language variant of the specified publication. If this parameter is not provided, update the sitemap with all language variants within the specified publication. Any language variant configured as "hidden" will be ignored and not added to the sitemap.
	** 
	** @RespSuccessXML:  {@code <result message="Sitemap: pub updated" messageKey="Sitemap: pub updated" status="SUCCESS" statusCode="0" /> }
	** 
	** @RespSuccessJSON: {@code{"statusCode":0,"messageKey":"Action = create_SiteMap","message":"Action = create_SiteMap","status":"SUCCESS","info":[]}} 
	** 
	** @RespFailedXML:  {@code  <result message="UNDEFINED" messageKey="Sitemap: pub update failed" status="FAIL" statusCode="500" /> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":0,"messageKey":"Sitemap: pub updated","message":"Sitemap: pub updated","status":"SUCCESS","info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/sitemap/pub/lang</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	** 
	** @ExplResponse:  {@code  <result message="Sitemap: pub updated" messageKey="Sitemap: pub updated" status="SUCCESS" statusCode="0" /> }
	** 
	**/    

	@PUT
	@Path("{pub}/{lang}")  
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel putSiteMap(@PathParam("pub") String pub, @PathParam("lang") String lang){
		logger.debug("[SDL LCR] GET /v2/sitemap/{pub}. \nPath params: pub=" + pub + "lang= " + lang + " \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app");
		ApiResultModel ar = new ApiResultModel();
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		
		if(dao.pubExists(pub) == false){
			ar.init(200, (pub + " does not exist!!"), false , user);
			ar.setMessage("Sitemap: FAILED. " + pub + " does not exist!");
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} 
		
		if(dao.checkVisibility(pub, lang) != null){
			ar.init(405, "Sitemap: FAILED, visibility for both pub and lang must be turned on.  Turn visibility on for " + dao.checkVisibility(pub, lang), false, user);
			ar.setMessage("Sitemap: FAILED. Please turn on visibility for " + dao.checkVisibility(pub, lang));
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		}
	 
		if (pub.length() <= 0 || lang.length() <= 0) { 
			ar.init(1000, "msg.missingparams", false, user);
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.containsIllegalCharacters(pub) || dao.containsIllegalCharacters(lang)) {
			ar.init(1251, "invalid.characters.alphanumeric", false, user); 
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.putSiteMap(pub, lang, request)) {
			logger.debug("[SDL LCR] API succeed: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Sitemap: pub updated", true, user);
		} else {
			ar.init(405, "Sitemap: pub update failed", false, user);
			ar.setMessageKey("Sitemap: caused a server error");
			ar.setMessage("Sitemap: FAILED. not a valid request");
	    }
		logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
		return ar;
	} 
	
  
	@PUT
	@Path("{pub}")  
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel putSiteMapAllLang(@PathParam("pub") String pub){
		logger.debug("[SDL LCR] PUT /v2/sitemap/{pub}. \nPath params: pub=" + pub + " \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app"); 
		ApiResultModel ar = new ApiResultModel();
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		
		if(dao.pubExists(pub) == false){
			ar.init(200, (pub + " does not exist!!"), false , user);
			ar.setMessage("Sitemap: FAILED. " + pub + " does not exist!");
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		} 		
		if(dao.checkVisibility(pub) != null){ 
			ar.init(300, "Sitemap: FAILED, visibility for both pub and lang must be turned on.  Turn visibility on for " + dao.checkVisibility(pub), false, user);
			ar.setMessage("Sitemap: FAILED. Please turn on visibility for " + dao.checkVisibility(pub));
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		}
		if (pub.length() <= 0) {  
			ar.init(1000, "msg.missingparams", false, user);
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled."); 
			return ar;
		} 
		if (dao.containsIllegalCharacters(pub)) {
			ar.init(1251, "invalid.characters.alphanumeric", false, user); 
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		}
		if(dao.containsInvisibleLang(pub).length > 0){
			String[] badlang = dao.containsInvisibleLang(pub);
			if(badlang.length > 0){
				String langNames = new String();
				for(int i = 0; i < badlang.length; i++){
					if(i != badlang.length - 1){
						langNames = langNames.concat(badlang[i] + ", ");
					}else{
						langNames = langNames.concat(badlang[i] + ".");
					} 
				}
				ar.init(1200, "FAILED: One or more language variants in the publication is invisible.  Turn visibility in these languages on: " + langNames, false, user); 
				logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. One or more language variants in " + pub + " Turn visibility in these languages on: " + langNames);
				return ar;
			}
		}
		if (dao.postAllLang(pub, request)) {
			logger.debug("[SDL LCR] API succeed: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Sitemap: " + pub + " publication updated", true, user);
		} else {
			ar.init(405, "Sitemap: pub update failed", false, user);
			ar.setMessageKey("Sitemap: caused a server error");
			ar.setMessage("Sitemap: FAILED. not a valid request"); 
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
	    }
		logger.debug("[SDL LCR] API fail: PUT /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
		return ar;
	} 

	
	@PUT
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })  
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel addAllPubs(){ 
		logger.debug("[SDL LCR] GET /v2/sitemap/{pub}. \nPath params: N/A \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app"); 
		ApiResultModel ar = new ApiResultModel();
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		ArrayList<String> pubNameList = dao.getPubList();
		if(pubNameList.size() > 0){
			for(int i = 0; i < pubNameList.size(); i++){
				ar.setStatusCode(0);
				ar.setMessage("Sitemap: attempted to generate sitemaps for " + pubNameList.size() + " publications.  Result models are children of this object");
				ar.setMessageKey("Sitemap: attempted to generate sitemaps for " + pubNameList.size() + "publications.");
				ar.addChildObject(this.putSiteMapAllLang(pubNameList.get(i)));
			}
		}else{
			logger.debug("[SDL LCR] API fail: PUT /v2/sitemap. ApiResultModel returned to be marshaled.");
			ar.init(200, "Sitemap: No publications exist", false, user);
		}
		logger.debug("[SDL LCR] API succeed: PUT /v2/sitemap. ApiResultModel returned to be marshaled.");
		return ar;
	}  
 
	
	/** 
	** 
	** @APIGUID: GUID-96237A49-0000-0140-1296-F0B7FFFF8003
	** 
	** @APITitle: Delete Sitemaps
	** 
	** @ShortDesc: Deletes the specified sitemaps.  
	** 
	** @Desc: 
	** 
	** @URI: /v2/sitemap/{pub}/{lang}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b> The name of the publication sitemap to delete. If this parameter is not provided, deletes all publication sitemaps.
	**
	** @PathParam: <b>lang: </b> The language variant sitemap to delete. If this parameter is not provided, deletes the specified publication sitemap, including all its language variant sitemaps.
	** 
	** @RespSuccessXML:  {@code <result message="Action = delete_SiteMap" messageKey="Action = delete_SiteMap" status="SUCCESS" statusCode="0" /> }
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = delete_SiteMap","message":"Action = delete_SiteMap","status":"SUCCESS","info":[]}}
	** 
	** @RespFailedXML:  {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":404,"messageKey":"Not Found","message":"Not Found","status":"FAIL","info":[]}}
	** 
	** @Permissions: @include_PermManageApp
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/sitemap/pub/lang</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete_SiteMap" messageKey="Action = delete_SiteMap" status="SUCCESS" statusCode="0" /> }
	**  
	**/
	@DELETE 
	@Path("{pub}/{lang}") 
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteSiteMapItem(@PathParam("pub") String pub, @PathParam("lang") String lang){
		logger.debug("[SDL LCR] DELETE /v2/sitemap/{pub}/{lang}. \nPath params: pub=" + pub + "lang= " + lang + " \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app");
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if (pub.length() <= 0 || lang.length() <= 0) {
			ar.init(1000, "msg.missingparams", false, user);
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if(dao.pubExists(pub) == false){
			ar.init(200, (pub + " does not exist!!"), false , user);
			ar.setMessage("Sitemap: FAILED. " + pub + " does not exist!");
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.containsIllegalCharacters(pub) || dao.containsIllegalCharacters(lang)) {
			ar.init(1251, "invalid.characters.alphanumeric", false, user);
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.deleteSiteMapItem(pub, lang)) {
			logger.debug("[SDL LCR] API succeed: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = delete_SiteMap", true, user);
		} else {
			ar.init(404, "Not Found", true, user);
		}
		logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}/{lang}. ApiResultModel returned to be marshaled.");
		return ar;
	}
	

	@DELETE 
	@Path("{pub}") 
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteSiteMap(@PathParam("pub") String pub){
		logger.debug("[SDL LCR] DELETE /v2/sitemap/{pub}/{lang}. \nPath params: pub=" + pub + " \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app");
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if (pub.length() <= 0) {
			ar.init(1000, "msg.missingparams", false, user);
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if(dao.pubExists(pub) == false){
			ar.init(200, (pub + " does not exist!!"), false , user);
			ar.setMessage("Sitemap: FAILED. " + pub + " does not exist!");
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.containsIllegalCharacters(pub)) {
			ar.init(1251, "invalid.characters.alphanumeric", false, user);
			logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			return ar;
		} else if (dao.deletePub(pub)) {
			logger.debug("[SDL LCR] API succeed: DELETE /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = delete_SiteMap", true, user);
		} else {
			ar.init(404, "Not Found", true, user);
		}
		logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap/{pub}. ApiResultModel returned to be marshaled.");
		return ar;
	}
	

	@DELETE 
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteSiteMapItem(){
		logger.debug("[SDL LCR] DELETE /v2/sitemap/{pub}/{lang}. \nPath params: N/A \nQuery params: N/A.");
		logger.debug("[SDL LCR] Request data:" + getRequestAsString());
		this.assertPermission(request, "Manage app");
		SiteMapDao dao = new SiteMapDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		if (dao.deleteAllPubs()) {
			logger.debug("[SDL LCR] API succeed: DELETE /v2/sitemap. ApiResultModel returned to be marshaled.");
			ar.init(0, "Action = delete_SiteMap", true, user);
		} else {
			ar.init(404, "Not Found", true, user);
		}
		logger.debug("[SDL LCR] API fail: DELETE /v2/sitemap. ApiResultModel returned to be marshaled.");
		return ar;
	}
}
