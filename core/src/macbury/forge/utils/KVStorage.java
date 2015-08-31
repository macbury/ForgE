package macbury.forge.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import macbury.forge.Config;

/**
 * Created by macbury on 25.08.15.
 */
public abstract class KVStorage<K extends Enum> implements Disposable {
  private ObjectMap<K, Object> values;
  private Array<OnChangeListener> listeners;
  public KVStorage() {
    this.values     = new ObjectMap<K, Object>();
    this.listeners  = new Array<OnChangeListener>();
    setDefaults();
  }

  public abstract void setDefaults();

  public void addListener(OnChangeListener listener) {
    if (!listeners.contains(listener, true))
      listeners.add(listener);
  }

  public void removeListener(OnChangeListener listener) {
    listeners.removeValue(listener, true);
  }

  public void putObject(K key, Object value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putInt(K key, int value) {
    values.put(key, value);
    triggerChange(key);
  }


  public void putFloat(K key, float value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putString(K key, String value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putBool(K key, boolean value) {
    values.put(key, value);
    triggerChange(key);
  }

  public int getInt(K key) {
    return (int)values.get(key);
  }

  public String getString(K key) {
    return (String)values.get(key);
  }

  public boolean getBool(K key) {
    return (boolean)values.get(key);
  }

  public Object getObject(K key) {
    return values.get(key);
  }

  public float getFloat(K key) {
    return (float)values.get(key);
  }

  protected void triggerChange(K key) {
    for (OnChangeListener listener : listeners) {
      listener.onKeyChange(key, this);
    }
  }

  @Override
  public void dispose() {
    listeners.clear();
    values.clear();
    listeners = null;
    values    = null;
  }



  public interface OnChangeListener<K> {
    public void onKeyChange(K key, KVStorage storage);
  }
}
