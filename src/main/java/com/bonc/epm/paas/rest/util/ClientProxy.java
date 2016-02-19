package com.bonc.epm.paas.rest.util;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class ClientProxy implements InvocationHandler
{
	private Map<Method, MethodInvoker> methodMap;
	private Class<?> clazz;

	public ClientProxy(Map<Method, MethodInvoker> methodMap)
	{
		super();
		this.methodMap = methodMap;
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public Object invoke(Object o, Method method, Object[] args)
           throws Throwable
   {

      MethodInvoker clientInvoker = methodMap.get(method);
      if (clientInvoker == null)
      {
         if (method.getName().equals("equals"))
         {
            return this.equals(o);
         }
         else if (method.getName().equals("hashCode"))
         {
            return this.hashCode();
         }
         else if (method.getName().equals("toString") && (args == null || args.length == 0))
         {
            return this.toString();
         }
      }

      if (clientInvoker == null)
      {
         throw new RuntimeException("Could not find a method for: " + method);
      }
      return clientInvoker.invoke(args);
   }

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof ClientProxy))
			return false;
		ClientProxy other = (ClientProxy) obj;
		if (other == this)
			return true;
		if (other.clazz != this.clazz)
			return false;
		return super.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return clazz.hashCode();
	}

	public String toString()
	{
		return "Resteasy Client Proxy for :" + clazz.getName();
	}
}
