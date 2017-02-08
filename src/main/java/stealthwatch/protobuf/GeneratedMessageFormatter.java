package stealthwatch.protobuf;

import com.google.common.base.Joiner;
import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessage;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.Lists.newArrayList;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_BYTE_STRING_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_EPOC_MICROSECONDS_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_FIELD_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_IP_ADDRESS_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_MAC_ADDRESS_TO_STRING;

public class GeneratedMessageFormatter {

    private final ChainOfResponsibility<Entry<FieldDescriptor, Object>, String> formatters =
            new ChainOfResponsibility<>();

    public GeneratedMessageFormatter() {
        add(field -> field.getKey().getName().endsWith("_usec"), FROM_EPOC_MICROSECONDS_TO_STRING);
        add(field -> field.getValue() instanceof MacAddress, FROM_MAC_ADDRESS_TO_STRING);
        add(field -> field.getValue() instanceof IPAddress, FROM_IP_ADDRESS_TO_STRING);
        add(field -> field.getValue() instanceof ByteString, FROM_BYTE_STRING_TO_STRING);
        add(field -> field.getKey().toProto().getType() != Type.TYPE_MESSAGE, FROM_FIELD_TO_STRING);
    }

    private void add(Predicate<Entry<FieldDescriptor, Object>> predicate,
                     Function<Entry<FieldDescriptor, Object>, String> function) {
        formatters.add(predicate, function);
    }

    public String format(GeneratedMessage message) {
        return Joiner.on("|").join(format(newArrayList(message), newArrayList(""), newArrayList()));
    }

    private Iterable<String> format(List<GeneratedMessage> messages, List<String> prefixes,
                                    Collection<String> formattedFields) {
        if (messages.isEmpty()) {
            return formattedFields;
        }

        GeneratedMessage message = messages.remove(0);
        String prefix = prefixes.remove(0);

        for (Entry<FieldDescriptor, Object> field : message.getAllFields().entrySet()) {
            Optional<String> value = formatters.apply(field);
            if (value.isPresent()) {
                formattedFields.add(prefix + value.get());
            } else if (field.getKey().isRepeated()) {
                int index = 0;
                for (Object element : (List<?>) field.getValue()) {
                    messages.add((GeneratedMessage) element);
                    prefixes.add(prefix + field.getKey().getName() + '_' + index++ + '_');
                }
            } else {
                messages.add((GeneratedMessage) field.getValue());
                prefixes.add(prefix + field.getKey().getName() + '_');
            }
        }
        return format(messages, prefixes, formattedFields);
    }
}
