package com.sdl.cd.livecontent.services.rest.resources;
/* $Id: src/com/sdl/cd/livecontent/services/rest/resources/SkinResource.java 1.19 2013/08/05 17:27:42EDT vpevunov Exp  $ */
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.sdl.cd.livecontent.dao.exist.SkinDao;
import com.sdl.cd.livecontent.exist.util.EXistUtil;
import com.sdl.cd.livecontent.model.ApiResultModel;

import com.sdl.cd.livecontent.services.rest.RestServiceBase;
import com.sdl.cd.livecontent.services.rest.exceptions.ApiException;


/** 
** 
** @CLASSGUID: GUID-1C58971C-F055-40E8-A0A0-498F31080B2F
** 
** @ClassTitle: Skin Management V2
** 
** @ShortDesc: Resources for creating, updating, deleting, and fetching skins.
** 
**/

@Path("/skin")
public class SkinResource extends RestServiceBase {
	
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF801C
	** 
	** @APITitle: Fetch Skins
	** 
	** @ShortDesc: Fetch Skins
	** 
	** @URI: v2/skin
	** 
	** @HTTPMethod: GET
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @RespSuccessXML:  {@code <result message="Action = getSkin" messageKey="Action = getSkin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="43684"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="base"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="Example"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="config"/></result>}
	** 
	** @RespSuccessJSON: {"statusCode":0,"messageKey":"Action = getSkin", "message":"Action = getSkin", "status":"SUCCESS", "info":[{"name":"skin", "value":"43684"},{"name":"skin", "value":"base"},{"name":"skin", "value":"Example"},{"name":"skin", "value":"config"}]}}
	** 
	** @RespFailedXML: {@code <result message="Not Found" messageKey="Not Found" status="FAIL" statusCode="404" /> }
	** 
	** @RespFailedJSON: {@code {"statusCode":404,"messageKey":"Not Found", "message":"Not Found", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermDevelopApp
	** 
	** @ExplRequest: <p><b>GET</b> <codeph>@include_ExampleURL/skin</codeph></p>
	** 
	** @ExplResponse:  {@code <result message="Action = getSkin" messageKey="Action = getSkin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="43684"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="base"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="Example"/><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="config"/></result>}
	** 
	**/	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel getSkin() throws ApiException{
		this.assertPermission(request,"Develop app");
		SkinDao dao = new SkinDao(request.getSession(true));
		ApiResultModel ar = new ApiResultModel();
		ArrayList<String> list = dao.getSkin();
		if(list.size() > 0){
		ar.init(0, "Action = getSkin", true, user);
			for(int i = 0; i < list.size(); i++){
				ar.addChildObject("skin", EXistUtil.decodeURI(list.get(i)));
			}
			return ar;
		}else{
			ar.init(1350, "FAIL", true, user);
			return ar;
		}
	}
 
	/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF801D
	** 
	** @APITitle: Create Skin
	** 
	** @ShortDesc: Creates the named skin and its resource collections.
	** 
	** @Desc: The name at the end of the URI determines which skin to create.
	** 
	** @URI: /v2/skin/{name}
	** 
	** @HTTPMethod: POST
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the skin item to create.
	** 
	** @RespSuccessXML:  {@code <result message="Action = add_skin" messageKey="Action = add_skin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="testskin"/></result>}
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = add_skin", "message":"Action = add_skin", "status":"SUCCESS", "info":[{"name":"skin", "value":"testskin"}]}}
	** 
	** @RespFailedXML:  {@code <result message="FAIL, skin name cannot start with a &quot;.&quot;" messageKey="FAIL, skin name cannot start with a &quot;.&quot;" status="FAIL" statusCode="1350" />}
	** 
	** @RespFailedJSON:  {@code {"statusCode":1350,"messageKey":"FAIL, skin name cannot start with a \".\"", "message":"FAIL, skin name cannot start with a \".\"", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermDevelopApp
	** 
	** @ExplRequest: <p><b>POST</b> <codeph>@include_ExampleURL/skin/{name}</codeph></p>
	** 
	** @ExplResponse: {@code <result message="Action = add_skin" messageKey="Action = add_skin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="testskin"/></result>}
	** 
	**/
	@POST
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel createSkin(@PathParam("name") String name) throws ApiException {
		this.assertPermission(request, "Develop app");
		// TODO - add regex to require alpha numeric characters 
		ApiResultModel ar = new ApiResultModel();
		if(name.startsWith(".")){
			ar.init(1350, "FAIL, skin name cannot start with a \".\"", true, user);
			return ar;
		}

		SkinDao dao = new SkinDao(request.getSession(true));
		ArrayList<String> list = dao.createSkin(name);
		if (list.size() > 0) {
			ar.init(0, "Action = add_skin", true, user);
			for(int i = 0; i < list.size(); i++){
				ar.addChildObject("skin", list.get(i));
			}
			return ar;
		} else {
			ar.init(1350, "FAIL", true, user);
			return ar;
		} 
	}
	 
		/** 
	** 
	** @APIGUID: GUID-A8B2EDEE-0000-013D-DAD3-F68EFFFF801E
	** 
	** @APITitle: Delete Skin
	** 
	** @ShortDesc: Deletes the named skin and its resource collections.
	** 
	** @URI: /v2/skin/{name}
	** 
	** @HTTPMethod: DELETE
	** 
	** @Accept: [application/json] OR [application/xml]
	** 
	** @ContentType: [application/json] OR [application/xml]
	** 
	** @PathParam: <b>name: </b> The name of the skin to delete.
	** 
	** @RespSuccessXML:  {@code <result message="Action = delete_skin" messageKey="Action = delete_skin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="testskin"/></result> }
	** 
	** @RespSuccessJSON:  {@code {"statusCode":0,"messageKey":"Action = delete_skin", "message":"Action = delete_skin", "status":"SUCCESS", "info":[{"name":"skin", "value":"testskin"}]} }
	** 
	** @RespFailedXML:  {@code <result message="FAIL" messageKey="FAIL" status="FAIL" statusCode="1350"/> }
	** 
	** @RespFailedJSON:  {@code {"statusCode":1350,"messageKey":"FAIL", "message":"FAIL", "status":"FAIL", "info":[]} }
	** 
	** @Permissions: @include_PermDevelopApp 
	** 
	** @ExplRequest: <p><b>DELETE</b> <codeph>@include_ExampleURL/skin/{name}</codeph></p>
	** 
	** @ExplResponse:  {@code <result message="Action = delete_skin" messageKey="Action = delete_skin" status="SUCCESS" statusCode="0"><info xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="apiInfoModel" name="skin" value="testskin"/></result> }
	** 
	**/
	@DELETE
	@Path("{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) 
	public ApiResultModel deleteSkin(@PathParam("name") String name) throws ApiException {
		this.assertPermission(request, "Develop app");
		// TODO - add regex to require alpha numeric characters 
		ApiResultModel ar = new ApiResultModel();
		SkinDao dao = new SkinDao(request.getSession(true));
		ArrayList<String> list = dao.deleteSkin(name);
		if (list.size() > 0) {
			ar.init(0, "Action = delete_skin", true, user);
			for(int i = 0; i < list.size(); i++){
				ar.addChildObject("skin", list.get(i));
			}
			return ar;
		} else {
			ar.init(1350, "FAIL", true, user);
			return ar;
		} 
	}
}
