/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package tech.nermindedovic.leaderboardstreams.models.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Player extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2100214462498859341L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Player\",\"namespace\":\"tech.nermindedovic.leaderboardstreams.models.avro\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"DOB\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Player> ENCODER =
      new BinaryMessageEncoder<Player>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Player> DECODER =
      new BinaryMessageDecoder<Player>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Player> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Player> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Player> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Player>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Player to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Player from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Player instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Player fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private long id;
  private java.lang.CharSequence name;
  private java.lang.CharSequence DOB;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Player() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param name The new value for name
   * @param DOB The new value for DOB
   */
  public Player(java.lang.Long id, java.lang.CharSequence name, java.lang.CharSequence DOB) {
    this.id = id;
    this.name = name;
    this.DOB = DOB;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return name;
    case 2: return DOB;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Long)value$; break;
    case 1: name = (java.lang.CharSequence)value$; break;
    case 2: DOB = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public long getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'DOB' field.
   * @return The value of the 'DOB' field.
   */
  public java.lang.CharSequence getDOB() {
    return DOB;
  }


  /**
   * Sets the value of the 'DOB' field.
   * @param value the value to set.
   */
  public void setDOB(java.lang.CharSequence value) {
    this.DOB = value;
  }

  /**
   * Creates a new Player RecordBuilder.
   * @return A new Player RecordBuilder
   */
  public static tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder newBuilder() {
    return new tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder();
  }

  /**
   * Creates a new Player RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Player RecordBuilder
   */
  public static tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder newBuilder(tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder other) {
    if (other == null) {
      return new tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder();
    } else {
      return new tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder(other);
    }
  }

  /**
   * Creates a new Player RecordBuilder by copying an existing Player instance.
   * @param other The existing instance to copy.
   * @return A new Player RecordBuilder
   */
  public static tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder newBuilder(tech.nermindedovic.leaderboardstreams.models.avro.Player other) {
    if (other == null) {
      return new tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder();
    } else {
      return new tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder(other);
    }
  }

  /**
   * RecordBuilder for Player instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Player>
    implements org.apache.avro.data.RecordBuilder<Player> {

    private long id;
    private java.lang.CharSequence name;
    private java.lang.CharSequence DOB;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.DOB)) {
        this.DOB = data().deepCopy(fields()[2].schema(), other.DOB);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing Player instance
     * @param other The existing instance to copy.
     */
    private Builder(tech.nermindedovic.leaderboardstreams.models.avro.Player other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.DOB)) {
        this.DOB = data().deepCopy(fields()[2].schema(), other.DOB);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public long getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder setId(long value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder clearId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder setName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'DOB' field.
      * @return The value.
      */
    public java.lang.CharSequence getDOB() {
      return DOB;
    }


    /**
      * Sets the value of the 'DOB' field.
      * @param value The value of 'DOB'.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder setDOB(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.DOB = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'DOB' field has been set.
      * @return True if the 'DOB' field has been set, false otherwise.
      */
    public boolean hasDOB() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'DOB' field.
      * @return This builder.
      */
    public tech.nermindedovic.leaderboardstreams.models.avro.Player.Builder clearDOB() {
      DOB = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Player build() {
      try {
        Player record = new Player();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Long) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.DOB = fieldSetFlags()[2] ? this.DOB : (java.lang.CharSequence) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Player>
    WRITER$ = (org.apache.avro.io.DatumWriter<Player>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Player>
    READER$ = (org.apache.avro.io.DatumReader<Player>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.id);

    out.writeString(this.name);

    out.writeString(this.DOB);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readLong();

      this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);

      this.DOB = in.readString(this.DOB instanceof Utf8 ? (Utf8)this.DOB : null);

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readLong();
          break;

        case 1:
          this.name = in.readString(this.name instanceof Utf8 ? (Utf8)this.name : null);
          break;

        case 2:
          this.DOB = in.readString(this.DOB instanceof Utf8 ? (Utf8)this.DOB : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










