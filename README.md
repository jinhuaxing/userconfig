An utility component which reads the configurable parameters (or properties) from database, and injects the values to objects (POJO) that define some of their parameters are configurable, through annotations.
=============================
Here is an example:
<pre><code>
class A {
	  private int count;

	  @ParamFromDb(value="countForA", description="this is a count", defaultValue="5")
	  public void setCount(int count) { this.count = count; }
	  
	}
}
...
A a = new A();
configService.configure(a); // The value of count defined in database would be injected.
</code></pre>

Descriptive information about those parameters could be provided through annotations, 
so we could present configurable parameters in GUI in an information rich way to users.

It also supports to save parameters to database.
