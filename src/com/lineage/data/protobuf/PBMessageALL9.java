package com.lineage.data.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.UnknownFieldSet;

/**
 * Srwh的PBMessageALL9
 * @author Srwh
 */
public final class PBMessageALL9 {
	private static Descriptors.Descriptor internal_static_com_lineage_data_protobuf_typeInvList_descriptor;
	private static GeneratedMessage.FieldAccessorTable internal_static_com_lineage_data_protobuf_typeInvList_fieldAccessorTable;
	private static Descriptors.Descriptor internal_static_com_lineage_data_protobuf_typeVersion_descriptor;
	private static GeneratedMessage.FieldAccessorTable internal_static_com_lineage_data_protobuf_typeVersion_fieldAccessorTable;
	private static Descriptors.Descriptor internal_static_com_lineage_data_protobuf_typeRank_descriptor;
	private static GeneratedMessage.FieldAccessorTable internal_static_com_lineage_data_protobuf_typeRank_fieldAccessorTable;
	private static Descriptors.Descriptor internal_static_com_lineage_data_protobuf_type31_descriptor;
	private static GeneratedMessage.FieldAccessorTable internal_static_com_lineage_data_protobuf_type31_fieldAccessorTable;
	private static Descriptors.Descriptor internal_static_com_lineage_data_protobuf_type2_descriptor;
	private static GeneratedMessage.FieldAccessorTable internal_static_com_lineage_data_protobuf_type2_fieldAccessorTable;
	private static Descriptors.FileDescriptor descriptor;

	public static void registerAllExtensions(ExtensionRegistry registry) {
	}

	public static Descriptors.FileDescriptor getDescriptor() {
		return descriptor;
	}

	static {
		String[] descriptorData = {
				"\n\023PBMessageALL9.proto\022\031com.lineage.data.protobuf\"U\002\n\013typeInvList\022\017\n\007value_1\030\001 \001(\005\022\017\n\007value_2\030\002 \001(\005\022\017\n\007value_3\030\003 \001(\005\022\017\n\007value_4\030\004 \001(\005\022\017\n\007value_5\030\005 \001(\005\022\017\n\007value_6\030\006 \001(\005\022\017\n\007value_7\030\007 \001(\005\022\017\n\007value_8\030\b \001(\005\022\017\n\007value_9\030\t \001(\005\022\020\n\bvalue_10\030\n \001(\005\022\020\n\bvalue_11\030\013 \001(\005\022\020\n\bvalue_12\030\f \001(\005\022\020\n\bvalue_13\030\r \001(\005\022\020\n\bvalue_14\030\016 \001(\005\022\020\n\bvalue_15\030\017 \001(\005\022\020\n\bvalue_16\030\020 \001(\005\022\020\n\bvalue_17\030\021 \001(\005\022\020\n\barray_18\030\022 \001(\f\022\020\n\barray_19\030\023 \001(\f\"U\002",
				"\n\013typeVersion\022\017\n\007value_1\030\001 \001(\005\022\017\n\007value_2\030\002 \001(\005\022\017\n\007value_3\030\003 \001(\005\022\017\n\007value_4\030\004 \001(\005\022\017\n\007value_5\030\005 \001(\005\022\017\n\007value_6\030\006 \001(\005\022\017\n\007value_7\030\007 \001(\005\022\017\n\007value_8\030\b \001(\005\022\017\n\007value_9\030\t \001(\005\022\020\n\bvalue_10\030\n \001(\005\022\020\n\bvalue_11\030\013 \001(\005\022\020\n\bvalue_12\030\f \001(\005\022\020\n\bvalue_13\030\r \001(\005\022\020\n\bvalue_14\030\016 \001(\005\022\020\n\bvalue_15\030\017 \001(\005\022\020\n\bvalue_16\030\020 \001(\005\022\020\n\bvalue_17\030\021 \001(\005\022\020\n\bvalue_18\030\022 \001(\005\022\020\n\bvalue_19\030\023 \001(\005\"￡\001\n\btypeRank\022\017\n\007value_1\030\001 \001(\005\022\017\n\007value_2\030\002 \001(\005\022\017\n\007val",
				"ue_3\030\003 \001(\005\022\017\n\007value_4\030\004 \001(\005\022\017\n\007value_5\030\005 \001(\005\022\017\n\007array_6\030\006 \003(\f\022\017\n\007value_7\030\007 \001(\005\022\017\n\007value_8\030\b \001(\005\022\017\n\007value_9\030\t \001(\005\"!\001\n\006type31\022\017\n\007value_1\030\001 \001(\005\022\017\n\007value_2\030\002 \001(\005\022\017\n\007value_3\030\003 \001(\005\022\017\n\007value_4\030\004 \001(\005\022\017\n\007array_5\030\005 \001(\f\022\017\n\007array_6\030\006 \003(\f\022\017\n\007value_7\030\007 \001(\005\022\017\n\007value_8\030\b \001(\005\022\017\n\007value_9\030\t \001(\005\"?\002\n\005type2\022\017\n\007array_1\030\001 \001(\f\022\017\n\007array_2\030\002 \001(\f\022\017\n\007value_3\030\003 \001(\005\022\017\n\007value_4\030\004 \001(\005\022\017\n\007value_5\030\005 \001(\005\022\017\n\007value_6\030\006 \001(\005\022\017\n\007value_7\030",
				"\007 \001(\005\022\017\n\007value_8\030\b \001(\005\022\017\n\007value_9\030\t \001(\005\022\020\n\bvalue_10\030\n \001(\005\022\020\n\bvalue_11\030\013 \001(\005\022\020\n\bvalue_12\030\f \001(\005\022\020\n\bvalue_13\030\r \001(\005\022\020\n\bvalue_14\030\016 \001(\005\022\020\n\bvalue_15\030\017 \001(\005B*\n\031com.lineage.data.protobufB\rPBMessageALL9" };

		Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() {
			public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
				/*PBMessageALL9.access$11102(root);
				PBMessageALL9
						.access$002((Descriptors.Descriptor) PBMessageALL9.getDescriptor().getMessageTypes().get(0));

				PBMessageALL9.access$102(new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeInvList_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8",
								"Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15", "Value16",
								"Value17", "Array18", "Array19" }));

				PBMessageALL9
						.access$2702((Descriptors.Descriptor) PBMessageALL9.getDescriptor().getMessageTypes().get(1));

				PBMessageALL9.access$2802(new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8",
								"Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15", "Value16",
								"Value17", "Value18", "Value19" }));

				PBMessageALL9
						.access$5402((Descriptors.Descriptor) PBMessageALL9.getDescriptor().getMessageTypes().get(2));

				PBMessageALL9.access$5502(new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeRank_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Array6", "Value7", "Value8",
								"Value9" }));

				PBMessageALL9
						.access$7102((Descriptors.Descriptor) PBMessageALL9.getDescriptor().getMessageTypes().get(3));

				PBMessageALL9.access$7202(new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_type31_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Array5", "Array6", "Value7", "Value8",
								"Value9" }));

				PBMessageALL9
						.access$8802((Descriptors.Descriptor) PBMessageALL9.getDescriptor().getMessageTypes().get(4));

				PBMessageALL9
						.access$8902(new GeneratedMessage.FieldAccessorTable(
								PBMessageALL9.internal_static_com_lineage_data_protobuf_type2_descriptor,
								new String[] { "Array1", "Array2", "Value3", "Value4", "Value5", "Value6", "Value7",
										"Value8", "Value9", "Value10", "Value11", "Value12", "Value13", "Value14",
										"Value15" }));*/

				//////////////////////////////////////////////
				descriptor = root;
				internal_static_com_lineage_data_protobuf_typeInvList_descriptor = getDescriptor().getMessageTypes()
						.get(0);
				internal_static_com_lineage_data_protobuf_typeInvList_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeInvList_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8",
								"Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15", "Value16",
								"Value17", "Array18", "Array19" });

