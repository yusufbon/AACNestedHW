package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Bonsen Yusuf
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> copy = new AssociativeArray<>();
    for (int i = 0; i < this.size; i++) {
      copy.pairs[i] = this.pairs[i].clone();
    }
    copy.size = this.size;
    return copy;
  } //clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    if (this.size == 0) {
      return "{}";
  }
  StringBuilder sb = new StringBuilder("{");
  for (int i = 0; i < this.size; i++) {
      sb.append(this.pairs[i]); // Directly append the KVPair
      if (i < this.size - 1) {
          sb.append(", ");
      }
  }
  sb.append("}");
  return sb.toString();
} // toString()


  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   *
   * @param K
   *   The key whose value we are seeting.
   * @param V
   *   The value of that key.
   *
   * @throws NullKeyException
   *   If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException("Null key.");
    }
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        this.pairs[i].val = value;
        return;
      }
    }
    if (this.size == this.pairs.length) {
      expand();
    }
    this.pairs[this.size] = new KVPair<>(key, value);
    this.size++;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key
   *   A key
   *
   * @throws KeyNotFoundException
   *   when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    int index = find(key);
    return this.pairs[index].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key.
   */
  public boolean hasKey(K key) {
    try {
      find(key);
      return true;
    } catch (KeyNotFoundException e) {
      return false;
    }
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {
try {
      int index = find(key);
      // Shift elements.
      for (int i = index; i < this.size - 1; i++) {
        this.pairs[i] = this.pairs[i + 1];
      }
      this.pairs[--this.size] = null; // Make the last spot empty.
    } catch (KeyNotFoundException e) {
      // If there is no key found, do nothing.
    }
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   *
   * @param key
   *   The key of the entry.
   *
   * @throws KeyNotFoundException
   *   If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      }
    }
    throw new KeyNotFoundException("Key " + key + " not found.");
  }


/**
 * Retrieve all the keys in the associative array.
 *
 * @return an array of all keys in the associative array
 */
public K[] getKeys() { // getKeys()
  K[] keys = (K[]) new Object[size]; // Create an array of keys
  for (int i = 0; i < size; i++) {
      keys[i] = pairs[i].key;
  } // for
  return keys;
} // getKeys()

  
} // class AssociativeArray


