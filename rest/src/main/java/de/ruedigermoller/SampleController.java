package de.ruedigermoller;

import java.util.Collections;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

public class SampleController
{
	public SampleController()
	{
		super();
	}

	public Object create(Request request, Response response)
	{
		//TODO: Your 'POST' logic here...
		return null;
	}

	public Object read(Request request, Response response)
	{
		Object a = request.getHeader("a");
		Object b = request.getHeader("b");

		Integer ia = Integer.parseInt(a.toString());
		Integer ib = Integer.parseInt(b.toString());

		return ia + ib;
	}

	public List<Object> readAll(Request request, Response response)
	{
		//TODO: Your 'GET collection' logic here...
		return Collections.emptyList();
	}

	public void update(Request request, Response response)
	{
		//TODO: Your 'PUT' logic here...
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		//TODO: Your 'DELETE' logic here...
		response.setResponseNoContent();
	}
}
