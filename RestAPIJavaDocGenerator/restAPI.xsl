<?xml version="1.0" encoding="utf-8" standalone="no"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<!-- $Id: restAPI.xsl 1.8 2013/08/14 14:41:55EDT bbathen Exp  $ -->
    <xsl:output method="xml" encoding="UTF-8" media-type="application/xml" indent="yes"/>
    <xsl:preserve-space elements="*"/>
    <xsl:include href="includes.xsl"/>

<!-- ============================================= -->
<!-- 		Document must have GUID defined	   -->
<!-- ============================================= -->
	<xsl:template match="/API_WRAPPER">
		<reference outputclass="NewPage"> 
			<xsl:attribute name="id">
				<xsl:apply-templates select="APIGUID"/>
			</xsl:attribute>
			<xsl:apply-templates select="APITitle"/>
			<xsl:apply-templates select="ShortDesc"/>
			<xsl:if test="count(IndexTerm)">
				<prolog><metadata><keywords>
					<xsl:apply-templates select="IndexTerm"/>
				</keywords></metadata></prolog>
			</xsl:if>
			<refbody>
				<xsl:apply-templates select="Desc"/>
				<xsl:apply-templates select="URI"/>
				<xsl:if test="RespSuccessXML or RespSuccessJSON or RespFailedXML or RespFailedJSON">
					<section><title>Response</title>
						<xsl:if test="RespSuccessXML or RespSuccessJSON">
							<p><b>Success</b><ul>
								<xsl:apply-templates select="RespSuccessXML"/>
								<xsl:apply-templates select="RespSuccessJSON"/>
							</ul></p> 
						</xsl:if>
						<xsl:if test="RespFailedXML or RespFailedJSON">
							<p><b>Failed</b><ul>
								<xsl:apply-templates select="RespFailedXML"/>
								<xsl:apply-templates select="RespFailedJSON"/>
							</ul></p> 
						</xsl:if>
					</section>
				</xsl:if>
				<xsl:apply-templates select="Permissions"/>
				<xsl:if test="ExplRequest or ExplResponse">
					<section><title>Example</title>
						<xsl:apply-templates select="ExplRequest"/>
						<xsl:apply-templates select="ExplResponse"/>
					</section>                                    
				</xsl:if>
			</refbody>
		</reference>
	</xsl:template>
	
<!-- ============================================= -->
<!-- 			API GUID                   -->
<!-- ============================================= -->
	<xsl:template match="APIGUID">
		 <xsl:apply-templates/>
	</xsl:template>

<!-- ============================================= -->
<!-- 			zStyling Tags   -->
<!-- ============================================= -->        
	<xsl:template match="b | i | u | p | codeph">
<xsl:copy>
<xsl:copy-of select="@*"/>
<xsl:apply-templates/>
</xsl:copy>
	</xsl:template>

	<xsl:template match="*" mode="copy">
		<xsl:copy>
			<xsl:copy-of select="@*"/>
			 <xsl:apply-templates mode="copy"/>
		</xsl:copy>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Title                  -->
<!-- ============================================= -->
	<xsl:template match="APITitle">
		<title><xsl:apply-templates/></title> 
	</xsl:template>
	
<!-- ============================================= -->
<!-- 			API Short Description            -->
<!-- ============================================= -->
	<xsl:template match="ShortDesc">
		<shortdesc><xsl:apply-templates/></shortdesc>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Description            -->
<!-- ============================================= -->
	<xsl:template match="Desc">
		<section><p><xsl:apply-templates/></p></section>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Index Terms            -->
<!-- ============================================= -->
	<xsl:template match="IndexTerm">
        <indexterm><xsl:apply-templates/></indexterm>
	</xsl:template>

	<xsl:template match="URI">
		<section>
			<title>Request</title>
			<p><parmname>URI: </parmname><codeph><xsl:apply-templates/></codeph></p> 
			<xsl:apply-templates select="following-sibling::HTTPMethod"/>
			<xsl:if test="following-sibling::Accept or following-sibling::ContentType">
				<p><b>Header params:</b> 
					<ul>
						<xsl:apply-templates select="following-sibling::Accept"/>
						<xsl:apply-templates select="following-sibling::ContentType"/>
					</ul></p>
			</xsl:if>
			<xsl:if test="count(following-sibling::PathParam)">
				<p><parmname>PATH Parameter(s):</parmname><ul><xsl:apply-templates select="following-sibling::PathParam"/></ul></p>
			</xsl:if>
			<xsl:if test="count(following-sibling::QueryParam)">
				<p><parmname>Query Parameter(s):</parmname><ul><xsl:apply-templates select="following-sibling::QueryParam"/></ul></p>
			</xsl:if>
			<xsl:if test="following-sibling::PostDataXML or following-sibling::PostDataJSON">
				<p><b>Request Body: </b><ul>
					<xsl:if test="following-sibling::PostDataXML">
							<li><parmname>XML: </parmname><codeph><xsl:apply-templates select="following-sibling::PostDataXML"/></codeph></li>
					</xsl:if>
					<xsl:if test="following-sibling::PostDataJSON">
							<li><parmname>JSON: </parmname><codeph><xsl:apply-templates select="following-sibling::PostDataJSON"/></codeph></li>
					</xsl:if>
				</ul></p>
			</xsl:if>
		</section> 
	</xsl:template>

	<xsl:template match="HTTPMethod">
			<p><parmname>HTTP Method: </parmname><xsl:apply-templates/></p> 
	</xsl:template>

	<xsl:template match="Accept">
			<li><parmname>Accept: </parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

	<xsl:template match="ContentType">
			<li><parmname>Content-Type: </parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

	<xsl:template match="PathParam">
                <li><xsl:apply-templates/></li>
	</xsl:template>

	<xsl:template match="QueryParam">
                <li><xsl:apply-templates/></li>
	</xsl:template>

	<xsl:template match="PostDataXML">
            <xsl:apply-templates mode="copy"/>
	</xsl:template>

	<xsl:template match="PostDataJSON">
            <xsl:apply-templates/>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Response Definition	   -->
<!-- ============================================= -->
	<xsl:template match="RespSuccessXML">
                <li><parmname>XML:</parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

	<xsl:template match="RespSuccessJSON">
                <li><parmname>JSON:</parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

	<xsl:template match="RespFailedXML">
                <li><parmname>XML:</parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

	<xsl:template match="RespFailedJSON">
                <li><parmname>JSON:</parmname><codeph><xsl:apply-templates/></codeph></li>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Permissions					   -->
<!-- ============================================= -->
	<xsl:template match="Permissions">
		<section>
			<title>Permissions</title><p><xsl:apply-templates/></p>
		</section>
	</xsl:template>

<!-- ============================================= -->
<!-- 			API Example						   -->
<!-- ============================================= -->
	<xsl:template match="ExplRequest">
                <p><b>Request:</b> </p><xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ExplResponse">
                <p><b>Response:</b> </p><p><codeph><xsl:apply-templates/></codeph></p>
	</xsl:template>
</xsl:stylesheet>

