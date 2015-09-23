# How set up a Zildo workspace #

I assumed that you're using Eclipse for developing Zildo, and you're a little bit familiar with.

So this is the steps you should follow:

  1. Install subclipse
> > Find the right one on this page
> > http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA
  1. Check out zildo projects with this URL : (see note at the bottom)
> > https://zildo.googlecode.com/svn/trunk
  1. Create a run configuration on 'zildo.Zildo' class.
> > Put these lines in the VM arguments (Arguments tab):<br />
> > `-Djava.library.path="c:\Code\Projets java\lwjgl-2.8.2\native\windows" -Xmx80M -Dsun.java2d.noddraw=false`<br /><br />
> > Where you should replace the path with yours, indeed.
  1. Edit the `Constantes.java` class at line 34 :<br />
> > `public static String DATA_PATH = "C:\\Data\\";`<br />
> > And replace the path by the one where you store the Zildo's data folder.
> > If you haven't any one, please download the latest Zildoxxx.jar at the [downloads section](http://code.google.com/p/zildo/downloads/list).
  1. F11 or CTRL+F11 to debug/run, and you're up to work on this amazing project !
<p /><p />

Note: it's not mandatory to check out all projects. But the less you need is :
  * **zildo** : the core engine
  * **zildo-platform-lwjgl** : the platform-dependent part, specific for "computers" : Windows/MacOS/Linux

And after that, the other ones are optional :
  * **zeditor** : the map editor
  * **zildo-platform-android-gles2** : specifically for phones using Android