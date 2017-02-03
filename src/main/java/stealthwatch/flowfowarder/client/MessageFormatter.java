package stealthwatch.flowfowarder.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;

import stealthwatch.flowfowarder.client.FieldFormatter.FieldFormatterType;

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

	/** The suffix. */
	private String suffix;

	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M>
	 *            the generic type
	 * @param message
	 *            the message
	 */
	public <M extends GeneratedMessage> MessageFormatter(M message) {
		this(message, null);
	}

	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M>
	 *            the generic type
	 * @param message
	 *            the message
	 * @param separator
	 *            the separator
	 */
	public <M extends GeneratedMessage> MessageFormatter(M message, String separator) {
		this(message, separator, null);
	}

	/**
	 * Instantiates a new message formatter.
	 *
	 * @param <M>
	 *            the generic type
	 * @param message
	 *            the message
	 * @param separator
	 *            the separator
	 * @param prefix
	 *            the prefix
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
	 * @param <M>
	 *            the generic type
	 * @param message
	 *            the message
	 * @param separator
	 *            the separator
	 * @param prefix
	 *            the prefix
	 * @param nameFieldFormatters
	 *            the name field formatters
	 * @param typeFieldFormatters
	 *            the type field formatters
	 * @param suffix
	 *            the suffix
	 */
	private <M extends GeneratedMessage> MessageFormatter(M message, String separator, String prefix,
			Map<String, FieldFormatterType> nameFieldFormatters, Map<String, FieldFormatterType> typeFieldFormatters,
			String suffix) {
		super();
		this.message = message;
		setSeparator(separator);
		setPrefix(prefix);
		this.nameFieldFormatters = nameFieldFormatters;
		this.typeFieldFormatters = typeFieldFormatters;
		this.suffix = suffix;
	}

	/**
	 * Sets the separator.
	 *
	 * @param separator
	 *            the new separator
	 */
	public void setSeparator(String separator) {
		if (separator != null) {
			this.separator = separator;
		}
	}

	/**
	 * Sets the prefix.
	 *
	 * @param prefix
	 *            the new prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Adds the name formatter.
	 *
	 * @param name
	 *            the name
	 * @param formatterType
	 *            the formatter type
	 */
	public void addNameFormatter(String name, FieldFormatterType formatterType) {
		nameFieldFormatters.put(name, formatterType);
	}

	/**
	 * Adds the type formatter.
	 *
	 * @param type
	 *            the type
	 * @param formatterType
	 *            the formatter type
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
			FieldDescriptor field = entry.getKey();
			FieldDescriptorProto desc = entry.getKey().toProto();
			Object valueObj = entry.getValue();
			String name = (prefix == null ? "" : prefix + "-") + desc.getName() + (suffix == null ? "" : "-" + suffix);
			if (result.length() > 0) {
				result.append(separator);
			}
			if (desc.getType() != Type.TYPE_MESSAGE) {
				formatSimpleField(result, valueObj, name);
			} else {
				formatNestedMessage(result, field, desc, valueObj, name);
			}

		}
		return result.toString();
	}

	/**
	 * Format simple field.
	 *
	 * @param result
	 *            the result
	 * @param valueObj
	 *            the value obj
	 * @param name
	 *            the name
	 */
	private void formatSimpleField(StringBuffer result, Object valueObj, String name) {
		String value = null;
		FieldFormatterType type = nameFieldFormatters.get(name);
		if (type != null) {
			value = formatByType(type, valueObj);
		} else {
			value = valueObj.toString();
		}
		result.append(name).append(EQUALS).append(value);
	}

	/**
	 * Format nested message.
	 *
	 * @param result
	 *            the result
	 * @param field
	 *            the field
	 * @param desc
	 *            the desc
	 * @param valueObj
	 *            the value obj
	 * @param name
	 *            the name
	 */
	private void formatNestedMessage(StringBuffer result, FieldDescriptor field, FieldDescriptorProto desc,
			Object valueObj, String name) {
		FieldFormatterType type = typeFieldFormatters.get(desc.getTypeName());
		if (type != null) {
			result.append(name).append(EQUALS).append(formatByType(type, valueObj));
		} else {
			if (field.isRepeated()) {
				formatRepeatedObject(result, valueObj, name);
			} else {
				MessageFormatter innerFormat = new MessageFormatter((GeneratedMessage) valueObj, separator, name,
						nameFieldFormatters, typeFieldFormatters, null);
				result.append(innerFormat.format());
			}
		}
	}

	/**
	 * Format repeated object.
	 *
	 * @param result
	 *            the result
	 * @param valueObj
	 *            the value obj
	 * @param name
	 *            the name
	 */
	private void formatRepeatedObject(StringBuffer result, Object valueObj, String name) {
		int idx = 0;
		for (Object val : ((Iterable<?>) valueObj)) {
			MessageFormatter innerFormat = new MessageFormatter((GeneratedMessage) val, separator, name,
					nameFieldFormatters, typeFieldFormatters, "" + idx);
			if (idx > 0) {
				result.append(separator);
			}
			result.append(innerFormat.format());
			idx++;
		}
	}

	/**
	 * Format by type.
	 *
	 * @param type
	 *            the type
	 * @param val
	 *            the val
	 * @return the string
	 */
	private String formatByType(FieldFormatterType type, Object val) {
		String result = null;
		switch (type) {
		case IP:
			IPAddress ip = (IPAddress) val;
			result = FieldFormatter.formatIPAddress(ip.getA0(), ip.getA1());
			break;
		case MAC:
			MacAddress mac = (MacAddress) val;
			result = FieldFormatter.formatMacAddress(mac.getA());
			break;
		default: // DATE
			Long microsecs = (Long) val;
			result = FieldFormatter.formatDateFromMicroseconds(microsecs);
			break;
		}
		return result;
	}

}
