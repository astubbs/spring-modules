<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document    : ivy2pom.xsl
    Created on  : 14 July 2006, 18:48
    Author      : Sergio Bossa (sergio.bossa@gmail.com)
    Description : Transforms IVY files into Maven2 POM files. After transformation, verify and fix dependencies as required by your project. 
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd" version="1.0">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:template match="/">
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
            <modelVersion>4.0.0</modelVersion>
            <groupId>org.springmodules</groupId>
            <artifactId><xsl:value-of select="/ivy-module/info/@module"/></artifactId>
            <packaging>jar</packaging>
            <version>0.5</version>
            <name><xsl:value-of select="/ivy-module/info/@module"/></name>
            <url>https://springmodules.dev.java.net/</url>
            <dependencies>
              <xsl:apply-templates/>
            </dependencies>
        </project>
    </xsl:template>
    
    <xsl:template match="/ivy-module/dependencies/dependency[count(artifact) = 0]">
        <dependency>
            <groupId><xsl:value-of select="@name"/></groupId>
            <artifactId><xsl:value-of select="@name"/></artifactId>
            <version><xsl:value-of select="@rev"/></version>
            <xsl:if test="starts-with(@conf, 'test')">
                <scope>test</scope>
            </xsl:if>
            <xsl:if test="starts-with(@conf, 'buildtime')">
                <scope>provided</scope>
            </xsl:if>
        </dependency>
    </xsl:template>
    
    <xsl:template match="/ivy-module/dependencies/dependency/artifact">
        <dependency>
            <groupId><xsl:value-of select="../@name"/></groupId>
            <artifactId><xsl:value-of select="@name"/></artifactId>
            <version><xsl:value-of select="../@rev"/></version>
            <xsl:if test="starts-with(@conf, 'test')">
                <scope>test</scope>
            </xsl:if>
            <xsl:if test="starts-with(@conf, 'buildtime')">
                <scope>provided</scope>
            </xsl:if>
        </dependency>
    </xsl:template>

</xsl:stylesheet>
