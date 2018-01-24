//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import com.google.protobuf.DescriptorProtos.FieldDescriptorProto;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import stealthwatch.flowforwarder.client.FieldFormatter.FieldFormatterType;

/**
 * The Message Formatter.
 */
class MessageFormatter {

    /** The message to format. */
    private final GeneratedMessage message;

    /** The separator for each field. */
    private String separator;

    /** The prefix for each field. */
    private final String prefix;

    /** The field formatters filtered by name. */
    private Map<String, FieldFormatterType> nameFieldFormatters = new HashMap<>();

    /** The field formatters filtered by type. */
    private Map<String, FieldFormatterType> typeFieldFormatters = new HashMap<>();

    /** The suffix. */
    private String suffix;

    /**
     * Instantiates a new message formatter.
     *
     * @param <M>     the generic type
     * @param message the message
     */
    <M extends GeneratedMessage> MessageFormatter(M message) {
        this(message, "|");
    }

    /**
     * Instantiates a new message formatter.
     *
     * @param <M>       the generic type
     * @param message   the message
     * @param separator the separator
     */
    private <M extends GeneratedMessage> MessageFormatter(M message, String separator) {
        this(message, separator, null);
    }

    /**
     * Instantiates a new message formatter.
     *
     * @param <M>       the generic type
     * @param message   the message
     * @param separator the separator
     * @param prefix    the prefix
     */
    private <M extends GeneratedMessage> MessageFormatter(M message, String separator, String prefix) {
        this.message = message;
        this.separator = separator;
        this.prefix = prefix;
    }

    /**
     * Instantiates a new message formatter.
     *
     * @param <M>                 the generic type
     * @param message             the message
     * @param separator           the separator
     * @param prefix              the prefix
     * @param nameFieldFormatters the name field formatters
     * @param typeFieldFormatters the type field formatters
     * @param suffix              the suffix
     */
    private <M extends GeneratedMessage> MessageFormatter(M message, String separator, String prefix,
                                                          Map<String, FieldFormatterType> nameFieldFormatters,
                                                          Map<String, FieldFormatterType> typeFieldFormatters,
                                                          String suffix) {
        this.message = message;
        this.separator = separator;
        this.prefix = prefix;
        this.nameFieldFormatters = nameFieldFormatters;
        this.typeFieldFormatters = typeFieldFormatters;
        this.suffix = suffix;
    }

    /**
     * Adds the name formatter.
     *
     * @param name          the name
     * @param formatterType the formatter type
     */
    void addNameFormatter(String name, FieldFormatterType formatterType) {
        nameFieldFormatters.put(name, formatterType);
    }

    /**
     * Adds the type formatter.
     *
     * @param type          the type
     * @param formatterType the formatter type
     */
    void addTypeFormatter(String type, FieldFormatterType formatterType) {
        typeFieldFormatters.put(type, formatterType);
    }

    /**
     * Format.
     *
     * @return the string
     */
    String format() {
        StringBuilder result = new StringBuilder();
        for (Entry<FieldDescriptor, Object> entry : message.getAllFields().entrySet()) {
            if (result.length() > 0) {
                result.append(separator);
            }
            String name = String.format("%s%s%s",
                                        prefix == null ? "" : prefix + '-',
                                        entry.getKey().toProto().getName(),
                                        suffix == null ? "" : '-' + suffix);
            if (entry.getKey().toProto().getType() == Type.TYPE_MESSAGE) {
                formatNestedMessage(result, entry.getKey(), entry.getKey().toProto(), entry.getValue(), name);
            } else {
                formatSimpleField(result, entry.getValue(), name);
            }
        }
        return result.toString();
    }

    /**
     * Format simple field.
     *
     * @param result   the result
     * @param valueObj the value obj
     * @param name     the name
     */
    private void formatSimpleField(StringBuilder result, Object valueObj, String name) {
        FieldFormatterType type = nameFieldFormatters.get(name);
        String value = type == null ? valueObj.toString() : formatByType(type, valueObj);
        result.append(name).append("=").append(value);
    }

    /**
     * Format nested message.
     *
     * @param result   the result
     * @param field    the field
     * @param desc     the desc
     * @param valueObj the value obj
     * @param name     the name
     */
    private void formatNestedMessage(StringBuilder result, FieldDescriptor field, FieldDescriptorProto desc,
                                     Object valueObj, String name) {
        FieldFormatterType type = typeFieldFormatters.get(desc.getTypeName());
        if (type != null) {
            result.append(name).append("=").append(formatByType(type, valueObj));
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
     * @param result   the result
     * @param valueObj the value obj
     * @param name     the name
     */
    private void formatRepeatedObject(StringBuilder result, Object valueObj, String name) {
        int idx = 0;
        for (Object val : (Iterable<?>) valueObj) {
            MessageFormatter innerFormat = new MessageFormatter((GeneratedMessage) val, separator, name,
                                                                nameFieldFormatters, typeFieldFormatters,
                                                                String.valueOf(idx));
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
     * @param type the type
     * @param val  the val
     *
     * @return the string
     */
    private static String formatByType(FieldFormatterType type, Object val) {
        switch (type) {
        case IP_ADDRESS:
            IPAddress ip = (IPAddress) val;
            return FieldFormatter.formatIPAddress(ip.getA0(), ip.getA1());
        case MAC_ADDRESS:
            MacAddress mac = (MacAddress) val;
            return FieldFormatter.formatMacAddress(mac.getA());
        case DATE:
        default:
            Long microsecs = (Long) val;
            return FieldFormatter.formatDateFromMicroseconds(microsecs);
        }
    }
}
