/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.nermdev.schemas.avro.leaderboards;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

/** An entry within a leaderboard continously being updated */
@org.apache.avro.specific.AvroGenerated
public class HistoricEntry extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 181663870186115925L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"HistoricEntry\",\"namespace\":\"io.nermdev.schemas.avro.leaderboards\",\"doc\":\"An entry within a leaderboard continously being updated\",\"fields\":[{\"name\":\"playerId\",\"type\":\"long\"},{\"name\":\"playerName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"productId\",\"type\":\"long\"},{\"name\":\"productName\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"score\",\"type\":\"double\"},{\"name\":\"entries\",\"type\":\"long\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<HistoricEntry> ENCODER =
      new BinaryMessageEncoder<HistoricEntry>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<HistoricEntry> DECODER =
      new BinaryMessageDecoder<HistoricEntry>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<HistoricEntry> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<HistoricEntry> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<HistoricEntry> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<HistoricEntry>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this HistoricEntry to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a HistoricEntry from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a HistoricEntry instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static HistoricEntry fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private long playerId;
  private java.lang.String playerName;
  private long productId;
  private java.lang.String productName;
  private double score;
  private long entries;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public HistoricEntry() {}

  /**
   * All-args constructor.
   * @param playerId The new value for playerId
   * @param playerName The new value for playerName
   * @param productId The new value for productId
   * @param productName The new value for productName
   * @param score The new value for score
   * @param entries The new value for entries
   */
  public HistoricEntry(java.lang.Long playerId, java.lang.String playerName, java.lang.Long productId, java.lang.String productName, java.lang.Double score, java.lang.Long entries) {
    this.playerId = playerId;
    this.playerName = playerName;
    this.productId = productId;
    this.productName = productName;
    this.score = score;
    this.entries = entries;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return playerId;
    case 1: return playerName;
    case 2: return productId;
    case 3: return productName;
    case 4: return score;
    case 5: return entries;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: playerId = (java.lang.Long)value$; break;
    case 1: playerName = value$ != null ? value$.toString() : null; break;
    case 2: productId = (java.lang.Long)value$; break;
    case 3: productName = value$ != null ? value$.toString() : null; break;
    case 4: score = (java.lang.Double)value$; break;
    case 5: entries = (java.lang.Long)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'playerId' field.
   * @return The value of the 'playerId' field.
   */
  public long getPlayerId() {
    return playerId;
  }


  /**
   * Sets the value of the 'playerId' field.
   * @param value the value to set.
   */
  public void setPlayerId(long value) {
    this.playerId = value;
  }

  /**
   * Gets the value of the 'playerName' field.
   * @return The value of the 'playerName' field.
   */
  public java.lang.String getPlayerName() {
    return playerName;
  }


  /**
   * Sets the value of the 'playerName' field.
   * @param value the value to set.
   */
  public void setPlayerName(java.lang.String value) {
    this.playerName = value;
  }

  /**
   * Gets the value of the 'productId' field.
   * @return The value of the 'productId' field.
   */
  public long getProductId() {
    return productId;
  }


  /**
   * Sets the value of the 'productId' field.
   * @param value the value to set.
   */
  public void setProductId(long value) {
    this.productId = value;
  }

  /**
   * Gets the value of the 'productName' field.
   * @return The value of the 'productName' field.
   */
  public java.lang.String getProductName() {
    return productName;
  }


  /**
   * Sets the value of the 'productName' field.
   * @param value the value to set.
   */
  public void setProductName(java.lang.String value) {
    this.productName = value;
  }

  /**
   * Gets the value of the 'score' field.
   * @return The value of the 'score' field.
   */
  public double getScore() {
    return score;
  }


  /**
   * Sets the value of the 'score' field.
   * @param value the value to set.
   */
  public void setScore(double value) {
    this.score = value;
  }

  /**
   * Gets the value of the 'entries' field.
   * @return The value of the 'entries' field.
   */
  public long getEntries() {
    return entries;
  }


  /**
   * Sets the value of the 'entries' field.
   * @param value the value to set.
   */
  public void setEntries(long value) {
    this.entries = value;
  }

  /**
   * Creates a new HistoricEntry RecordBuilder.
   * @return A new HistoricEntry RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder newBuilder() {
    return new io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder();
  }

  /**
   * Creates a new HistoricEntry RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new HistoricEntry RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder newBuilder(io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder other) {
    if (other == null) {
      return new io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder();
    } else {
      return new io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder(other);
    }
  }

  /**
   * Creates a new HistoricEntry RecordBuilder by copying an existing HistoricEntry instance.
   * @param other The existing instance to copy.
   * @return A new HistoricEntry RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder newBuilder(io.nermdev.schemas.avro.leaderboards.HistoricEntry other) {
    if (other == null) {
      return new io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder();
    } else {
      return new io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder(other);
    }
  }

  /**
   * RecordBuilder for HistoricEntry instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<HistoricEntry>
    implements org.apache.avro.data.RecordBuilder<HistoricEntry> {

    private long playerId;
    private java.lang.String playerName;
    private long productId;
    private java.lang.String productName;
    private double score;
    private long entries;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.playerId)) {
        this.playerId = data().deepCopy(fields()[0].schema(), other.playerId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.playerName)) {
        this.playerName = data().deepCopy(fields()[1].schema(), other.playerName);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.productId)) {
        this.productId = data().deepCopy(fields()[2].schema(), other.productId);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.productName)) {
        this.productName = data().deepCopy(fields()[3].schema(), other.productName);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.score)) {
        this.score = data().deepCopy(fields()[4].schema(), other.score);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.entries)) {
        this.entries = data().deepCopy(fields()[5].schema(), other.entries);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
    }

    /**
     * Creates a Builder by copying an existing HistoricEntry instance
     * @param other The existing instance to copy.
     */
    private Builder(io.nermdev.schemas.avro.leaderboards.HistoricEntry other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.playerId)) {
        this.playerId = data().deepCopy(fields()[0].schema(), other.playerId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.playerName)) {
        this.playerName = data().deepCopy(fields()[1].schema(), other.playerName);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.productId)) {
        this.productId = data().deepCopy(fields()[2].schema(), other.productId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.productName)) {
        this.productName = data().deepCopy(fields()[3].schema(), other.productName);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.score)) {
        this.score = data().deepCopy(fields()[4].schema(), other.score);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.entries)) {
        this.entries = data().deepCopy(fields()[5].schema(), other.entries);
        fieldSetFlags()[5] = true;
      }
    }

    /**
      * Gets the value of the 'playerId' field.
      * @return The value.
      */
    public long getPlayerId() {
      return playerId;
    }


    /**
      * Sets the value of the 'playerId' field.
      * @param value The value of 'playerId'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setPlayerId(long value) {
      validate(fields()[0], value);
      this.playerId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'playerId' field has been set.
      * @return True if the 'playerId' field has been set, false otherwise.
      */
    public boolean hasPlayerId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'playerId' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearPlayerId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'playerName' field.
      * @return The value.
      */
    public java.lang.String getPlayerName() {
      return playerName;
    }


    /**
      * Sets the value of the 'playerName' field.
      * @param value The value of 'playerName'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setPlayerName(java.lang.String value) {
      validate(fields()[1], value);
      this.playerName = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'playerName' field has been set.
      * @return True if the 'playerName' field has been set, false otherwise.
      */
    public boolean hasPlayerName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'playerName' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearPlayerName() {
      playerName = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'productId' field.
      * @return The value.
      */
    public long getProductId() {
      return productId;
    }


    /**
      * Sets the value of the 'productId' field.
      * @param value The value of 'productId'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setProductId(long value) {
      validate(fields()[2], value);
      this.productId = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'productId' field has been set.
      * @return True if the 'productId' field has been set, false otherwise.
      */
    public boolean hasProductId() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'productId' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearProductId() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'productName' field.
      * @return The value.
      */
    public java.lang.String getProductName() {
      return productName;
    }


    /**
      * Sets the value of the 'productName' field.
      * @param value The value of 'productName'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setProductName(java.lang.String value) {
      validate(fields()[3], value);
      this.productName = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'productName' field has been set.
      * @return True if the 'productName' field has been set, false otherwise.
      */
    public boolean hasProductName() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'productName' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearProductName() {
      productName = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'score' field.
      * @return The value.
      */
    public double getScore() {
      return score;
    }


    /**
      * Sets the value of the 'score' field.
      * @param value The value of 'score'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setScore(double value) {
      validate(fields()[4], value);
      this.score = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'score' field has been set.
      * @return True if the 'score' field has been set, false otherwise.
      */
    public boolean hasScore() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'score' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearScore() {
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'entries' field.
      * @return The value.
      */
    public long getEntries() {
      return entries;
    }


    /**
      * Sets the value of the 'entries' field.
      * @param value The value of 'entries'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder setEntries(long value) {
      validate(fields()[5], value);
      this.entries = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'entries' field has been set.
      * @return True if the 'entries' field has been set, false otherwise.
      */
    public boolean hasEntries() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'entries' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.HistoricEntry.Builder clearEntries() {
      fieldSetFlags()[5] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HistoricEntry build() {
      try {
        HistoricEntry record = new HistoricEntry();
        record.playerId = fieldSetFlags()[0] ? this.playerId : (java.lang.Long) defaultValue(fields()[0]);
        record.playerName = fieldSetFlags()[1] ? this.playerName : (java.lang.String) defaultValue(fields()[1]);
        record.productId = fieldSetFlags()[2] ? this.productId : (java.lang.Long) defaultValue(fields()[2]);
        record.productName = fieldSetFlags()[3] ? this.productName : (java.lang.String) defaultValue(fields()[3]);
        record.score = fieldSetFlags()[4] ? this.score : (java.lang.Double) defaultValue(fields()[4]);
        record.entries = fieldSetFlags()[5] ? this.entries : (java.lang.Long) defaultValue(fields()[5]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<HistoricEntry>
    WRITER$ = (org.apache.avro.io.DatumWriter<HistoricEntry>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<HistoricEntry>
    READER$ = (org.apache.avro.io.DatumReader<HistoricEntry>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeLong(this.playerId);

    out.writeString(this.playerName);

    out.writeLong(this.productId);

    out.writeString(this.productName);

    out.writeDouble(this.score);

    out.writeLong(this.entries);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.playerId = in.readLong();

      this.playerName = in.readString();

      this.productId = in.readLong();

      this.productName = in.readString();

      this.score = in.readDouble();

      this.entries = in.readLong();

    } else {
      for (int i = 0; i < 6; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.playerId = in.readLong();
          break;

        case 1:
          this.playerName = in.readString();
          break;

        case 2:
          this.productId = in.readLong();
          break;

        case 3:
          this.productName = in.readString();
          break;

        case 4:
          this.score = in.readDouble();
          break;

        case 5:
          this.entries = in.readLong();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}









