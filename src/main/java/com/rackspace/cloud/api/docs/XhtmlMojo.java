package com.rackspace.cloud.api.docs;

import com.rackspace.cloud.api.docs.FileUtils;
import com.agilejava.docbkx.maven.AbstractHtmlMojo;
import com.agilejava.docbkx.maven.PreprocessingFilter;
import com.agilejava.docbkx.maven.TransformerBuilder;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public abstract class XhtmlMojo extends AbstractHtmlMojo {

    private File xslDirectory;

    /**
     * @parameter expression="${project.build.directory}"
     */
    private String projectBuildDirectory;

    /**
     * 
     * @parameter 
     *     expression="${generate-html.failOnValidationError}"
     *     default-value="yes"
     */
    private String failOnValidationError;
    
    /**
     * A parameter used to specify the security level (external, internal, reviewer, writeronly) of the document.
     *
     * @parameter 
     *     expression="${generate-html.security}" 
     *     default-value=""
     */
    private String security;
    
    
    
        /**
     * Controls whether output is colorized based on revisionflag attributes.
     *
     * @parameter expression="${generate-webhelp.show.changebars}"
     */
    private String showChangebars;
    
     /**
     * Display built for OpenStack logo?
     *
     * @parameter expression="${generate-webhelp.builtForOpenStack}" default-value="0"
     */
    private String builtForOpenStack;

    /**
     * Controls whether output is colorized based on revisionflag attributes.
     *
     * @parameter expression="${generate-webhelp.meta.robots}" 
     */
    private String metaRobots;

    /**
     * Controls whether the version string is used as part of the Disqus identifier.
     *
     * @parameter expression="${generate-webhelp.use.version.for.disqus}" default-value="0"
     */
    private String useVersionForDisqus;

    /**
     * Controls whether the disqus identifier is used.
     *
     * @parameter expression="${generate-webhelp.use.disqus.id}" default-value="1"
     */
    private String useDisqusId;

    /**
     * Controls the branding of the output.
     *
     * @parameter expression="${generate-webhelp.branding}" default-value="rackspace"
     */
    private String branding;

    /**
     * Controls whether Disqus comments appear at the bottom of each page.
     *
     * @parameter expression="${generate-webhelp.enable.disqus}" default-value="0"
     */
    private String enableDisqus;

    /**
     * A parameter used by the Disqus comments.
     *
     * @parameter expression="${generate-webhelp.disqus.shortname}" default-value=""
     */
    private String disqusShortname;

    /**
     * A parameter used to control whether to include Google Analytics goo.
     *
     * @parameter expression="${generate-webhelp.enable.google.analytics}" default-value=""
     */
    private String enableGoogleAnalytics;

    /**
     * A parameter used to control whether to include Google Analytics goo.
     *
     * @parameter expression="${generate-webhelp.google.analytics.id}" default-value=""
     */
    private String googleAnalyticsId;

    /**
     * A parameter used to specify the path to the pdf for download in webhelp.
     *
     * @parameter expression="${generate-webhelp.pdf.url}" default-value=""
     */
    private String pdfUrl;

    /**
     * @parameter 
     *     expression="${generate-webhelp.canonicalUrlBase}"
     *     default-value=""
     */
    private String canonicalUrlBase;
 
    /**
     * 
     *
     * @parameter expression="${basedir}"
     */
    private File baseDir;

    /**
     * A parameter used to specify the presence of extensions metadata.
     *
     * @parameter 
     *     expression="${generate-webhelp.includes}" 
     *     default-value=""
     */
    private String transformDir;   
    
    /**
     * A parameter used to configure how many elements to trim from the URI in the documentation for a wadl method.
     *
     * @parameter expression="${generate-webhelp.trim.wadl.uri.count}" default-value=""
     */
    private String trimWadlUriCount;

    /**
     * Controls how the path to the wadl is calculated. If 0 or not set, then
     * The xslts look for the normalized wadl in /generated-resources/xml/xslt/.
     * Otherwise, in /generated-resources/xml/xslt/path/to/docbook-src, e.g.
     * /generated-resources/xml/xslt/src/docbkx/foo.wadl
     *
     * @parameter expression="${generate-webhelp.compute.wadl.path.from.docbook.path}" default-value="0"
     */
    private String computeWadlPathFromDocbookPath;

     /**
      * Sets the email for TildeHash (internal) comments. Note that this
      * doesn't affect Disqus comments.
      *
      * @parameter expression="${generate-webhelp.feedback.email}" default-value=""
      */
    private String feedbackEmail;

     /**
      * Controls whether or not the social icons are displayed.
      *
      * @parameter expression="${generate-webhelp.social.icons}" default-value="0"
      */
    private String socialIcons;
    /**
     * A parameter used to specify the path to the lega notice in webhelp.
     *
     * @parameter expression="${generate-webhelp.legal.notice.url}" default-value="index.html"
     */
    private String legalNoticeUrl;
    
    
    
    
    
    
    

    protected TransformerBuilder createTransformerBuilder(URIResolver resolver) {
        return super.createTransformerBuilder (new DocBookResolver (resolver, getType()));
    }

    protected String getNonDefaultStylesheetLocation() {
	// Is this even used?
        return "cloud/war/copy.xsl";
    }

    protected void setXslDirectory (File xslDirectory) {
        this.xslDirectory = xslDirectory;
    }

    protected File getXslDirectory() {
        return this.xslDirectory;
    }

    public void postProcessResult(File result) throws MojoExecutionException {
	
	super.postProcessResult(result);
	
	//final File targetDirectory = result.getParentFile();
	// com.rackspace.cloud.api.docs.FileUtils.extractJaredDirectory("apiref",ApiRefMojo.class,targetDirectory);
	String warBasename = result.getName().substring(0, result.getName().lastIndexOf('.'));

	// Zip up the war from here.
	String sourceDir = result.getParentFile() + "/" + warBasename;
	String zipFile = result.getParentFile()  + "/" + warBasename + ".war";
	result.deleteOnExit();

	try
	    {
		//create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(zipFile);
                                 
		//create object of ZipOutputStream from FileOutputStream
		ZipOutputStream zout = new ZipOutputStream(fout);
                       
		//create File object from source directory
		File fileSource = new File(sourceDir);
                       
		FileUtils.addDirectory(zout, fileSource);
                       
		//close the ZipOutputStream
		zout.close();
                       
		System.out.println("Zip file has been created!");
                       
	    }
	catch(IOException ioe)
	    {
		System.out.println("IOException :" + ioe);     
	    }

    }

    public void preProcess() throws MojoExecutionException {
        super.preProcess();

        final File targetDirectory = getTargetDirectory();
        File xslParentDirectory  = targetDirectory.getParentFile();

        if (!targetDirectory.exists()) {
            FileUtils.mkdir(targetDirectory);
        }

        //
        // Extract all images into the image directory.
        //
        FileUtils.extractJaredDirectory("cloud/war",PDFMojo.class,xslParentDirectory);
        setXslDirectory (new File (xslParentDirectory, "xsls"));

        //
        // Extract all fonts into fonts directory
        //
        //FileUtils.extractJaredDirectory("fonts",PDFMojo.class,imageParentDirectory);
    }



    @Override
    protected Source createSource(String inputFilename, File sourceFile, PreprocessingFilter filter)
            throws MojoExecutionException {

        String pathToPipelineFile = "classpath:/war.xpl"; //use "classpath:/path" for this to work
        Source source = super.createSource(inputFilename, sourceFile, filter);

        Map map=new HashMap<String, String>();
        
        map.put("failOnValidationError", failOnValidationError);
        map.put("transform.dir", transformDir);        
             
    if(feedbackEmail != null){
      map.put("feedback.email", feedbackEmail);
    }
    if(useDisqusId != null){
	   map.put("use.disqus.id", useDisqusId);
    }
    if (useVersionForDisqus != null) {
       map.put("use.version.for.disqus", useVersionForDisqus);
    }
        map.put("project.build.directory", projectBuildDirectory);
        map.put("branding", branding);
        map.put("builtForOpenStack", builtForOpenStack);
        map.put("enable.disqus", enableDisqus);
        if (disqusShortname != null) {
            map.put("disqus.shortname", disqusShortname);
        }
        if (enableGoogleAnalytics != null) {
            map.put("enable.google.analytics", enableGoogleAnalytics);
        }
        if (googleAnalyticsId != null) {
            map.put("google.analytics.id", googleAnalyticsId);
        }
        if (pdfUrl != null) {
            map.put("pdf.url", pdfUrl);
        }
        if (legalNoticeUrl != null) {
            map.put("legal.notice.url", legalNoticeUrl);
        }

    if(canonicalUrlBase != null){
	map.put("canonical.url.base",canonicalUrlBase);
    }

    if(security != null){
	map.put("security",security);
    }
   if(showChangebars != null){
	map.put("show.changebars",showChangebars);
    }
   if(metaRobots != null){
	map.put("meta.robots",metaRobots);
    }
   if(trimWadlUriCount != null){
	map.put("trim.wadl.uri.count",trimWadlUriCount);
    }

	map.put("social.icons",socialIcons);

//   sourceDocBook = new File(sourceFilename);
//   sourceDirectory = sourceDocBook.getParentFile();
//   map.put("docbook.infile",sourceDocBook.getAbsolutePath());
//   map.put("source.directory",sourceDirectory);
           
        
        int lastSlash=inputFilename.lastIndexOf("/");

        //This is the case if the path includes a relative path
        if(-1!=lastSlash){
        	String theFileName=inputFilename.substring(lastSlash);
        	String theDirName=inputFilename.substring(0,lastSlash);
            
        	int index = theFileName.indexOf('.');
        	if(-1!=index){
            	String targetDir="target/docbkx/xhtml/"+theDirName+theFileName.substring(0,index) + "/";

            	map.put("base.dir", targetDir);        		
        	}
        	else{
        		//getLog().info("~~~~~~~~theFileName file has incompatible format: "+theFileName);
        	}

        }
        //This is the case when it's just a file name with no path information
        else{
        	String theFileName=inputFilename;
        	int index = theFileName.indexOf('.');
        	if(-1!=index){
            	String targetDir="target/docbkx/xhtml/"+theFileName.substring(0,index) + "/";
            	map.put("base.dir", targetDir);        		
        	}
        	else{
        		//getLog().info("~~~~~~~~inputFilename file has incompatible format: "+inputFilename);
        	}
        }


        return CalabashHelper.createSource(source, pathToPipelineFile, map);
    }
}