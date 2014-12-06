package org.virtual.ows.common;

import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static java.net.URI.*;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Utils {

	//parameter validation
	
	public static void verify(String msg, boolean expression) {
	
		if (!expression)
			throw new IllegalArgumentException(msg);
	}
	
	
	public static void notNull(String name, Object o) throws IllegalArgumentException {
		
		verify(name+" is null",o!=null);
	
	}
	
	//strings 
	
	public static void notEmpty(String name, String s) throws IllegalArgumentException {

		notNull(name, s);
		verify(name+" is empty", !s.isEmpty());
	
	}

	public static void validUri(String msg, String uri) throws IllegalArgumentException {

		try {
			
			URI o = create(uri);
			
			if (!o.isAbsolute())
				throw new Exception("not an absolute URI");
		}
		catch(Exception e) {
			throw new IllegalArgumentException(msg,e);
		}
	
	}
	
	public static void validUris(String name, List<String> uris) throws IllegalArgumentException {

		notNull(name, uris);
		for (String uri : uris)
			validUri(name, uri);
	
	}
	
	
	public static void valid(String name, String s) throws IllegalArgumentException {

		notNull(name, s);
		notEmpty(name,s);
	
	}
	
	//numbers
	
	public static void positive(String name, int val) throws IllegalArgumentException {
	
		verify(name+" is not positive",val>0);
	
	}
	
	
	//collections
	
	public static void notEmpty(String name, Collection<? extends Object> c) throws IllegalArgumentException {

		notNull(name, c);
		verify(name+" is empty", !c.isEmpty());
	}
	
	public static void notEmpty(String name, Object[] a) throws IllegalArgumentException {
		
		notNull(name,a);
		verify(name+" is empty",a.length>0);
	}

	public static void valid(String name, Object[] a) throws IllegalArgumentException {
		
		notNull(name,a);
		
		for (Object e : a)
			notNull(name+"'s element", e);
	}

	public static void noNulls(String name, Collection<?> c) throws IllegalArgumentException {

		notNull(name, c);
		notEmpty(name,c);
		
		for (Object e : c)
			notNull(name+"'s element", e);
	}
	
	public static void valid(String name, Collection<String> c) throws IllegalArgumentException {

		notNull(name, c);
		notEmpty(name,c);
		
		for (String e : c)
			valid(name+"'s element", e);
	}
	
	
	//files
	public static void valid(File file) throws IllegalArgumentException {
	
		notNull("file", file);
		verify(file+" does not exist, is a directory, or cannot be read",file.exists() && !file.isDirectory() && file.canRead());
	}
	
	public static void validDirectory(File dir) throws IllegalArgumentException {
		
		notNull("directory", dir);
		verify(dir+" does not exist or cannot be read",dir.exists() && dir.canRead());
	}
	
	public static boolean isValid(File file) {
		
		notNull("file", file);
		return file.exists() && !file.isDirectory() && file.canRead();
	}
	
	public static boolean isValidDirectory(File dir) {
		
		notNull("directory", dir);
		return dir.exists() && dir.canRead();
	}
	
	//emails
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
 	public static void validEmail(String email) {
 		
 		verify("invalid email address "+email,pattern.matcher(email).matches());
	
 	}
	
	
	// other
	
	public static void valid(String text,QName name) throws IllegalArgumentException {
		notNull(text, name);
		valid(text,name.getLocalPart());
	}
	
	
	
	//casts
	
	public static <T> T reveal(Object o, Class<T> type) {
		
		notNull("object", o);
		
		try {
			return type.cast(o);
		}
		catch(ClassCastException e) {
			throw new AssertionError("expected a "+type+ ", found instead a "+o.getClass());
		}
	}
	
	
	//faults

		public static RuntimeException unchecked(Throwable t) {
		
			
			return 
					t instanceof RuntimeException? RuntimeException.class.cast(t) :
					new RuntimeException(t);
			}
	
		public static RuntimeException unchecked(String msg, Throwable t) {
			
			msg = msg + " (see cause) ";
			return 
					t instanceof IllegalArgumentException? new IllegalArgumentException(msg,t) :
					t instanceof IllegalStateException ? new IllegalStateException(msg,t) :
					new RuntimeException(msg,t);
		}
		
		public static void rethrowUnchecked(String msg,Throwable t) throws RuntimeException {
			
			throw unchecked(msg,t);

		}
		
		public static void rethrowUnchecked(Throwable t) throws RuntimeException {
			
			throw unchecked(t);

		}
		
		static JsonNodeFactory factory = JsonNodeFactory.instance;
		static ObjectMapper mapper = new ObjectMapper();
		
		static {
			
			mapper.enable(INDENT_OUTPUT);
		}
		
		public static ObjectNode toJson(Object o) {
			
			return  toJson(o,false);
			
		}
		
		public static ObjectNode toJson(Object o, boolean print) {
			
			ObjectNode node =   (ObjectNode) mapper.valueToTree(o);
			
			if (print)
				try {
					System.out.println(mapper.writeValueAsString(node));
				}
				catch(Exception e) {
					throw unchecked(e);
				}
			
			return node;
		}
		
		public static <T> T fromJson(JsonNode node, Class<T> type) {
			
			try {
				return  mapper.treeToValue(node, type);
			}
			catch(Exception e) {
				throw unchecked("cannot parse json",e);
			}
			
		}
		
		public static <T> T jsonRoundtrip(T o) {
			
			return jsonRoundtrip(o,false);
			
		}

		@SuppressWarnings("unchecked")
		public static <T> T jsonRoundtrip(T o, boolean print) {
			
			return fromJson(toJson(o,print), (Class<T>) o.getClass());
			
		}
}