				internal_static_com_lineage_data_protobuf_typeVersion_descriptor = getDescriptor().getMessageTypes()
						.get(1);
				internal_static_com_lineage_data_protobuf_typeVersion_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8",
								"Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15", "Value16",
								"Value17", "Value18", "Value19" });

				internal_static_com_lineage_data_protobuf_typeRank_descriptor = getDescriptor().getMessageTypes()
						.get(2);
				internal_static_com_lineage_data_protobuf_typeRank_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_typeRank_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Value5", "Array6", "Value7", "Value8",
								"Value9" });

				internal_static_com_lineage_data_protobuf_type31_descriptor = getDescriptor().getMessageTypes().get(3);
				internal_static_com_lineage_data_protobuf_type31_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_type31_descriptor,
						new String[] { "Value1", "Value2", "Value3", "Value4", "Array5", "Array6", "Value7", "Value8",
								"Value9" });

				internal_static_com_lineage_data_protobuf_type2_descriptor = getDescriptor().getMessageTypes().get(4);
				internal_static_com_lineage_data_protobuf_type2_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(
						PBMessageALL9.internal_static_com_lineage_data_protobuf_type2_descriptor,
						new String[] { "Array1", "Array2", "Value3", "Value4", "Value5", "Value6", "Value7", "Value8",
								"Value9", "Value10", "Value11", "Value12", "Value13", "Value14", "Value15" });
				return null;
			}
		};
		Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0],
				assigner);
	}

	public static final class typeVersion extends GeneratedMessage implements typeVersionOrBuilder {
		private static final typeVersion defaultInstance;
		private final UnknownFieldSet unknownFields;
		public static com.google.protobuf.Parser<typeVersion> PARSER = new com.google.protobuf.AbstractParser<typeVersion>() {
			public typeVersion parsePartialFrom(CodedInputStream input,
					ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
				return new typeVersion(input, extensionRegistry);
			}
		};
		private int bitField0_;
		public static final int VALUE_1_FIELD_NUMBER = 1;
		private int value1_;
		public static final int VALUE_2_FIELD_NUMBER = 2;
		private int value2_;
		public static final int VALUE_3_FIELD_NUMBER = 3;
		private int value3_;
		public static final int VALUE_4_FIELD_NUMBER = 4;
		private int value4_;
		public static final int VALUE_5_FIELD_NUMBER = 5;
		private int value5_;
		public static final int VALUE_6_FIELD_NUMBER = 6;
		private int value6_;
		public static final int VALUE_7_FIELD_NUMBER = 7;
		private int value7_;
		public static final int VALUE_8_FIELD_NUMBER = 8;
		private int value8_;
		public static final int VALUE_9_FIELD_NUMBER = 9;
		private int value9_;
		public static final int VALUE_10_FIELD_NUMBER = 10;
		private int value10_;
		public static final int VALUE_11_FIELD_NUMBER = 11;
		private int value11_;
		public static final int VALUE_12_FIELD_NUMBER = 12;
		private int value12_;
		public static final int VALUE_13_FIELD_NUMBER = 13;
		private int value13_;
		public static final int VALUE_14_FIELD_NUMBER = 14;
		private int value14_;
		public static final int VALUE_15_FIELD_NUMBER = 15;
		private int value15_;
		public static final int VALUE_16_FIELD_NUMBER = 16;
		private int value16_;
		public static final int VALUE_17_FIELD_NUMBER = 17;
		private int value17_;
		public static final int VALUE_18_FIELD_NUMBER = 18;
		private int value18_;
		public static final int VALUE_19_FIELD_NUMBER = 19;
		private int value19_;
		private byte memoizedIsInitialized = -1;

		private int memoizedSerializedSize = -1;
		private static final long serialVersionUID = 0L;

		private typeVersion(GeneratedMessage.Builder<?> builder) {
			super(builder);
			this.unknownFields = builder.getUnknownFields();
		}

		private typeVersion(boolean noInit) {
			this.unknownFields = UnknownFieldSet.getDefaultInstance();
		}

		public static typeVersion getDefaultInstance() {
			return defaultInstance;
		}

		public typeVersion getDefaultInstanceForType() {
			return defaultInstance;
		}

		public final UnknownFieldSet getUnknownFields() {
			return this.unknownFields;
		}

		private typeVersion(CodedInputStream input, ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			initFields();
			int mutable_bitField0_ = 0;
			UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
			try {
				boolean done = false;
				while (!done) {
					int tag = input.readTag();
					switch (tag) {
					case 0:
						done = true;
						break;
					default:
						if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
							done = true;
						}
						break;
					case 8:
						this.bitField0_ |= 1;
						this.value1_ = input.readInt32();
						break;
					case 16:
						this.bitField0_ |= 2;
						this.value2_ = input.readInt32();
						break;
					case 24:
						this.bitField0_ |= 4;
						this.value3_ = input.readInt32();
						break;
					case 32:
						this.bitField0_ |= 8;
						this.value4_ = input.readInt32();
						break;
					case 40:
						this.bitField0_ |= 16;
						this.value5_ = input.readInt32();
						break;
					case 48:
						this.bitField0_ |= 32;
						this.value6_ = input.readInt32();
						break;
					case 56:
						this.bitField0_ |= 64;
						this.value7_ = input.readInt32();
						break;
					case 64:
						this.bitField0_ |= 128;
						this.value8_ = input.readInt32();
						break;
					case 72:
						this.bitField0_ |= 256;
						this.value9_ = input.readInt32();
						break;
					case 80:
						this.bitField0_ |= 512;
						this.value10_ = input.readInt32();
						break;
					case 88:
						this.bitField0_ |= 1024;
						this.value11_ = input.readInt32();
						break;
					case 96:
						this.bitField0_ |= 2048;
						this.value12_ = input.readInt32();
						break;
					case 104:
						this.bitField0_ |= 4096;
						this.value13_ = input.readInt32();
						break;
					case 112:
						this.bitField0_ |= 8192;
						this.value14_ = input.readInt32();
						break;
					case 120:
						this.bitField0_ |= 16384;
						this.value15_ = input.readInt32();
						break;
					case 128:
						this.bitField0_ |= 32768;
						this.value16_ = input.readInt32();
						break;
					case 136:
						this.bitField0_ |= 65536;
						this.value17_ = input.readInt32();
						break;
					case 144:
						this.bitField0_ |= 131072;
						this.value18_ = input.readInt32();
						break;
					case 152:
						this.bitField0_ |= 262144;
						this.value19_ = input.readInt32();
						break;
					}
				}
			} catch (InvalidProtocolBufferException e) {
				throw e.setUnfinishedMessage(this);
			} catch (IOException e) {
				throw new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
			} finally {
				this.unknownFields = unknownFields.build();
				makeExtensionsImmutable();
			}
		}

		public static final Descriptors.Descriptor getDescriptor() {
			return PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_descriptor;
		}

		protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
			return PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_fieldAccessorTable
					.ensureFieldAccessorsInitialized(typeVersion.class, Builder.class);
		}

		public Parser<typeVersion> getParserForType() {
			return PARSER;
		}

		public boolean hasValue1() {
			return (this.bitField0_ & 0x1) == 1;
		}

		public int getValue1() {
			return this.value1_;
		}

		public boolean hasValue2() {
			return (this.bitField0_ & 0x2) == 2;
		}

		public int getValue2() {
			return this.value2_;
		}

		public boolean hasValue3() {
			return (this.bitField0_ & 0x4) == 4;
		}

		public int getValue3() {
			return this.value3_;
		}

		public boolean hasValue4() {
			return (this.bitField0_ & 0x8) == 8;
		}

		public int getValue4() {
			return this.value4_;
		}

		public boolean hasValue5() {
			return (this.bitField0_ & 0x10) == 16;
		}

		public int getValue5() {
			return this.value5_;
		}

		public boolean hasValue6() {
			return (this.bitField0_ & 0x20) == 32;
		}

		public int getValue6() {
			return this.value6_;
		}

		public boolean hasValue7() {
			return (this.bitField0_ & 0x40) == 64;
		}

		public int getValue7() {
			return this.value7_;
		}

		public boolean hasValue8() {
			return (this.bitField0_ & 0x80) == 128;
		}

		public int getValue8() {
			return this.value8_;
		}

		public boolean hasValue9() {
			return (this.bitField0_ & 0x100) == 256;
		}

		public int getValue9() {
			return this.value9_;
		}

		public boolean hasValue10() {
			return (this.bitField0_ & 0x200) == 512;
		}

		public int getValue10() {
			return this.value10_;
		}

		public boolean hasValue11() {
			return (this.bitField0_ & 0x400) == 1024;
		}

		public int getValue11() {
			return this.value11_;
		}

		public boolean hasValue12() {
			return (this.bitField0_ & 0x800) == 2048;
		}

		public int getValue12() {
			return this.value12_;
		}

		public boolean hasValue13() {
			return (this.bitField0_ & 0x1000) == 4096;
		}

		public int getValue13() {
			return this.value13_;
		}

		public boolean hasValue14() {
			return (this.bitField0_ & 0x2000) == 8192;
		}

		public int getValue14() {
			return this.value14_;
		}

		public boolean hasValue15() {
			return (this.bitField0_ & 0x4000) == 16384;
		}

		public int getValue15() {
			return this.value15_;
		}

		public boolean hasValue16() {
			return (this.bitField0_ & 0x8000) == 32768;
		}

		public int getValue16() {
			return this.value16_;
		}

		public boolean hasValue17() {
			return (this.bitField0_ & 0x10000) == 65536;
		}

		public int getValue17() {
			return this.value17_;
		}

		public boolean hasValue18() {
			return (this.bitField0_ & 0x20000) == 131072;
		}

		public int getValue18() {
			return this.value18_;
		}

		public boolean hasValue19() {
			return (this.bitField0_ & 0x40000) == 262144;
		}

		public int getValue19() {
			return this.value19_;
		}

		private void initFields() {
			this.value1_ = 0;
			this.value2_ = 0;
			this.value3_ = 0;
			this.value4_ = 0;
			this.value5_ = 0;
			this.value6_ = 0;
			this.value7_ = 0;
			this.value8_ = 0;
			this.value9_ = 0;
			this.value10_ = 0;
			this.value11_ = 0;
			this.value12_ = 0;
			this.value13_ = 0;
			this.value14_ = 0;
			this.value15_ = 0;
			this.value16_ = 0;
			this.value17_ = 0;
			this.value18_ = 0;
			this.value19_ = 0;
		}

		public final boolean isInitialized() {
			byte isInitialized = this.memoizedIsInitialized;
			if (isInitialized != -1)
				return isInitialized == 1;

			this.memoizedIsInitialized = 1;
			return true;
		}

		public void writeTo(CodedOutputStream output) throws IOException {
			getSerializedSize();
			if ((this.bitField0_ & 0x1) == 1) {
				output.writeInt32(1, this.value1_);
			}
			if ((this.bitField0_ & 0x2) == 2) {
				output.writeInt32(2, this.value2_);
			}
			if ((this.bitField0_ & 0x4) == 4) {
				output.writeInt32(3, this.value3_);
			}
			if ((this.bitField0_ & 0x8) == 8) {
				output.writeInt32(4, this.value4_);
			}
			if ((this.bitField0_ & 0x10) == 16) {
				output.writeInt32(5, this.value5_);
			}
			if ((this.bitField0_ & 0x20) == 32) {
				output.writeInt32(6, this.value6_);
			}
			if ((this.bitField0_ & 0x40) == 64) {
				output.writeInt32(7, this.value7_);
			}
			if ((this.bitField0_ & 0x80) == 128) {
				output.writeInt32(8, this.value8_);
			}
			if ((this.bitField0_ & 0x100) == 256) {
				output.writeInt32(9, this.value9_);
			}
			if ((this.bitField0_ & 0x200) == 512) {
				output.writeInt32(10, this.value10_);
			}
			if ((this.bitField0_ & 0x400) == 1024) {
				output.writeInt32(11, this.value11_);
			}
			if ((this.bitField0_ & 0x800) == 2048) {
				output.writeInt32(12, this.value12_);
			}
			if ((this.bitField0_ & 0x1000) == 4096) {
				output.writeInt32(13, this.value13_);
			}
			if ((this.bitField0_ & 0x2000) == 8192) {
				output.writeInt32(14, this.value14_);
			}
			if ((this.bitField0_ & 0x4000) == 16384) {
				output.writeInt32(15, this.value15_);
			}
			if ((this.bitField0_ & 0x8000) == 32768) {
				output.writeInt32(16, this.value16_);
			}
			if ((this.bitField0_ & 0x10000) == 65536) {
				output.writeInt32(17, this.value17_);
			}
			if ((this.bitField0_ & 0x20000) == 131072) {
				output.writeInt32(18, this.value18_);
			}
			if ((this.bitField0_ & 0x40000) == 262144) {
				output.writeInt32(19, this.value19_);
			}
			getUnknownFields().writeTo(output);
		}

		public int getSerializedSize() {
			int size = this.memoizedSerializedSize;
			if (size != -1)
				return size;

			size = 0;
			if ((this.bitField0_ & 0x1) == 1) {
				size += CodedOutputStream.computeInt32Size(1, this.value1_);
			}

			if ((this.bitField0_ & 0x2) == 2) {
				size += CodedOutputStream.computeInt32Size(2, this.value2_);
			}

			if ((this.bitField0_ & 0x4) == 4) {
				size += CodedOutputStream.computeInt32Size(3, this.value3_);
			}

			if ((this.bitField0_ & 0x8) == 8) {
				size += CodedOutputStream.computeInt32Size(4, this.value4_);
			}

			if ((this.bitField0_ & 0x10) == 16) {
				size += CodedOutputStream.computeInt32Size(5, this.value5_);
			}

			if ((this.bitField0_ & 0x20) == 32) {
				size += CodedOutputStream.computeInt32Size(6, this.value6_);
			}

			if ((this.bitField0_ & 0x40) == 64) {
				size += CodedOutputStream.computeInt32Size(7, this.value7_);
			}

			if ((this.bitField0_ & 0x80) == 128) {
				size += CodedOutputStream.computeInt32Size(8, this.value8_);
			}

			if ((this.bitField0_ & 0x100) == 256) {
				size += CodedOutputStream.computeInt32Size(9, this.value9_);
			}

			if ((this.bitField0_ & 0x200) == 512) {
				size += CodedOutputStream.computeInt32Size(10, this.value10_);
			}

			if ((this.bitField0_ & 0x400) == 1024) {
				size += CodedOutputStream.computeInt32Size(11, this.value11_);
			}

			if ((this.bitField0_ & 0x800) == 2048) {
				size += CodedOutputStream.computeInt32Size(12, this.value12_);
			}

			if ((this.bitField0_ & 0x1000) == 4096) {
				size += CodedOutputStream.computeInt32Size(13, this.value13_);
			}

			if ((this.bitField0_ & 0x2000) == 8192) {
				size += CodedOutputStream.computeInt32Size(14, this.value14_);
			}

			if ((this.bitField0_ & 0x4000) == 16384) {
				size += CodedOutputStream.computeInt32Size(15, this.value15_);
			}

			if ((this.bitField0_ & 0x8000) == 32768) {
				size += CodedOutputStream.computeInt32Size(16, this.value16_);
			}

			if ((this.bitField0_ & 0x10000) == 65536) {
				size += CodedOutputStream.computeInt32Size(17, this.value17_);
			}

			if ((this.bitField0_ & 0x20000) == 131072) {
				size += CodedOutputStream.computeInt32Size(18, this.value18_);
			}

			if ((this.bitField0_ & 0x40000) == 262144) {
				size += CodedOutputStream.computeInt32Size(19, this.value19_);
			}

			size += getUnknownFields().getSerializedSize();
			this.memoizedSerializedSize = size;
			return size;
		}

		protected Object writeReplace() throws ObjectStreamException {
			return super.writeReplace();
		}

		public static typeVersion parseFrom(ByteString data) throws InvalidProtocolBufferException {
			return (typeVersion) PARSER.parseFrom(data);
		}

		public static typeVersion parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			return (typeVersion) PARSER.parseFrom(data, extensionRegistry);
		}

		public static typeVersion parseFrom(byte[] data) throws InvalidProtocolBufferException {
			return (typeVersion) PARSER.parseFrom(data);
		}

		public static typeVersion parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry)
				throws InvalidProtocolBufferException {
			return (typeVersion) PARSER.parseFrom(data, extensionRegistry);
		}

		public static typeVersion parseFrom(InputStream input) throws IOException {
			return (typeVersion) PARSER.parseFrom(input);
		}

		public static typeVersion parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry)
				throws IOException {
			return (typeVersion) PARSER.parseFrom(input, extensionRegistry);
		}

		public static typeVersion parseDelimitedFrom(InputStream input) throws IOException {
			return (typeVersion) PARSER.parseDelimitedFrom(input);
		}

		public static typeVersion parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry)
				throws IOException {
			return (typeVersion) PARSER.parseDelimitedFrom(input, extensionRegistry);
		}

		public static typeVersion parseFrom(CodedInputStream input) throws IOException {
			return (typeVersion) PARSER.parseFrom(input);
		}

		public static typeVersion parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry)
				throws IOException {
			return (typeVersion) PARSER.parseFrom(input, extensionRegistry);
		}

		public static Builder newBuilder() {
			return Builder.create(); // access$3000
		}

		public Builder newBuilderForType() {
			return newBuilder();
		}

		public static Builder newBuilder(typeVersion prototype) {
			return newBuilder().mergeFrom(prototype);
		}

		public Builder toBuilder() {
			return newBuilder(this);
		}

		protected Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
			Builder builder = new Builder(parent);
			return builder;
		}

		static {
			defaultInstance = new typeVersion(true);
			defaultInstance.initFields();
		}

		public static final class Builder extends GeneratedMessage.Builder<Builder>
				implements PBMessageALL9.typeVersionOrBuilder {
			private int bitField0_;
			private int value1_;
			private int value2_;
			private int value3_;
			private int value4_;
			private int value5_;
			private int value6_;
			private int value7_;
			private int value8_;
			private int value9_;
			private int value10_;
			private int value11_;
			private int value12_;
			private int value13_;
			private int value14_;
			private int value15_;
			private int value16_;
			private int value17_;
			private int value18_;
			private int value19_;

			public static final Descriptors.Descriptor getDescriptor() {
				return PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_descriptor;
			}

			protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
				return PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_fieldAccessorTable
						.ensureFieldAccessorsInitialized(PBMessageALL9.typeVersion.class, Builder.class);
			}

			private Builder() {
				maybeForceBuilderInitialization();
			}

			private Builder(GeneratedMessage.BuilderParent parent) {
				super(parent);
				maybeForceBuilderInitialization();
			}

			private void maybeForceBuilderInitialization() {
				if (PBMessageALL9.typeVersion.alwaysUseFieldBuilders)
					;
			}

			private static Builder create() {
				return new Builder();
			}

			public Builder clear() {
				super.clear();
				this.value1_ = 0;
				this.bitField0_ &= -2;
				this.value2_ = 0;
				this.bitField0_ &= -3;
				this.value3_ = 0;
				this.bitField0_ &= -5;
				this.value4_ = 0;
				this.bitField0_ &= -9;
				this.value5_ = 0;
				this.bitField0_ &= -17;
				this.value6_ = 0;
				this.bitField0_ &= -33;
				this.value7_ = 0;
				this.bitField0_ &= -65;
				this.value8_ = 0;
				this.bitField0_ &= -129;
				this.value9_ = 0;
				this.bitField0_ &= -257;
				this.value10_ = 0;
				this.bitField0_ &= -513;
				this.value11_ = 0;
				this.bitField0_ &= -1025;
				this.value12_ = 0;
				this.bitField0_ &= -2049;
				this.value13_ = 0;
				this.bitField0_ &= -4097;
				this.value14_ = 0;
				this.bitField0_ &= -8193;
				this.value15_ = 0;
				this.bitField0_ &= -16385;
				this.value16_ = 0;
				this.bitField0_ &= -32769;
				this.value17_ = 0;
				this.bitField0_ &= -65537;
				this.value18_ = 0;
				this.bitField0_ &= -131073;
				this.value19_ = 0;
				this.bitField0_ &= -262145;
				return this;
			}

			public Builder clone() {
				return create().mergeFrom(buildPartial());
			}

			public Descriptors.Descriptor getDescriptorForType() {
				return PBMessageALL9.internal_static_com_lineage_data_protobuf_typeVersion_descriptor;
			}

			public PBMessageALL9.typeVersion getDefaultInstanceForType() {
				return PBMessageALL9.typeVersion.getDefaultInstance();
			}

			public PBMessageALL9.typeVersion build() {
				PBMessageALL9.typeVersion result = buildPartial();
				if (!result.isInitialized()) {
					throw newUninitializedMessageException(result);
				}
				return result;
			}

			public PBMessageALL9.typeVersion buildPartial() {
				PBMessageALL9.typeVersion result = new PBMessageALL9.typeVersion(this);
				int from_bitField0_ = this.bitField0_;
				int to_bitField0_ = 0;
				if ((from_bitField0_ & 0x1) == 1) {
					to_bitField0_ |= 1;
				}
				result.value1_ = this.value1_;
				if ((from_bitField0_ & 0x2) == 2) {
					to_bitField0_ |= 2;
				}
				result.value2_ = this.value2_;
				if ((from_bitField0_ & 0x4) == 4) {
					to_bitField0_ |= 4;
				}
				result.value3_ = this.value3_;
				if ((from_bitField0_ & 0x8) == 8) {
					to_bitField0_ |= 8;
				}
				result.value4_ = this.value4_;
				if ((from_bitField0_ & 0x10) == 16) {
					to_bitField0_ |= 16;
				}
				result.value5_ = this.value5_;
				if ((from_bitField0_ & 0x20) == 32) {
					to_bitField0_ |= 32;
				}
				result.value6_ = this.value6_;
				if ((from_bitField0_ & 0x40) == 64) {
					to_bitField0_ |= 64;
				}
				result.value7_ = this.value7_;
				if ((from_bitField0_ & 0x80) == 128) {
					to_bitField0_ |= 128;
				}
				result.value8_ = this.value8_;
				if ((from_bitField0_ & 0x100) == 256) {
					to_bitField0_ |= 256;
				}
				result.value9_ = this.value9_;
				if ((from_bitField0_ & 0x200) == 512) {
					to_bitField0_ |= 512;
				}
				result.value10_ = this.value10_;
				if ((from_bitField0_ & 0x400) == 1024) {
					to_bitField0_ |= 1024;
				}
				result.value11_ = this.value11_;
				if ((from_bitField0_ & 0x800) == 2048) {
					to_bitField0_ |= 2048;
				}
				result.value12_ = this.value12_;
				if ((from_bitField0_ & 0x1000) == 4096) {
					to_bitField0_ |= 4096;
				}
				result.value13_ = this.value13_;
				if ((from_bitField0_ & 0x2000) == 8192) {
					to_bitField0_ |= 8192;
				}
				result.value14_ = this.value14_;
				if ((from_bitField0_ & 0x4000) == 16384) {
					to_bitField0_ |= 16384;
				}
				result.value15_ = this.value15_;
				if ((from_bitField0_ & 0x8000) == 32768) {
					to_bitField0_ |= 32768;
				}
				result.value16_ = this.value16_;
				if ((from_bitField0_ & 0x10000) == 65536) {
					to_bitField0_ |= 65536;
				}
				result.value17_ = this.value17_;
				if ((from_bitField0_ & 0x20000) == 131072) {
					to_bitField0_ |= 131072;
				}
				result.value18_ = this.value18_;
				if ((from_bitField0_ & 0x40000) == 262144) {
					to_bitField0_ |= 262144;
				}
				result.value19_ = this.value19_;
				result.bitField0_ = to_bitField0_;
				onBuilt();
				return result;
			}

			public Builder mergeFrom(Message other) {
				if ((other instanceof PBMessageALL9.typeVersion)) {
					return mergeFrom((PBMessageALL9.typeVersion) other);
				}
				super.mergeFrom(other);
				return this;
			}

			public Builder mergeFrom(PBMessageALL9.typeVersion other) {
				if (other == PBMessageALL9.typeVersion.getDefaultInstance())
					return this;
				if (other.hasValue1()) {
					setValue1(other.getValue1());
				}
				if (other.hasValue2()) {
					setValue2(other.getValue2());
				}
				if (other.hasValue3()) {
					setValue3(other.getValue3());
				}
				if (other.hasValue4()) {
					setValue4(other.getValue4());
				}
				if (other.hasValue5()) {
					setValue5(other.getValue5());
				}
				if (other.hasValue6()) {
					setValue6(other.getValue6());
				}
				if (other.hasValue7()) {
					setValue7(other.getValue7());
				}
				if (other.hasValue8()) {
					setValue8(other.getValue8());
				}
				if (other.hasValue9()) {
					setValue9(other.getValue9());
				}
				if (other.hasValue10()) {
					setValue10(other.getValue10());
				}
				if (other.hasValue11()) {
					setValue11(other.getValue11());
				}
				if (other.hasValue12()) {
					setValue12(other.getValue12());
				}
				if (other.hasValue13()) {
					setValue13(other.getValue13());
				}
				if (other.hasValue14()) {
					setValue14(other.getValue14());
				}
				if (other.hasValue15()) {
					setValue15(other.getValue15());
				}
				if (other.hasValue16()) {
					setValue16(other.getValue16());
				}
				if (other.hasValue17()) {
					setValue17(other.getValue17());
				}
				if (other.hasValue18()) {
					setValue18(other.getValue18());
				}
				if (other.hasValue19()) {
					setValue19(other.getValue19());
				}
				mergeUnknownFields(other.getUnknownFields());
				return this;
			}

			public final boolean isInitialized() {
				return true;
			}

			public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry)
					throws IOException {
				PBMessageALL9.typeVersion parsedMessage = null;
				try {
					parsedMessage = (PBMessageALL9.typeVersion) PBMessageALL9.typeVersion.PARSER.parsePartialFrom(input,
							extensionRegistry);
				} catch (InvalidProtocolBufferException e) {
					parsedMessage = (PBMessageALL9.typeVersion) e.getUnfinishedMessage();
					throw e;
				} finally {
					if (parsedMessage != null) {
						mergeFrom(parsedMessage);
					}
				}
				return this;
			}

			public boolean hasValue1() {
				return (this.bitField0_ & 0x1) == 1;
			}

			public int getValue1() {
				return this.value1_;
			}

			public Builder setValue1(int value) {
				this.bitField0_ |= 1;
				this.value1_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue1() {
				this.bitField0_ &= -2;
				this.value1_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue2() {
				return (this.bitField0_ & 0x2) == 2;
			}

			public int getValue2() {
				return this.value2_;
			}

			public Builder setValue2(int value) {
				this.bitField0_ |= 2;
				this.value2_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue2() {
				this.bitField0_ &= -3;
				this.value2_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue3() {
				return (this.bitField0_ & 0x4) == 4;
			}

			public int getValue3() {
				return this.value3_;
			}

			public Builder setValue3(int value) {
				this.bitField0_ |= 4;
				this.value3_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue3() {
				this.bitField0_ &= -5;
				this.value3_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue4() {
				return (this.bitField0_ & 0x8) == 8;
			}

			public int getValue4() {
				return this.value4_;
			}

			public Builder setValue4(int value) {
				this.bitField0_ |= 8;
				this.value4_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue4() {
				this.bitField0_ &= -9;
				this.value4_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue5() {
				return (this.bitField0_ & 0x10) == 16;
			}

			public int getValue5() {
				return this.value5_;
			}

			public Builder setValue5(int value) {
				this.bitField0_ |= 16;
				this.value5_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue5() {
				this.bitField0_ &= -17;
				this.value5_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue6() {
				return (this.bitField0_ & 0x20) == 32;
			}

			public int getValue6() {
				return this.value6_;
			}

			public Builder setValue6(int value) {
				this.bitField0_ |= 32;
				this.value6_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue6() {
				this.bitField0_ &= -33;
				this.value6_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue7() {
				return (this.bitField0_ & 0x40) == 64;
			}

			public int getValue7() {
				return this.value7_;
			}

			public Builder setValue7(int value) {
				this.bitField0_ |= 64;
				this.value7_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue7() {
				this.bitField0_ &= -65;
				this.value7_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue8() {
				return (this.bitField0_ & 0x80) == 128;
			}

			public int getValue8() {
				return this.value8_;
			}

			public Builder setValue8(int value) {
				this.bitField0_ |= 128;
				this.value8_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue8() {
				this.bitField0_ &= -129;
				this.value8_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue9() {
				return (this.bitField0_ & 0x100) == 256;
			}

			public int getValue9() {
				return this.value9_;
			}

			public Builder setValue9(int value) {
				this.bitField0_ |= 256;
				this.value9_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue9() {
				this.bitField0_ &= -257;
				this.value9_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue10() {
				return (this.bitField0_ & 0x200) == 512;
			}

			public int getValue10() {
				return this.value10_;
			}

			public Builder setValue10(int value) {
				this.bitField0_ |= 512;
				this.value10_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue10() {
				this.bitField0_ &= -513;
				this.value10_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue11() {
				return (this.bitField0_ & 0x400) == 1024;
			}

			public int getValue11() {
				return this.value11_;
			}

			public Builder setValue11(int value) {
				this.bitField0_ |= 1024;
				this.value11_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue11() {
				this.bitField0_ &= -1025;
				this.value11_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue12() {
				return (this.bitField0_ & 0x800) == 2048;
			}

			public int getValue12() {
				return this.value12_;
			}

			public Builder setValue12(int value) {
				this.bitField0_ |= 2048;
				this.value12_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue12() {
				this.bitField0_ &= -2049;
				this.value12_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue13() {
				return (this.bitField0_ & 0x1000) == 4096;
			}

			public int getValue13() {
				return this.value13_;
			}

			public Builder setValue13(int value) {
				this.bitField0_ |= 4096;
				this.value13_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue13() {
				this.bitField0_ &= -4097;
				this.value13_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue14() {
				return (this.bitField0_ & 0x2000) == 8192;
			}

			public int getValue14() {
				return this.value14_;
			}

			public Builder setValue14(int value) {
				this.bitField0_ |= 8192;
				this.value14_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue14() {
				this.bitField0_ &= -8193;
				this.value14_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue15() {
				return (this.bitField0_ & 0x4000) == 16384;
			}

			public int getValue15() {
				return this.value15_;
			}

			public Builder setValue15(int value) {
				this.bitField0_ |= 16384;
				this.value15_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue15() {
				this.bitField0_ &= -16385;
				this.value15_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue16() {
				return (this.bitField0_ & 0x8000) == 32768;
			}

			public int getValue16() {
				return this.value16_;
			}

			public Builder setValue16(int value) {
				this.bitField0_ |= 32768;
				this.value16_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue16() {
				this.bitField0_ &= -32769;
				this.value16_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue17() {
				return (this.bitField0_ & 0x10000) == 65536;
			}

			public int getValue17() {
				return this.value17_;
			}

			public Builder setValue17(int value) {
				this.bitField0_ |= 65536;
				this.value17_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue17() {
				this.bitField0_ &= -65537;
				this.value17_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue18() {
				return (this.bitField0_ & 0x20000) == 131072;
			}

			public int getValue18() {
				return this.value18_;
			}

			public Builder setValue18(int value) {
				this.bitField0_ |= 131072;
				this.value18_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue18() {
				this.bitField0_ &= -131073;
				this.value18_ = 0;
				onChanged();
				return this;
			}

			public boolean hasValue19() {
				return (this.bitField0_ & 0x40000) == 262144;
			}

			public int getValue19() {
				return this.value19_;
			}

			public Builder setValue19(int value) {
				this.bitField0_ |= 262144;
				this.value19_ = value;
				onChanged();
				return this;
			}

			public Builder clearValue19() {
				this.bitField0_ &= -262145;
				this.value19_ = 0;
				onChanged();
				return this;
			}
		}
	}

	public static abstract interface typeVersionOrBuilder extends MessageOrBuilder {
		public abstract boolean hasValue1();

		public abstract int getValue1();

		public abstract boolean hasValue2();

		public abstract int getValue2();

		public abstract boolean hasValue3();

		public abstract int getValue3();

		public abstract boolean hasValue4();

		public abstract int getValue4();

		public abstract boolean hasValue5();

		public abstract int getValue5();

		public abstract boolean hasValue6();

		public abstract int getValue6();

		public abstract boolean hasValue7();

		public abstract int getValue7();

		public abstract boolean hasValue8();

		public abstract int getValue8();

		public abstract boolean hasValue9();

		public abstract int getValue9();

		public abstract boolean hasValue10();

		public abstract int getValue10();

		public abstract boolean hasValue11();

		public abstract int getValue11();

		public abstract boolean hasValue12();

		public abstract int getValue12();

		public abstract boolean hasValue13();

		public abstract int getValue13();

		public abstract boolean hasValue14();

		public abstract int getValue14();

		public abstract boolean hasValue15();

		public abstract int getValue15();

		public abstract boolean hasValue16();

		public abstract int getValue16();

		public abstract boolean hasValue17();

		public abstract int getValue17();

		public abstract boolean hasValue18();

		public abstract int getValue18();

		public abstract boolean hasValue19();

		public abstract int getValue19();
	}
}
