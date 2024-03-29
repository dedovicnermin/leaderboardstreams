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

@org.apache.avro.specific.AvroGenerated
public class ScoreCard extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 954049347799451807L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ScoreCard\",\"namespace\":\"io.nermdev.schemas.avro.leaderboards\",\"fields\":[{\"name\":\"player\",\"type\":{\"type\":\"record\",\"name\":\"Player\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}},{\"name\":\"product\",\"type\":{\"type\":\"record\",\"name\":\"Product\",\"fields\":[{\"name\":\"id\",\"type\":\"long\"},{\"name\":\"name\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}},{\"name\":\"score\",\"type\":\"double\"},{\"name\":\"latestDate\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ScoreCard> ENCODER =
      new BinaryMessageEncoder<ScoreCard>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ScoreCard> DECODER =
      new BinaryMessageDecoder<ScoreCard>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ScoreCard> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ScoreCard> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ScoreCard> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ScoreCard>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ScoreCard to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ScoreCard from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ScoreCard instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ScoreCard fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private io.nermdev.schemas.avro.leaderboards.Player player;
  private io.nermdev.schemas.avro.leaderboards.Product product;
  private double score;
  private java.lang.String latestDate;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ScoreCard() {}

  /**
   * All-args constructor.
   * @param player The new value for player
   * @param product The new value for product
   * @param score The new value for score
   * @param latestDate The new value for latestDate
   */
  public ScoreCard(io.nermdev.schemas.avro.leaderboards.Player player, io.nermdev.schemas.avro.leaderboards.Product product, java.lang.Double score, java.lang.String latestDate) {
    this.player = player;
    this.product = product;
    this.score = score;
    this.latestDate = latestDate;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return player;
    case 1: return product;
    case 2: return score;
    case 3: return latestDate;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: player = (io.nermdev.schemas.avro.leaderboards.Player)value$; break;
    case 1: product = (io.nermdev.schemas.avro.leaderboards.Product)value$; break;
    case 2: score = (java.lang.Double)value$; break;
    case 3: latestDate = value$ != null ? value$.toString() : null; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'player' field.
   * @return The value of the 'player' field.
   */
  public io.nermdev.schemas.avro.leaderboards.Player getPlayer() {
    return player;
  }


  /**
   * Sets the value of the 'player' field.
   * @param value the value to set.
   */
  public void setPlayer(io.nermdev.schemas.avro.leaderboards.Player value) {
    this.player = value;
  }

  /**
   * Gets the value of the 'product' field.
   * @return The value of the 'product' field.
   */
  public io.nermdev.schemas.avro.leaderboards.Product getProduct() {
    return product;
  }


  /**
   * Sets the value of the 'product' field.
   * @param value the value to set.
   */
  public void setProduct(io.nermdev.schemas.avro.leaderboards.Product value) {
    this.product = value;
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
   * Gets the value of the 'latestDate' field.
   * @return The value of the 'latestDate' field.
   */
  public java.lang.String getLatestDate() {
    return latestDate;
  }


  /**
   * Sets the value of the 'latestDate' field.
   * @param value the value to set.
   */
  public void setLatestDate(java.lang.String value) {
    this.latestDate = value;
  }

  /**
   * Creates a new ScoreCard RecordBuilder.
   * @return A new ScoreCard RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder newBuilder() {
    return new io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder();
  }

  /**
   * Creates a new ScoreCard RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ScoreCard RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder newBuilder(io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder other) {
    if (other == null) {
      return new io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder();
    } else {
      return new io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder(other);
    }
  }

  /**
   * Creates a new ScoreCard RecordBuilder by copying an existing ScoreCard instance.
   * @param other The existing instance to copy.
   * @return A new ScoreCard RecordBuilder
   */
  public static io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder newBuilder(io.nermdev.schemas.avro.leaderboards.ScoreCard other) {
    if (other == null) {
      return new io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder();
    } else {
      return new io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder(other);
    }
  }

  /**
   * RecordBuilder for ScoreCard instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ScoreCard>
    implements org.apache.avro.data.RecordBuilder<ScoreCard> {

    private io.nermdev.schemas.avro.leaderboards.Player player;
    private io.nermdev.schemas.avro.leaderboards.Player.Builder playerBuilder;
    private io.nermdev.schemas.avro.leaderboards.Product product;
    private io.nermdev.schemas.avro.leaderboards.Product.Builder productBuilder;
    private double score;
    private java.lang.String latestDate;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.player)) {
        this.player = data().deepCopy(fields()[0].schema(), other.player);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (other.hasPlayerBuilder()) {
        this.playerBuilder = io.nermdev.schemas.avro.leaderboards.Player.newBuilder(other.getPlayerBuilder());
      }
      if (isValidValue(fields()[1], other.product)) {
        this.product = data().deepCopy(fields()[1].schema(), other.product);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (other.hasProductBuilder()) {
        this.productBuilder = io.nermdev.schemas.avro.leaderboards.Product.newBuilder(other.getProductBuilder());
      }
      if (isValidValue(fields()[2], other.score)) {
        this.score = data().deepCopy(fields()[2].schema(), other.score);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.latestDate)) {
        this.latestDate = data().deepCopy(fields()[3].schema(), other.latestDate);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing ScoreCard instance
     * @param other The existing instance to copy.
     */
    private Builder(io.nermdev.schemas.avro.leaderboards.ScoreCard other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.player)) {
        this.player = data().deepCopy(fields()[0].schema(), other.player);
        fieldSetFlags()[0] = true;
      }
      this.playerBuilder = null;
      if (isValidValue(fields()[1], other.product)) {
        this.product = data().deepCopy(fields()[1].schema(), other.product);
        fieldSetFlags()[1] = true;
      }
      this.productBuilder = null;
      if (isValidValue(fields()[2], other.score)) {
        this.score = data().deepCopy(fields()[2].schema(), other.score);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.latestDate)) {
        this.latestDate = data().deepCopy(fields()[3].schema(), other.latestDate);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'player' field.
      * @return The value.
      */
    public io.nermdev.schemas.avro.leaderboards.Player getPlayer() {
      return player;
    }


    /**
      * Sets the value of the 'player' field.
      * @param value The value of 'player'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setPlayer(io.nermdev.schemas.avro.leaderboards.Player value) {
      validate(fields()[0], value);
      this.playerBuilder = null;
      this.player = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'player' field has been set.
      * @return True if the 'player' field has been set, false otherwise.
      */
    public boolean hasPlayer() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'player' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public io.nermdev.schemas.avro.leaderboards.Player.Builder getPlayerBuilder() {
      if (playerBuilder == null) {
        if (hasPlayer()) {
          setPlayerBuilder(io.nermdev.schemas.avro.leaderboards.Player.newBuilder(player));
        } else {
          setPlayerBuilder(io.nermdev.schemas.avro.leaderboards.Player.newBuilder());
        }
      }
      return playerBuilder;
    }

    /**
     * Sets the Builder instance for the 'player' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setPlayerBuilder(io.nermdev.schemas.avro.leaderboards.Player.Builder value) {
      clearPlayer();
      playerBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'player' field has an active Builder instance
     * @return True if the 'player' field has an active Builder instance
     */
    public boolean hasPlayerBuilder() {
      return playerBuilder != null;
    }

    /**
      * Clears the value of the 'player' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder clearPlayer() {
      player = null;
      playerBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'product' field.
      * @return The value.
      */
    public io.nermdev.schemas.avro.leaderboards.Product getProduct() {
      return product;
    }


    /**
      * Sets the value of the 'product' field.
      * @param value The value of 'product'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setProduct(io.nermdev.schemas.avro.leaderboards.Product value) {
      validate(fields()[1], value);
      this.productBuilder = null;
      this.product = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'product' field has been set.
      * @return True if the 'product' field has been set, false otherwise.
      */
    public boolean hasProduct() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'product' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public io.nermdev.schemas.avro.leaderboards.Product.Builder getProductBuilder() {
      if (productBuilder == null) {
        if (hasProduct()) {
          setProductBuilder(io.nermdev.schemas.avro.leaderboards.Product.newBuilder(product));
        } else {
          setProductBuilder(io.nermdev.schemas.avro.leaderboards.Product.newBuilder());
        }
      }
      return productBuilder;
    }

    /**
     * Sets the Builder instance for the 'product' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */

    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setProductBuilder(io.nermdev.schemas.avro.leaderboards.Product.Builder value) {
      clearProduct();
      productBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'product' field has an active Builder instance
     * @return True if the 'product' field has an active Builder instance
     */
    public boolean hasProductBuilder() {
      return productBuilder != null;
    }

    /**
      * Clears the value of the 'product' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder clearProduct() {
      product = null;
      productBuilder = null;
      fieldSetFlags()[1] = false;
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
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setScore(double value) {
      validate(fields()[2], value);
      this.score = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'score' field has been set.
      * @return True if the 'score' field has been set, false otherwise.
      */
    public boolean hasScore() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'score' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder clearScore() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'latestDate' field.
      * @return The value.
      */
    public java.lang.String getLatestDate() {
      return latestDate;
    }


    /**
      * Sets the value of the 'latestDate' field.
      * @param value The value of 'latestDate'.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder setLatestDate(java.lang.String value) {
      validate(fields()[3], value);
      this.latestDate = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'latestDate' field has been set.
      * @return True if the 'latestDate' field has been set, false otherwise.
      */
    public boolean hasLatestDate() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'latestDate' field.
      * @return This builder.
      */
    public io.nermdev.schemas.avro.leaderboards.ScoreCard.Builder clearLatestDate() {
      latestDate = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ScoreCard build() {
      try {
        ScoreCard record = new ScoreCard();
        if (playerBuilder != null) {
          try {
            record.player = this.playerBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("player"));
            throw e;
          }
        } else {
          record.player = fieldSetFlags()[0] ? this.player : (io.nermdev.schemas.avro.leaderboards.Player) defaultValue(fields()[0]);
        }
        if (productBuilder != null) {
          try {
            record.product = this.productBuilder.build();
          } catch (org.apache.avro.AvroMissingFieldException e) {
            e.addParentField(record.getSchema().getField("product"));
            throw e;
          }
        } else {
          record.product = fieldSetFlags()[1] ? this.product : (io.nermdev.schemas.avro.leaderboards.Product) defaultValue(fields()[1]);
        }
        record.score = fieldSetFlags()[2] ? this.score : (java.lang.Double) defaultValue(fields()[2]);
        record.latestDate = fieldSetFlags()[3] ? this.latestDate : (java.lang.String) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ScoreCard>
    WRITER$ = (org.apache.avro.io.DatumWriter<ScoreCard>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ScoreCard>
    READER$ = (org.apache.avro.io.DatumReader<ScoreCard>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    this.player.customEncode(out);

    this.product.customEncode(out);

    out.writeDouble(this.score);

    out.writeString(this.latestDate);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (this.player == null) {
        this.player = new io.nermdev.schemas.avro.leaderboards.Player();
      }
      this.player.customDecode(in);

      if (this.product == null) {
        this.product = new io.nermdev.schemas.avro.leaderboards.Product();
      }
      this.product.customDecode(in);

      this.score = in.readDouble();

      this.latestDate = in.readString();

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (this.player == null) {
            this.player = new io.nermdev.schemas.avro.leaderboards.Player();
          }
          this.player.customDecode(in);
          break;

        case 1:
          if (this.product == null) {
            this.product = new io.nermdev.schemas.avro.leaderboards.Product();
          }
          this.product.customDecode(in);
          break;

        case 2:
          this.score = in.readDouble();
          break;

        case 3:
          this.latestDate = in.readString();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










