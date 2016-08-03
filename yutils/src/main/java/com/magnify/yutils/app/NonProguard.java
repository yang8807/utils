package com.magnify.yutils.app;

/**
 * The class of the interface implementation code confusion will not be involved<br>
 * Usually used for class using reflection, to implement the interface will not be change name
 * will not be deleted without direct reference to methods and properties<br>
 * <br>
 * In the proguard configuration file add the following statement to achieve<br>
 * -keep interface com.eelly.lib.app.NonProguard <br>
 * -keep class * extends com.eelly.lib.app.NonProguard { *; }
 */
public interface NonProguard {

}
