
import java.io.*;
import java.net.*;

class fetchUrl
{
   private static String proxy_string;
   private static    int proxy_port;
   private static  Proxy proxy;

   private static int getopt(String args[])
   {
      int index;
      int num_args;
      int flags;

      fetchUrl.proxy_string = new String("");

      try
      {
         num_args = java.lang.reflect.Array.getLength(args);
      }
      catch (IllegalArgumentException e0)
      {
         System.out.println("Error: getopt.1");
         return -1;
      }

      if (num_args == 0)
      {
         System.out.println("Usage:");
         System.out.println("   fetchUrl [flags] [URL]");
         System.out.println("");
         System.out.println("Flags:");
         System.out.println("   --proxy [proxy] ..... URL to proxy server.");
         System.out.println("   --port  [port] ...... Port that proxy server uses.");
         return -1;
      }

      for (index = 0, flags = 0; index < java.lang.reflect.Array.getLength(args); index++)
      {
         if (flags != 0)
         {
            if (flags == 1)
            {
               fetchUrl.proxy_string = args[index];
            }

            if (flags == 2)
            {
               fetchUrl.proxy_port = java.lang.Integer.decode(args[index]);
            }

            flags = 0;
         }
         else if (args[index].equals("--proxy"))
         {
            flags = 1;
         }
         else if (args[index].equals("--port"))
         {
            flags = 2;
         }
         else
         {
            // it is a url.
         }
      }

      return num_args;
   }

   private static void setupProxy()
   {
      InetSocketAddress isa;

      if (fetchUrl.proxy_string.isEmpty() == false)
      {
         isa = new InetSocketAddress(fetchUrl.proxy_string, fetchUrl.proxy_port);
         fetchUrl.proxy = new Proxy(Proxy.Type.HTTP, isa);
      }
   }

   private static void fetchUrl(String url_string)
   {
      URL url;
      InputStream stream;
      byte data[] = { 0, 0, 0, 0, 0 };
      int len;

      try
      {
         url = new URL(url_string);
      }
      catch (MalformedURLException e1)
      {
         //System.out.printf("Error: could not open URL, %s", url_string);
         return;
      }

      try
      {
         if (fetchUrl.proxy_string.isEmpty() == false)
         {
            stream = url.openConnection(fetchUrl.proxy).getInputStream();
         }
         else
         {
            stream = url.openStream();
         }
      }
      catch (IOException e2)
      {
         System.out.println("Error: could not open stream");
         return;
      }

      for (;;)
      {
         try
         {
            len = stream.read(data,0,5);
         }
         catch (IOException e3)
         {
            System.out.println("Error: could not read stream");
            return;
         }

         if (len > 0)
         {
            System.out.write(data,0,len);
            System.out.flush();
         }
         else
         {
            break;
         }
      }
   }

   public static void main(String args[])
   {
      int index;
      int num_args;

      num_args = fetchUrl.getopt(args);

      if (num_args < 0)
      {
         return;
      }

      fetchUrl.setupProxy();

      for (index = 0; index < num_args; index++)
      {
         fetchUrl.fetchUrl(args[index]);
      }
   }
}
