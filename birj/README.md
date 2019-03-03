### sources and javadoc gradle or eclipse bug
The sources or javadocs aren't included even you refresh the project so you'll have to 
1- Open and close eclipse
2- Or add the following code in the build.gradle
[Solution @SOF](http://stackoverflow.com/questions/12836089/why-is-eclipse-not-attaching-3rd-party-libs-source-files-to-a-wtp-faceted-gradle)
```
eclipse.classpath.file {
    // Classpath entry for Eclipse which changes the order of classpathentries; 
    // otherwise no sources for 3rd party jars are shown
    
    withXml { xml ->
        def node = xml.asNode()
        node.remove( node.find { it.@path == 'org.eclipse.jst.j2ee.internal.web.container' } )
        node.appendNode( 'classpathentry', [ kind: 'con', path: 'org.eclipse.jst.j2ee.internal.web.container', exported: 'true'])
    }
}
```