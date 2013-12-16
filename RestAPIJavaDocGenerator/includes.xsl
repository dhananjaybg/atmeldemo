<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lcui="http://www.sdlxysoft.com/LiveContent/UITags" xmlns:langres="com.sdl.cd.livecontent.config.ConfigFacade" version="2.0">
<!-- $Id: includes.xsl 1.5 2013/04/22 15:24:15EDT vpevunov Exp  $ -->

    <xsl:template match="include_SDL">SDL</xsl:template>
	
    <xsl:template match="include_AppName">SDL LiveContent Reach</xsl:template>
	
    <xsl:template match="include_RLMServer">SDL License Server</xsl:template>
	
    <xsl:template match="include_ExampleURL">http://example.corp:8080/LiveContent/v2</xsl:template>
	
    <xsl:template match="include_PermManageApp">"Manage application"</xsl:template>
	
    <xsl:template match="include_PermUseApp">"Use application"</xsl:template>
	
    <xsl:template match="include_PermManagePubs">"Manage publications"</xsl:template>
	
    <xsl:template match="include_PermDevelopApp">"Develop application"</xsl:template>
	
</xsl:stylesheet>

