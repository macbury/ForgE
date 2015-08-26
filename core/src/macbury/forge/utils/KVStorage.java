package macbury.forge.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by macbury on 25.08.15.
 */
public class KVStorage implements Disposable {
  private ObjectMap<String, Object> values;
  private Array<OnChangeListener> listeners;
  public KVStorage() {
    this.values     = new ObjectMap<String, Object>();
    this.listeners  = new Array<OnChangeListener>();
  }

  public void addListener(OnChangeListener listener) {
    if (!listeners.contains(listener, true))
      listeners.add(listener);
  }

  public void removeListener(OnChangeListener listener) {
    listeners.removeValue(listener, true);
  }

  public void putObject(String key, Object value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putInt(String key, int value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putString(String key, String value) {
    values.put(key, value);
    triggerChange(key);
  }

  public void putBool(String key, boolean value) {
    values.put(key, value);
    triggerChange(key);
  }

  public int getInt(String key) {
    return (int)values.get(key);
  }

  public String getString(String key) {
    return (String)values.get(key);
  }

  public boolean getBool(String key) {
    return (boolean)values.get(key);
  }

  public Object getObject(String key) {
    return values.get(key);
  }

  protected void triggerChange(String key) {
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

  public interface OnChangeListener {
    public void onKeyChange(String key, KVStorage storage);
  }
}
