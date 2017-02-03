package stealthwatch.flowfowarder.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.protobuf.GeneratedMessage;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;

import stealthwatch.flowfowarder.client.FieldFormatter.FieldFormatterType;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;

/**
 * The Message Formatter.
 */
public class MessageFormatter {

	/** The Constant EQUALS sign. */
	private static final String EQUALS = "=";

	/** The message to format. */
	private GeneratedMessage message;
	
	/** The separator for each field. */
	private String separator = "|";

	/** The prefix for each field. */
	private String prefix;

	/** The field formatters filtered by name. */
	private Map<String, FieldFormatterType> nameFieldFormatters = new HashMap<String, FieldFormatterType>();
	
	/** The field formatters filtered by type. */
	private Map<String, FieldFormatterType> typeFieldFormatters = new HashMap<String, FieldFormatterType>();
	
	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M> the generic type
	 * @param message the message
	 */
	public <M extends GeneratedMessage> MessageFormatter(M message) {
		this(message, null);
	}

	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M> the generic type
	 * @param message the message
	 * @param separator the separator
	 */
	public <M extends GeneratedMessage> MessageFormatter(M message, String separator) {
		this(message, separator, null);
	}

	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M> the generic type
	 * @param message the message
	 * @param separator the separator
	 * @param prefix the prefix
	 */
	public <M extends GeneratedMessage> MessageFormatter(M message, String separator, String prefix) {
		super();
		this.message = message;
		setSeparator(separator);
		setPrefix(prefix);
	}
	
	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M> the generic type
	 * @param message the message
	 * @param separator the separator
	 * @param prefix the prefix
	 * @param nameFieldFormatters the name field formatters
	 * @param typeFieldFormatters the type field formatters
	 */
	private <M extends GeneratedMessage> MessageFormatter(M message, String separator, String prefix, Map<String, FieldFormatterType> nameFieldFormatters, Map<String, FieldFormatterType> typeFieldFormatters) {
		super();
		this.message = message;
		setSeparator(separator);
		setPrefix(prefix);
		this.nameFieldFormatters = nameFieldFormatters;
		this.typeFieldFormatters = typeFieldFormatters;
	}

	/**
	 * Sets the separator.
	 *
	 * @param separator the new separator
	 */
	public void setSeparator(String separator) {
		if (separator != null) {
			this.separator = separator;
		}
	}

	/**
	 * Sets the prefix.
	 *
	 * @param prefix the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Adds the name formatter.
	 *
	 * @param name the name
	 * @param formatterType the formatter type
	 */
	public void addNameFormatter(String name, FieldFormatterType formatterType) {
		nameFieldFormatters.put(name, formatterType);
	}
	
	/**
	 * Adds the type formatter.
	 *
	 * @param type the type
	 * @param formatterType the formatter type
	 */
	public void addTypeFormatter(String type, FieldFormatterType formatterType) {
		typeFieldFormatters.put(type, formatterType);
	}

	/**
	 * Format.
	 *
	 * @return the string
	 */
	public String format() {
		StringBuffer result = new StringBuffer("");
    	Map<FieldDescriptor, Object> fields = message.getAllFields();
    	for (Entry<FieldDescriptor, Object> entry : fields.entrySet()) {
    		FieldDescriptorProto desc = entry.getKey().toProto();
    		Object valueObj = entry.getValue();
    		String name = (prefix == null ? "" : prefix + "-") + desc.getName();
    		if (result.length() > 0) {
    			result.append(separator);
    		}
    		if (desc.getType() != Type.TYPE_MESSAGE) {
    			String value = null;
    			FieldFormatterType type = nameFieldFormatters.get(name);
    			if (type != null) {
    				value = formatByType(type, valueObj);
    			} else {
    				value = valueObj.toString();
    			}
    			result.append(name).append(EQUALS).append(value);
    		} else {
    			FieldFormatterType type = typeFieldFormatters.get(desc.getTypeName());
    			if (type != null) {
    				result.append(name).append(EQUALS).append(formatByType(type, valueObj));
    			} else {
    				MessageFormatter innerFormat = new MessageFormatter((GeneratedMessage) valueObj, separator, name, nameFieldFormatters, typeFieldFormatters);
    				result.append(innerFormat.format());
    			}
    		}
    		
    	}
		return result.toString();
	}

	/**
	 * Format by type.
	 *
	 * @param type the type
	 * @param val the val
	 * @return the string
	 */
	private String formatByType(FieldFormatterType type, Object val) {
		// TODO to test
		String result = null;
		switch (type) {
			case IP :
				IPAddress ip = (IPAddress) val;
				result = FieldFormatter.formatIPAddress(ip.getA0(), ip.getA1());
				break;
			case MAC :
				MacAddress mac = (MacAddress) val;
				result = FieldFormatter.formatMacAddress(mac.getA());
				break;
			default : // DATE
				Long microsecs = (Long) val;
				result = FieldFormatter.formatDateFromMicroseconds(microsecs);
				break;
		}
		return result;
	}

}
