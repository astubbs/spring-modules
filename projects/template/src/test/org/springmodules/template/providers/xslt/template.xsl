<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0">
    <xsl:template match="/">
        <xsl:value-of select="person/name"/>,<xsl:value-of select="person/age"/>
    </xsl:template>
</xsl:stylesheet>
