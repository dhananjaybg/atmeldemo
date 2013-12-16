package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/PublicationResource.java 1.50 2013/08/08 18:42:26EDT vpevunov Exp  $ */
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.MediaType;

import org.w3c.dom.Document;

import com.sdl.cd.livecontent.dao.exist.ContentDao;
import com.sdl.cd.livecontent.dao.exist.StandardDao;
import com.sdl.cd.livecontent.model.ApiResultModel;
import com.sdl.cd.livecontent.model.ConfigurationItemModel;
import com.sdl.cd.livecontent.model.LanguageVariantModel;
import com.sdl.cd.livecontent.model.PublicationModel;
import com.sdl.cd.livecontent.model.PublicationListModel;
import com.sdl.cd.livecontent.dao.exist.PublicationDao;
import com.sdl.cd.livecontent.exist.util.EXistUtil;
import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;

/**
** @CLASSGUID: GUID-F262882F-C735-4432-9593-2B0E547A2338
** 
** @ClassTitle: Publication Management V2
** 
** @ShortDesc: Resources to create, manage, and display publications.
**
**/
@Path("/content")
public class PublicationResource extends RestServiceBase {
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8006
	** 
	** @APITitle: Validate Language Code
	** 
	** @ShortDesc: Validates the language code and returns a normalized version, if possible.
	** 
	** @Desc: Returns normalized language code in the "messageKey" attribute of the result model, or returns a 1200 result model if the specified language code could not be normalized.
	** 
	** @URI: /v2/content/config/validate/{lang}
	** 
	** @HTTPMethod: GET 
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @RespSuccessXML: {@code <result message="en-US" messageKey="en-US" status="SUCCESS" statusCode="0"/>} 
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"en-US", "message":"en-US", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="Invalid Language Code. You must use a valid language code such as &quot;en&quot; or &quot;en-GB&quot;" messageKey="pubs.lang.badlanguage" status="FAIL" statusCode="1200"/>}
	** 
	** @RespFailedJSON: {"statusCode":1200, "messageKey":"pubs.lang.badlanguage", "message":"Invalid Language Code. You must use a valid language code such as \"en\" or \"en-GB\"", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/config/validate/en-us</codeph></p>
	**
	** @ExplResponse: {@code <result message="en-US" messageKey="en-US" status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@GET
	@Path("config/validate/{lang}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createPublication(@PathParam("lang") String lang) {
		this.assertPermission(request, "Use application");
		// build the result model:
		ApiResultModel ar = new ApiResultModel();
		
		String safeLang = LanguageVariantModel.getSafeLang(lang);
		if (safeLang == null) {
			ar.init(1200, "pubs.lang.badlanguage", false, user);
		} else {
			ar.init(0, safeLang, true, user);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8007
	** 
	** @APITitle: Fetch Publication Configuration 
	** 
	** @ShortDesc: Fetches the configuration for the specified publication.
	** 
	** @Desc: Request will fail and return the 404 result model if the publication does not exist or if it is hidden.
	** 
	** @URI: /v2/content/{pub}/config
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @RespSuccessXML: {@code <pub id="LiveContentDoc">
	**								<lang code="en">
	**									<configitem name="title" value="SDL LiveContent Reach Documentation"/>
	**									<configitem name="toc" value="GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"/>
	**									<configitem name="visible" value="visible"/>
	**								</lang>
	**								<configitem name="standard" value="DITA"/>
	**								<configitem name="visible" value="visible"/>
	**								<configitem name="default_language" value="en"/>
	**							</pub>}
	** 
	** @RespSuccessJSON: {"id":"LiveContentDoc", "visible":true,
	**						"lang":[{"code":"en", "visible":true,
	**							"configitem":[{"name":"title", "value":"SDL LiveContent Reach Documentation"},
	**								{"name":"toc", "value":"GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"},
	**								{"name":"visible", "value":"visible"}]}],
	**						"configitem":[{"name":"standard", "value":"DITA"},
	**							{"name":"visible", "value":"visible"},
	**							{"name":"default_language", "value":"en"}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/config</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <pub id="LiveContentDoc">
	**							<lang code="en"><configitem name="title" value="SDL LiveContent Reach Documentation"/>
	**								<configitem name="toc" value="GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"/>
	**								<configitem name="visible" value="visible"/>
	**								<configitem name="test" value="post test"/>
	**								<configitem name="shortdesc" value=""/>
	**								<configitem name="prepared" value="prepared"/>
	**								<configitem name="working_status" value="unlocked"/>
	**								<configitem name="filter.file" value=""/>
	**								<configitem name="filter.file.empty" value="1"/>
	**							</lang>
	**							<configitem name="standard" value="DITA"/>
	**							<configitem name="visible" value="visible"/>
	**							<configitem name="default_language" value="en"/>
	**						</pub>}
	** 
	**/	
	@GET
	@Path("{pub}/config")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public PublicationModel getPublication(@PathParam("pub") String name) throws ApiException{
		// permission check
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		
		//requires a check that this pub exists and is visible
		isPubAccessible(dao, name);
		
		PublicationModel pub2 = dao.getPublication(name);
		return pub2;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8008
	** 
	** @APITitle: Delete Publication
	** 
	** @ShortDesc: Deletes the specified publication.
	** 
	** @Desc: Deletes the publication configuration and related resources. Also deletes related comments unless the query parameter is delete_forms=false. Request fails publication or any of its languages are visible.
	** 
	** @URI: /v2/content/{pub}?delete_forms={delete_forms}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @QueryParam: <b>delete_forms: </b>could be "true"(by default) or "false". Specifies whether to delete related comments for the publication.
	** 
	** @RespSuccessXML: {@code <result message="Action = delete publication" messageKey="Action = delete publication" status="SUCCESS" statusCode="0">
	**								<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="Deleted objects" value="1"/>
	**							</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = delete publication", "message":"Action = delete publication", "status":"SUCCESS",
	**						"info":[{"name":"Deleted objects", "value":"1"}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404,"messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/content/content/LiveContentDoc?delete_forms=true</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete publication" messageKey="Action = delete publication" status="SUCCESS" statusCode="0"/>}
	**
	**/
	@DELETE
	@Path("{pub}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deletePublication(@PathParam("pub") String pub, @QueryParam("delete_forms") @DefaultValue( "true" ) boolean deleteXforms) throws ApiException{
		// permission check
		this.assertPermission(request, "Manage pubs");
		ApiResultModel ar = new ApiResultModel();
		PublicationDao dao = new PublicationDao(request.getSession(true));

		// first confirm that:
		// 1. the publication exists:
		if(! dao.checkPublication(pub)) { 
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
		// 2. that the language variant is hidden:
		if(dao.isAnyLanguageVisibile(pub) || dao.isPublicationVisible(pub)) {
			throw new ApiException(200, 371, "pubs.delete.visible", false, user, request);
		}
		// 3. that the language variant is not locked:
		if(! dao.isLocked(pub, "ALL").equals("")) {
			throw new ApiException(200, 351, "pubs.delete.cannotDelete", false, user, request);
		}
		// alright go!
		if(dao.deletePublication(pub)) {
			ar.init(0, "Action = delete publication", true, user);
			// to be backwards-compat, include this listing of the number of deleted objects
			ar.addChildObject("Deleted objects", "1");
			if(deleteXforms) {
				dao.deletePublicationXForms(pub);
			}
		} else {
			// return the generic 500 error
			//ar.init(500, "Failed, Action delete config item", true, user);
			throw new ApiException(200, 404, "Publication is not found.", true, user, request);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8009
	** 
	** @APITitle: Create Publication
	** 
	** @ShortDesc: Creates the specified publication, of the specified standard, with the specified language.
	** 
	** @Desc: Sets the default configuration items. The request will fail if the specified publication exists. Both query parameters (lang and standard) are required.
	** 
	** @URI:  {@code /v2/content/{pub}?lang={lang}&standard={standard}}
	** 
	** @HTTPMethod: POST
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @QueryParam: <b>lang: </b>language code
	** 
	** @QueryParam: <b>standard: </b>the standard (such as "DITA") under which the publication is written.
	**
	** @RespSuccessXML: {@code <result message="Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en" messageKey="Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en" status="SUCCESS" statusCode="0"/>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en", "message":"Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="You must provide all required parameters." messageKey="msg.missingparams" status="FAIL" statusCode="1000"/>}
	** 
	** @RespFailedJSON: {"statusCode":1000, "messageKey":"msg.missingparams", "message":"You must provide all required parameters.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>POST</b> <codeph>@include_ExampleURL/content/DATA_Sample{@code ?lang=en&standard=DITA}</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en" messageKey="Action = add_pub, Pubid = DATA_Sample, Standard = DITA, Default Lang = en" status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@POST
	@Path("{pub}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createPublication(@PathParam("pub") String pub, @DefaultValue("") @QueryParam("lang") String lang,  @DefaultValue("") @QueryParam("standard") String standard) {
		// permission check
		this.assertPermission(request, "Manage pubs");
		ApiResultModel ar = new ApiResultModel();
		
		String safeLang = LanguageVariantModel.getSafeLang(lang);
		StandardDao stDao = new StandardDao(request.getSession(true)); 
		if (!PublicationModel.isPubNameLegal(pub)) {
		ar.init(1201, "invalid.characters.pubname", false, user);
		} else if(!stDao.isSupportedStandard(standard)) {
			ar.init(1000, "msg.missingparams", false, user); //message to be replaced with "Bad standard"
		} else if (safeLang == null) {
			ar.init(1200, "pubs.lang.badlanguage", false, user);
		} else {
			PublicationDao pubDao = new PublicationDao(request.getSession(true));
			PublicationModel pubModel = new PublicationModel();
			pubModel.init(pub, safeLang, standard);
			
			if (pubDao.createPublication(pubModel)) {// build our result
				ar.init(0, "Action = add_pub, Pubid = " + pub + ", Standard = " + standard + ", Default Lang = " + safeLang, true, user);
			} else {
				ar.init(100, "msg.duplicate", false, user);
			}
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800A
	** 
	** @APITitle: Delete Language
	** 
	** @ShortDesc: Deletes the specified language of the specified publication.
	** 
	** @Desc: Deletes language configuration and related resources. Also deletes related comments unless query parameter delete_forms=false. The request fails if the language is visible.
	** 
	** @URI: /v2/content/{pub}/{lang}?delete_forms={delete_forms}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @QueryParam: <b>delete_forms: </b>could be "true"(by default) or "false". Specifies whether to delete comments in addition to the LiveContent Reach publication being deleted.
	** 
	** @RespSuccessXML: {@code <result message="Action = delete language" messageKey="Action = delete language" status="SUCCESS" statusCode="0">
	**								<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="Deleted objects" value="1"/>
	**							</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = delete publication language", "message":"Action = delete publication language", "status":"SUCCESS",
	**						"info":[{"name":"Deleted objects", "value":"1"}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/content/content/LiveContentDoc/en?delete_forms=true</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete config item" messageKey="Action = delete config item" status="SUCCESS" statusCode="0"/>}
	**
	**/
	@DELETE
	@Path("{pub}/{lang}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deleteLanguage(@PathParam("pub") String pub, @PathParam("lang") String lang, @QueryParam("delete_forms") @DefaultValue( "true" ) boolean deleteXforms) throws ApiException{
		// permission check
		this.assertPermission(request, "Manage pubs");
		ApiResultModel ar = new ApiResultModel();
		PublicationDao dao = new PublicationDao(request.getSession(true));
		
		// first confirm that:
		// 1. the language variant exists:
		if(! dao.checkPubLanguage(pub, lang)) { 
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
		// 2. that the language variant is hidden:
		if(dao.isLanguageVisibile(pub, lang)) {
			throw new ApiException(200, 371, "pubs.lang.delete.visible", false, user, request);
		}
		// 3. that the language variant is not locked:
		if(! dao.isLocked(pub, lang).equals("")) {
			throw new ApiException(200, 351, "pubs.lang.delete.cannotDelete", false, user, request);
		}
		// alright go!
		if(dao.deleteLanguage(pub, lang)) {
			ar.init(0, "Action = delete language", true, user);
			// to be backwards-compat, include this listing of the number of deleted objects
			ar.addChildObject("Deleted objects", "1");
			if(deleteXforms) {
				dao.deleteLanguageXForms(pub, lang);
			}
		} else {
			// return the generic 500 error
			//ar.init(500, "Failed, Action delete config item", true, user);
			throw new ApiException(200, 404, "Language variant is not found.", true, user, request);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800B
	** 
	** @APITitle: Create Language
	** 
	** @ShortDesc: Creates a specified language for the specified publication.
	** 
	** @Desc: The request will fail if the specified publication does not exist, or if the specified language for the publication already exists.
	** 
	** @URI: /v2/content/{pub}/{lang}
	** 
	** @HTTPMethod: POST
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>language code
	** 
	** @RespSuccessXML: {@code <result message="Action = add_lang_version, Pubid = DITA_Sample, Lang = en" messageKey="Action = add_lang_version, Pubid = DITA_Sample, Lang = en" status="SUCCESS" statusCode="0"/>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = add_lang_version, Pubid = DITA_Sample, Lang = en", "message":"Action = add_lang_version, Pubid = DITA_Sample, Lang = en", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="An item with that name already exists." messageKey="msg.duplicate" status="FAIL" statusCode="100"/>}
	** 
	** @RespFailedJSON: {"statusCode":100, "messageKey":"msg.duplicate", "message":"An item with that name already exists.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>POST</b> <codeph>@include_ExampleURL/content/DATA_Sample/en</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = add_lang_version, Pubid = DITA_Sample, Lang = en" messageKey="Action = add_lang_version, Pubid = DITA_Sample, Lang = en" status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@POST
	@Path("{pub}/{lang}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createLanguage(@PathParam("pub") String pub, @PathParam("lang") String lang) {
		// permission check
		this.assertPermission(request, "Manage pubs");
		ApiResultModel ar = new ApiResultModel();
		
		String safeLang = LanguageVariantModel.getSafeLang(lang);
		PublicationDao pubDao = new PublicationDao(request.getSession(true));
		if (!pubDao.checkPublication(pub)) {
		ar.init(404, "pubs.nonexist", false, user);
		} else if (safeLang == null) {
			ar.init(1200, "pubs.lang.badlanguage", false, user);
		} else {
			LanguageVariantModel langModel = new LanguageVariantModel();
			langModel.init(safeLang);
			
			if (pubDao.createLanguage(pub, langModel)) {// build our result
				ar.init(0, "Action = add_lang_version, Pubid = " + pub + ", Lang = " + safeLang, true, user);
			} else {
				ar.init(100, "msg.duplicate", false, user);
			}
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800C
	** 
	** @APITitle: Fetch Publications Configuration
	** 
	** @ShortDesc: Fetches a list of Publication Configurations.
	** 
	** @Desc: Configurations for hidden publications and languages will not appear if the user does not have a permission to access hidden publications.
	** 
	** @URI: /v2/content/config
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML: {@code <configuration>
	**							<pub id="LiveContentDoc">
	**								<lang code="en">
	**									<configitem name="title" value="SDL LiveContent Reach Documentation"></configitem>
	**									<configitem name="toc" value="GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"></configitem>
	**									<configitem name="visible" value="visible"></configitem>
	**								</lang>
	**								<configitem name="standard" value="DITA"></configitem>
	**								<configitem name="visible" value="visible"></configitem>
	**								<configitem name="default_language" value="en"></configitem>
	**							</pub>
	**						</configuration>}
	** 
	** @RespSuccessJSON: {"pub":[{"id":"LiveContentDoc", "visible":true,
	**								"lang":[{"code":"en", "visible":true,
	**									"configitem":[{"name":"title", "value":"SDL LiveContent Reach Documentation"},
	**										{"name":"toc", "value":"GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"},
	**										{"name":"visible", "value":"visible"},{"name":"test", "value":"post test"},
	**										{"name":"shortdesc", "value":""},{"name":"prepared", "value":"prepared"},
	**										{"name":"working_status", "value":"unlocked"},{"name":"filter.file", "value":""},
	**										{"name":"filter.file.empty", "value":"1"}]}],
	**								"configitem":[{"name":"standard", "value":"DITA"},
	**									{"name":"visible", "value":"visible"},
	**									{"name":"default_language", "value":"en"}]}]}
	** 
	** @RespFailedXML: {@code <result message="Your account does not have the required permission to perform this action." messageKey="unauthorized.noauth" status="FAIL" statusCode="5000"/>}
	** 
	** @RespFailedJSON: {"statusCode":5000, "messageKey":"unauthorized.noauth", "message":"Your account does not have the required permission to perform this action.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/config</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <configuration>
	**							<pub id="LiveContentDoc">
	**								<lang code="en">
	**									<configitem name="title" value="SDL LiveContent Reach Documentation"></configitem>
	**									<configitem name="toc" value="GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"></configitem>
	**									<configitem name="visible" value="visible"></configitem>
	**									<configitem name="test" value="post test"></configitem>
	**									<configitem name="shortdesc" value=""></configitem>
	**									<configitem name="prepared" value="prepared"></configitem>
	**									<configitem name="working_status" value="unlocked"></configitem>
	**									<configitem name="filter.file" value=""></configitem>
	**									<configitem name="filter.file.empty" value="1"></configitem>
	**								</lang>
	**								<configitem name="standard" value="DITA"></configitem>
	**								<configitem name="visible" value="visible"></configitem>
	**								<configitem name="default_language" value="en"></configitem>
	**							</pub>
	**							<pub id="DITA_Sample">
	**								<lang code="en-US">
	**									<configitem name="draft" value="no"></configitem>
	**									<configitem name="title" value="SDL LiveContent Sample Publication"></configitem>
	**									<configitem name="toc" value="sample.ditamap"></configitem>
	**									<configitem name="visible" value="visible"></configitem>
	**									<configitem name="shortdesc" value="This sample illustrates many of the features of LiveContent."></configitem>
	**									<configitem name="prepared" value="prepared"></configitem>
	**									<configitem name="working_status" value="unlocked"></configitem>
	**									<configitem name="filter.file" value=""></configitem>
	**									<configitem name="filter.file.empty" value="1"></configitem>
	**								</lang>
	**								<configitem name="standard" value="DITA"></configitem>
	**								<configitem name="visible" value="visible"></configitem>
	**								<configitem name="default_language" value="en-US"></configitem>
	**							</pub>
	**						</configuration>}
	** 
	**/	
	@GET
	@Path("config")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public PublicationListModel getPublicationList() throws ApiException{
		// permission check
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		PublicationListModel pubMod = dao.getPublicationList();
		/*
		 * if the user does not have manage pubs permission, remove all invisible items.
		 */
		try {
			this.assertPermission(request, "Manage pubs");
			return pubMod;
		} catch (Exception e) {
			pubMod.deleteInvisibleItems();
			return pubMod;
		}
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800D
	** 
	** @APITitle: Fetch Publication Configuration Item
	** 
	** @ShortDesc: Fetches the requested configuration item for the specified publication.
	** 
	** @Desc: Request will fail and return the 404 result model if any of the publication configuration items do not exist, or if the publication is hidden and the user does not have permission to access hidden publications.
	** 
	** @URI: /v2/content/{pub}/config/{item_name}
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>item_name: </b>publication language item name
	** 
	** @RespSuccessXML: {@code <configitem name="title" value="SDL LiveContent Reach Documentation"/>}
	** 
	** @RespSuccessJSON: {"name":"title", "value":"LiveContent Reach Documentation"}
	** 
	** @RespFailedXML: {@code <result message="The requested configuration item does not exist." messageKey="pubs.config.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.config.nonexist", "message":"The requested configuration item does not exist.", "status":"FAIL", "info":[]} 
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/config/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <configitem name="shortdesc" value="This publication covers installation, maintenance, and usage of the API in SDL LiveContent Reach"/>}
	** 
	**/
	@GET
	@Path("{pub}/config/{item}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ConfigurationItemModel getPubConfigItem(@PathParam("pub") String pub, @PathParam("item") String itemName) throws ApiException{
		// permission check
		this.assertPermission(request, "Use application");
		
		PublicationDao dao = new PublicationDao(request.getSession(true));

		//requires a check that this pub exists and it is visible.
		isPubAccessible(dao, pub);
		
		ConfigurationItemModel configItem = dao.getPubConfigItem(pub, itemName);
		if (configItem == null) throw new ApiException(200, 404, "pubs.config.nonexist", false, user, request);
		return configItem;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800E
	** 
	** @APITitle: Fetch Language Configuration Item
	** 
	** @ShortDesc: Fetches the requested configuration item for the specified language of the specified publication.
	** 
	** @Desc: Request will fail and return the 404 result model if any of the publication configuration items do not exist, or if the publication is hidden and the user does not have permission to access hidden publications.
	** 
	** @URI: /v2/content/{pub}/{lang}/config/{item_name}
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @PathParam: <b>item_name: </b>publication language item name
	** 
	** @RespSuccessXML: {@code <configitem name="title" value="SDL LiveContent Reach Documentation"/>}
	** 
	** @RespSuccessJSON: {"name":"title", "value":"LiveContent Reach Documentation"}
	** 
	** @RespFailedXML: {@code <result message="The requested configuration item does not exist." messageKey="pubs.config.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.config.nonexist", "message":"The requested configuration item does not exist.", "status":"FAIL", "info":[]} 
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/en/config/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <configitem name="shortdesc" value="This publication covers installation, maintenance and usage of the API used in SDL LiveContent Reach"/>}
	** 
	**/
	@GET
	@Path("{pub}/{lang}/config/{item_name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ConfigurationItemModel getLangConfigItem(@PathParam("pub") String pub, @PathParam("lang") String lang, @PathParam("item_name") String itemName) throws ApiException{
		// permission check
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));

		// make sure the pub / language exists:
			isPubLanguageAccessible(dao, pub, lang);

		/*
			 * otherwise, just get the config item as expected
			 */
			ConfigurationItemModel configItem = dao.getLangConfigItem(pub, lang, itemName);
			if (configItem == null) throw new ApiException(200, 404, "pubs.config.nonexist", false, user, request);
			return configItem;
		}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF800F
	** 
	** @APITitle: Fetch Language Configuration
	** 
	** @ShortDesc: Fetches the configuration for the specified language of the specified publication.
	** 
	** @Desc: Request will fail and return the 404 result model if the publication or language does not exist, or if the publication or language is hidden and the user does not have permission to access hidden publications.
	** 
	** @URI: /v2/content/{pub}/{lang}/config
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @RespSuccessXML: {@code <lang code="en">
	**								<configitem name="title" value="LiveContent Reach Documentation"></configitem>
	**								<configitem name="visible" value="visible"></configitem>
	**							</lang>}
	** 
	** @RespSuccessJSON: {"code":"en", "visible":true,
	**						"configitem":[
	**							{"name":"title", "value":"LiveContent Reach Documentation"},
	**							{"name":"visible", "value":"visible"}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/en/config</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <lang code="en">
	**							<configitem name="title" value="LiveContent Reach Documentation"></configitem>
	**							<configitem name="toc" value="GUID-FEC8FA08-E122-46F2-81CC-2B839CCFEE6C"></configitem>
	**							<configitem name="visible" value="visible"></configitem>
	**							<configitem name="test" value="post test"></configitem>
	**							<configitem name="shortdesc" value="This publication about instalation, maintenance and usage of the API in SDL LiveContent Reach"></configitem>
	**							<configitem name="prepared" value="prepared"></configitem>
	**							<configitem name="working_status" value="unlocked"></configitem>
	**							<configitem name="filter.file" value=""></configitem>
	**							<configitem name="filter.file.empty" value="1"></configitem>
	**						</lang>}
	** 
	**/
	@GET
	@Path("{pub}/{lang}/config")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public LanguageVariantModel getLangElement(@PathParam("pub") String pub, @PathParam("lang") String lang) throws ApiException{
		// permission check
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));

		//requires a check that this pub exists and is visible
		isPubLanguageAccessible(dao, pub, lang);		
		
		PublicationModel pubMod = dao.getPublication(pub);
		LanguageVariantModel langMod = pubMod.getLanguage(lang);
		return langMod;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8010
	** 
	** @APITitle: Delete Publication Configuration Item
	** 
	** @ShortDesc: Deletes the requested configuration item for the specified publication.
	** 
	** @Desc: The request will fail and return the 301 result model if any of the protected items are requested to be deleted: {"visible", "standard", "default_language"}
	** 
	** @URI: /v2/content/{pub}/config/{item_name}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>item_name: </b>publication item name
	**
	** @RespSuccessXML: {@code <result message="Action = delete config item" messageKey="Action = delete config item" status="SUCCESS" statusCode="0"/>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = delete config item", "message":"Action = delete config item", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="Failed, Action delete config item" messageKey="Failed, Action delete config item" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"Failed, Action delete config item", "message":"Failed, Action delete config item", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/content/content/LiveContentDoc/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete config item" messageKey="Action = delete config item" status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@DELETE
	@Path("{pub}/config/{item_name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deletePubConfigItem(@PathParam("pub") String pub, @PathParam("item_name") String itemName) {
		// permission check
		this.assertPermission(request, "Manage pubs");

		ApiResultModel ar = new ApiResultModel();
		PublicationDao dao = new PublicationDao(request.getSession(true));
		if (PublicationModel.isProtected(itemName)) {
			ar.init(301, "msg.delete.deny", false, user);
		}
		else if(dao.deletePubConfigItem(pub, itemName)) {
			ar.init(0, "Action = delete config item", true, user);
		} else {
			// return the generic 500 error
			ar.init(404, "Failed, Action delete config item", true, user);
		}
		return ar;
	}
	
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8011
	** 
	** @APITitle: Delete Language Configuration Item
	** 
	** @ShortDesc: Deletes the requested configuration item for the specified language of the specified publication.
	** 
	** @Desc: The request will fail and return the 301 result model if any of the protected items are requested to be deleted: {"visible", "toc"}
	** 
	** @URI: /v2/content/{pub}/{lang}/config/{item_name}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @PathParam: <b>item_name: </b>publication language item name
	**
	** @RespSuccessXML: {@code <result message="Action = delete config item" messageKey="Action = delete config item" status="SUCCESS" statusCode="0"/>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = delete config item", "message":"Action = delete config item", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="Failed, Action delete config item" messageKey="Failed, Action delete config item" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"Failed, Action delete config item", "message":"Failed, Action delete config item", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/content/content/LiveContentDoc/en/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = delete config item" messageKey="Action = delete config item" status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@DELETE
	@Path("{pub}/{lang}/config/{item_name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel deleteLangConfigItem(@PathParam("pub") String pub,@PathParam("lang") String lang, @PathParam("item_name") String itemName) {
		// permission check
		this.assertPermission(request, "Manage pubs");

		ApiResultModel ar = new ApiResultModel();
		PublicationDao dao = new PublicationDao(request.getSession(true));
		if (LanguageVariantModel.checkProtected(itemName)) {
			ar.init(301, "msg.delete.deny", false, user);
		}
		else if(dao.deleteLangConfigItem(pub, lang, itemName)) {
			ar.init(0, "Action = delete config item", true, user);
		} else {
			// return the generic 500 error
			ar.init(404, "Failed, Action delete config item", true, user);
		}
		return ar;
	}
	
	/**
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8012
	** 
	** @APITitle: Create or Update Publication Configuration Item
	** 
	** @ShortDesc: Creates or updates the requested configuration item for the specified publication.
	** 
	** @Desc: If it changes visibility of the language (item_name = "visible"), the visibility will be also updated in the sitemap.
	**			The request will fail if the specified publication does not exist, or if the publication does not have the specified language.
	** 
	** @URI: /v2/content/{pub}/config/{item_name}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication id
	** 
	** @PathParam: <b>item_name: </b>configuration item name
	** 
	** @PostDataXML: {@code <configitem name="title" value="LiveContent Reach Documentation"/>}
	** 
	** @PostDataJSON: {"name":"title", "value":"LiveContent Reach Documentation"}
	** 
	** @RespSuccessXML: {@code <result message="Action = add config item" messageKey="Action = add config item" status="SUCCESS" statusCode="0">
	**								<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="configurationItemModel" name="title" value="LiveContent Reach Documentation"/>
	**							</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = add config item", "message":"Action = add config item", "status":"SUCCESS",
	**						"info":[{"name":"title", "value":"LiveContent Reach Documentation"}]}
	** 
	** @RespFailedXML: {@code <result message="Failed, Action add config item with name: test" messageKey="Failed, Action add config item with name: test" status="FAIL" statusCode="500"/>}
	** 
	** @RespFailedJSON: {"statusCode":500, "messageKey":"Failed, Action add config item with name: title", "message":"Failed, Action add config item with name: title", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/content/LiveContentDoc/config/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Request Body:</b>  <codeph>{@code <configitem name="shortdesc" value="This publication about installation, maintenance and usage of the API in SDL LiveContent Reach"/>}</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = add config item" messageKey="Action = add config item" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="configurationItemModel" name="shortdesc" value="This publication about installation, maintenance and usage of the API in SDL LiveContent Reach"/>
	**						</result>}
	** 
	**/
	@PUT
	@Path("{pub}/config/{item_name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createPubConfigItem(ConfigurationItemModel configItem, @PathParam("pub") String pub) {
		// permission check
		 
		this.assertPermission(request, "Manage pubs");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();

		// does this publication exist?
		if(! dao.checkPublication(pub)) {
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}	

			
		 //build our result -- include an <info/> if successful
		if(dao.createPubConfigItem(pub, configItem)) { 
			ar.init(0, "Action = add config item", true, user);
			ar.addChildObject(configItem);

			// if the item was named "visible" make sure that we respect the sitemap update
			if(configItem.getName().equals("visible")) {
				if(configItem.getValue().equals("hidden")) {
					// delete the sitemap in question:
					dao.deletePubSitemaps(pub);
		} else {
					// create the sitemap in question for this Publication and ALL visible languages
					String appUrl = EXistUtil.getServerUrl(request, false);
					dao.updatePublicationSitemap(pub, appUrl);
				}
			}			
		} else {
			// return the generic 500 error
			ar.init(500, "Failed, Action add config item with name: " + pub, true, user);
		}
		return ar;
	}
	
	 
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8013
	** 
	** @APITitle: Create or Update Language Configuration Item
	** 
	** @ShortDesc: Creates or updates the requested configuration item, for the specified language, in the specified publication.
	** 
	** @Desc: If it changes the visibility of the language (item_name = "visible"), then the visibility will also be updated in the sitemap.
	**			The request will fail if the specified publication does not exist or if the publication does not have the specified language.
	** 
	** @URI: /v2/content/{pub}/{lang}/config/{item_name}
	** 
	** @HTTPMethod: PUT
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication id
	** 
	** @PathParam: <b>lang: </b>language code
	** 
	** @PathParam: <b>item_name: </b>configuration item name
	** 
	** @PostDataXML: {@code <configitem name="title" value="LiveContent Reach Documentation"/>}
	** 
	** @PostDataJSON: {"name":"title", "value":"LiveContent Reach Documentation"}
	** 
	** @RespSuccessXML: {@code <result message="Action = add config item" messageKey="Action = add config item" status="SUCCESS" statusCode="0">
	**								<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="configurationItemModel" name="title" value="LiveContent Reach Documentation"/>
	**							</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Action = add config item", "message":"Action = add config item", "status":"SUCCESS",
	**						"info":[{"name":"title", "value":"LiveContent Reach Documentation"}]}
	** 
	** @RespFailedXML: {@code <result message="Failed, Action add config item with name: test" messageKey="Failed, Action add config item with name: test" status="FAIL" statusCode="500"/>}
	** 
	** @RespFailedJSON: {"statusCode":500, "messageKey":"Failed, Action add config item with name: title", "message":"Failed, Action add config item with name: title", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermManagePubs
	** 
	** @ExplRequest: <p><b>PUT</b> <codeph>@include_ExampleURL/content/LiveContentDoc/en/config/shortdesc</codeph></p>
	**					<p><b>Accept:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Content-Type:</b> <codeph>applicaton/xml</codeph></p>
	**					<p><b>Request Body:</b>  <codeph>{@code <configitem name="shortdesc" value="This publication about installation, maintenance and usage of the API in SDL LiveContent Reach"/>}</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = add config item" messageKey="Action = add config item" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="configurationItemModel" name="shortdesc" value="This publication about installation, maintenance and usage of the API in SDL LiveContent Reach"/>
	**						</result>}
	** 
	**/
	@PUT
	@Path("{pub}/{lang}/config/{item_name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel createLangConfigItem(ConfigurationItemModel configItem, @PathParam("pub") String pub, @PathParam("lang") String lang) {
		// permission check
		this.assertPermission(request, "Manage pubs");
		PublicationDao dao = new PublicationDao(request.getSession(true));	
		ApiResultModel ar = new ApiResultModel();
		
		// does this publication exist?
		if(! dao.checkPubLanguage(pub, lang)) {
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
		
		// build our result -- include an <info/> if successful
		if(dao.createLangConfigItem(pub, configItem, lang)) { 
			ar.init(0, "Action = add config item", true, user);
			ar.addChildObject(configItem);
			// if the item was named "filter.file", add special business logic
			// to additionally include "filter.file.empty" flag
			if(configItem.getName().equals("filter.file")) {
				ContentDao cdao = new ContentDao(request.getSession(true));
				ConfigurationItemModel fileEmpty = new ConfigurationItemModel();
				fileEmpty.setName("filter.file.empty");
				Document filterDom = cdao.getDocumentById(pub, lang, configItem.getValue(), "toc", "live");
				if(filterDom != null && filterDom.getElementsByTagName("feature").getLength() > 0) {
					// NOT empty -- this means that the UI will show "Personalize Content" options
					fileEmpty.setValue("0");
		} else {
					// empty filter file -- this means the UI will avoid prompting to personalize content
					fileEmpty.setValue("1");
				}
				// store this configration value
				dao.createLangConfigItem(pub, fileEmpty, lang);
			}
			// if the item was named "visible" make sure that we respect the sitemap update
			if(configItem.getName().equals("visible")) {
				if(configItem.getValue().equals("hidden")) {
					// delete the sitemap in question:
					dao.deletePubLanguageSitemap(pub, lang);
				} else {
					// create the sitemap in question for this language
					String appUrl = EXistUtil.getServerUrl(request, false);
					dao.updatePubLanguageSitemap(pub, lang, appUrl);
				}
			}
		} else {
			// return the generic 500 error
			ar.init(500, "Failed, Action add config item with name: " + configItem.getName(), true, user);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8014
	** 
	** @APITitle: Check Publication Visiblity
	** 
	** @ShortDesc: Checks if the publication and any one of its' languages are visible.
	** 
	** @Desc: Request will fail and return the 404 result model if the publication configuration does not exist, or if the publication is hidden and the user does not have permission to access hidden publications. 
	** 
	** @URI: /v2/content/{pub}/ifvisible
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @RespSuccessXML: {@code <result message="Publication exists and publication and at least one language variant is visible." messageKey="Publication exists and publication or at least one language variant is visible." status="SUCCESS" statusCode="0"/>}
	** 
	** @RespSuccessJSON: {"statusCode":0, "messageKey":"Publication exists and publication and at least one language variant is visible.", "message":"Publication exists and publication or at least one language variant is visible.", "status":"SUCCESS", "info":[]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404, "messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/ifvisible</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Publication exists and publication and at least one language variant is visible." messageKey="Publication exists and publication or at least one language variant is visible." status="SUCCESS" statusCode="0"/>}
	** 
	**/
	@GET
	@Path("{pub}/ifvisible")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel checkPubVisible(@PathParam("pub") String pub) {
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		
		//requires a check that this pub exists and is visible
		isPubAccessible(dao, pub);
		
		ApiResultModel ar = new ApiResultModel();		
		if(dao.isPublicationVisible(pub) && dao.isAnyLanguageVisibile(pub)) {
			ar.init(0, "Publication exists and publication and at least one language variant is visible.", true, user);
		} else {
			ar.init(404, "pubs.nonexist", false, user);
		}		
		return ar;
	}	
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8015
	** 
	** @APITitle: Check Publication Existence
	** 
	** @ShortDesc: Ensures that the specified publication exists.
	** 
	** @Desc: Request will fail and return the 404 result model if the publication configuration does not exist, or if the publication is hidden and the user does not have permission to access hidden publications.
	** 
	** @URI: /v2/content/{pub}/ifexist
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @RespSuccessXML: {@code <result message="Publication Exists" messageKey="Publication Exists" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="timestamp" value=""/>
	**						</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0,"messageKey":"Publication Exists", "message":"Publication Exists", "status":"SUCCESS", "info":[{"name":"timestamp", "value":""}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404,"messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/ifexist</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Publication Exists" messageKey="Publication Exists" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="timestamp" value=""/>
	**						</result>}
	** 
	**/
	@GET
	@Path("{pub}/ifexist")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel checkPub(@PathParam("pub") String pub) {
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		
		//requires a check that this pub exists and is visible
		isPubAccessible(dao, pub);
		
		ApiResultModel ar = new ApiResultModel();
		
		// build our result -- include an <info/> if successful
		if(dao.checkPublication(pub)) { 
			ar.init(0, "Publication Exists", true, user);
			ar.addChildObject("timestamp", "");
		} else {
			// return the generic error
			ar.init(404, "pubs.nonexist", false, user);
		}
		return ar;
	}
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF8016
	** 
	** @APITitle: Check Language Existence
	** 
	** @ShortDesc: Ensures that the specified language exists.
	** 
	** @Desc: Request will fail and return the 404 result model if the publication configuration does not exist, or if the publication or language is hidden and the user does not have permission to access hidden publications.
	** 
	** @URI: /v2/content/{pub}/{lang}/ifexist
	** 
	** @HTTPMethod: GET
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>pub: </b>publication ID
	** 
	** @PathParam: <b>lang: </b>publication language
	** 
	** @RespSuccessXML: {@code <result message="Publication Exists" messageKey="Publication Exists" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="timestamp" value="2013-04-02T17:55:29.239-04:00"></info>
	**						</result>}
	** 
	** @RespSuccessJSON: {"statusCode":0,"messageKey":"Publication Exists", "message":"Publication Exists", "status":"SUCCESS", "info":[{"name":"timestamp", "value":"2013-04-02T17:55:29.239-04:00"}]}
	** 
	** @RespFailedXML: {@code <result message="The requested publication or language version does not exist." messageKey="pubs.nonexist" status="FAIL" statusCode="404"/>}
	** 
	** @RespFailedJSON: {"statusCode":404,"messageKey":"pubs.nonexist", "message":"The requested publication or language version does not exist.", "status":"FAIL", "info":[]}
	** 
	** @Permissions: @include_PermUseApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/content/LiveContentDoc/en/ifexist</codeph></p>
	**					<p><b>Accept:</b> <codeph>application/xml</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Publication Exists" messageKey="Publication Exists" status="SUCCESS" statusCode="0">
	**							<info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="timestamp" value="2013-04-02T17:55:29.239-04:00"></info>
	**						</result>}
	** 
	**/
	@GET
	@Path("{pub}/{lang}/ifexist")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ApiResultModel checkLang(@PathParam("pub") String pub, @PathParam("lang") String lang) {
		this.assertPermission(request, "Use application");
		PublicationDao dao = new PublicationDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		
		//requires a check that this pub exists and is visible
		isPubLanguageAccessible(dao, pub, lang);
		
		// build our result -- include an <info/> if successful
		if(dao.checkPubLanguage(pub, lang)) { 
			ar.init(0, "Publication Exists", true, user);
			ar.addChildObject("timestamp", dao.getPubTimeStamp(pub, lang));
		} else {
			// return the generic error
			ar.init(404, "pubs.nonexist", false, user);
		}
		return ar;
	}	
	
	/*
	 * EXIST / VISIBLE Helper
	 *   - Private Helper Functions
	 *   - Make it easier to check if a pub exists and is accesssible
	 *   - throw exceptions if fail
	 */
	private void isPubAccessible(PublicationDao dao, String pub) {
		// does this publication exist?
		if(! dao.checkPublication(pub)) {
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
		// does this user have Manage Pubs?
		try {
			this.assertPermission(request, "Manage pubs");
			// if they pass the "Manage pubs" test, then it does not matter if the publication is visible
			return;
		} catch (Exception e) {
			// this user does not pass the "Manage pubs" test then it does matter if the pub is visible -- proceed
		}
		
		if(! dao.isPublicationVisible(pub)) {
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
	}

	private void isPubLanguageAccessible(PublicationDao dao, String pub, String lang) {
		// does this publication exist?
		if(! dao.checkPublication(pub) || ! dao.checkPubLanguage(pub, lang)) {
			// return the generic error
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
		// does this user have Manage Pubs permission?
		try {
			this.assertPermission(request, "Manage pubs");
			// if they pass the "Manage pubs" test then it does not matter if the publication is visible
			return;
		} catch (Exception e) {
			// this user does not pass the "Manage pubs" test then it does matter if the pub is visible -- proceed
		}
		
		if(! dao.isPublicationVisible(pub) || ! dao.isLanguageVisibile(pub, lang)) {
			throw new ApiException(200, 404, "pubs.nonexist", false, user, request);
		}
	}
}
